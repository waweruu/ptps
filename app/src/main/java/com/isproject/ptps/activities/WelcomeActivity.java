package com.isproject.ptps.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ptps.Operator;
import com.isproject.ptps.R;
import com.isproject.ptps.User;
import com.isproject.ptps.fragments.SignInOptionsFragment;
import com.isproject.ptps.fragments.operator.ProvideDetailsFragment;

import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity implements ProvideDetailsFragment.OnButtonClickedListener,
        SignInOptionsFragment.OnSignInOptionSelected {

    private static final String TAG = "WelcomeActivity";
    private static final int SIGN_IN_RC = 2377;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        signInWithChecks();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_RC) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {
                //startActivity(new Intent(this, LandingActivity.class).putExtra("TOKEN", response.getIdpToken()));
                //finish();

                signInWithChecks();
            } else {
                //sign in has failed
                if(response == null) {
                    //User has pressed back button
                    //add snackbar/toast
                    return;
                }

                if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //toast no internet connection
                    return;
                }

                //toast unknown error
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    protected void signInWithChecks() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            //user is already signed in
            String userUid = auth.getCurrentUser().getUid();
            databaseReference.child("Users").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        //user has not registered
                        //lead to register activities
                        Intent intent = new Intent(WelcomeActivity.this, InputDetailsActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(WelcomeActivity.this, LandingTwoActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, databaseError.toString());
                }
            });
        } else {
            //user is not signed in
            Fragment fragment = new SignInOptionsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.activityWelcomeContent, fragment).commit();
        }
    }

    public void createSignInUi() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("ke").build()
                        ))
                        .setTheme(R.style.VerifyTheme)
                        .setIsSmartLockEnabled(false)
                        .build(),
                SIGN_IN_RC
        );
    }

    public void checkUserPhoneNumber() {
        final String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        databaseReference.child("Vehicles").child("KDB 017B").child("Conductor").child("phoneNumber").equalTo(phoneNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            //user is not operator
                            Intent intent = new Intent(WelcomeActivity.this, LandingActivity.class);
                            intent.putExtra("PUT", "User not operator");
                            intent.putExtra("PHONE", phoneNumber);
                            startActivity(intent);
                        } else {
                            //user is operator
                            addUserDetails(dataSnapshot);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, databaseError.toString());
                    }
                });
    }

    public void addUserDetails(DataSnapshot dataSnapshot) {
        User operator = new User();
        operator.setFirstName(dataSnapshot.getValue(Operator.class).getFirstName());
        operator.setLastName(dataSnapshot.getValue(Operator.class).getLastName());
        operator.setPhoneNumber(dataSnapshot.getValue(Operator.class).getPhoneNumber());
        operator.setUserType(dataSnapshot.getKey());

        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("Users").child(userUid).setValue(operator);
        Intent intent = new Intent(WelcomeActivity.this, LandingActivity.class);
        intent.putExtra("PUT", "User is operator");
        startActivity(intent);
    }

    @Override
    public void checkOperatorDetails(String result, Bundle bundle) {
        String message;
        switch(result) {
            case "FAIL":
                //result false
                message = "You are not operator!";
                toastMessage(message);
                break;
            case "SUCCESS":
                //result true
                message = "Check successful!";
                toastMessage(message);
                break;
            case "ERROR":
                //result error
                message = "Wrong details!";
                toastMessage(message);
                break;
        }
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void actionSignInSelected(String option) {
        switch(option) {
            case "Passenger":
                createSignInUi();
                signInWithChecks();
                break;
            case "Operator":
                Fragment fragment = new ProvideDetailsFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                /*fragmentManager.beginTransaction().replace(R.id.fragmentSignInOptions, fragment).commit();*/
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activityWelcomeContent, fragment);
                fragmentTransaction.commit();
        }
    }

    //TODO: Implemement on back presssed twice exit
}

package com.isproject.ptps.fragments.operator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ptps.Operator;
import com.isproject.ptps.R;
import com.isproject.ptps.User;
import com.isproject.ptps.activities.LandingActivity;
import com.isproject.ptps.activities.LandingTwoActivity;

import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

public class SignInOperatorFragment extends Fragment {

    private static final int SIGN_IN_RC = 2377;
    private static final String TAG = "SignInOperatorFragment";
    Bundle bundle;

    public SignInOperatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in_operator, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bundle = this.getArguments();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("ke")
                                        .build()
                        ))
                        .setTheme(R.style.VerifyTheme)
                        .setIsSmartLockEnabled(false)
                        .build(),
                SIGN_IN_RC
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_RC) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {
                //sign in complete
                final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Vehicles" + "/" + bundle.getString("LICENCE_PLATE") +
                        "/" + bundle.getString("USER_TYPE")).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User op = new User();
                                op.setFirstName(dataSnapshot.getValue(Operator.class).getFirstName());
                                op.setLastName(dataSnapshot.getValue(Operator.class).getLastName());
                                op.setUserType(bundle.getString("USER_TYPE"));
                                op.setPhoneNumber(dataSnapshot.getValue(Operator.class).getPhoneNumber());

                                databaseReference.child("Users").child(userUid).setValue(op);

                                //todo: change to landingtwoactivity
                                Intent intent = new Intent(getContext(), LandingTwoActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }
                );

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
}

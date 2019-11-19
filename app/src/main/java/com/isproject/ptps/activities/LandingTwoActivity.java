package com.isproject.ptps.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.isproject.ptps.fragments.owner.AddVehicleFragment;
import com.isproject.ptps.mpesa.Mpesa;
import com.isproject.ptps.mpesa.interfaces.AuthListener;
import com.isproject.ptps.mpesa.interfaces.MpesaListener;
import com.isproject.ptps.mpesa.utils.Pair;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ptps.R;
import com.isproject.ptps.fragments.operator.OperatorAccountFragment;
import com.isproject.ptps.fragments.operator.OperatorHomeFragment;
import com.isproject.ptps.fragments.operator.PassengerPaymentsFragment;
import com.isproject.ptps.fragments.owner.FareChartFragment;
import com.isproject.ptps.fragments.owner.OwnerAccountFragment;
import com.isproject.ptps.fragments.owner.OwnerHomeFragment;
import com.isproject.ptps.fragments.owner.PassengerReviewsFragment;
import com.isproject.ptps.fragments.owner.PaymentDetailsFragment;
import com.isproject.ptps.fragments.passenger.PassengerAccountFragment;
import com.isproject.ptps.fragments.passenger.PassengerHomeFragment;
import com.isproject.ptps.fragments.passenger.PaymentHistoryFragment;
import com.isproject.ptps.fragments.passenger.ScanQrCodeFragment;
import com.isproject.ptps.fragments.passenger.ViewVehicleFareChartFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class LandingTwoActivity extends AppCompatActivity
        implements ScanQrCodeFragment.OnCodeScannedListener,
        PassengerHomeFragment.OnCardClickedListener,OwnerHomeFragment.OnOwnerCardClickedListener,
        OperatorHomeFragment.OnOperatorCardClicked, AuthListener, MpesaListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            //user is signed in
            setContentView(R.layout.activity_landing_two);
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if(getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle = setupDrawerToggle();
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.syncState();


            navigationView = findViewById(R.id.nav_view);

            String userUid = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(userUid);
            databaseReference.child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userType = dataSnapshot.getValue().toString();
                    if(userType != null) {
                        if (userType.equals("Passenger")) {
                            navigationView.inflateMenu(R.menu.passenger_drawer_view);
                            setupPassengerDrawerContent(navigationView);
                            Fragment fragment = new PassengerHomeFragment();
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.fl_content, fragment);
                            setTitle("Home");
                            ft.commitAllowingStateLoss();
                        } else if (userType.equals("Owner")) {
                            navigationView.inflateMenu(R.menu.owner_drawer_view);
                            setupOwnerDrawerContent(navigationView);
                            Fragment fragment = new OwnerHomeFragment();
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.fl_content, fragment);
                            setTitle("Home");
                            ft.commitAllowingStateLoss();
                        } else if (userType.equals("Conductor") || userType.equals("Driver")) {
                            navigationView.inflateMenu(R.menu.operator_drawer_view);
                            setupOperatorDrawerContent(navigationView);
                            Fragment fragment = new OperatorHomeFragment();
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(R.id.fl_content, fragment);
                            setTitle("Home");
                            ft.commitAllowingStateLoss();
                        }
                    } else {
                        String message = "Not working";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            //user is signed out
            Intent intent = new Intent(LandingTwoActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupPassengerDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectPassengerDrawerItem(menuItem);
                return true;
            }
        });
    }

    private void setupOwnerDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectOwnerDrawerItem(menuItem);
                return true;
            }
        });
    }

    private void setupOperatorDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectOperatorDrawerItem(menuItem);
                return true;
            }
        });
    }

    public void selectPassengerDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;

        switch(menuItem.getItemId()) {
            case R.id.nav_scan_frag:
                fragmentClass = ScanQrCodeFragment.class;
                break;
            case R.id.nav_payment_history_frag:
                fragmentClass = PaymentHistoryFragment.class;
                break;
            case R.id.nav_passenger_account_frag:
                fragmentClass = PassengerAccountFragment.class;
                break;
            default:
                fragmentClass = PassengerHomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, fragment);
        fragmentTransaction.addToBackStack(menuItem.getTitle().toString());
        fragmentTransaction.commit();

        menuItem.setChecked(true);

        setTitle(menuItem.getTitle());

        drawerLayout.closeDrawers();
    }

    public void selectOwnerDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;

        switch(menuItem.getItemId()) {
            case R.id.nav_fare_chart_frag:
                fragmentClass = FareChartFragment.class;
                break;
            case R.id.nav_payment_details_frag:
                fragmentClass = PaymentDetailsFragment.class;
                break;
            case R.id.nav_passenger_reviews_frag:
                fragmentClass = PassengerReviewsFragment.class;
                break;
            case R.id.nav_add_vehicle_frag:
                fragmentClass = AddVehicleFragment.class;
                break;
            case R.id.nav_owner_account_frag:
                fragmentClass = OwnerAccountFragment.class;
                break;
            default:
                fragmentClass = OwnerHomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, fragment);
        fragmentTransaction.addToBackStack(menuItem.getTitle().toString());
        fragmentTransaction.commit();

        menuItem.setChecked(true);

        setTitle(menuItem.getTitle());

        drawerLayout.closeDrawers();
    }

    public void selectOperatorDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;

        switch(menuItem.getItemId()) {
            case R.id.nav_passenger_payments_frag:
                fragmentClass = PassengerPaymentsFragment.class;
                break;
            case R.id.nav_operator_account_frag:
                fragmentClass = OperatorAccountFragment.class;
                break;
            default:
                fragmentClass = OperatorHomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_content, fragment);
        fragmentTransaction.addToBackStack(menuItem.getTitle().toString());
        fragmentTransaction.commit();

        menuItem.setChecked(true);

        setTitle(menuItem.getTitle());

        drawerLayout.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void sendLicencePlate(String licencePlate) {
        Fragment fragment = new ViewVehicleFareChartFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();

        Bundle bundle = new Bundle();
        bundle.putString("LICENCE_PLATE", licencePlate);
        fragment.setArguments(bundle);
    }

    @Override
    public void goToPassengerFragment(String fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frag;

        switch (fragment) {
            case "scan":
                frag = new ScanQrCodeFragment();
                ft.replace(R.id.fl_content, frag);
                ft.addToBackStack("Scan QR code");
                ft.commit();
                setTitle("Scan QR Code");
                break;
            case "history":
                frag = new PaymentHistoryFragment();
                ft.replace(R.id.fl_content, frag);
                ft.addToBackStack("Payment History");
                ft.commit();
                setTitle("Payment History");
                break;
            case "account":
                frag = new PassengerAccountFragment();
                ft.replace(R.id.fl_content, frag);
                ft.addToBackStack("Account");
                setTitle("Account");
                ft.commit();
                break;
        }
    }
    public void goToOwnerFragment(String fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frag;

        switch(fragment){
            case "farechart":
                frag = new FareChartFragment();
                ft.replace(R.id.fl_content, frag);
                ft.addToBackStack("Fare Chart");
                ft.commit();
                setTitle("Fare Chart");
                break;
            case "details":
                frag = new PaymentDetailsFragment();
                ft.replace(R.id.fl_content, frag);
                ft.addToBackStack("Payment Details");
                ft.commit();
                setTitle("Payment Details");
                break;
            case "reviews":
                frag = new PassengerReviewsFragment();
                ft.replace(R.id.fl_content, frag);
                ft.addToBackStack("Passenger Reviews");
                setTitle("Passenger Reviews");
                ft.commit();
                break;
            case "vehicle":
                frag = new AddVehicleFragment();
                ft.replace(R.id.fl_content, frag);
                ft.addToBackStack("Add Vehicle");
                setTitle("Add Vehicle");
                ft.commit();
                break;
            case "account":
                frag = new OwnerAccountFragment();
                ft.replace(R.id.fl_content, frag);
                ft.addToBackStack("Account");
                setTitle("Account");
                ft.commit();
                break;
        }
    }

    @Override
    public void goToOperatorFragment(String fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment frag;

        switch (fragment) {
            case "payments":
                frag = new PassengerPaymentsFragment();
                ft.replace(R.id.fl_content, frag);
                ft.addToBackStack("payments");
                setTitle("Passenger Payments");
                ft.commit();
                break;
            case "account":
                frag = new OperatorAccountFragment();
                ft.replace(R.id.fl_content, frag);
                ft.addToBackStack("account");
                setTitle("Account");
                ft.commit();
                break;
        }
    }

    @Override
    public void onAuthError(Pair<Integer, String> result) {
        Log.e("Error", result.message);
    }

    @Override
    public void onAuthSuccess() {
        Toast.makeText(this, "M-Pesa Auth successful!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMpesaError(Pair<Integer, String> result) {
        ViewVehicleFareChartFragment.alertDialog.hide();
        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage) {
        Toast.makeText(this, CustomerMessage, Toast.LENGTH_SHORT).show();
    }
}

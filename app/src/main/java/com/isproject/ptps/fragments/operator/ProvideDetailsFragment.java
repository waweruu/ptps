package com.isproject.ptps.fragments.operator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ptps.Operator;
import com.isproject.ptps.R;
import com.isproject.ptps.User;
import com.isproject.ptps.activities.LandingActivity;


public class ProvideDetailsFragment extends Fragment {

    private OnButtonClickedListener mListener;

    public interface OnButtonClickedListener {
        void checkOperatorDetails(String result, Bundle args);
    }

    private EditText phoneNumber, licencePlate;
    private Spinner userType;
    private Button provideDetails;

    public ProvideDetailsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_provide_details, container, false);
        phoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        licencePlate = view.findViewById(R.id.editTextLicencePlate);
        userType = view.findViewById(R.id.spinnerUserType);
        provideDetails = view.findViewById(R.id.buttonProvideDetails);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] userChoices;
        userChoices = new String[]{"Select operator type...", "Conductor", "Driver"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, userChoices) {
            @Override
            public boolean isEnabled(int position) {
                if(position == 0) return false;
                else return true;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView)view;
                if(position == 0) tv.setTextColor(Color.GRAY);
                else tv.setTextColor(Color.BLACK);
                return view;
            }
        };



        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        userType.setAdapter(spinnerAdapter);

        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String)adapterView.getItemAtPosition(i);
                if(i > 0) {
                    Toast.makeText(getContext(), "Selected: " + selectedItem,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        provideDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastMessage(licencePlate.getText().toString());
                toastMessage(phoneNumber.getText().toString());
                toastMessage(userType.getSelectedItem().toString());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference();
                /*databaseReference.orderByChild("phoneNumber").equalTo(phoneNumber.getText().toString())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    //user is not operator
                                    //show not operator
                                    //back to welcome activity
                                    Fragment fragment = ProvideDetailsFragment.this;
                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.remove(fragment).commit();
                                    mListener.checkOperatorDetails(false, null);
                                } else {
                                    //user is operator
                                    //add as user
                                    //sign in
                                    Bundle bundle = new Bundle();
                                    bundle.putString("LICENCE_PLATE", licencePlate.getText().toString());
                                    bundle.putString("USER_TYPE", userType.getSelectedItem().toString());
                                    bundle.putString("PHONE_NUMBER", licencePlate.getText().toString());

                                    Fragment fragment = new SignInOperatorFragment();
                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();
                                    fragment.setArguments(bundle);
                                    ft.replace(R.id.fragmentSignInOptions, fragment).commit();

                                    mListener.checkOperatorDetails(true, bundle);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/

                databaseReference.child("Vehicles" + "/" + licencePlate.getText().toString() +
                        "/" + userType.getSelectedItem().toString()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.exists()) {
                                    Fragment fragment = ProvideDetailsFragment.this;
                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.remove(fragment).commit();
                                    mListener.checkOperatorDetails("FAIL", null);
                                } else {

                                    if(dataSnapshot.getValue(Operator.class).getPhoneNumber()
                                            .equals(phoneNumber.getText().toString())) {
                                        //user is operator
                                        //add as user
                                        //sign in
                                        Bundle bundle = new Bundle();
                                        bundle.putString("LICENCE_PLATE", licencePlate.getText().toString());
                                        bundle.putString("USER_TYPE", userType.getSelectedItem().toString());
                                        bundle.putString("PHONE_NUMBER", licencePlate.getText().toString());

                                        Fragment fragment = new SignInOperatorFragment();
                                        //Fragment previousFragment = ProvideDetailsFragment.this;
                                        FragmentManager fm = getActivity().getSupportFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();
                                        fragment.setArguments(bundle);
                                        ft.replace(R.id.fragmentProvideDetails, fragment).commit();

                                        mListener.checkOperatorDetails("SUCCESS", bundle);
                                    } else {
                                        //user provided wrong number
                                        //back to provide details
                                        phoneNumber.setText("");
                                        licencePlate.setText("");
                                        userType.setSelection(0);
                                        mListener.checkOperatorDetails("ERROR", null);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }
                );
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnButtonClickedListener) {
            mListener = (OnButtonClickedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnButtonClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}

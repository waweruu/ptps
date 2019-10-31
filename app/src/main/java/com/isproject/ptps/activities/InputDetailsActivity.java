package com.isproject.ptps.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isproject.ptps.R;
import com.isproject.ptps.User;

public class InputDetailsActivity extends AppCompatActivity {

    Button buttonInputDetails;
    Spinner spinnerUserType;
    Toolbar toolbar;
    EditText textFirstName, textLastName;
    CheckBox termsAndConditions;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_details);

        //initialising views
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textFirstName = findViewById(R.id.editTextFirstName);
        textLastName = findViewById(R.id.editTextLastName);
        termsAndConditions = findViewById(R.id.checkBoxTermsAndConditions);

        spinnerUserType = findViewById(R.id.spinnerUserType);

        String[] userChoices;
        userChoices = new String[]{"Select user type...", "Passenger", "Owner"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, userChoices) {
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
        spinnerUserType.setAdapter(spinnerAdapter);

        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String)adapterView.getItemAtPosition(i);
                if(i > 0) {
                    Toast.makeText(getApplicationContext(), "Selected: " + selectedItem,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonInputDetails = findViewById(R.id.buttonInputDetails);
        buttonInputDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textFirstName.getText().toString().isEmpty()||textLastName.getText().toString().isEmpty()||spinnerUserType.getSelectedItem().toString().equals(""))
                {
                    builder = new AlertDialog.Builder(InputDetailsActivity.this);
                    builder.setMessage("Please fill all the fields.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(!(termsAndConditions.isChecked()))
                {
                    Toast.makeText(InputDetailsActivity.this,"You have to accept the terms and conditions!", Toast.LENGTH_LONG).show();
                }
                else{
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String userUID = current_user.getUid();
                    String phoneNumber = current_user.getPhoneNumber();

                    String userType = spinnerUserType.getSelectedItem().toString().trim();

                    User user = new User();
                    user.setFirstName(textFirstName.getText().toString());
                    user.setLastName(textLastName.getText().toString());
                    user.setPhoneNumber(phoneNumber);
                    user.setUserType(userType);

                    databaseReference.child("Users").child(userUID).setValue(user);

                    if(userType == "Owner"){
                        Intent in = new Intent(InputDetailsActivity.this, VehicleDetailsActivity.class);
                        startActivity(in);
                        finish();
                    }
                    else if(userType == "Passenger") {
                        Intent in = new Intent(InputDetailsActivity.this, LandingTwoActivity.class);
                        finish();
                    }
                }
            }
        });




    }
}

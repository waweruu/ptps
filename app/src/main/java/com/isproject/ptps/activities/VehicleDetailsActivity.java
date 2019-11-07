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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isproject.ptps.NumberPlate;
import com.isproject.ptps.R;
import com.isproject.ptps.Vehicle;

public class VehicleDetailsActivity extends AppCompatActivity {

    EditText textVehicleCapacity, textLicencePlate;
    Toolbar toolbar;
    Button buttonVehicleDetails;
    Spinner spinnerCarType;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.title_vehicle_details);

        textLicencePlate = findViewById(R.id.editTextLicencePlate);
        textVehicleCapacity = findViewById(R.id.editTextVehicleCapacity);

        spinnerCarType = findViewById(R.id.spinnerCarType);
        String[] carTypes;
        carTypes = new String[]{"Select car type...", "Bus", "Matatu"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, carTypes) {
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
        spinnerCarType.setAdapter(spinnerAdapter);

        spinnerCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        buttonVehicleDetails = findViewById(R.id.buttonVehicleDetails);
        buttonVehicleDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinnerCarType.getSelectedItem().toString().isEmpty()||textVehicleCapacity.getText().toString().isEmpty()||textLicencePlate.getText().toString().isEmpty())
                {
                    builder=new AlertDialog.Builder(VehicleDetailsActivity.this);
                    builder.setMessage("Please fill all the fields.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String userUID = current_user.getUid();
                    String licencePlate = textLicencePlate.getText().toString();

                    Vehicle vehicle = new Vehicle();
                    vehicle.setVehicleCapacity(textVehicleCapacity.getText().toString());
                    vehicle.setVehicleType(spinnerCarType.getSelectedItem().toString());

                    databaseReference.child("Vehicles").child(licencePlate)
                            .child("Vehicle Details").setValue(vehicle);
                    databaseReference.child("Users").child(userUID).child("vehicles").push().setValue(licencePlate);

                    NumberPlate numberPlate = new NumberPlate(licencePlate);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userUID)
                            .child("vehicles").push().setValue(numberPlate);

                    Intent in = new Intent(VehicleDetailsActivity.this, DriverDetailsActivity.class);
                    in.putExtra("LICENCE_PLATE", licencePlate);
                    startActivity(in);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //disabled back button
        Utilities.showWarningDialog(this);
    }
}

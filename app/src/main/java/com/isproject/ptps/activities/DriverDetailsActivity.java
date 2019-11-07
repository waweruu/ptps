package com.isproject.ptps.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isproject.ptps.Operator;
import com.isproject.ptps.R;

public class DriverDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button buttonDriverDetails;
    EditText textDriverFirstName, textDriverLastName, textDriverId, textDriverPhoneNumber;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.title_driver_details);

        textDriverFirstName = findViewById(R.id.editTextDriverFirstName);
        textDriverLastName = findViewById(R.id.editTextDriverLastName);
        textDriverId = findViewById(R.id.editTextDriverId);
        textDriverPhoneNumber = findViewById(R.id.editTextDriverPhoneNumber);

        buttonDriverDetails = findViewById(R.id.buttonDriverDetails);
        buttonDriverDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(textDriverFirstName.getText().toString().isEmpty()||textDriverLastName.getText().toString().isEmpty()||textDriverId.getText().toString().isEmpty()||textDriverPhoneNumber.getText().toString().isEmpty())
                {
                    builder = new AlertDialog.Builder(DriverDetailsActivity.this);
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
                else if(textDriverId.length()!=10)
                {
                    builder=new AlertDialog.Builder(DriverDetailsActivity.this);
                    builder.setMessage("ID number should be 10 values");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            textDriverId.setText("");

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(textDriverPhoneNumber.length()!=10)
                {
                    builder=new AlertDialog.Builder(DriverDetailsActivity.this);
                    builder.setMessage("Phone Number should have 10 digits");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            textDriverPhoneNumber.setText("");

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else
                {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String userUID = current_user.getUid();

                    Intent previousIntent = getIntent();
                    String licencePlate = previousIntent.getStringExtra("LICENCE_PLATE");

                    Operator operator = new Operator();
                    operator.setFirstName(textDriverFirstName.getText().toString());
                    operator.setLastName(textDriverLastName.getText().toString());
                    operator.setIdNumber(textDriverId.getText().toString());
                    operator.setPhoneNumber(textDriverPhoneNumber.getText().toString());

                    databaseReference.child("Vehicles").child(licencePlate)
                            .child("Driver").setValue(operator);

                    Intent intent = new Intent(DriverDetailsActivity.this, ConductorDetailsActivity.class);
                    intent.putExtra("LICENCE_PLATE", licencePlate);
                    startActivity(intent);
                    finish();
                }

                }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Disabled back button
        Utilities.showWarningDialog(this);
    }
}

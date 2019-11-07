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

public class ConductorDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button buttonConductorDetails;
    EditText textConductorFirstName, textConductorLastName, textConductorId, textConductorPhoneNumber;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.title_conductor_details);

        textConductorFirstName = findViewById(R.id.editTextConductorFirstName);
        textConductorLastName = findViewById(R.id.editTextConductorLastName);
        textConductorId = findViewById(R.id.editTextConductorId);
        textConductorPhoneNumber = findViewById(R.id.editTextConductorPhoneNumber);

        buttonConductorDetails = findViewById(R.id.buttonConductorDetails);
        buttonConductorDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textConductorFirstName.getText().toString().isEmpty()||textConductorLastName.getText().toString().isEmpty()||textConductorId.getText().toString().isEmpty()||textConductorPhoneNumber.getText().toString().isEmpty())
                {
                    builder = new AlertDialog.Builder(ConductorDetailsActivity.this);
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
                else if(textConductorId.length()!=10)
                {
                    builder=new AlertDialog.Builder(ConductorDetailsActivity.this);
                    builder.setMessage("ID number should be 10 values");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            textConductorId.setText("");

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(textConductorPhoneNumber.length()!=10)
                {
                    builder=new AlertDialog.Builder(ConductorDetailsActivity.this);
                    builder.setMessage("Phone Number should have 10 digits");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            textConductorPhoneNumber.setText("");

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
                    operator.setFirstName(textConductorFirstName.getText().toString());
                    operator.setLastName(textConductorLastName.getText().toString());
                    operator.setIdNumber(textConductorId.getText().toString());
                    operator.setPhoneNumber(textConductorPhoneNumber.getText().toString());

                    databaseReference.child("Vehicles").child(licencePlate)
                            .child("Conductor").setValue(operator);

                    Intent intent = new Intent(ConductorDetailsActivity.this, LandingTwoActivity.class);
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

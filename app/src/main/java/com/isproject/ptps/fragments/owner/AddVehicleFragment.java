package com.isproject.ptps.fragments.owner;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isproject.ptps.NumberPlate;
import com.isproject.ptps.R;
import com.isproject.ptps.Vehicle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class AddVehicleFragment extends Fragment {
    Spinner types;
    EditText capacity,license;
    Button button;
    AlertDialog.Builder builder;

    public AddVehicleFragment() {
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
        View view= inflater.inflate(R.layout.fragment_add_vehicle, container, false);

        types=view.findViewById(R.id.spinnerVehicleType);
        capacity=view.findViewById(R.id.editTextSetVehicleCapacity);
        license=view.findViewById(R.id.editTextSetVehicleLicense);

        String[] carTypes;
        carTypes = new String[]{"Select car type...", "Bus", "Matatu"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, carTypes) {
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
        types.setAdapter(spinnerAdapter);

        types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        button=view.findViewById(R.id.buttonInAddVehicle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(types.getSelectedItem().toString().isEmpty()||capacity.getText().toString().isEmpty()||license.getText().toString().isEmpty())
                {
                    builder=new AlertDialog.Builder(getContext());
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
                else{
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String userUID = current_user.getUid();
                    String licencePlate = license.getText().toString();

                    Vehicle vehicle = new Vehicle();
                    vehicle.setVehicleCapacity(capacity.getText().toString());
                    vehicle.setVehicleType(types.getSelectedItem().toString());

                    databaseReference.child("Vehicles").child(licencePlate)
                            .child("Vehicle Details").setValue(vehicle);

                    NumberPlate plate = new NumberPlate(licencePlate);
                    databaseReference.child("Users").child(userUID).child("vehicles").push().setValue(plate);
                    Toast.makeText(getContext(),"Vehicle Added Successfully",Toast.LENGTH_LONG).show();
                    getInfo(licencePlate);

                }

            }
        });


        return view;
    }
    public void getInfo(String licencePlate)
    {
        Fragment fragment = new AddDriverFragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.addToBackStack("FRAGMENT");
        ft.commit();

        Bundle bundle = new Bundle();
        bundle.putString("LICENSE_PLATE",licencePlate);


        fragment.setArguments(bundle);
    }


}

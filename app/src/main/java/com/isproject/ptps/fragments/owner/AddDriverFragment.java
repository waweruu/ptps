package com.isproject.ptps.fragments.owner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isproject.ptps.Operator;
import com.isproject.ptps.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class AddDriverFragment extends Fragment {

    EditText fname,lname,id_number,phone_number;
    Button button;
    AlertDialog.Builder builder;


    public AddDriverFragment() {
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

        View view=inflater.inflate(R.layout.fragment_add_driver, container, false);



        fname=view.findViewById(R.id.editTextSetDriverFirstName);

        lname=view.findViewById(R.id.editTextSetDriverLastName);

        id_number=view.findViewById(R.id.editTextSetDriverId);

        phone_number=view.findViewById(R.id.editTextSetDriverPhoneNumber);

        button=view.findViewById(R.id.buttonInAddDrriver);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fname.getText().toString().isEmpty()||lname.getText().toString().isEmpty()||id_number.getText().toString().isEmpty()||phone_number.getText().toString().isEmpty())
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
                else if(id_number.length()!=10)
                {
                    builder=new AlertDialog.Builder(getContext());
                    builder.setMessage("ID number should be 10 values");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            id_number.setText("");

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
                else if(phone_number.length()!=10)
                {
                    builder=new AlertDialog.Builder(getContext());
                    builder.setMessage("Phone Number should have 10 digits");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            phone_number.setText("");

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
                else {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String userUID = current_user.getUid();

                    final Bundle bundle = getArguments();
                    final String licencePlate = bundle.getString("LICENSE_PLATE");

                    Operator operator = new Operator();
                    operator.setFirstName(fname.getText().toString());
                    operator.setLastName(lname.getText().toString());
                    operator.setIdNumber(id_number.getText().toString());
                    operator.setPhoneNumber(phone_number.getText().toString());

                    databaseReference.child("Vehicles").child(licencePlate)
                            .child("Driver").setValue(operator);

                    Toast.makeText(getContext(),"Driver Registered Successfully",Toast.LENGTH_LONG).show();
                    getInfo(licencePlate);

                }

            }
        });

        return view;
    }

    public void getInfo(String licencePlate)
    {
        Fragment fragment = new AddConductorFragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.addToBackStack("FRAGMENT");
        ft.commit();

        Bundle bundle = new Bundle();
        bundle.putString("LICENSE_PLATE",licencePlate);


        fragment.setArguments(bundle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

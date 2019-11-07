package com.isproject.ptps.fragments.owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isproject.ptps.R;
import com.isproject.ptps.Routes;
import com.isproject.ptps.SubRoute;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class EditChartFragment extends Fragment {

    TextView number, start, finish, viewChart;
    Spinner first_subroute, last_subroute;
    EditText price;
    String licencePlate;
    DatabaseReference reff;
    FirebaseDatabase firebaseDatabase;
    Button createSubroute,back;
    SubRoute subRoute=new SubRoute();
    String subrouteStart;
    String subrouteFinish;
    String subroutePrice;

    String routeNumber;
    String routeStart;
    String routeFinish;


    public EditChartFragment() {
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
        View view = inflater.inflate(R.layout.fragment_edit_chart, container, false);

        number = view.findViewById(R.id.textviewRouteNumber);

        start = view.findViewById(R.id.textviewRouteStart);

        finish = view.findViewById(R.id.textviewRouteFinish);

        first_subroute = view.findViewById(R.id.spinnerFirstSubroute);

        last_subroute = view.findViewById(R.id.spinnerLastSubroute);

        price=view.findViewById(R.id.editTextPrice);

        createSubroute=view.findViewById(R.id.buttonCreateSubroute);

        viewChart=view.findViewById(R.id.textviewViewChart);

        back=view.findViewById(R.id.buttonBack);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Bundle bundle = this.getArguments();
        routeNumber= bundle.getString("ROUTE_NUMBER");
        routeStart = bundle.getString("ROUTE_START");
        routeFinish = bundle.getString("ROUTE_FINISH");

        firebaseDatabase = FirebaseDatabase.getInstance();
        reff = firebaseDatabase.getReference();

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

        String userUID = current_user.getUid();
        Intent previousIntent = getActivity().getIntent();

        DatabaseReference db_ref = firebaseDatabase.getReference().child("Users")
                .child(userUID).child("vehicles");

        db_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                licencePlate = dataSnapshot.child("vehicle").getValue().toString();
                Toast.makeText(getContext(), licencePlate, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        createSubroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subrouteStart=first_subroute.getSelectedItem().toString();
                subrouteFinish=last_subroute.getSelectedItem().toString();
                subroutePrice=price.getText().toString().trim();

                subRoute.setSubrouteStart(subrouteStart);

                subRoute.setSubrouteFinish(subrouteFinish);

                subRoute.setSubroutePrice(subroutePrice);

                reff.child("Vehicles").child(licencePlate)
                        .child("FareChart/SubRoute").push().setValue(subRoute);
                //reff.push().setValue(fareChart);

                Toast.makeText(getActivity(),"Sub-Route added successfully",Toast.LENGTH_LONG).show();

            }
        });

        viewChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBack();
            }
        });



        number.setText(routeNumber);
        start.setText(routeStart);
        finish.setText(routeFinish);

        if(routeNumber == "1"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route1_));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route1_));
        }
        else if(routeNumber=="2") {
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route2_));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route2_));
        }
        else if(routeNumber=="3"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route3_));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route3_));
        }
        else if(routeNumber=="4"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route4));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route4));
        }
        else if(routeNumber=="6"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, Routes.route6));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route6));
        }
        else if(routeNumber=="7c"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route7c));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route7c));
        }
        else if(routeNumber=="8"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route8));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route8));
        } else if(routeNumber=="9"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route9));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route9));
        } else if(routeNumber=="11"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route11));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route11));
        }
        else if(routeNumber=="15"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route15));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route15));
        }
        else if(routeNumber=="14"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route14));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route14));
        }
        else if(routeNumber=="17B"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route17B));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route17B));
        }
        else if(routeNumber=="23"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route23));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route23));
        }
        else if(routeNumber=="24"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route24));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route24));
        }
        else if(routeNumber=="25"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route25));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route25));
        }
        else if(routeNumber=="33"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33));
        }
        else if(routeNumber=="33B"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33B));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33B));
        }
        else if(routeNumber=="34"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34));
        }
        else if(routeNumber=="34(buses)"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34_Buses));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34_Buses));
        } else if(routeNumber=="35/60"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route35_60_));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route35_60_));
        }
        else if(routeNumber=="44"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route44));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route44));
        }
        else if(routeNumber=="45"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route45));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route45));
        }
        else if(routeNumber=="58"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route58));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route58));
        }
        else if(routeNumber=="100"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route100));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route100));
        }
        else if(routeNumber=="102"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route102));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route102));
        }
        else if(routeNumber=="105"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route105_));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route105_));
        }
        else if(routeNumber=="106"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route106));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route106));
        }
        else if(routeNumber=="110"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route110_));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route110_));
        }
        else if(routeNumber=="111"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route111_));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route111_));
        }
        else if(routeNumber=="125/126"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route125_126));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route125_126));
        }
        else if(routeNumber=="146"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route146));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route146));
        }
        else if(routeNumber=="237"){
            first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route237));
            last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route237));
        }
        else{
            Toast.makeText(getContext(),"Invalid Selection",Toast.LENGTH_SHORT).show();
        }

    }
    public void getInfo()
    {
        Fragment fragment = new ViewFareChartFragment();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("LICENCE_PLATE", licencePlate);
        bundle.putString("ROUTE_NUMBER",routeNumber );
        bundle.putString("ROUTE_START", routeStart);
        bundle.putString("ROUTE_FINISH", routeFinish);
        fragment.setArguments(bundle);

        ft.replace(R.id.fl_content, fragment);
        ft.addToBackStack("FRAGMENT");
        ft.commit();
    }
    public void moveBack()
    {
        Fragment fragment = new FareChartFragment();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        ft.replace(R.id.fl_content, fragment);
        ft.addToBackStack("FRAGMENT");
        ft.commit();
    }
}

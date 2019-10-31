package com.isproject.ptps.fragments.owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ptps.FareChart;
import com.isproject.ptps.R;
import com.isproject.ptps.Routes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class FareChartFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner start,finish,route_number;

    Button create;
    String licencePlate;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String routeNumber;
    FareChart fareChart = new FareChart();

    public FareChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fare_chart, container, false);

        route_number=view.findViewById(R.id.spinnerRouteNumber);
        start=view.findViewById(R.id.spinnerRouteStart);
        finish=view.findViewById(R.id.spinnerRouteFinish);
        create=view.findViewById(R.id.buttonCreateChart);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        route_number.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.routes_numbers)
        {
            @Override
            public boolean isEnabled(int position) {
                if(position == 0) return false;
                else return true;
            }
        });

        route_number.setOnItemSelectedListener(this);

        routeNumber = route_number.getSelectedItem().toString();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

        String userUID = current_user.getUid();
        Intent previousIntent =getActivity().getIntent();

        DatabaseReference db_ref = firebaseDatabase.getReference().child("Users")
                .child(userUID).child("vehicles");


        /*db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                Toast.makeText(getContext(), key, Toast.LENGTH_LONG).show();
                licencePlate = dataSnapshot.child(key).getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

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

        DatabaseReference bdref =firebaseDatabase.getReference().child("Vehicles/" +
                licencePlate + "/FareChart");
        bdref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    //farechart node
                    String routeStart = dataSnapshot.getValue(FareChart.class).getRouteStart();
                    String routeFinish = dataSnapshot.getValue(FareChart.class).getRouteFinish();
                    String routeNumber = dataSnapshot.getValue(FareChart.class).getRouteNumber();
                    getInfo(routeNumber, routeStart, routeFinish);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final Bundle bundle = getArguments();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String routeNumber = route_number.getSelectedItem().toString();
                String routeStart=start.getSelectedItem().toString();
                String routeFinish=finish.getSelectedItem().toString();
                fareChart.setRouteStart(routeStart);
                fareChart.setRouteFinish(routeFinish);
                //FareChart fareChart = new FareChart(routeNumber, routeStart, routeFinish);
                databaseReference.child("Vehicles").child(licencePlate)
                        .child("FareChart").setValue(fareChart);
                Toast.makeText(getActivity(),"Chart Created successfully",Toast.LENGTH_LONG).show();
                getInfo(routeNumber,routeStart,routeFinish);
            }
        });

    }
    public void getInfo(String routeNumber,String routeStart,String routeFinish)
    {
        Fragment fragment = new EditChartFragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();

        Bundle bundle = new Bundle();
        bundle.putString("ROUTE_NUMBER", routeNumber);
        bundle.putString("ROUTE_START",routeStart);
        bundle.putString("ROUTE_FINISH",routeFinish);
        fragment.setArguments(bundle);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        routeNumber = spinner.getSelectedItem().toString();
        fareChart.setRouteNumber(routeNumber);
        if(routeNumber == "1"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route1_));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route1_));
        }
        else if(routeNumber=="2") {
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route2_));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route2_));
        }
        else if(routeNumber=="3"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route3_));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route3_));
        }
        else if(routeNumber=="4"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route4));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route4));
        }
        else if(routeNumber=="6"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route6));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route6));
        }
        else if(routeNumber=="7c"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route7c));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route7c));
        }
        else if(routeNumber=="8"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route8));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route8));
        } else if(routeNumber=="9"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route9));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route9));
        } else if(routeNumber=="11"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route11));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route11));
        }
        else if(routeNumber=="15"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route15));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route15));
        }
        else if(routeNumber=="14"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route14));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route14));
        }
        else if(routeNumber=="17B"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route17B));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route17B));
        }
        else if(routeNumber=="23"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route23));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route23));
        }
        else if(routeNumber=="24"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route24));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route24));
        }
        else if(routeNumber=="25"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route25));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route25));
        }
        else if(routeNumber=="33"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33));
        }
        else if(routeNumber=="33B"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33B));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33B));
        }
        else if(routeNumber=="34"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34));
        }
        else if(routeNumber=="34(buses)"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34_Buses));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34_Buses));
        } else if(routeNumber=="35/60"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route35_60_));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route35_60_));
        }
        else if(routeNumber=="44"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route44));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route44));
        }
        else if(routeNumber=="45"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route45));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route45));
        }
        else if(routeNumber=="58"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route58));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route58));
        }
        else if(routeNumber=="100"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route100));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route100));
        }
        else if(routeNumber=="102"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route102));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route102));
        }
        else if(routeNumber=="105"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route105_));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route105_));
        }
        else if(routeNumber=="106"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route106));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route106));
        }
        else if(routeNumber=="110"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route110_));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route110_));
        }
        else if(routeNumber=="111"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route111_));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route111_));
        }
        else if(routeNumber=="125/126"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route125_126));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route125_126));
        }
        else if(routeNumber=="146"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route146));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route146));
        }
        else if(routeNumber=="237"){
            start.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route237));
            finish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, Routes.route237));
        }
        else{
            Toast.makeText(getContext(),"Invalid Selection",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

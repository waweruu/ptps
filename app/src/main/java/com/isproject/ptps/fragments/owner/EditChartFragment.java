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
    Button createSubroute,back;
    SubRoute subRoute=new SubRoute();
    String subrouteStart;
    String subrouteFinish;
    String subroutePrice;

    public EditChartFragment() {
        // Required empty public constructor
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

        Bundle bundle = this.getArguments();
        final String routeNumber = bundle.getString("ROUTE_NUMBER");
        final String routeStart = bundle.getString("ROUTE_START");
        final String routeFinish = bundle.getString("ROUTE_FINISH");
        final String licencePlate = bundle.getString("LICENCE_PLATE");

        number.setText(routeNumber);
        start.setText(routeStart);
        finish.setText(routeFinish);

        setAdapter(routeNumber);

        createSubroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subrouteStart=first_subroute.getSelectedItem().toString();
                subrouteFinish=last_subroute.getSelectedItem().toString();
                subroutePrice=price.getText().toString().trim();

                subRoute.setSubrouteStart(subrouteStart);

                subRoute.setSubrouteFinish(subrouteFinish);

                subRoute.setSubroutePrice(subroutePrice);

                FirebaseDatabase.getInstance().getReference().child("Vehicles").child(licencePlate)
                        .child("FareChart/SubRoute").push().setValue(subRoute);
                //reff.push().setValue(fareChart);

                Toast.makeText(getActivity(),"Sub-Route added successfully",Toast.LENGTH_LONG).show();

            }
        });

        viewChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo(licencePlate, routeNumber, routeStart, routeFinish);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBack();
            }
        });



    }
    public void getInfo(String licencePlate, String routeNumber, String routeStart, String routeFinish)
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

    public void setAdapter(String routeNumber) {
        switch(routeNumber) {
            case "1":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route1_));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route1_));
                break;
            case "2":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route2_));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route2_));
                break;
            case "3":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route3_));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route3_));
                break;
            case "4":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route4));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route4));
                break;
            case "6":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, Routes.route6));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route6));
                break;
            case "7c":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route7c));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route7c));
                break;
            case "8":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route8));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route8));
                break;
            case "9":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route9));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route9));
                break;
            case "11":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route11));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route11));
                break;
            case "15":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route15));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route15));
                break;
            case "14":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route14));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route14));
                break;
            case "17B":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route17B));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route17B));
                break;
            case "23":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route23));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route23));
                break;
            case "24":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route24));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route24));
                break;
            case "25":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route25));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route25));
                break;
            case "33":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33));
                break;
            case "33B":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33B));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33B));
                break;
            case "34":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34));
                break;
            case "34(buses)":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34_Buses));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34_Buses));
                break;
            case "35/60":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route35_60_));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route35_60_));
                break;
            case "44":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route44));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route44));
                break;
            case "45":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route45));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route45));
                break;
            case "58":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route58));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route58));
                break;
            case "100":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route100));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route100));
                break;
            case "102":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route102));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route102));
                break;
            case "105":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route105_));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route105_));
                break;
            case "106":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route106));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route106));
                break;
            case "110":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route110_));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route110_));
                break;
            case "111":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route111_));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route111_));
                break;
            case "125/126":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route125_126));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route125_126));
                break;
            case "146":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route146));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route146));
                break;
            case "237":
                first_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route237));
                last_subroute.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route237));
                break;
            default:
                Toast.makeText(getContext(),"Invalid Selection",Toast.LENGTH_SHORT).show();
        }
    }
}

package com.isproject.ptps.fragments.owner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ptps.DataModelsAdapter;
import com.isproject.ptps.DataObject;
import com.isproject.ptps.FareChart;
import com.isproject.ptps.R;
import com.isproject.ptps.SubRoute;
import com.isproject.ptps.SubRoutesList;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ViewFareChartFragment extends Fragment {

//    final String licencePlate = this.getArguments().getString("LICENCE PLATE");
    RecyclerView recyclerView;
    ArrayList<DataObject> mDataModels = new ArrayList<>();
    ArrayList<SubRoute> mSubRoutes = new ArrayList<>();
    SubRoutesList list = new SubRoutesList();
    FareChart fareChart = new FareChart();
    //SubRoute subRoute = new SubRoute();

    public ViewFareChartFragment() {
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
        View view=inflater.inflate(R.layout.fragment_view_fare_chart, container, false);
        recyclerView=view.findViewById(R.id.recyclerViewShowFareChart);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();
        final String licencePlate = bundle.getString("LICENCE PLATE");

        String userUid = FirebaseAuth.getInstance().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vehicles");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //FareChart fareChart = new FareChart();
                /*fareChart.setRouteNumber(dataSnapshot.child(licencePlate).child("FareChart")
                        .getValue(FareChart.class).getRouteNumber());
                fareChart.setRouteStart(dataSnapshot.child(licencePlate).child("FareChart")
                        .getValue(FareChart.class).getRouteStart());
                fareChart.setRouteFinish(dataSnapshot.child(licencePlate).child("FareChart")
                        .getValue(FareChart.class).getRouteFinish());*/
                /*fareChart.setRouteNumber(dataSnapshot.child(licencePlate).child("FareChart/routeNumber")
                        .getValue().toString());
                fareChart.setRouteStart(dataSnapshot.child(licencePlate).child("FareChart/routeStart")
                        .getValue().toString());
                fareChart.setRouteFinish(dataSnapshot.child(licencePlate).child("FareChart/routeFinish")
                        .getValue().toString());*/

                fareChart = dataSnapshot.child(licencePlate).child("FareChart").getValue(FareChart.class);

                mDataModels.add(fareChart);

                for(DataSnapshot ds : dataSnapshot.child(licencePlate).child("FareChart/SubRoute").getChildren()) {
                    SubRoute subRoute = new SubRoute();
                    subRoute.setSubrouteStart(ds.getValue(SubRoute.class).getSubrouteStart());
                    subRoute.setSubrouteFinish(ds.getValue(SubRoute.class).getSubrouteFinish());
                    subRoute.setSubroutePrice(ds.getValue(SubRoute.class).getSubroutePrice());
                    //Toast.makeText(getContext(), subRoute.getSubroutePrice(), Toast.LENGTH_LONG).show();
                    mSubRoutes.add(subRoute);
                }

                mDataModels.add(list);

                DataModelsAdapter adapter = new DataModelsAdapter(mDataModels, mSubRoutes, getContext());
                //adapter.setmDataModelsList(mDataModels);
                LinearLayoutManager lean = new LinearLayoutManager(getContext());
                lean.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(lean);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

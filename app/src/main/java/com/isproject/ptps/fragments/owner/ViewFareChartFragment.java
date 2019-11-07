package com.isproject.ptps.fragments.owner;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.isproject.ptps.fragments.passenger.ViewVehicleFareChartFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
    String routeStart;
    String routeFinish;
    String routePrice;

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
        routeStart = bundle.getString("ROUTE_NUMBER");
        routeFinish = bundle.getString("ROUTE_START");
        routePrice = bundle.getString("ROUTE_FINISH");

        String userUid = FirebaseAuth.getInstance().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vehicles");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


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
                adapter.setmListener(new DataModelsAdapter.DataPasser() {
                    @Override
                    public void passData(final SubRoute subRoute) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setTitle("Update Sub Route");
                        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                Fragment fragment = new UpdateChartFragment();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.fl_content, fragment);
                                ft.commit();

                                Bundle bundle = new Bundle();
                                bundle.putString("LICENCE_PLATE", licencePlate);
                                bundle.putString("START", subRoute.getSubrouteStart());
                                bundle.putString("FINISH", subRoute.getSubrouteFinish());
                                bundle.putString("PRICE", subRoute.getSubroutePrice());
                                bundle.putString("ROUTE_NUMBER", routeStart);
                                bundle.putString("ROUTE_START", routeFinish);
                                bundle.putString("ROUTE_FINISH", routePrice);
                                fragment.setArguments(bundle);
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setCancelable(false);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
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

package com.isproject.ptps.fragments.passenger;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.isproject.ptps.mpesa.Mode;
import com.isproject.ptps.mpesa.Mpesa;
import com.isproject.ptps.mpesa.interfaces.AuthListener;
import com.isproject.ptps.mpesa.interfaces.MpesaListener;
import com.isproject.ptps.mpesa.models.STKPush;
import com.isproject.ptps.mpesa.utils.Pair;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ptps.DataModels;
import com.isproject.ptps.DataModelsAdapter;
import com.isproject.ptps.DataObject;
import com.isproject.ptps.FareChart;
import com.isproject.ptps.Operator;
import com.isproject.ptps.R;
import com.isproject.ptps.SubRoute;
import com.isproject.ptps.SubRouteAdapter;
import com.isproject.ptps.SubRoutesList;
import com.isproject.ptps.User;
import com.isproject.ptps.UserHolder;
import com.isproject.ptps.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class ViewVehicleFareChartFragment extends Fragment {

    Operator conductor = new Operator(), driver = new Operator();
    Vehicle vehicle = new Vehicle();
    FareChart fareChart = new FareChart();
    SubRoutesList list = new SubRoutesList();
    ArrayList<DataObject> mDataModels;
    ArrayList<SubRoute> mSubRoutes;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    public static final String BUSINESS_SHORT_CODE = "174379";
    public static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String CONSUMER_KEY = "oMbBhDW1Dj4HewRIjaZXTJNDjQ06b48z";
    public static final String CONSUMER_SECRET = "KAA2rAkwbDUxgXWe";
    public static final String CALLBACK_URL = "https://us-central1-public-transport-system-84f9c.cloudfunctions.net/details/";
    public static final String SHARED_PREFERENCES = "com.isproject.ptps";
    public static final String USER_UID = FirebaseAuth.getInstance().getUid();

    public ViewVehicleFareChartFragment() {
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
        View view = inflater.inflate(R.layout.fragment_view_vehicle_fare_chart, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewBoo);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Mpesa.with(getContext(), CONSUMER_KEY, CONSUMER_SECRET, Mode.SANDBOX);

        final Bundle bundle = this.getArguments();
        final String licencePlate = bundle.getString("LICENCE_PLATE");

        Query query = FirebaseDatabase.getInstance().getReference().child("Vehicles")
                .orderByKey().equalTo(licencePlate).limitToFirst(1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDataModels = new ArrayList<>();
                mSubRoutes = new ArrayList<>();

                vehicle.setVehicleType(dataSnapshot.child(licencePlate).child("Vehicle Details")
                        .getValue(Vehicle.class).getVehicleType());
                vehicle.setVehicleCapacity(dataSnapshot.child(licencePlate).child("Vehicle Details")
                        .getValue(Vehicle.class).getVehicleCapacity());

                conductor.setFirstName(dataSnapshot.child(licencePlate).child("Conductor")
                        .getValue(Operator.class).getFirstName());
                conductor.setLastName(dataSnapshot.child(licencePlate).child("Conductor")
                        .getValue(Operator.class).getLastName());
                conductor.setIdNumber(dataSnapshot.child(licencePlate).child("Conductor")
                        .getValue(Operator.class).getIdNumber());
                conductor.setPhoneNumber(dataSnapshot.child(licencePlate).child("Conductor")
                        .getValue(Operator.class).getPhoneNumber());

                driver.setFirstName(dataSnapshot.child(licencePlate).child("Driver")
                        .getValue(Operator.class).getFirstName());
                driver.setLastName(dataSnapshot.child(licencePlate).child("Driver")
                        .getValue(Operator.class).getLastName());
                driver.setIdNumber(dataSnapshot.child(licencePlate).child("Driver")
                        .getValue(Operator.class).getIdNumber());
                driver.setPhoneNumber(dataSnapshot.child(licencePlate).child("Driver")
                        .getValue(Operator.class).getPhoneNumber());

                fareChart.setRouteNumber(dataSnapshot.child(licencePlate).child("FareChart")
                        .getValue(FareChart.class).getRouteNumber());
                fareChart.setRouteStart(dataSnapshot.child(licencePlate).child("FareChart")
                        .getValue(FareChart.class).getRouteStart());
                fareChart.setRouteFinish(dataSnapshot.child(licencePlate).child("FareChart")
                        .getValue(FareChart.class).getRouteFinish());

                mDataModels.add(vehicle);
                mDataModels.add(conductor);
                mDataModels.add(driver);
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
                //toastMessage(fareChart.getRouteNumber());

                DataModelsAdapter adapter = new DataModelsAdapter(mDataModels, mSubRoutes, getContext());
                adapter.setmListener(new DataModelsAdapter.DataPasser() {
                    @Override
                    public void passData(SubRoute subRoute) {
                        int amount = Integer.parseInt(subRoute.getSubroutePrice());
                        String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                        phoneNumber = phoneNumber.substring(1);
                        //String phoneNumber = "0712771254";
                        Toast.makeText(getContext(), phoneNumber, Toast.LENGTH_LONG).show();

                        builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Processing payment");
                        builder.setCancelable(true);
                        alertDialog = builder.create();
                        alertDialog.show();

                        String[] keys = licencePlate.split(" ");
                        String numberPlate = keys[0] + "_" + keys[1];

                        STKPush.Builder stkBuilder = new STKPush.Builder(BUSINESS_SHORT_CODE, PASSKEY,
                                amount, BUSINESS_SHORT_CODE, phoneNumber, CALLBACK_URL +
                                numberPlate + "/" + USER_UID + "/" + subRoute.getSubrouteStart() +
                                "/" + subRoute.getSubrouteFinish());
                        //stkBuilder.setCallBackURL(CALLBACK_URL);

                        //SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES,
                                //Context.MODE_PRIVATE);
                        //String token = sharedPreferences.getString("InstanceID", "");
                        //stkBuilder.setFirebaseRegID(token);
                        STKPush push = stkBuilder.build();
                        Toast.makeText(getContext(), push.getCallBackURL(), Toast.LENGTH_LONG).show();
                        Mpesa.getInstance().pay(getContext(), push);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

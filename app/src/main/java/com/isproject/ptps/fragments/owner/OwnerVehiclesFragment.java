package com.isproject.ptps.fragments.owner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.isproject.ptps.NumberPlate;
import com.isproject.ptps.R;
import com.isproject.ptps.Vehicle;
import com.isproject.ptps.activities.LandingTwoActivity;

import java.util.ArrayList;


public class OwnerVehiclesFragment extends Fragment {

    ArrayList<DataObject> mDataModels = new ArrayList<>();
    Vehicle vehicle = new Vehicle();
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    String licencePlate;
    Button backButton;

    public OwnerVehiclesFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_owner_vehicles, container, false);
        recyclerView=view.findViewById(R.id.recyclerViewDisplayOwnerVehicles);

        backButton=view.findViewById(R.id.buttonBackFromOwnerVehicles);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBack();
            }
        });



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String userUID = FirebaseAuth.getInstance().getUid();
        Toast.makeText(getContext(), "This is ovc user: " + userUID, Toast.LENGTH_LONG).show();

        Query db_ref = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userUID).child("vehicles");
        db_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Toast.makeText(getContext(), dataSnapshot.getValue(NumberPlate.class).getNumberPlate(), Toast.LENGTH_SHORT).show();
                final NumberPlate numberPlate = dataSnapshot.getValue(NumberPlate.class);
                //numberPlate.setNumberPlate(dataSnapshot.getValue().toString());
                mDataModels.add(numberPlate);
                DataModelsAdapter adapter = new DataModelsAdapter(mDataModels, null, getContext());
                adapter.setmCallbacks(new DataModelsAdapter.OnLicencePlateSelectedListener() {
                    @Override
                    public void onPositiveClick() {
                        Toast.makeText(getContext(), numberPlate.getNumberPlate(), Toast.LENGTH_LONG).show();
                        /*Fragment fragment = new ViewFareChartFragment();

                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fl_content, fragment);
                        ft.commit();

                        Bundle bundle = new Bundle();
                        bundle.putString("LICENCE PLATE", numberPlate.getNumberPlate());
                        fragment.setArguments(bundle);*/
                    }
                });

                recyclerView.setHasFixedSize(true);
                LinearLayoutManager lean = new LinearLayoutManager(getContext());
                lean.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(lean);
                recyclerView.setAdapter(adapter);


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
        DataModelsAdapter adapter = new DataModelsAdapter(mDataModels,null, getContext());
        //adapter.setmDataModelsList(mDataModels);


    }
    public void moveBack()
    {
        Intent intent=new Intent(getActivity(),LandingTwoActivity.class);
        startActivity(intent);
    }

}

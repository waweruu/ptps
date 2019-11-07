package com.isproject.ptps.fragments.owner;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.isproject.ptps.DataModelsAdapter;
import com.isproject.ptps.DataObject;
import com.isproject.ptps.NumberPlate;
import com.isproject.ptps.R;
import com.isproject.ptps.Vehicle;
import com.isproject.ptps.activities.LandingTwoActivity;

import java.util.ArrayList;


public class DisplayLicencesFragment extends Fragment {
    ArrayList<DataObject> mDataModels = new ArrayList<>();
    Vehicle vehicle = new Vehicle();
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    String licencePlate;
    Button backButton;


    public DisplayLicencesFragment() {
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
        View view= inflater.inflate(R.layout.fragment_display_licences, container, false);

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
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

        String userUID = current_user.getUid();
        Toast.makeText(getContext(), "This is dlf user: " + userUID, Toast.LENGTH_SHORT).show();

        DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userUID).child("vehicles");
        db_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                NumberPlate numberPlate = new NumberPlate();
                numberPlate.setNumberPlate(dataSnapshot.getValue().toString());

                mDataModels.add(numberPlate);
                DataModelsAdapter adapter = new DataModelsAdapter(mDataModels, null, getContext());
                adapter.setmCallbacks(new DataModelsAdapter.OnLicencePlateSelectedListener() {
                    @Override
                    public void onPositiveClick() {
                        Fragment fragment = new PaymentDetailsFragment();

                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fl_content, fragment);
                        ft.commit();

                        Bundle bundle = new Bundle();
                        bundle.putString("LICENCE_PLATE", dataSnapshot.getValue().toString());
                        fragment.setArguments(bundle);
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


    }

    public void moveBack()
    {
        Intent intent=new Intent(getActivity(), LandingTwoActivity.class);
        startActivity(intent);
    }


}

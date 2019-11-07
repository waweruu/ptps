package com.isproject.ptps.fragments.operator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ptps.DataModelsAdapter;
import com.isproject.ptps.DataObject;
import com.isproject.ptps.PaymentReceipt;
import com.isproject.ptps.R;
import com.isproject.ptps.Trip;
import com.isproject.ptps.mpesa.utils.TimeUtil;

import java.util.ArrayList;

public class PassengerPaymentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button start, stop;
    private ArrayList<DataObject> mDataModels = new ArrayList<>();
    private long startTime, stopTime = 0;
    private DataModelsAdapter startAdapter, stopAdapter;
    private String licencePlate;

    public PassengerPaymentsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_passenger_payments, container, false);
        recyclerView = view.findViewById(R.id.paymentsRecyclerView);
        LinearLayoutManager lean = new LinearLayoutManager(getContext());
        lean.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lean);
        start = view.findViewById(R.id.buttonStartTrip);
        stop = view.findViewById(R.id.buttonStopTrip);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Starting trip...", Toast.LENGTH_LONG).show();
                startTime = Long.parseLong(TimeUtil.getTimestamp());
                getLicencePlate();
                Query query = FirebaseDatabase.getInstance().getReference().child("Payments")
                        .orderByChild("transactionDate").startAt(startTime);

                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        PaymentReceipt receipt = dataSnapshot.getValue(PaymentReceipt.class);
                        if(receipt.getLicencePlate().equals(licencePlate)) mDataModels.add(receipt);
                        startAdapter = new DataModelsAdapter(mDataModels, null, getContext());
                        recyclerView.setAdapter(startAdapter);
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
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startTime == 0) {
                    AlertDialog.Builder warningBuilder = new AlertDialog.Builder(getContext());
                    warningBuilder.setCancelable(false);
                    warningBuilder.setTitle("No trip to stop");
                    warningBuilder.setMessage("You cannot stop a trip you have not started.");
                    warningBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog warning = warningBuilder.create();
                    warning.show();
                } else {
                    Toast.makeText(getContext(), "Stopping trip...", Toast.LENGTH_LONG).show();
                    String time = TimeUtil.getTimestamp();
                    stopTime = Long.parseLong(time);
                    mDataModels.clear();
                    startAdapter = null;
                    Trip trip = new Trip(startTime, stopTime);
                    startTime = 0;
                    stopTime = 0;
                    getLicencePlate();
                    FirebaseDatabase.getInstance().getReference().child("Vehicles/" + licencePlate + "/Trips").push().setValue(trip);
                    Query query = FirebaseDatabase.getInstance().getReference().child("Payments")
                            .orderByChild("transactionDate").startAt(startTime).endAt(stopTime);
                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if (dataSnapshot.getValue(PaymentReceipt.class).getLicencePlate().equals("KDB 017B")) {
                                PaymentReceipt receipt = dataSnapshot.getValue(PaymentReceipt.class);
                                if (receipt.getTransactionDate() <= stopTime)
                                    mDataModels.add(receipt);
                                stopAdapter = new DataModelsAdapter(mDataModels, null, getContext());
                                recyclerView.setAdapter(stopAdapter);
                            }
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
            }
        });
    }

    public void getLicencePlate() {
        String userUid = FirebaseAuth.getInstance().getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(userUid)
                .child("vehicle");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                licencePlate = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
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

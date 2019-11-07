package com.isproject.ptps.fragments.passenger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.isproject.ptps.DataModelsAdapter;
import com.isproject.ptps.DataObject;
import com.isproject.ptps.PaymentReceipt;
import com.isproject.ptps.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<DataObject> mDataModels = new ArrayList<>();

    public PaymentHistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_payment_history, container, false);
        recyclerView = view.findViewById(R.id.paymentHistoryRecyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String userUid = FirebaseAuth.getInstance().getUid();

        Query query = FirebaseDatabase.getInstance().getReference().child("Payments").orderByChild("userUid").equalTo(userUid);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PaymentReceipt receipt = dataSnapshot.getValue(PaymentReceipt.class);
                mDataModels.add(receipt);
                DataModelsAdapter adapter = new DataModelsAdapter(mDataModels, null, getContext());
                LinearLayoutManager lean = new LinearLayoutManager(getContext());
                lean.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setHasFixedSize(true);
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
}

package com.isproject.ptps.fragments.owner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.isproject.ptps.DataModelsAdapter;
import com.isproject.ptps.DataObject;
import com.isproject.ptps.R;
import com.isproject.ptps.Review;

import java.util.ArrayList;

public class ReviewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<DataObject> mDataObjects = new ArrayList<>();

    public ReviewsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        recyclerView  = view.findViewById(R.id.reviewsSpecRecycler);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();
        String licencePlate = bundle.getString("LICENCE_PLATE");

        Query query = FirebaseDatabase.getInstance().getReference().child("Reviews")
                .orderByChild("licencePlate").equalTo(licencePlate);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //if(dataSnapshot.exists()) Toast.makeText(getContext(), "No Daa!",
                //Toast.LENGTH_SHORT).show();
                Review review = dataSnapshot.getValue(Review.class);
                Toast.makeText(getContext(), review.getTimeStamp(), Toast.LENGTH_SHORT).show();
                mDataObjects.add(review);

                LinearLayoutManager lean = new LinearLayoutManager(getContext());
                lean.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(lean);
                DataModelsAdapter adapter = new DataModelsAdapter(mDataObjects, null, getContext());
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

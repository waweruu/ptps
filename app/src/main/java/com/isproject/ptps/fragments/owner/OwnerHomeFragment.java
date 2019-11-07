package com.isproject.ptps.fragments.owner;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ptps.R;
import com.isproject.ptps.fragments.passenger.PassengerHomeFragment;

public class OwnerHomeFragment extends Fragment {

    private OnOwnerCardClickedListener mlistener;

    TextView displayFirstName, displayLastName;
    private CardView farechart,payment_details,passenger_reviews,add_vehicle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owner_home, container, false);
        displayFirstName=view.findViewById(R.id.textdisplayFirstName);
        displayLastName=view.findViewById(R.id.textdisplayLastName);

        farechart=view.findViewById(R.id.cardFareChart);
        payment_details=view.findViewById(R.id.cardPaymentDetails);
        passenger_reviews=view.findViewById(R.id.cardPassengerReviews);
        add_vehicle=view.findViewById(R.id.cardAddVehicle);
//        owner_account=view.findViewById(R.id.cardOwnerAccount);

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference=firebaseDatabase.getReference();

        FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();

        final String userUID=current_user.getUid();

        databaseReference.child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName=dataSnapshot.child(userUID).child("firstName").getValue(String.class);
                displayFirstName.setText(firstName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        farechart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mlistener != null) {
                    mlistener.goToOwnerFragment("farechart");
                }
            }
        });

        payment_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mlistener != null) {
                    mlistener.goToOwnerFragment("details");
                }
            }
        });

        passenger_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mlistener != null) {
                    mlistener.goToOwnerFragment("reviews");
                }
            }
        });
        add_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mlistener != null) {
                    mlistener.goToOwnerFragment("vehicle");
                }
            }
        });

    }
   public interface OnOwnerCardClickedListener{
        void goToOwnerFragment(String fragment);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnOwnerCardClickedListener) {
            mlistener = (OnOwnerCardClickedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCardClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mlistener = null;
    }
}




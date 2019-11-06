package com.isproject.ptps.fragments.owner;

import android.content.Context;
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
import com.isproject.ptps.NumberPlate;
import com.isproject.ptps.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseLicencePlateFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<DataObject> mDataObjects = new ArrayList<>();
    OnLicencePlateClicked onLicencePlateClickedListener;

    public ChooseLicencePlateFragment() {
        //
    }

    public interface OnLicencePlateClicked {
        void sendSelectedLicencePlate(String licencePlate);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_licence_plate, container, false);
        recyclerView = view.findViewById(R.id.chooseLicencePlatesRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lean = new LinearLayoutManager(getContext());
        lean.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(lean);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String userUid = FirebaseAuth.getInstance().getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(userUid)
                .child("vehicles");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NumberPlate plate = dataSnapshot.getValue(NumberPlate.class);
                mDataObjects.add(plate);

                DataModelsAdapter adapter = new DataModelsAdapter(mDataObjects, null, getContext());
                adapter.setmCallback(new DataModelsAdapter.OnNumberPlateClicked() {
                    @Override
                    public void sendNumberPlate(String numberPlate) {
                        //Toast.makeText(getContext(), "" + numberPlate + " clicked!", Toast.LENGTH_SHORT).show();

                        onLicencePlateClickedListener.sendSelectedLicencePlate(numberPlate);

                        /*Fragment fragment = new ReviewsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("LICENCE_PLATE", numberPlate);
                        fragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fl_content, fragment);
                        ft.commit();*/
                    }
                });
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

    public void onAddChildToParentFragment(Fragment fragment) {
        try {
            onLicencePlateClickedListener = (OnLicencePlateClicked) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnLicencePlateClickedListener"
            );
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onAddChildToParentFragment(getParentFragment());
    }
}

package com.isproject.ptps.fragments.owner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.isproject.ptps.R;
import com.isproject.ptps.User;
import com.isproject.ptps.UserHolder;
import com.isproject.ptps.activities.LandingActivity;
import com.isproject.ptps.activities.WelcomeActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OwnerAccountFragment extends Fragment {

    private Button buttonSignOut;
    private RecyclerView recyclerView;
    private ArrayList<User> arrayList;
    private FirebaseRecyclerOptions<User> options;
    private FirebaseRecyclerAdapter<User, UserHolder> adapter;

    public OwnerAccountFragment() {
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
        View view = inflater.inflate(R.layout.fragment_owner_account, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewUserInfo);
        buttonSignOut = view.findViewById(R.id.button_sign_out);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<User>();

        String userUid = currentUser.getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByKey().equalTo(userUid).limitToFirst(1);

        /*SnapshotParser<User> snapshotParser = new SnapshotParser<User>() {
            @NonNull
            @Override
            public User parseSnapshot(@NonNull DataSnapshot snapshot) {
                return snapshot.child("User").getValue(User.class);
            }
        };*/

        options = new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, UserHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull User model) {
                holder.textFirstName.setText(model.getFirstName());
                holder.textLastName.setText(model.getLastName());
                holder.textPhoneNumber.setText(model.getPhoneNumber());
                holder.textUserType.setText(model.getUserType());
            }

            @Override
            public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.user_info, parent, false);
                return new UserHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(getActivity().getApplicationContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(getActivity().getApplicationContext(), WelcomeActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }
}

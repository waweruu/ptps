package com.isproject.ptps.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.isproject.ptps.R;
import com.isproject.ptps.User;
import com.isproject.ptps.UserHolder;

import java.util.ArrayList;


public class LandingActivity extends AppCompatActivity {

    private Button buttonSignOut;
    private RecyclerView recyclerView;
    private ArrayList<User> arrayList;
    private FirebaseRecyclerOptions<User> options;
    private FirebaseRecyclerAdapter<User, UserHolder> adapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        /*textView = findViewById(R.id.textViewType);
        Intent in = getIntent();
        String a = in.getStringExtra("PUT");
        String b = in.getStringExtra("PHONE");
        textView.setText(a + " " + b);*/

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            //user is signed in
            recyclerView = findViewById(R.id.recyclerViewUserInfo);
            recyclerView.setHasFixedSize(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);

            arrayList = new ArrayList<User>();

            String userUid = currentUser.getUid();
            /*Query query = FirebaseDatabase.getInstance().getReference()
                    .child("Users/" + userUid);*/

            Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                    .orderByKey().equalTo(userUid);

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
                    View view = LayoutInflater.from(LandingActivity.this)
                            .inflate(R.layout.user_info, parent, false);
                    return new UserHolder(view);
                }
            };

            recyclerView.setAdapter(adapter);

        } else {
            //user is signed out
            Intent intent = new Intent(LandingActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }


        buttonSignOut = findViewById(R.id.button_sign_out);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(LandingActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(LandingActivity.this, WelcomeActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LandingActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }
}

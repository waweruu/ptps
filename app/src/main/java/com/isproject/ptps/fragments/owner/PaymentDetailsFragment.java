package com.isproject.ptps.fragments.owner;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ptps.PaymentReceipt;
import com.isproject.ptps.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentDetailsFragment extends Fragment {

    TextView labelDay,labelWeek,labelMonth,textPaymentsDay,textPaymentsWeek,textPaymentsMonth;
    View subItemDay,subItemWeek,subItemMonth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    PaymentReceipt paymentReceipt=new PaymentReceipt();
    String licencePlate;

    List<PaymentReceipt> paymentsList;

    public PaymentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = this.getArguments();
        licencePlate = bundle.getString("LICENCE_PLATE");

//        Log.e("ASD", licencePlate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_payment_details, container, false);

        labelDay=view.findViewById(R.id.labelDayPayments);
        labelWeek=view.findViewById(R.id.labelWeekPayments);
        labelMonth=view.findViewById(R.id.labelMonthPayments);
        textPaymentsDay=view.findViewById(R.id.day_payments_total);
        textPaymentsWeek=view.findViewById(R.id.week_payments_total);
        textPaymentsMonth=view.findViewById(R.id.month_payments_total);

        subItemDay=view.findViewById(R.id.subItemDayPayments);
        subItemWeek=view.findViewById(R.id.subItemWeekPayments);
        subItemMonth=view.findViewById(R.id.subItemMonthPayments);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

        String userUID = current_user.getUid();

        DatabaseReference db_ref = databaseReference
                .child("Users")
                .child(userUID).child("vehicles");

        databaseReference=firebaseDatabase.getReference().child("Payments");

        final Query payments = databaseReference.orderByChild("licencePlate").equalTo(licencePlate);

        payments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, PaymentReceipt> td = (HashMap<String, PaymentReceipt>) dataSnapshot.getValue();

//                Collection<PaymentReceipt> values = td.values();
                ArrayList<PaymentReceipt> valueList = new ArrayList<PaymentReceipt>(td.values());

//                Log.e("ASD", dataSnapshot.getValue().toString());

                if (td != null) {
                    paymentsList = valueList;
                    Log.e("ASD", paymentsList.toString());
                }
                else
                    Toast.makeText(getContext(), "No Payments available", Toast.LENGTH_SHORT).show();

//                Log.e("ASD", values.toString());

                calculatePayments();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                licencePlate = dataSnapshot.child("vehicle").getValue().toString();
//                List<PaymentReceipt> pr = new ArrayList<>();
//
//                pr.add(dataSnapshot.getValue(PaymentReceipt.class));
//
////                for (DataSnapshot d: dataSnapshot.getChildren()) {
//                    Log.e("ASD", pr.toString());
////                }

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


        return view;
    }

    private void calculatePayments() {
        textPaymentsDay.setText(calculateDaysPayments());
    }

    private String calculateDaysPayments() {
        List<PaymentReceipt> payments = new ArrayList<>();

//        Log.e("ASD", paymentsList.toString());

        for (PaymentReceipt pr: paymentsList) {
            if (DateUtils.isToday(pr.getTransactionDate()))
                payments.add(pr);
        }
//
        Log.e("ASD", payments.toString());

        return "1";
    }
}

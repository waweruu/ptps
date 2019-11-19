package com.isproject.ptps.fragments.owner;

import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.isproject.ptps.DataObject;
import com.isproject.ptps.PaymentReceipt;
import com.isproject.ptps.R;
import com.isproject.ptps.activities.Utilities;
import com.isproject.ptps.mpesa.utils.TimeUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PaymentDetailsFragment extends Fragment implements
        ChooseLicencePlateFragment.OnLicencePlateClicked, ViewPaymentHistoryFragment.OnGoBackButtonClicked {

    TextView textPaymentsDay,textPaymentsPastMonth, textPaymentsCurrentMonth;
    View subItemDay,subItemWeek,subItemMonth;
    onPaymentDetailClicked mClicker;
    String licencePlate;
    Fragment fragment = new ChooseLicencePlateFragment();
    Fragment paymentHistoryFrag;
    ArrayList<Long> totalCurrentMonthAmount = new ArrayList<>();
    ArrayList<Long> totalPastMonthAmount = new ArrayList<>();
    ArrayList<Long> totalDayAmount = new ArrayList<>();
    ArrayList<DataObject> mCurrentMonth = new ArrayList<>();
    ArrayList<DataObject> mPastMonth = new ArrayList<>();
    ArrayList<DataObject> mDay = new ArrayList<>();


    public PaymentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mClicker = (onPaymentDetailClicked) paymentHistoryFrag;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement onPaymentDetailClicked"
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_details, container, false);

        textPaymentsDay = view.findViewById(R.id.day_payments_total);
        textPaymentsCurrentMonth = view.findViewById(R.id.current_month_payments_total);
        textPaymentsPastMonth = view.findViewById(R.id.past_month_payments_total);

        subItemDay = view.findViewById(R.id.subItemDayPayments);
        subItemWeek = view.findViewById(R.id.subItemWeekPayments);
        subItemMonth = view.findViewById(R.id.subItemMonthPayments);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addLicencePlateFragment();
    }

    private void addLicencePlateFragment() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentPaymentDetails, fragment);
        ft.addToBackStack("ChoosePlateToReview");
        ft.commit();
    }

    @Override
    public void sendSelectedLicencePlate(String licencePlate) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();

        //showing payment details
        loadPaymentDetails(licencePlate);
    }

    public void loadPaymentDetails(String licencePlate) {
        Query payments = FirebaseDatabase.getInstance().getReference("Payments")
                .orderByChild("licencePlate").equalTo(licencePlate);

        payments.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String date = TimeUtil.getTimestamp();
                PaymentReceipt receipt = dataSnapshot.getValue(PaymentReceipt.class);
                String receiptDate = String.valueOf(receipt.getTransactionDate());
                if (receiptDate.substring(4, 6).equals(date.substring(4, 6))) {
                    //receiptDate equals current month
                    mCurrentMonth.add(receipt);
                    totalCurrentMonthAmount.add(receipt.getAmount());
                } else if (receiptDate.substring(6, 8).equals(date.substring(6, 8))) {
                    //receiptDate equals day
                    mDay.add(receipt);
                    totalDayAmount.add(receipt.getAmount());
                } else if(receiptDate.substring(4, 6).equals(String.valueOf(Long.valueOf(date.substring(4, 6)) - 1))) {
                    //receiptDate equals past month
                    mPastMonth.add(receipt);
                    totalPastMonthAmount.add(receipt.getAmount());
                }

                textPaymentsCurrentMonth.setText(String.valueOf(Utilities.getArrayListSum(totalCurrentMonthAmount)));
                textPaymentsDay.setText(String.valueOf(Utilities.getArrayListSum(totalDayAmount)));
                textPaymentsPastMonth.setText(String.valueOf(Utilities.getArrayListSum(totalPastMonthAmount)));

                textPaymentsPastMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*mClicker.sendDetails(mPastMonth);
                        FragmentManager fm = getChildFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentPaymentDetails, paymentHistoryFrag);
                        ft.addToBackStack("PaymentHistoryPastMonthFragment");
                        ft.commit();*/
                        paymentHistoryFrag = new ViewPaymentHistoryFragment(mPastMonth);
                        FragmentManager fm = getChildFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentPaymentDetails, paymentHistoryFrag);
                        ft.addToBackStack("PaymentHistoryPastMonthFragment");
                        ft.commit();
                    }
                });

                textPaymentsCurrentMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*mClicker.sendDetails(mCurrentMonth);
                        FragmentManager fm = getChildFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentPaymentDetails, paymentHistoryFrag);
                        ft.addToBackStack("PaymentHistoryCurrentMonthFragment");
                        ft.commit();*/
                        paymentHistoryFrag = new ViewPaymentHistoryFragment(mCurrentMonth);
                        FragmentManager fm = getChildFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentPaymentDetails, paymentHistoryFrag);
                        ft.addToBackStack("PaymentHistoryCurrentMonthFragment");
                        ft.commit();
                    }
                });

                textPaymentsDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*mClicker.sendDetails(mDay);
                        FragmentManager fm = getChildFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentPaymentDetails, paymentHistoryFrag);
                        ft.addToBackStack("PaymentHistoryTodayFragment");
                        ft.commit();*/
                        paymentHistoryFrag = new ViewPaymentHistoryFragment(mDay);
                        FragmentManager fm = getChildFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentPaymentDetails, paymentHistoryFrag);
                        ft.addToBackStack("PaymentHistoryDayFragment");
                        ft.commit();
                    }
                });
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

    @Override
    public void sendResponse() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(paymentHistoryFrag);
        ft.commit();
    }

    public interface onPaymentDetailClicked {
        void sendDetails(ArrayList<DataObject> mDataModels);
    }
}

package com.isproject.ptps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubRouteAdapter extends RecyclerView.Adapter {

    private ArrayList<SubRoute> mSubRoutesList;
    private Context context;
    OnSubRouteSelectedListener onSubRouteSelectedListener;

    public OnSubRouteSelectedListener getOnSubRouteSelectedListener() {
        return onSubRouteSelectedListener;
    }

    public void setOnSubRouteSelectedListener(OnSubRouteSelectedListener onSubRouteSelectedListener) {
        this.onSubRouteSelectedListener = onSubRouteSelectedListener;
    }

    public SubRouteAdapter(ArrayList<SubRoute> mSubRoutesList, Context context) {
        this.mSubRoutesList = mSubRoutesList;
        this.context = context;
        //this.onSubRouteSelectedListener = onSubRouteSelectedListener;
    }

    public interface OnSubRouteSelectedListener {
        void paymentDetails(SubRoute subRoute);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subroute_info, parent, false);
        return new SubRouteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SubRouteViewHolder) holder).bindView(position);
        //TODO: Implement onClickListener
        View view = holder.itemView;
        TextView textPrice = view.findViewById(R.id.textRoutePrice);
        TextView textStart = view.findViewById(R.id.textRouteStart);
        TextView textFinish = view.findViewById(R.id.textRouteFinish);

        final SubRoute subRoute = new SubRoute();
        subRoute.setSubroutePrice(textPrice.getText().toString());
        subRoute.setSubrouteStart(textStart.getText().toString());
        subRoute.setSubrouteFinish(textFinish.getText().toString());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubRouteSelectedListener.paymentDetails(subRoute);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        if(mSubRoutesList == null) {
            return 0;
        } else {
            return mSubRoutesList.size();
        }
    }

    public class SubRouteViewHolder extends RecyclerView.ViewHolder {

        private TextView subrouteStart, subrouteFinish, subroutePrice;
        private View subItem;


        public SubRouteViewHolder(@NonNull View itemView) {
            super(itemView);

            subrouteStart = itemView.findViewById(R.id.textRouteStart);
            subrouteFinish = itemView.findViewById(R.id.textRouteFinish);
            subroutePrice = itemView.findViewById(R.id.textRoutePrice);

            subItem = itemView.findViewById(R.id.subItemSubRoutes);
        }

        public void bindView(int position) {
            SubRoute subRoute = mSubRoutesList.get(position);

            subrouteStart.setText(subRoute.getSubrouteStart());
            subrouteFinish.setText(subRoute.getSubrouteFinish());
            subroutePrice.setText(subRoute.getSubroutePrice());
        }
    }
}

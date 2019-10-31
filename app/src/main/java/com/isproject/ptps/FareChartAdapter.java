package com.isproject.ptps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FareChartAdapter extends RecyclerView.Adapter<FareChartAdapter.MyViewHolder>{

    Context context;
    ArrayList<FareChart> mFareChartList;

    public FareChartAdapter(FragmentActivity activity, ArrayList<FareChart> mFareChartList) {

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.farechart_info,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        View view = holder.itemView;
        TextView textNumber = view.findViewById(R.id.textviewRouteNumber);
        TextView textStart = view.findViewById(R.id.textviewRouteStart);
        TextView textFinish = view.findViewById(R.id.textviewRouteFinish);
        TextView textPrice = view.findViewById(R.id.textviewSubRoutePrice);
        TextView textSubrouteStart = view.findViewById(R.id.textviewSubRouteStart);
        TextView textSubrouteFinish = view.findViewById(R.id.textviewSubRouteFinish);


        final FareChart farechart = new FareChart();
        farechart.setRouteNumber(textNumber.getText().toString());
        farechart.setRouteStart(textStart.getText().toString());
        farechart.setRouteFinish(textFinish.getText().toString());
        //farechart.setSubrouteStart(textSubrouteStart.getText().toString());
        //farechart.setSubrouteFinish(textSubrouteFinish.getText().toString());
        //farechart.setSubroutePrice(textPrice.getText().toString());

    }

    @Override
    public int getItemCount() {
        if(mFareChartList == null) {
            return 0;
        } else {
            return mFareChartList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView route_number,route_start,route_finish,subroute_start,subroute_finish,subroute_price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            route_number=itemView.findViewById(R.id.textviewRouteNumber);
            route_start=itemView.findViewById(R.id.textviewRouteStart);
            route_finish=itemView.findViewById(R.id.textviewRouteFinish);
            subroute_start=itemView.findViewById(R.id.textviewSubRouteStart);
            subroute_finish=itemView.findViewById(R.id.textviewSubRouteFinish);
            subroute_price=itemView.findViewById(R.id.textviewSubRoutePrice);

        }
    }
}

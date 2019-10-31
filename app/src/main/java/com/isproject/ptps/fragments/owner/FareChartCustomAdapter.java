package com.isproject.ptps.fragments.owner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.isproject.ptps.FareChart;
import com.isproject.ptps.R;

import java.util.ArrayList;

public class FareChartCustomAdapter extends BaseAdapter {
    Context c;
    ArrayList<FareChart> fareCharts;

    public FareChartCustomAdapter(Context c, ArrayList<FareChart> fareCharts) {
        this.c = c;
        this.fareCharts = fareCharts;
    }

    @Override
    public int getCount() {
        return fareCharts.size();
    }

    @Override
    public Object getItem(int position) {
        return fareCharts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.farechart_info,parent,false);
        }
        TextView routeNumber=convertView.findViewById(R.id.textviewRouteNumber);
        TextView routeStart=convertView.findViewById(R.id.textviewRouteStart);
        TextView routefinish=convertView.findViewById(R.id.textviewRouteFinish);
        TextView subrouteStart=convertView.findViewById(R.id.textviewSubRouteStart);
        TextView subrouteFinish=convertView.findViewById(R.id.textviewSubRouteFinish);
        TextView price =convertView.findViewById(R.id.textviewSubRoutePrice);

        final FareChart fareChart=(FareChart) this.getItem(position);
        routeNumber.setText(fareChart.getRouteNumber());
        routeStart.setText(fareChart.getRouteStart());
        routefinish.setText(fareChart.getRouteFinish());
        //subrouteStart.setText(fareChart.getSubrouteStart());
        subrouteFinish.setText(fareChart.getRouteFinish());
        //price.setText(fareChart.getSubroutePrice());

        return convertView;
    }
}

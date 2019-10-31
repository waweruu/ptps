package com.isproject.ptps;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class VehicleHolder extends RecyclerView.ViewHolder {
    private TextView labelVehicleCapacity, labelVehicleType;
    private TextView textVehicleCapacity, textVehicleType;

    public VehicleHolder(View itemView) {
        super(itemView);

        labelVehicleType = itemView.findViewById(R.id.labelVehicleType);
        textVehicleType = itemView.findViewById(R.id.textVehicleType);
        labelVehicleCapacity = itemView.findViewById(R.id.labelVehicleCapacity);
        textVehicleCapacity = itemView.findViewById(R.id.textVehicleCapacity);
    }
}

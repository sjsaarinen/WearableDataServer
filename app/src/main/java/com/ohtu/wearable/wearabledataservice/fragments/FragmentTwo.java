package com.ohtu.wearable.wearabledataservice.fragments;

import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ohtu.wearable.wearabledataservice.R;
import com.ohtu.wearable.wearabledataservice.SelectedSensorsInterface;

import java.util.List;

public class FragmentTwo extends Fragment {
    List<Sensor> sensorList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get list of sensor from MainActivity by calling it getSelectedSensors method
        sensorList = ((SelectedSensorsInterface)getActivity()).getSelectedSensors();

        View view = inflater.inflate(R.layout.fragment_two_layout, container, false);

        TextView mTextView = (TextView) view.findViewById(R.id.textView2);
        mTextView.setText(sensorList.toString());

        return view;
    }

}
package com.snapdiet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    EditText yValue;
    Button btn_insert;

    FirebaseDatabase database;
    DatabaseReference reference;

    SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss");
    GraphView graphView;
    LineGraphSeries series;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yValue = getView().findViewById(R.id.y_value);
        btn_insert = getView().findViewById(R.id.btn_insert);
        graphView = getView().findViewById(R.id.graphView);

        series = new LineGraphSeries();
        graphView.addSeries(series);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("chartTable");

        setListeners();

        graphView.getGridLabelRenderer().setNumVerticalLabels(10);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return sdf.format(new Date((long) value));
                }else{
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }

    private void setListeners() {
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = reference.push().getKey();
                long x = new Date().getTime();
                int y = Integer.parseInt(yValue.getText().toString());
                PointValue pointValue = new PointValue(x,y);
                reference.child(id).setValue(pointValue);
                graphView.getViewport().setScalable(true);
                graphView.getViewport().setScrollable(true);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index = 0;
                for (DataSnapshot myDataSnapshot:dataSnapshot.getChildren()){
                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                    dp[index] = new DataPoint(pointValue.getxValue(),pointValue.getyValue());
                    index++;
                }
                series.resetData(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_home, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final Button btnBMI = view.findViewById(R.id.btnBMI);
        final TextView hasilBMI = view.findViewById(R.id.resultBMI);

        btnBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final View bmiView = inflater.inflate(R.layout.popwindow_bmi, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(inflater.getContext());

                alertDialogBuilder.setView(bmiView);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //sisipin fungsi perhitungan bmi
                                EditText beratBadan = bmiView.findViewById(R.id.beratBadan);
                                EditText tinggiBadan = bmiView.findViewById(R.id.tinggiBadan);

                                String beratStr = beratBadan.getText().toString();
                                String tinggiStr = tinggiBadan.getText().toString();

                                if (tinggiStr != null && !"".equals(tinggiStr) && beratStr != null && !"".equals(beratStr)) {
                                    float tinggiValue = Float.parseFloat(tinggiStr);
                                    float beratValue = Float.parseFloat(beratStr);

                                    float bmi = beratValue/(tinggiValue*tinggiValue);

                                    String bmiLabel = "";

                                    if (Float.compare(bmi, 15f) <= 0){
                                        bmiLabel = "very severely underweight";
                                    } else  if (Float.compare(bmi, 15f) > 0 && Float.compare(bmi, 16f) <= 0){
                                        bmiLabel = "severelu underweight";
                                    } else if (Float.compare(bmi, 16f) > 0 && Float.compare(bmi, 18.5f) <= 0){
                                        bmiLabel = "underweight";
                                    } else if (Float.compare(bmi, 18.5f) > 0 && Float.compare(bmi, 25f) <= 0){
                                        bmiLabel = "normal (healthy weight)";
                                    } else if (Float.compare(bmi, 25f) > 0 && Float.compare(bmi, 30f) <= 0){
                                        bmiLabel = "overweight";
                                    } else if (Float.compare(bmi, 30f) > 0 && Float.compare(bmi, 35f) <= 0){
                                        bmiLabel = "obese level I";
                                    } else if (Float.compare(bmi, 35f) > 0 && Float.compare(bmi, 40f) <= 0){
                                        bmiLabel = "obese level II";
                                    } else{
                                        bmiLabel = "obese level III";
                                    }

                                    bmiLabel = bmiLabel;
                                    hasilBMI.setText(bmiLabel);
                                }


//                                hasilBMI.setText("Hasil");
                                hasilBMI.setVisibility(View.VISIBLE);
                                btnBMI.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

            }
        });



        return view;

    }


}

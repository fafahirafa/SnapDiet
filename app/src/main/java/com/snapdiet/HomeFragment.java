package com.snapdiet;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.snapdiet.RecyclerViewAdapter;


public class HomeFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String userId;

    private SimpleDateFormat sdf = new SimpleDateFormat("M-dd-yyyy");
    private SimpleDateFormat sdf1 = new SimpleDateFormat("d MMM");
    private GraphView graphView;
    private LineGraphSeries series;

    private String TAG = "HomeFragment";
    private CalendarView mCalendarView;
    private TextView listMakanan, tvListMakanan, tvTotalKalori;
    private RecyclerView recyclerView;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImageUrls = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCalendarView = (CalendarView) getView().findViewById(R.id.calendar_view);
        tvListMakanan = (TextView) getView().findViewById(R.id.tv_list_makanan);
        tvTotalKalori = (TextView) getView().findViewById(R.id.tv_total_kalori);

        graphView = getView().findViewById(R.id.graphView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1 + 1) + "-" + i2 + "-" + i;
                Log.d(TAG, "onSelectedDayChange: mm/dd/yyyy:" + date);
                date(date);
            }

        });

        series = new LineGraphSeries();
        graphView.addSeries(series);

        Activity activity = (MainActivity) getActivity();
        userId = ((MainActivity) activity).getUserId();
        Toast.makeText(getActivity(), userId, Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("journal");

        graphView.setTitle("Calories per day graph");
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Date");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("Calories");
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getGridLabelRenderer().setNumVerticalLabels(5);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
        graphView.getViewport().setMinX(1);
        graphView.getViewport().setMaxX(30);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(2000);
        graphView.getViewport().setScrollable(true);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }

    private void date(final String date) {

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("journal").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                int kalori = 0;
                ArrayList<String> listMakanan = new ArrayList<String>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot myDataSnapshot : dataSnapshot.getChildren()) {
                        String inputKey = myDataSnapshot.getKey();
                        PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                        String tanggal = String.valueOf(pointValue.getTanggal());
                        if (tanggal.equals(date)) {
                            final int kalori1 = pointValue.getKalori();
                            final String makanan = pointValue.getNamaMakanan();
                            if (!makanan.isEmpty())
                                listMakanan.add(makanan);
                            kalori = kalori + kalori1;
                        } else {
                            kalori = kalori + 0;
                        }
                        tvTotalKalori.setText("" + kalori);
                        tvListMakanan.setText(listMakanan.toString()
                                .replace("[", "")
                                .replace("]",""));
                    }
                } else {
                    tvListMakanan.setText("no data exist");
                    tvTotalKalori.setText("no data exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        reference = database.getReference("journal").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index = 0;

                for (DataSnapshot myDataSnapshot : dataSnapshot.getChildren()) {
                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                    final int todayDate = pointValue.getTodayDate();
                    final Date realDate = pointValue.getxValue();
                    final int totalKalori = pointValue.getTotalKalori();
                    dp[index] = new DataPoint(todayDate, totalKalori);
                    index++;
                }
                series.resetData(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add(R.drawable.suggestion_1);
        mNames.add("Red Beans");

        mImageUrls.add(R.drawable.suggestion_2);
        mNames.add("Salmon");

        mImageUrls.add(R.drawable.suggestion_3);
        mNames.add("Ocha");

        mImageUrls.add(R.drawable.suggestion_4);
        mNames.add("Potato");

        mImageUrls.add(R.drawable.suggestion_5);
        mNames.add("Egg");

        mImageUrls.add(R.drawable.suggestion_6);
        mNames.add("Coffee");

        mImageUrls.add(R.drawable.suggestion_7);
        mNames.add("Almond");

        mImageUrls.add(R.drawable.suggestion_8);
        mNames.add("Berries");

        mImageUrls.add(R.drawable.suggestion_9);
        mNames.add("Avocado");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final Button btnBMI = view.findViewById(R.id.btnBMI);
        final Button hasilBMI = view.findViewById(R.id.resultBMI);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTotalKalori = view.findViewById(R.id.tv_total_kalori);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        getImages();

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls);
        recyclerView.setAdapter(adapter);

        btnBMI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
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
                                    float tinggiValue = Float.parseFloat(tinggiStr) / 100;
                                    float beratValue = Float.parseFloat(beratStr);

                                    float bmiValue = calculateBMI(beratValue, tinggiValue);
                                    String bmiLabel = interpretBMI(bmiValue);
                                    hasilBMI.setText(String.valueOf(bmiLabel));
                                }

                                hasilBMI.setVisibility(View.VISIBLE);
                                btnBMI.setVisibility(View.INVISIBLE);
                            }

                            private float calculateBMI(float beratValue, float tinggiValue) {
                                return (float) (beratValue / (tinggiValue * tinggiValue));
                            }

                            private String interpretBMI(float bmiValue) {
                                if (bmiValue <= 15) {
                                    return "Very Severely Underweight";
                                } else if (bmiValue <= 16) {
                                    return "Severely Underweight";
                                } else if (bmiValue > 16 && bmiValue <= 18.5) {
                                    return "Underweight";
                                } else if (bmiValue > 18.5 && bmiValue <= 25) {
                                    return "Normal (Healthy Weight)";
                                } else if (bmiValue > 25 && bmiValue <= 30) {
                                    return "Overweight";
                                } else if (bmiValue > 30 && bmiValue <= 35) {
                                    return "Obese Level I";
                                } else if (bmiValue > 35 && bmiValue <= 45) {
                                    return "Obese Level II";
                                } else {
                                    return "Obese Level III";
                                }

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

        hasilBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                    float tinggiValue = Float.parseFloat(tinggiStr) / 100;
                                    float beratValue = Float.parseFloat(beratStr);

                                    float bmiValue = calculateBMI(beratValue, tinggiValue);
                                    String bmiLabel = interpretBMI(bmiValue);
                                    hasilBMI.setText(String.valueOf(bmiLabel));
                                }

                                hasilBMI.setVisibility(View.VISIBLE);
                                btnBMI.setVisibility(View.INVISIBLE);
                            }

                            private float calculateBMI(float beratValue, float tinggiValue) {
                                return (float) (beratValue / (tinggiValue * tinggiValue));
                            }

                            private String interpretBMI(float bmiValue) {
                                if (bmiValue <= 15) {
                                    return "Very Severely Underweight";
                                } else if (bmiValue <= 16) {
                                    return "Severely Underweight";
                                } else if (bmiValue > 16 && bmiValue <= 18.5) {
                                    return "Underweight";
                                } else if (bmiValue > 18.5 && bmiValue <= 25) {
                                    return "Normal (Healthy Weight)";
                                } else if (bmiValue > 25 && bmiValue <= 30) {
                                    return "Overweight";
                                } else if (bmiValue > 30 && bmiValue <= 35) {
                                    return "Obese Level I";
                                } else if (bmiValue > 35 && bmiValue <= 45) {
                                    return "Obese Level II";
                                } else {
                                    return "Obese Level III";
                                }

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
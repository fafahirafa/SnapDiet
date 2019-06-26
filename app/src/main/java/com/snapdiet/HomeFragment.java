package com.snapdiet;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
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

    private boolean recyclerEmpty = true;
    private String TAG = "HomeFragment";
    private CalendarView mCalendarView;
    private TextView listMakanan, tvListMakanan, tvTotalKalori, txtRecomendation;
    private RecyclerView recyclerView;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImageUrls = new ArrayList<>();
    private ArrayList<String> mParagraph = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCalendarView = (CalendarView) getView().findViewById(R.id.calendar_view);
        tvListMakanan = (TextView) getView().findViewById(R.id.tv_list_makanan);
        tvTotalKalori = (TextView) getView().findViewById(R.id.tv_total_kalori);
        txtRecomendation=(TextView) getView().findViewById(R.id.txtRecomendation);

        graphView = getView().findViewById(R.id.graphView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        if (mNames.isEmpty()) {
            getImages();
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls, mParagraph);
        recyclerView.setAdapter(adapter);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1 + 1) + "-" + i2 + "-" + i;
                if (date.equals(sdf.format(new Date().getTime()))){
                    showRecommendation();
                }
                else
                    hideRecommendation();
                Log.d(TAG, "onSelectedDayChange: mm/dd/yyyy:" + date);
                date(date);
            }

        });

        series = new LineGraphSeries();
        graphView.addSeries(series);

        Activity activity = (MainActivity) getActivity();
        userId = ((MainActivity) activity).getUserId();
//        Toast.makeText(getActivity(), userId, Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("journal");

        graphView.setTitle("Calories per day graph");
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getGridLabelRenderer().setNumVerticalLabels(5);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
        graphView.getGridLabelRenderer().setVerticalAxisTitle("Date");
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
                        tvListMakanan.setTextColor(Color.parseColor("#313131"));
                        tvTotalKalori.setTextColor(Color.parseColor("#313131"));
                    }
                    if (listMakanan.isEmpty())
                        tvListMakanan.setText("no foods");
                } else {

                    tvListMakanan.setGravity(Gravity.CENTER);
                    tvListMakanan.setText("no data exist");
                    tvTotalKalori.setGravity(Gravity.CENTER);
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
        mParagraph.add("\n" + "The beans that are usually processed into red bean ice have benefits for losing weight. Do you know the reason? Because in every 100 grams it only contains 0.8 grams of fat and zero cholesterol! " +"\n" +
                "\n" +"Red beans are good for maintaining heart health because in 100 grams there are at least 1406 milligrams of potassium. Besides that, diabetics can also use it to help control blood sugar levels.");

        mImageUrls.add(R.drawable.suggestion_2);
        mNames.add("Salmon");
        mParagraph.add("\n" + "Salmon is rich in polyunsaturated fatty acids (PUFA) and monounsaturated fatty acids (MUFA). The content of EPA and DHA which are compounds from omega 3 fatty acids (one form of PUFA) in salmon, plays an important role for heart health and brain development.\n" +
                "\n" +
                "In order for a variety of nutrients to be fully utilized, salmon must be processed in the right way. Steaming or roasting for a while is the best way to maintain nutritional value, besides being eaten raw.");

        mImageUrls.add(R.drawable.suggestion_3);
        mNames.add("Ocha");
        mParagraph.add("\n" + "The high antioxidant content of ocha can help fight reserves, reduce the risk of Parkinson's, ovarian cancer, colorectal cancer, skin cancer and prostate.\n" +
                "\n" +
                "To improve its ability to lose weight, compilation can be combined with a squeeze of lime or lemon. Get the best time to drink it. Avoid drinking ocha in the stomach because it can increase stomach acid. So, enjoy after breakfast or one hour after eating.");

        mImageUrls.add(R.drawable.suggestion_4);
        mNames.add("Potato");
        mParagraph.add("\n" +
                "Potatoes are one source of resistant starch which is very effective in helping the body burn fat. The fiber content in it is also able to provide satiety and maintain a healthy digestive system.\n" +
                "\n" +
                "If you want to make potatoes as a diet food, don't process them into french fries or potato chips. Simply boiled or baked before enjoying.");

        mImageUrls.add(R.drawable.suggestion_5);
        mNames.add("Egg");
        mParagraph.add("\n" +
                "Eggs are packed with proteins that can help reduce appetite. In one egg only contains 80 calories, even when fried with a little butter even the calorific value is still below 100.\n" +
                "\n" +
                "Enjoying one egg with a slice of whole wheat bread plus one apple seems to be the best alternative weight control food. Interested in trying it?");

        mImageUrls.add(R.drawable.suggestion_6);
        mNames.add("Coffee");
        mParagraph.add("\n" +
                "The content of caffeine in coffee is proven to accelerate the body's metabolism. By consuming just one cup, it can help the body burn as many as 26 calories.\n" +
                "\n" +
                "But remember, for diet drinks you should not add any ingredients to coffee such as sugar, whipped cream or sweetened condensed milk. Just enjoy coffee with its original taste that is bitter and slightly sour.");

        mImageUrls.add(R.drawable.suggestion_7);
        mNames.add("Almond");
        mParagraph.add("\n" + "When feeling hungry or need snacks, just take a handful of almonds. These beans contain monounsaturated fats and polyunsaturated fats which are certainly very very reliable as a diet food while being able to help lower cholesterol.\n" +
                "\n" +
                "The calories are also less than other types of beans. In 1 ounce or about 23 almonds, there are only 163 calories. Not to mention the content of fiber and vitamin E in it which helps prevent constipation and maintain healthy skin.");

        mImageUrls.add(R.drawable.suggestion_8);
        mNames.add("Berries");
        mParagraph.add("\n" +
                "Fruit belonging to the family of berries such as strawberry, raspberry or blueberry tastes appropriate to be used as food for the diet. Why? because on average in one cup berries contain 4 grams of fiber and only contain 80 calories.\n" +
                "\n" +
                "Antioxidant compounds contained in berries are effective in preventing cancer, keeping skin healthy and reducing the risk of dementia (age-related memory decline).");

        mImageUrls.add(R.drawable.suggestion_9);
        mNames.add("Avocado");
        mParagraph.add("\n" + "Avocado fruit is rich in monounsaturated fats, potassium, magnesium, folate, vitamins C and E as well as fiber which makes it has various properties for the body. One of them is to reduce excess weight.\n" +
                "\n" +
                "To enjoy the fruit is so simple. Simply by simply spooning the fruit or can also be processed into avocado ice which can be combined with red beans or almonds.");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final Button btnBMI = view.findViewById(R.id.btnBMI);
        final Button hasilBMI = view.findViewById(R.id.resultBMI);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTotalKalori = view.findViewById(R.id.tv_total_kalori);

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
                                    hasilBMI.setText(String.valueOf(bmiLabel) + " (" + bmiValue + ")");
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
                                    hasilBMI.setText(String.valueOf(bmiLabel)+ " (" + bmiValue + ")");
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

    private void showRecommendation() {
        String strKalori = tvTotalKalori.getText().toString();
        if (!strKalori.isEmpty()) {
            int kalori = Integer.parseInt(strKalori);
            if (kalori<=1500){
                recyclerView.setVisibility(View.GONE);
                txtRecomendation.setText("Your calories are safe");
                txtRecomendation.setTextColor(Color.parseColor("#313131"));
            }
            if (kalori >= 1500) {
                recyclerView.setVisibility(View.VISIBLE);
                txtRecomendation.setText("Here are some recommendations for you");
                txtRecomendation.setTextColor(Color.parseColor("#313131"));
            }
            if (kalori>=2000){
                recyclerView.setVisibility(View.GONE);
                hideRecommendation();
                txtRecomendation.setText("Your calories are over 2000! We recommend you to stop eating :)");
                txtRecomendation.setTextColor(Color.parseColor("#8B281F"));
            }
        }
    }

    private void hideRecommendation(){
        recyclerView.setVisibility(View.GONE);
    }
}
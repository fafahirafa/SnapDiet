package com.snapdiet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AdapterPager extends PagerAdapter {
    Context context;
    List<Integer> dataImage = new ArrayList<>();
    List<String> title = new ArrayList<>();
    List<String> text = new ArrayList<>();

    public AdapterPager(Context context, List<Integer> dataImage, List<String> title, List<String> text) {
        this.context = context;
        this.dataImage = dataImage;
        this.title = title;
        this.text = text;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider, container, false);


        ImageView imageView = view.findViewById(R.id.imageItem);
        imageView.setImageResource(dataImage.get(position));

        TextView tvTitle = view.findViewById(R.id.Slider_title);
        TextView tvText = view.findViewById(R.id.Slider_text);

        tvTitle.setText(title.get(position));
        tvText.setText(text.get(position));

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return dataImage.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}

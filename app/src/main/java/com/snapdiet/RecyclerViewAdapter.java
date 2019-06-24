package com.snapdiet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by User on 2/12/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private Dialog myDialog;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImageUrls = new ArrayList<>();
    private int[] mImageUrls1 = {R.drawable.redbeans_2, R.drawable.salmon_2,R.drawable.ocha_2, R.drawable.kentang_2, R.drawable.telur_2, R.drawable.coffee_2, R.drawable.almond_2,R.drawable.berries_2,R.drawable.avocado_2};
    private ArrayList<String> mParagraph = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<Integer> imageUrls, ArrayList<String> paragraph) {
        mNames = names;
        mImageUrls = imageUrls;
        mParagraph = paragraph;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recomendation, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.image);

        holder.name.setText(mNames.get(position));

        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.popup_rec);


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView namaMakanan = (TextView)myDialog.findViewById(R.id.textNamaMakananRec);
                ImageView imageMakanan = (ImageView)myDialog.findViewById(R.id.imageRec);
                TextView paragrafMakanan = (TextView)myDialog.findViewById(R.id.textParagraphRec);

                namaMakanan.setText(mNames.get(position));
//                imageMakanan.setImageResource(mImageUrls1[position]);
                Glide.with(mContext)
                        .asBitmap()
                        .override(250,250)
                        .load(mImageUrls1[position])
                        .into(imageMakanan);
                paragrafMakanan.setText(mParagraph.get(position));

                myDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
    }
}
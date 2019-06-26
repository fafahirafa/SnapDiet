package com.snapdiet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CameraFragment extends Fragment {

    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        Activity activity = (MainActivity) getActivity();
        userId = ((MainActivity) activity).getUserId();
//        Toast.makeText(getActivity(), userId, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), DetectorActivity.class);
        intent.putExtra("userid", userId);
        startActivity(intent);

        return view;
    }
}

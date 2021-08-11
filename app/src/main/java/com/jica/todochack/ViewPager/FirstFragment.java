package com.jica.todochack.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


import com.jica.todochack.R;


public class FirstFragment extends Fragment {
    // 프래그먼트는 뷰 필수
    private View view;

    // 상태저장을 위함
    public static FirstFragment newInstance() {
        FirstFragment firstFragment = new FirstFragment();
        return firstFragment;
    }



    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_first, container, false);

                return view;
    }
}


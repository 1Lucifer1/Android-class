package com.example.chapter3.homework;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.chapter3.homework.masterdetail.ItemsListFragment;

public class PlaceholderFragment extends Fragment {

    private View view;
    private LottieAnimationView animationView;
    private ItemsListFragment itemsListFragment = new ItemsListFragment();
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        animationView = view.findViewById(R.id.animation_view1);
        animationView.playAnimation();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                animationView.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                fragmentTransaction.add(R.id.fragment_container, itemsListFragment).commit();
            }
        }, 5000);
    }
}

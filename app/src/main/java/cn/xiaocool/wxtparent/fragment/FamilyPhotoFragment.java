package cn.xiaocool.wxtparent.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xiaocool.wxtparent.R;

/**
 * Created by Administrator on 2016/7/31 0031.
 */
public class FamilyPhotoFragment extends Fragment {
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.family_photo_fragment,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
    }
}

package com.just.machine.util;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.just.machine.ui.adapter.SixMinPrescriptionAdapter;
import com.just.news.R;
import java.util.ArrayList;
import java.util.List;

public class DropDownPopWindow extends PopupWindow {

    private final Context mContext;
    public RecyclerView mRecyclerView;
    private List<String> list = new ArrayList<>();
    public SixMinPrescriptionAdapter adapter;

    public DropDownPopWindow(Context context) {
        this.mContext = context;
        setContentView(LayoutInflater.from(context).inflate(R.layout.prescription_list, null));
        setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
//        setAnimationStyle(com.luck.picture.lib.R.style.PictureThemeWindowStyle);
        setFocusable(true);
        setOutsideTouchable(true);
        update();
        initViews();
    }

    private void initViews() {
        mRecyclerView = getContentView().findViewById(R.id.rv_prescription_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new SixMinPrescriptionAdapter(list);
        mRecyclerView.setAdapter(adapter);

        getContentView().findViewById(R.id.rootView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    dismiss();
                }
            }
        });
    }

    public void setData(List<String> mData){
        this.list = mData;
        adapter.setData(mData);
    }

    public static DropDownPopWindow buildPopWindow(Context context) {
        return new DropDownPopWindow(context);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            int[] location = new int[2];
            anchor.getLocationInWindow(location);
            showAtLocation(anchor, Gravity.NO_GRAVITY, 0, location[1] + anchor.getHeight());
        } else {
            super.showAsDropDown(anchor);
        }
    }
}

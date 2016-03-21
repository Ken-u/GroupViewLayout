package com.wiipu.groupview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.wiipu.groupviewlayout.view.GroupViewLayout;

public class MainActivity extends Activity {

    private RelativeLayout mTitleView;
    private RelativeLayout mHideView;
    private GroupViewLayout groupViewLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        groupViewLayout=(GroupViewLayout)findViewById(R.id.group_view);
        mHideView=(RelativeLayout)findViewById(R.id.hide_view);
        mTitleView=(RelativeLayout)findViewById(R.id.rl_myTitleView);
        groupViewLayout.setTitle("测试一下是否能隐藏",20,R.color.colorAccent,R.drawable.update_detail_right,R.drawable.update_detail_down);
//        方法一、使用默认的标题栏控制抽屉开关
        groupViewLayout.settingGroupView(mHideView);
//        方法二、（测试用）使用自定义的标题栏控制抽屉开关，但仍需要对原标题栏进行初始化
        //groupViewLayout.settingGroupView(mTitleView, mHideView);
    }
}

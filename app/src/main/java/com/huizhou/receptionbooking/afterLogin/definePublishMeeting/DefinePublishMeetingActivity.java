package com.huizhou.receptionbooking.afterLogin.definePublishMeeting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.tab1.FragmentAdapter;
import com.huizhou.receptionbooking.common.XTextView;

import java.util.ArrayList;
import java.util.List;

public class DefinePublishMeetingActivity extends AppCompatActivity
{
    private String[] titles = new String[]{"群组","联系人"};
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentAdapter adapter;
    //ViewPage选项卡页面列表
    private List<Fragment> mFragments;
    private List<String> mTitles;
    private XTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_publish_meeting);

        mViewPager = null;
        mTabLayout = null;
        mTitles = null;
        mFragments = null;
        adapter = null;
        mViewPager = null;
        mTabLayout = null;

        tv = (XTextView) findViewById(R.id.defineTop);
        tv.setText("发布会议");
        tv.setTextColor(Color.rgb(225, 225, 225));
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });


        mViewPager = (ViewPager) findViewById(R.id.definePublishMeetingTlVp);
        mTabLayout = (TabLayout) findViewById(R.id.definePublishMeetingTl);

        mTitles = new ArrayList<>();
        for (int i = 0; i < 2; i++)
        {
            mTitles.add(titles[i]);
        }

        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.size(); i++)
        {
            mFragments.add(DefineMeetingFragment.newInstance(mTitles.get(i)));
        }

        FragmentManager childFragmentManager = getSupportFragmentManager() ;
        adapter = new FragmentAdapter(childFragmentManager, mFragments, mTitles);
        adapter.notifyDataSetChanged();

        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来
    }
}

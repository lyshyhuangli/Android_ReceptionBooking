package com.huizhou.receptionbooking.afterLogin;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.tab1.FragmentAdapter;
import com.huizhou.receptionbooking.afterLogin.tab3.TabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录后主页显示
 */
public class MyMeetingFragment extends Fragment
{

    private String[] titles = new String[]{"待开会议", "已开会议"};
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentAdapter adapter;
    //ViewPage选项卡页面列表
    private List<Fragment> mFragments;
    private List<String> mTitles;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(R.layout.tab01, container, false);
        mViewPager = null;
        mTabLayout = null;
        mTitles = null;
        mFragments = null;
        adapter = null;
        mViewPager = null;
        mTabLayout = null;

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);

        mTitles = new ArrayList<>();
        for (int i = 0; i < 4; i++)
        {
            mTitles.add(titles[i]);
        }

        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.size(); i++)
        {
            mFragments.add(TabFragment.newInstance(mTitles.get(i)));
        }

        FragmentManager childFragmentManager = getChildFragmentManager();
        adapter = new FragmentAdapter(childFragmentManager, mFragments, mTitles);
        adapter.notifyDataSetChanged();

        //mViewPager.removeAllViews();
        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来

        //引入布局s
        return view;
    }

}

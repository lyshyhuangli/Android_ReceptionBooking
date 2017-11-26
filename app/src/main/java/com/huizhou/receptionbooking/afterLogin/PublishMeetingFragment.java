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
import com.huizhou.receptionbooking.afterLogin.tab3.Tab3Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PublishMeetingFragment extends Fragment
{
    private View view;

    private String[] titles = new String[]{"推荐", "娱乐", "科技", "军事", "奥运会", "视频", "情感", "图片", "时尚", "教育"};

    //private String[] titles = new String[]{};
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
        view = inflater.inflate(R.layout.tab03, null);

        mViewPager = null;
        mTabLayout = null;
        mTitles = null;
        mFragments = null;
        adapter = null;
        mViewPager = null;
        mTabLayout = null;

        mViewPager = (ViewPager) view.findViewById(R.id.viewpagerTab3);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayoutTab3);

        //只显示3天的会议发布
        mTitles = new ArrayList<>();
        mTitles.add(getSpecifiedDayAfter(0));
        mTitles.add(getSpecifiedDayAfter(1));
        mTitles.add(getSpecifiedDayAfter(2));

//        mTitles = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            mTitles.add(titles[i]);
//        }

        mFragments = new ArrayList<>();
        for (int i = 0; i < mTitles.size(); i++)
        {
            mFragments.add(Tab3Fragment.newInstance(mTitles.get(i)));
        }

        FragmentManager childFragmentManager = getChildFragmentManager();
        adapter = new FragmentAdapter(childFragmentManager, mFragments, mTitles);

        mViewPager.setAdapter(adapter);//给ViewPager设置适配器s
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来

        //引入布局
        return view;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param i
     * @return
     */
    public static String getSpecifiedDayAfter(int i)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + i);
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }


}

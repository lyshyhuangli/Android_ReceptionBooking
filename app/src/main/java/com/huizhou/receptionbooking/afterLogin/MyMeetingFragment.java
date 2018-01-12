package com.huizhou.receptionbooking.afterLogin;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.huizhou.receptionbooking.afterLogin.tab1.TabFragment;
import com.huizhou.receptionbooking.common.XTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录后主页显示
 */
public class MyMeetingFragment extends Fragment
{
    private String userName;
    private String loginShowName;

    private String[] titles = new String[]{"待开会议", "已开会议"};
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentAdapter adapter;
    //ViewPage选项卡页面列表
    private List<Fragment> mFragments;
    private List<String> mTitles;
    private XTextView tv;

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

        SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");
        loginShowName = userSettings.getString("loginShowName", "default");

        tv = (XTextView) view.findViewById(R.id.topCommon);
        tv.setText("我的会议");

        //弹出menu菜单
        tv.setDrawableRightListener(new XTextView.DrawableRightListener()
        {
            @Override
            public void onDrawableRightClick(View view)
            {
                Intent intent = new Intent(getActivity(), CommonMenuActitity.class);
                startActivityForResult(intent, 100);
            }
        });

//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarTab1);
//        toolbar.inflateMenu(R.menu.menu_main);
//        toolbar.setTitle("");
//        TextView textView = (TextView) view.findViewById(R.id.toolbar_title);
//        textView.setText("我的会议");
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
//        {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem)
//            {
//                String msg = "";
//                switch (menuItem.getItemId())
//                {
//                    case R.id.scamToMeeting:
//                        startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 0);
//                        break;
//                    case R.id.defineMeeting:
//                        msg += "Click toolbar_action2";
//                        break;
//
//                }
//                if (!msg.equals(""))
//                {
//                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//                }
//
//                return true;
//            }
//        });

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);

        mTitles = new ArrayList<>();
        for (int i = 0; i < 2; i++)
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

        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来

        //引入布局s
        return view;
    }
}

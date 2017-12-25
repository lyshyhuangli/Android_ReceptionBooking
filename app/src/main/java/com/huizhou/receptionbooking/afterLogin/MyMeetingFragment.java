package com.huizhou.receptionbooking.afterLogin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contactGroup.ActivityGroupAdd;
import com.huizhou.receptionbooking.afterLogin.contactGroup.ActivityGroupList;
import com.huizhou.receptionbooking.afterLogin.tab1.FragmentAdapter;
import com.huizhou.receptionbooking.afterLogin.tab1.TabFragment;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.request.GetMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.request.SaveMeetingConfirmReq;
import com.huizhou.receptionbooking.request.UpdateMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.response.GetMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.response.SaveMeetingConfirmResp;
import com.huizhou.receptionbooking.response.UpdateMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录后主页显示
 */
public class MyMeetingFragment extends Fragment
{
    private ReceiveBroadCast receiveBroadCast;
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

    class ReceiveBroadCast extends BroadcastReceiver
    {
        //接受二维码扫描后的会议Id
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String id = intent.getExtras().getString("result");

            //MySignMeetingTask myTask = new MySignMeetingTask();
            //myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {

        /** 注册广播 */
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.gasFragment");    //只有持有相同的action的接受者才能接收此广播
        activity.registerReceiver(receiveBroadCast, filter);
        super.onAttach(activity);
    }

    /**
     * 扫一扫签到会议
     */
    private class MySignMeetingTask extends AsyncTask<String, Integer, String>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params)
        {
            String id = params[0];
            GetMeetingConfirmByMeetingIdAndPhoneReq
                    req = new GetMeetingConfirmByMeetingIdAndPhoneReq();
            req.setOperatorId(userName);
            req.setMeetingId(Integer.parseInt(id));
            req.setPhone(userName);
            String result = HttpClientClass.httpPost(req, "getMeetingConfirmByMeetingIdAndPhone");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            GetMeetingConfirmByMeetingIdAndPhoneResp info = gson.fromJson(result, GetMeetingConfirmByMeetingIdAndPhoneResp.class);
            if (null != info)
            {
                if (0 == info.getResultCode())
                {
                    if (null != info.getInfo())
                    {
                        //表示之前保存一条参加会议的信息
                        //为空，表示新插入记录
                        UpdateMeetingConfirmByMeetingIdAndPhoneReq
                                req2 = new UpdateMeetingConfirmByMeetingIdAndPhoneReq();
                        req2.setOperatorId(userName);
                        req2.setMeetingId(Integer.parseInt(id));
                        req2.setPhone(userName);
                        req2.setAttendType(1);
                        req2.setIsSign(1);

                        String result2 = HttpClientClass.httpPost(req2, "updateMeetingConfirmByMeetingIdAndPhone");

                        if (StringUtils.isBlank(result2))
                        {
                            return null;
                        }

                        Gson gson2 = new Gson();
                        UpdateMeetingConfirmByMeetingIdAndPhoneResp info2 = gson2.fromJson(
                                result2,
                                UpdateMeetingConfirmByMeetingIdAndPhoneResp.class
                        );
                        if (null != info2)
                        {
                            if (0 == info2.getResultCode())
                            {
                                return "OK";
                            }
                        }
                    }
                    else
                    {
                        //为空，表示新插入记录
                        SaveMeetingConfirmReq
                                req2 = new SaveMeetingConfirmReq();
                        req2.setOperatorId(userName);
                        req2.setMeetingId(Integer.parseInt(id));
                        req2.setPhone(userName);
                        req2.setUserName(loginShowName);
                        req2.setAttendType(1);
                        req2.setIsSign(1);
                        String result2 = HttpClientClass.httpPost(req2, "saveMeetingConfirm");

                        if (StringUtils.isBlank(result2))
                        {
                            return null;
                        }

                        Gson gson2 = new Gson();
                        SaveMeetingConfirmResp info2 = gson2.fromJson(result2, SaveMeetingConfirmResp.class);
                        if (null != info2)
                        {
                            if (0 == info2.getResultCode())
                            {
                                return "OK";
                            }
                        }
                    }
                }
            }

            return null;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses)
        {

        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result)
        {
            if (!"OK".equals(result))
            {
                Toast tos = Toast.makeText(getActivity(), "保存数据失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }
            else
            {
                Toast tos = Toast.makeText(getActivity(), "签到成功!", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }


}

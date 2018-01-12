package com.huizhou.receptionbooking.afterLogin.tab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.bookDining.BookDiningListActivity;
import com.huizhou.receptionbooking.database.vo.MyMeetingInfoRecord;
import com.huizhou.receptionbooking.request.GetMyBedMeetingReq;
import com.huizhou.receptionbooking.request.GetMyBeingMeetingReq;
import com.huizhou.receptionbooking.response.GetMyBedMeetingResp;
import com.huizhou.receptionbooking.response.GetMyBeingMeetingResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;
import com.service.TimeGetDataService;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/10/16.
 */

public class TabFragment extends Fragment
{
    private String type;
    private int count = 1;
    private String userName;
    private View view;

    private RecyclerView mRecyclerView;
    //支持下拉刷新的ViewGroup
    private PtrClassicFrameLayout mPtrFrame;
    //List数据
    private List<Integer> idsList = new ArrayList<>();
    private Map<Integer, String> threadItem = new HashMap<>();
    private Map<Integer, String> meetingTime = new HashMap<>();
    private Map<Integer, String> departmentItem = new HashMap<>();
    private Map<Integer, String> meetingRoomItem = new HashMap<>();

    private int refleshType = 0;

    //RecyclerView自定义Adapter
    private RvAdapter adapter;
    //添加Header和Footer的封装类
    private RecyclerAdapterWithHF mAdapter;


    public static TabFragment newInstance(String str)
    {
        Bundle bundle = new Bundle();
        bundle.putString("type", str);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setAdapter()
    {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new RvAdapter(getActivity(), idsList, threadItem, meetingTime, departmentItem, meetingRoomItem);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setAdapter(mAdapter);

        adapter.setOnItemClickListener(new RvAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view)
            {
                RvAdapter.NormalViewHolder vh = (RvAdapter.NormalViewHolder) view.getTag();

                //清楚未读会议的颜色和缓存
                UnreadMeetingInfoRecord unreadMeetingInfoRecord = null;
                SharedPreferences unreadingMeeting = getActivity().getSharedPreferences("unreadingMeeting"+userName, Context.MODE_MULTI_PROCESS);
                if (unreadingMeeting != null)
                {
                    Gson gson = new Gson();
                    String ids = unreadingMeeting.getString("ids", "default");
                    if (StringUtils.isNotBlank(ids) && !"default".equals(ids))
                    {
                        unreadMeetingInfoRecord = gson.fromJson(ids, UnreadMeetingInfoRecord.class);
                        Map<Integer, UnreadMeetingMoreRecord> map = unreadMeetingInfoRecord.getUnreadMeetingIds();
                        Integer id = Integer.valueOf(vh.meetingId.getText().toString());
                        if (map.containsKey(id))
                        {
                            UnreadMeetingMoreRecord v = map.get(id);
                            v.setRead(true);
                        }

                        String jsonString = gson.toJson(unreadMeetingInfoRecord);
                        SharedPreferences.Editor editor = unreadingMeeting.edit();
                        editor.putString("ids", jsonString);
                        editor.commit();
                    }
                }

                //跳转到详细页面
                Intent it = new Intent(getActivity(), ViewAndConfirmMeetingActivity.class);
                it.putExtra("id", vh.meetingId.getText().toString());
                it.putExtra("meetingRoom", vh.meetingRoomItem.getText().toString());
                it.putExtra("departmentItem", vh.departmentItem.getText().toString());
                startActivity(it);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.tab1_fragment, container, false);

        SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");
        try
        {
            //先等service中的Thread获取未读会议信息
            Thread.sleep(1000);
        }
        catch (Exception e)
        {

        }

        type = getArguments().getString("type");

        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
//下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this);
//下拉刷新一些设置 详情参考文档
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
// default is false
        mPtrFrame.setPullToRefresh(false);
// default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
//进入Activity就进行自动下拉刷新
        mPtrFrame.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mPtrFrame.autoRefresh();
            }
        }, 100);
//下拉刷新
        mPtrFrame.setPtrHandler(new PtrDefaultHandler()
        {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame)
            {
                idsList.clear();
                threadItem.clear();
                meetingTime.clear();
                departmentItem.clear();
                meetingRoomItem.clear();

                refleshType = 0;

                if ("待开会议".equals(type))
                {
                    MyBingMeetingTask myTask = new MyBingMeetingTask();
                    myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                else
                {
                    count = 1;
                    MyBedMeetingTask myTask = new MyBedMeetingTask();
                    myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
//模拟联网 延迟更新列表
                new Handler().postDelayed(new Runnable()
                {
                    public void run()
                    {
                        //mAdapter.notifyDataSetChanged();
                        //mPtrFrame.refreshComplete();
                        // mPtrFrame.setLoadMoreEnable(true);
                    }
                }, 0);
            }
        });
//上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener()
        {
            @Override
            public void loadMore()
            {
//模拟联网延迟更新数据
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
//模拟数据
//                        for (int i = 0; i <= 5; i++)
//                        {
//                            idsList.add(String.valueOf(i));
//                        }

                        refleshType = 1;
                        if ("待开会议".equals(type))
                        {
                            idsList.clear();
                            threadItem.clear();
                            meetingTime.clear();
                            departmentItem.clear();
                            meetingRoomItem.clear();

                            MyBingMeetingTask myTask = new MyBingMeetingTask();
                            myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                        else
                        {
                            idsList.clear();
                            threadItem.clear();
                            meetingTime.clear();
                            departmentItem.clear();
                            meetingRoomItem.clear();

                            count++;
                            MyBedMeetingTask myTask = new MyBedMeetingTask();
                            myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }


                    }
                }, 0);
            }
        });

        if ("待开会议".equals(type))
        {
            SharedPreferences startThread = getActivity().getSharedPreferences("startThread", 0);
            if (startThread != null)
            {
                String newThread = startThread.getString("newThread", "default");

                if (StringUtils.isNotBlank(newThread))
                {
                    //获取最新会议信息
                    MyNewMeetingThread myNewMeetingThread = new MyNewMeetingThread();
                    Thread t1 = new Thread(myNewMeetingThread);
                    t1.setName(newThread);
                    t1.start();
                }
            }
        }

        return view;
    }

    /**
     * 待开会议
     */
    private class MyBingMeetingTask extends AsyncTask<String, Integer, String>
    {
        //onPreExecute 方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params)
        {
            GetMyBeingMeetingReq
                    req = new GetMyBeingMeetingReq();
            req.setPhone(userName);
            req.setOperatorId(userName);
            try
            {
                String result = HttpClientClass.httpPost(req, "getMyBingMeeting");

                if (StringUtils.isBlank(result))
                {
                    return null;
                }

                Gson gson = new Gson();
                GetMyBeingMeetingResp info = gson.fromJson(result, GetMyBeingMeetingResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        List<MyMeetingInfoRecord> myBeingMeetingInfo = info.getMyBeingMeetingInfo();
                        for (MyMeetingInfoRecord m : myBeingMeetingInfo)
                        {
                            idsList.add(m.getId());
                            threadItem.put(m.getId(), m.getThreaf());
                            meetingTime.put(m.getId(), m.getMeetingDate() + " " + m.getStartTime());
                            departmentItem.put(m.getId(), m.getDepartment());
                            meetingRoomItem.put(m.getId(), m.getName());
                        }

                        return "OK";
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
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
                Toast tos = Toast.makeText(getActivity(), "查询会议信息失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }

            setAdapter();
            if (refleshType == 0)
            {
                mAdapter.notifyDataSetChanged();
                mPtrFrame.refreshComplete();
                mPtrFrame.setLoadMoreEnable(true);
            }
            else
            {
                mAdapter.notifyDataSetChanged();
                mPtrFrame.loadMoreComplete(true);
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

    /**
     * 已开完的会议
     */
    private class MyBedMeetingTask extends AsyncTask<String, Integer, String>
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

            GetMyBedMeetingReq
                    req = new GetMyBedMeetingReq();
            req.setPhone(userName);
            req.setOperatorId(userName);
            req.setCount(count * 20);

            try
            {
                String result = HttpClientClass.httpPost(req, "getMyBedMeeting");

                if (StringUtils.isBlank(result))
                {
                    return null;
                }

                Gson gson = new Gson();
                GetMyBedMeetingResp info = gson.fromJson(result, GetMyBedMeetingResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        List<MyMeetingInfoRecord> myBeingMeetingInfo = info.getMyBedMeetingInfo();
                        for (MyMeetingInfoRecord m : myBeingMeetingInfo)
                        {
                            idsList.add(m.getId());
                            threadItem.put(m.getId(), m.getThreaf());
                            meetingTime.put(m.getId(), m.getMeetingDate() + " " + m.getStartTime());
                            departmentItem.put(m.getId(), m.getDepartment());
                            meetingRoomItem.put(m.getId(), m.getName());
                        }

                        return "OK";
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
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
                Toast tos = Toast.makeText(getActivity(), "查询会议信息失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }

            setAdapter();

            if (refleshType == 0)
            {
                mAdapter.notifyDataSetChanged();
                mPtrFrame.refreshComplete();
                mPtrFrame.setLoadMoreEnable(true);
            }
            else
            {
                mAdapter.notifyDataSetChanged();
                mPtrFrame.loadMoreComplete(true);
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }


    /**
     * 获取最新会议信息线程
     */
    public class MyNewMeetingThread extends Thread
    {
        public void run()
        {
            while (true)
            {
                String str = Thread.currentThread().getName();

                //登录出会抛空指针
                if(null != getActivity())
                {
                    SharedPreferences startThread = getActivity().getSharedPreferences("startThread", 0);
                    if (startThread != null)
                    {
                        String newThread = startThread.getString("newThread", "default");
                        if (!str.equals(newThread))
                        {
                            break;
                        }

                    }
                }
                else
                {
                    break;
                }

                try
                {
                    //要比service的Thread慢一点
                    Thread.sleep(2000);

                    if ("待开会议".equals(type))
                    {
                        idsList.clear();
                        threadItem.clear();
                        meetingTime.clear();
                        departmentItem.clear();
                        meetingRoomItem.clear();

                        MyBingMeetingTask myTask = new MyBingMeetingTask();
                        myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    else
                    {

                    }
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}

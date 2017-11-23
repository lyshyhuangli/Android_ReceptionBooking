package com.huizhou.receptionbooking.afterLogin.tab3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.database.vo.BookingMeetingRecord;
import com.huizhou.receptionbooking.request.GetMyMeetingRoomByPhoneReq;
import com.huizhou.receptionbooking.response.GetMyMeetingRoomByPhoneResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/16.
 */

public class Tab3Fragment extends Fragment implements AbsListView.OnScrollListener
{
    private ListView listView;
    private Map<Integer, String> mapNames = new HashMap<Integer, String>();
    private List<Integer> listIds = new ArrayList<Integer>(1000);
    private int last_index;
    private int total_index;

    private boolean isLoading = false;//表示是否正处于加载状态
    private ListViewAdapterTab3 adapter;

    private MyTask myTask;

    private String userName;

    private String date;

    public static Tab3Fragment newInstance(String date)
    {
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        Tab3Fragment fragment = new Tab3Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");

        date = getArguments().getString("date");

        View view = inflater.inflate(R.layout.tab3_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.listviewTab3);
        listView.setOnScrollListener(this);

        loadFirstTime(date);

        return view;
    }

    /**
     * 第一次加载数据
     */
    private void loadFirstTime(String date)
    {
        listIds.clear();
        myTask = new MyTask();
        myTask.execute(date);
    }

    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
//            ListViewAdapterGroup.ViewHolder holder = (ListViewAdapterGroup.ViewHolder) view.getTag();
//            String ids = holder.id.getText().toString();
//
//            //跳转到详细页面
//            Intent it = new Intent(getActivity(), ActivityPublishMeetingAdd.class);
//            it.putExtra("id", ids);
//            startActivity(it);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        try
        {
            last_index = firstVisibleItem + visibleItemCount;
            total_index = totalItemCount;
        }
        catch (Exception e)
        {
            // CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), userName);
            e.printStackTrace();
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        try
        {
            if (last_index == total_index && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE))
            {
                //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
                if (!isLoading)
                {
                    //不处于加载状态的话对其进行加载
                    isLoading = true;
                    //滑动次数加1
                    //getDataNo += 1;
                    // onLoad();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), userName);
        }

    }

    /**
     * 刷新加载
     */
    public void onLoad()
    {
        try
        {
            if (adapter == null)
            {
                //adapter = new ListViewAdapterGroup(this, mapNames, listIds);
                //listView.setAdapter(adapter);
            }
            else
            {
                //获取更多数据
                mapNames.clear();
                listIds.clear();

                myTask = new MyTask();
                //myTask.execute(getDataNo);

                //adapter.updateView(mapNames, listIds)

            }

            loadComplete();//刷新结束
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 加载完成
     */
    public void loadComplete()
    {
        try
        {
            isLoading = false;//设置正在刷新标志位false
            //this.invalidateOptionsMenu();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), userName);
        }
    }

    private class MyTask extends AsyncTask<String, Integer, List<BookingMeetingRecord>>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected List<BookingMeetingRecord> doInBackground(String... params)
        {
            GetMyMeetingRoomByPhoneReq
                    req = new GetMyMeetingRoomByPhoneReq();
            req.setPhone(userName);
            req.setOperatorId(userName);
            req.setDate(params[0]);
            try
            {
                String result = HttpClientClass.httpPost(req, "getMyMeetingRoomByPhone");

                if (StringUtils.isBlank(result))
                {
                    return null;
                }

                Gson gson = new Gson();
                GetMyMeetingRoomByPhoneResp info = gson.fromJson(result, GetMyMeetingRoomByPhoneResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        return info.getMyMeetingRoom();
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
        protected void onPostExecute(List<BookingMeetingRecord> result)
        {
            if (null == result)
            {
                return;
            }

            adapter = new ListViewAdapterTab3(getActivity(), result);
            listView.setAdapter(adapter);

            //ListView item的点击事件
            listView.setOnItemClickListener(new Tab3Fragment.ItemClickListener());

            //ListView item 中的TextView的点击事件
            adapter.setOnItemTextViewClickListener(new ListViewAdapterTab3.onItemTextViewListener()
            {
                @Override
                public void onTextViewClick(View view, int i, String type)
                {
                    ListViewAdapterTab3.ViewHolder holder = (ListViewAdapterTab3.ViewHolder) view.getTag();
                    String meetingRoomId = holder.meetingRoomId.getText().toString();
                    String meetingRoom = holder.meetingPlace.getText().toString();
                    String meetingDate = date;

                    Intent it = null;

                    if (("am".equals(type) && StringUtils.isBlank(holder.am.getText())) || ("pm".equals(type) && StringUtils.isBlank(holder.pm.getText())))
                    {
                        it = new Intent(getActivity(), ActivityPublishMeetingAdd.class);
                    }
                    else
                    {
                        it = new Intent(getActivity(), ActivityPublishMeetingEdit.class);
                    }

                    if (null != holder.publishRoomIdAm.getText())
                    {
                        String publishRoomIdAm = holder.publishRoomIdAm.getText().toString();
                        it.putExtra("publishRoomIdAm", publishRoomIdAm);
                        String bookUserIdAm = holder.bookUserIdAm.getText().toString();
                        it.putExtra("bookUserIdAm", bookUserIdAm);
                        String am = holder.am.getText().toString();
                        it.putExtra("am", am);
                    }

                    if (null != holder.publishRoomIdPm.getText())
                    {
                        String publishRoomIdPm = holder.publishRoomIdPm.getText().toString();
                        it.putExtra("publishRoomIdPm", publishRoomIdPm);
                        String bookUserIdPm = holder.bookUserIdPm.getText().toString();
                        it.putExtra("bookUserIdPm", bookUserIdPm);
                        String pm = holder.pm.getText().toString();
                        it.putExtra("pm", pm);
                    }

                    it.putExtra("meetingRoomId", meetingRoomId);
                    it.putExtra("meetingDate", meetingDate);
                    it.putExtra("meetingRoom", meetingRoom);
                    it.putExtra("anOrPmType", type);
                    startActivity(it);
                    //.notifyDataSetChanged();
                }
            });

        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

}

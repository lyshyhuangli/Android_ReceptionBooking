package com.huizhou.receptionbooking.afterLogin.meetingRoom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.definePublishMeeting.DefineMeetingViewActivity;
import com.huizhou.receptionbooking.afterLogin.department.ActivityDepartmentAdd;
import com.huizhou.receptionbooking.afterLogin.department.ActivityDepartmentEdit;
import com.huizhou.receptionbooking.afterLogin.department.ActivityDepartmentList;
import com.huizhou.receptionbooking.common.BaseTreeBean;
import com.huizhou.receptionbooking.common.Node;
import com.huizhou.receptionbooking.common.SimpleTreeAdapter;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.DepartmentDAO;
import com.huizhou.receptionbooking.database.dao.MeetingRoomDAO;
import com.huizhou.receptionbooking.database.dao.impl.DepartmentDAOImpl;
import com.huizhou.receptionbooking.database.dao.impl.MeetingRoomDAOImpl;
import com.huizhou.receptionbooking.utils.ActivityCalendarPickerView;

import java.util.ArrayList;
import java.util.List;

public class ActivityMeetingRoomList extends AppCompatActivity
{
    private List<BaseTreeBean> mDatas = new ArrayList<BaseTreeBean>();
    private ListView mLvTree;
    private SimpleTreeAdapter mAdapter;
    private MyTask mTask;
    private XTextView tv;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_room_list);

        Intent inte = getIntent();
        flag = inte.getStringExtra("flag");

        tv = (XTextView) this.findViewById(R.id.meetingRoomList);
        //进入增加页面
        tv.setDrawableRightListener(new XTextView.DrawableRightListener()
        {
            @Override
            public void onDrawableRightClick(View view)
            {
                Intent intent = new Intent(ActivityMeetingRoomList.this, ActivityMeetingRoomAdd.class);
                startActivityForResult(intent, 100);
            }
        });

        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });

        mLvTree = (ListView) findViewById(R.id.listviewMeetingRoom);

        mTask = new MyTask();
        mTask.execute();
    }

    private class MyTask extends AsyncTask<String, Integer, List<BaseTreeBean>>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected List<BaseTreeBean> doInBackground(String... params)
        {
            List<String> errorList = new ArrayList<>();
            List<BaseTreeBean> result = null;
            try
            {
                MeetingRoomDAO m = new MeetingRoomDAOImpl();
                result = m.getAllMeetingRoom(errorList);
                if (null == result)
                {
                    return null;
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return result;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses)
        {

        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(List<BaseTreeBean> list)
        {
            if (null == list)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "查询会议室信息失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }

            //初始化数据
            mDatas.addAll(list);

            //可以一直添加数据层
            try
            {
                mAdapter = new SimpleTreeAdapter<BaseTreeBean>(mLvTree, ActivityMeetingRoomList.this, mDatas, 0);
                mAdapter.expandOrCollapse(0);

                mAdapter.setSubClickListener(new SimpleTreeAdapter.SubClickListener()
                {
                    @Override
                    public void onClick(Node node, int position)
                    {
                        String type = node.getType();
                        if ("2".equals(type))
                        {
                            if ("setting".equals(flag))
                            {
                                Intent intent = new Intent(ActivityMeetingRoomList.this, ActivityMeetingRoomEdit.class);
                                intent.putExtra("id", node.getId());
                                startActivityForResult(intent, 100);
                            }
                            else if ("defineMeeting".equals(flag))
                            {
                                Intent intent = new Intent(ActivityMeetingRoomList.this, DefineMeetingViewActivity.class);
                                intent.putExtra("id", node.getId());
                                intent.putExtra("name", node.getName());
                                setResult(RESULT_OK, intent);
                                onBackPressed();
                            }
                        }

                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            mLvTree.setAdapter(mAdapter);
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

    @Override
    protected void onResume()
    {
        //重新刷新数据
        super.onResume();
        if (null != mAdapter)
        {
            mDatas.clear();
            mTask = new MyTask();
            mTask.execute();
        }
    }
}

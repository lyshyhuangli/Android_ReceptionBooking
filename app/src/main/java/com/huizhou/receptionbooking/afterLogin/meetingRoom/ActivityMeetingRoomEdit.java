package com.huizhou.receptionbooking.afterLogin.meetingRoom;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.department.ActivityDepartmentEdit;
import com.huizhou.receptionbooking.afterLogin.department.ActivityDepartmentList;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.DepartmentDAO;
import com.huizhou.receptionbooking.database.dao.MeetingRoomDAO;
import com.huizhou.receptionbooking.database.dao.impl.DepartmentDAOImpl;
import com.huizhou.receptionbooking.database.dao.impl.MeetingRoomDAOImpl;
import com.huizhou.receptionbooking.database.vo.DepartmentInfoRecord;
import com.huizhou.receptionbooking.database.vo.MeetingRoomInfoRecord;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityMeetingRoomEdit extends AppCompatActivity
{

    private MyEditTask mTask;
    private MyShowTask showTask;
    private XTextView tv;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_room_edit);

        Intent i = getIntent();
        id = i.getStringExtra("id");


        tv = (XTextView) this.findViewById(R.id.meettingRoomEditBack);

        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });

        showTask = new MyShowTask();
        showTask.execute(id);
    }


    public void saveMeetingRoomEdit(View view)
    {
        TextView meetingRoomDepatmentMr = (TextView) findViewById(R.id.meetingRoomDepatmentMr);

        TextView meetingRoomDepatmentEditMrId = (TextView) findViewById(R.id.meetingRoomDepatmentEditMrId);
        int parentId = Integer.valueOf((String) meetingRoomDepatmentEditMrId.getText());

        EditText meetingRoomNameEt = (EditText) findViewById(R.id.meetingRoomNameEt);
        String meetingRoomName = null;
        if (StringUtils.isBlank(meetingRoomNameEt.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写会议室", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            meetingRoomName = meetingRoomNameEt.getText().toString();
        }

        EditText meetingRoomRemarkEt = (EditText) findViewById(R.id.meetingRoomRemarkEt);
        String remark = meetingRoomRemarkEt.getText().toString();

        mTask = new MyEditTask();
        mTask.execute(String.valueOf(parentId), meetingRoomName, remark);

    }

    public void getDepartmentEditForMeetingRoom(View view)
    {
        Intent intent = new Intent(ActivityMeetingRoomEdit.this, ActivityDepartmentList.class);
        intent.putExtra("type", "editMeettingRoom");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            String parentId = data.getStringExtra("parentId");
            String name = data.getStringExtra("name");

            TextView meetingRoomDepatmentMr = (TextView) findViewById(R.id.meetingRoomDepatmentMr);
            meetingRoomDepatmentMr.setText(name);

            TextView meetingRoomDepatmentEditMrId = (TextView) findViewById(R.id.meetingRoomDepatmentEditMrId);
            meetingRoomDepatmentEditMrId.setText(parentId);
        }
    }

    private class MyShowTask extends AsyncTask<String, Integer, MeetingRoomInfoRecord>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected MeetingRoomInfoRecord doInBackground(String... params)
        {
            List<String> errorList = new ArrayList<>();
            try
            {
                MeetingRoomDAO dao = new MeetingRoomDAOImpl();
                MeetingRoomInfoRecord m = dao.getMeetingRoomById(Integer.valueOf(params[0]), errorList);
                return m;
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
        protected void onPostExecute(MeetingRoomInfoRecord d)
        {
            if (null == d)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "查询会议室信息失败", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }
            else
            {
                TextView meetingRoomDepatmentMr = (TextView) findViewById(R.id.meetingRoomDepatmentMr);
                meetingRoomDepatmentMr.setText(d.getParentName());

                TextView meetingRoomDepatmentEditMrId = (TextView) findViewById(R.id.meetingRoomDepatmentEditMrId);
                meetingRoomDepatmentEditMrId.setText(String.valueOf(d.getParentId()));

                EditText meetingRoomNameEt = (EditText) findViewById(R.id.meetingRoomNameEt);
                meetingRoomNameEt.setText(d.getName());

                EditText meetingRoomRemarkEt = (EditText) findViewById(R.id.meetingRoomRemarkEt);
                meetingRoomRemarkEt.setText(d.getRemark());
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

    private class MyEditTask extends AsyncTask<String, Integer, List<String>>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected List<String> doInBackground(String... params)
        {
            List<String> errorList = new ArrayList<>();
            MeetingRoomInfoRecord d = new MeetingRoomInfoRecord();
            d.setParentId(Integer.valueOf(params[0]));
            d.setName(params[1]);
            d.setRemark(params[2]);
            d.setId(Integer.valueOf(id));
            try
            {
                MeetingRoomDAO dao = new MeetingRoomDAOImpl();
                dao.updateMeetingRoom(d, errorList);
                return errorList;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return errorList;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses)
        {

        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(List<String> list)
        {
            if (!list.isEmpty())
            {
                Toast tos = Toast.makeText(getApplicationContext(), list.get(0), Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }
            else
            {
                Toast tos = Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                onBackPressed();
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }
}

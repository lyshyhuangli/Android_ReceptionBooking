package com.huizhou.receptionbooking.afterLogin.meetingRoom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.department.ActivityDepartmentList;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.request.AddMeetingRoomRequest;
import com.huizhou.receptionbooking.response.AddMeetingRoomResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

public class ActivityMeetingRoomAdd extends AppCompatActivity
{
    private MyTask mTask;
    private XTextView tv;
    private String loginUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_room_add);

        //获取所属部门
        SharedPreferences userSettings = ActivityMeetingRoomAdd.this.getSharedPreferences("userInfo", 0);
        String department = userSettings.getString("department", "default");
        String departmentId = userSettings.getString("departmentId", "default");
        loginUserName = userSettings.getString("loginUserName", "default");

        TextView meetingRoomDepAddMr = (TextView) findViewById(R.id.meetingRoomDepAddMr);
        meetingRoomDepAddMr.setText(department);

        TextView meetingRoomDepAddMrId = (TextView) findViewById(R.id.meetingRoomDepAddMrId);
        meetingRoomDepAddMrId.setText(departmentId);

        tv = (XTextView) this.findViewById(R.id.meetingRoomAddBack);

        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });
    }


    public void saveMeetingRoomAdd(View view)
    {
        TextView meetingRoomAddMr = (TextView) findViewById(R.id.meetingRoomDepAddMr);

        TextView meetingRoomAddMrId = (TextView) findViewById(R.id.meetingRoomDepAddMrId);
        int parentId = Integer.valueOf((String) meetingRoomAddMrId.getText());

        EditText meetingRoomNameAdd = (EditText) findViewById(R.id.meetingRoomNameAdd);
        String meetingRoomName = null;
        if (StringUtils.isBlank(meetingRoomNameAdd.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写会议室", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            meetingRoomName = meetingRoomNameAdd.getText().toString();
        }

        EditText remarkMeetingRoomAdd = (EditText) findViewById(R.id.remarkMeetingRoomAdd);
        String remark = remarkMeetingRoomAdd.getText().toString();

        mTask = new MyTask();
        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(parentId), meetingRoomName, remark);

    }

    public void getDepartmentForMeetingRoom(View view)
    {
        Intent intent = new Intent(ActivityMeetingRoomAdd.this, ActivityDepartmentList.class);
        intent.putExtra("type", "addMeettingRoom");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            String id = data.getStringExtra("parentId");
            String name = data.getStringExtra("name");

            TextView meetingRoomDepAddMr = (TextView) findViewById(R.id.meetingRoomDepAddMr);
            meetingRoomDepAddMr.setText(name);

            TextView meetingRoomDepAddMrId = (TextView) findViewById(R.id.meetingRoomDepAddMrId);
            meetingRoomDepAddMrId.setText(id);
        }
    }

    private class MyTask extends AsyncTask<String, Integer, Integer>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Integer doInBackground(String... params)
        {
            AddMeetingRoomRequest req = new AddMeetingRoomRequest();
            req.setOperatorId(loginUserName);
            req.setName(params[1]);
            req.setParentId(Integer.valueOf(params[0]));
            req.setType(2);
            req.setRemark(params[2]);

            String result = HttpClientClass.httpPost(req, "addMeetingRoom");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            AddMeetingRoomResp info = gson.fromJson(result, AddMeetingRoomResp.class);
            if (null != info)
            {
                if (0 == info.getResultCode())
                {
                    return info.getResult();
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
        protected void onPostExecute(Integer result)
        {
            if (result == null || result != 1)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_LONG);
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

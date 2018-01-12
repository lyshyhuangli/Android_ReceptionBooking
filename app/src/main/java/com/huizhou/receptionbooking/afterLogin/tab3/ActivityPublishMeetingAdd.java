package com.huizhou.receptionbooking.afterLogin.tab3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contactGroup.checkbox.ActivityGroupPersonCheckBox;
import com.huizhou.receptionbooking.afterLogin.contactGroup.checkbox.Model;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityCheckBoxContactList;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.vo.BookMeetingDbInfoRecord;
import com.huizhou.receptionbooking.database.vo.GroupPersonInfoRecord;
import com.huizhou.receptionbooking.request.GetAllGroupPersonByUserPhoneReq;
import com.huizhou.receptionbooking.request.InsertPublishMeetingReq;
import com.huizhou.receptionbooking.response.GetAllGroupPersonByUserPhoneResp;
import com.huizhou.receptionbooking.response.InsertPublishMeetingResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.security.spec.ECField;
import java.util.List;


public class ActivityPublishMeetingAdd extends AppCompatActivity implements TimePicker.OnTimeChangedListener
{

    int houre = 0;
    int minute = 0;

    private String type;
    private String meetingRoomId;
    private String meetingDate;
    private String meetingRoom;
    private String userName;
    private String publishRoomIdAm;
    private String publishRoomIdPm;

    private TextView startTime;
    private TextView endTime;
    private XTextView tv;

    private Context context;

    //在TextView上显示的字符
    private StringBuffer time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_meeting_add);
        context = this;
        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");
        String department = userSettings.getString("department", "default");

        EditText zuzhiDep = (EditText) findViewById(R.id.zuzhiDep);
        zuzhiDep.setText(department);

        Intent i = getIntent();
        meetingDate = i.getStringExtra("meetingDate");
        type = i.getStringExtra("anOrPmType");
        meetingRoomId = i.getStringExtra("meetingRoomId");
        meetingRoom = i.getStringExtra("meetingRoom");

        startTime = (TextView) findViewById(R.id.meetingBeginTime);
        endTime = (TextView) findViewById(R.id.meetingEndTime);

        TextView meetingRoomEtAdd = (TextView) findViewById(R.id.meetingRoomEtAdd);
        meetingRoomEtAdd.setText("会议室：" + meetingRoom);
        TextView dateEtAdd = (TextView) findViewById(R.id.dateEtAdd);
        dateEtAdd.setText("日     期：" + meetingDate);

        time = new StringBuffer();

        tv = (XTextView) this.findViewById(R.id.publishMeetingBack);
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

    public void getStartTime(View view)
    {
        houre = 0;
        minute = 0;
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);

        builder2.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (time.length() > 0)
                { //清除上次记录的日期
                    time.delete(0, time.length());
                }

                StringBuilder h = new StringBuilder();
                if (0 == houre)
                {
                    h.append("00");
                }
                else if(houre<10)
                {
                    h.append("0"+houre);
                }
                else
                {
                    h.append(houre);
                }

                StringBuilder m = new StringBuilder();
                if (0 == minute)
                {
                    m.append("00");
                }else if(minute<10)
                {
                    m.append("0"+minute);
                }

                else
                {
                    m.append(minute);
                }


                startTime.setText(time.append(h.toString()).append(":").append(m.toString()).append(""));
                dialog.dismiss();
            }
        });
        builder2.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog dialog2 = builder2.create();
        View dialogView2 = View.inflate(context, R.layout.dialog_time, null);
        TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
        timePicker.setCurrentHour(houre);
        timePicker.setCurrentMinute(minute);
        timePicker.setIs24HourView(true); //设置24小时制
        timePicker.setOnTimeChangedListener(this);
        //dialog2.setTitle("获取时间");
        dialog2.setView(dialogView2);
        dialog2.show();
    }

    /**
     * 时间改变的监听事件
     *
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
    {
        this.houre = hourOfDay;
        this.minute = minute;
    }

    public void getEndTime(View view)
    {
        houre = 00;
        minute = 00;

        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        builder2.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (time.length() > 0)
                { //清除上次记录的日期
                    time.delete(0, time.length());
                }

                StringBuilder h = new StringBuilder();
                if (0 == houre)
                {
                    h.append("00");
                }
                else if(houre<10)
                {
                    h.append("0"+houre);
                }
                else
                {
                    h.append(houre);
                }

                StringBuilder m = new StringBuilder();
                if (0 == minute)
                {
                    m.append("00");
                }
                else if(minute<10)
                {
                    m.append("0"+minute);
                }
                else
                {
                    m.append(minute);
                }

                endTime.setText(time.append(h.toString()).append(":").append(m.toString()).append(""));
                dialog.dismiss();
            }
        });
        builder2.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog dialog2 = builder2.create();
        View dialogView2 = View.inflate(context, R.layout.dialog_time, null);
        TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
        timePicker.setCurrentHour(houre);
        timePicker.setCurrentMinute(minute);
        timePicker.setIs24HourView(true); //设置24小时制
        timePicker.setOnTimeChangedListener(this);
        //dialog2.setTitle("获取时间");
        dialog2.setView(dialogView2);
        dialog2.show();
    }

    public void getFromContact(View view)
    {
        Intent it = new Intent(this, ActivityCheckBoxContactList.class);
        startActivityForResult(it, 100);
    }

    public void getFromGroup(View view)
    {
        Intent it = new Intent(this, ActivityGroupPersonCheckBox.class);
        startActivityForResult(it, 100);
    }

    /**
     * 清空与会人
     *
     * @param v
     */
    public void clearPerson(View v)
    {
        TextView meetingPersonName = (TextView) findViewById(R.id.meetingPersonName);
        meetingPersonName.setText(null);
        TextView meetingPersonId = (TextView) findViewById(R.id.meetingPersonId);
        meetingPersonId.setText(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            String id = data.getStringExtra("id");
            String name = data.getStringExtra("name");

            TextView meetingPersonName = (TextView) findViewById(R.id.meetingPersonName);
            meetingPersonName.setText(name);

            TextView meetingPersonId = (TextView) findViewById(R.id.meetingPersonId);
            meetingPersonId.setText(id);
        }
    }

    public void publishMeetingSave(View view)
    {
        BookMeetingDbInfoRecord info = new BookMeetingDbInfoRecord();
        info.setMeetingDate(meetingDate);
        info.setAmOrPm(type);
        info.setMeetingroom(meetingRoomId);
        info.setBookUser(userName);

        TextView meetingBeginTime = (TextView) findViewById(R.id.meetingBeginTime);
        if (StringUtils.isBlank(meetingBeginTime.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请选择开始时间", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            info.setStartTime(meetingBeginTime.getText().toString());
        }

        TextView meetingEndTime = (TextView) findViewById(R.id.meetingEndTime);
        if (StringUtils.isBlank(meetingEndTime.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请选择结束时间", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            info.setEndTime(meetingEndTime.getText().toString());
        }

        EditText threadEt = (EditText) findViewById(R.id.threadEt);
        if (StringUtils.isBlank(threadEt.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写主题", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            info.setThreaf(threadEt.getText().toString());
        }


        EditText meetingContent = (EditText) findViewById(R.id.meetingContent);
        info.setContent(meetingContent.getText().toString());

        TextView meetingPersonId = (TextView) findViewById(R.id.meetingPersonId);
        if (StringUtils.isBlank(meetingPersonId.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请选择参会人", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            info.setPerson(meetingPersonId.getText().toString());
        }

        TextView meetingPersonName = (TextView) findViewById(R.id.meetingPersonName);
        info.setPersonName(meetingPersonName.getText().toString());

        EditText meetingClothes = (EditText) findViewById(R.id.meetingClothes);
        info.setClothes(meetingClothes.getText().toString());

        EditText meetingDiscipline = (EditText) findViewById(R.id.meetingDiscipline);
        info.setMeetingDiscipline(meetingDiscipline.getText().toString());

        EditText meetingConnectPerson = (EditText) findViewById(R.id.meetingConnectPerson);
        info.setConnectPerson(meetingConnectPerson.getText().toString());

        EditText meetingConnectPhone = (EditText) findViewById(R.id.meetingConnectPhone);
        info.setConnectPhone(meetingConnectPhone.getText().toString());

        EditText zuzhiDep = (EditText) findViewById(R.id.zuzhiDep);
        if (StringUtils.isBlank(zuzhiDep.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写组织部门", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            info.setDepartmentName(zuzhiDep.getText().toString());
        }

        EditText meetingRemark = (EditText) findViewById(R.id.meetingRemark);
        info.setRemark(meetingRemark.getText().toString());

        MyTask m = new MyTask();
        m.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, info);


    }

    private class MyTask extends AsyncTask<BookMeetingDbInfoRecord, Integer, Integer>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Integer doInBackground(BookMeetingDbInfoRecord... params)
        {
            InsertPublishMeetingReq
                    req = new InsertPublishMeetingReq();
            req.setOperatorId(userName);

            req.setAmOrPm(params[0].getAmOrPm());
            req.setBookUser(params[0].getBookUser());
            req.setClothes(params[0].getClothes());
            req.setConnectPerson(params[0].getConnectPerson());
            req.setConnectPhone(params[0].getConnectPhone());
            req.setContent(params[0].getContent());
            req.setCreateTime(params[0].getCreateTime());
            req.setEndTime(params[0].getEndTime());
            req.setFiles(params[0].getFiles());
            req.setMeetingDate(params[0].getMeetingDate());
            req.setMeetingDiscipline(params[0].getMeetingDiscipline());
            req.setMeetingroom(params[0].getMeetingroom());
            req.setPerson(params[0].getPerson());
            req.setPersonName(params[0].getPersonName());
            req.setQRcode(params[0].getQRcode());
            req.setRemark(params[0].getRemark());
            req.setStartTime(params[0].getStartTime());
            req.setThreaf(params[0].getThreaf());
            req.setWakeType(params[0].getWakeType());
            req.setDepartmentName(params[0].getDepartmentName());

            String result = HttpClientClass.httpPost(req, "insertPublishMeeting");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            InsertPublishMeetingResp info = gson.fromJson(result, InsertPublishMeetingResp.class);
            if (null != info)
            {
                return info.getResult();
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

            if (null == result || result == 0)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }
            else if (10004 == result)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "会议室已被预定，请重新预定", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
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

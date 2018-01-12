package com.huizhou.receptionbooking.afterLogin.definePublishMeeting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.meetingRoom.ActivityMeetingRoomList;
import com.huizhou.receptionbooking.afterLogin.tab3.ActivityPublishMeetingAdd;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.vo.BookMeetingDbInfoRecord;
import com.huizhou.receptionbooking.request.InsertPublishMeetingReq;
import com.huizhou.receptionbooking.response.InsertPublishMeetingResp;
import com.huizhou.receptionbooking.utils.ActivityCalendarPickerView;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DefineMeetingViewActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener
{
    private String userName;
    private XTextView tv;

    int houre = 00;
    int minute = 00;

    //在TextView上显示的字符
    private StringBuffer time;

    private Context context;

    private TextView startTime;
    private TextView endTime;

    private Spinner spinnerAmPm;
    private List<String> data_listAmPm;
    private ArrayAdapter<String> arr_adapterAmPm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_meeting_view);
        context = this;
        time = new StringBuffer();

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");
        String department = userSettings.getString("department", "default");
        EditText zuzhiDepDefine = (EditText) findViewById(R.id.zuzhiDepDefine);
        zuzhiDepDefine.setText(department);

        startTime = (TextView) findViewById(R.id.meetingBeginTimeDefine);
        endTime = (TextView) findViewById(R.id.meetingEndTimeDefine);

        tv = (XTextView) findViewById(R.id.publishMeetingBackDefine);
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });


        //获取参会人Id和名字
        Intent i = getIntent();
        String userIdMeeting = i.getStringExtra("id");
        String userNameMeeting = i.getStringExtra("name");
        TextView meetingPersonNameDefine = (TextView) findViewById(R.id.meetingPersonNameDefine);
        meetingPersonNameDefine.setText(userNameMeeting);
        TextView meetingPersonIdDefine = (TextView) findViewById(R.id.meetingPersonIdDefine);
        meetingPersonIdDefine.setText(userIdMeeting);

        ///////////////////
        spinnerAmPm = (Spinner) findViewById(R.id.amOrPmDefine);

        //数据
        data_listAmPm = new ArrayList<String>();
        data_listAmPm.add("am");
        data_listAmPm.add("pm");

        //适配器
        arr_adapterAmPm = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data_listAmPm);
        //设置样式
        arr_adapterAmPm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinnerAmPm.setAdapter(arr_adapterAmPm);

        //////////////////
    }

    public void chooseMeetingRoom(View view)
    {
        Intent intent = new Intent(DefineMeetingViewActivity.this, ActivityMeetingRoomList.class);
        intent.putExtra("flag", "defineMeeting");
        startActivityForResult(intent, 100);
    }

    public void chooseDate(View view)
    {
        Intent intent = new Intent(DefineMeetingViewActivity.this, ActivityCalendarPickerView.class);
        startActivityForResult(intent, 100);
    }

    public void getStartTime(View view)
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

    public void publishMeetingSaveDefine(View view)
    {
        BookMeetingDbInfoRecord info = new BookMeetingDbInfoRecord();

        TextView meetingRoomIdAddDefine = (TextView) findViewById(R.id.meetingRoomIdAddDefine);
        if (StringUtils.isBlank(meetingRoomIdAddDefine.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请选择会议室", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            info.setMeetingroom(meetingRoomIdAddDefine.getText().toString());
        }

        TextView dateTime = (TextView) findViewById(R.id.dateEtAddDefine);
        if (StringUtils.isBlank(dateTime.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请选择日期", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            info.setMeetingDate(dateTime.getText().toString());
        }

        TextView meetingBeginTime = (TextView) findViewById(R.id.meetingBeginTimeDefine);
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

        TextView meetingEndTime = (TextView) findViewById(R.id.meetingEndTimeDefine);
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

        Spinner amOrPmDefine = (Spinner) findViewById(R.id.amOrPmDefine);
        String amPm = amOrPmDefine.getSelectedItem().toString();
        info.setAmOrPm(amPm);

        info.setBookUser(userName);

        EditText threadEt = (EditText) findViewById(R.id.threadEtDefine);
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

        EditText meetingContent = (EditText) findViewById(R.id.meetingContentDefine);
        info.setContent(meetingContent.getText().toString());

        TextView meetingPersonId = (TextView) findViewById(R.id.meetingPersonIdDefine);
        info.setPerson(meetingPersonId.getText().toString());

        TextView meetingPersonName = (TextView) findViewById(R.id.meetingPersonNameDefine);
        info.setPersonName(meetingPersonName.getText().toString());

        EditText meetingClothes = (EditText) findViewById(R.id.meetingClothesDefine);
        info.setClothes(meetingClothes.getText().toString());

        EditText meetingDiscipline = (EditText) findViewById(R.id.meetingDisciplineDefine);
        info.setMeetingDiscipline(meetingDiscipline.getText().toString());

        EditText meetingConnectPerson = (EditText) findViewById(R.id.meetingConnectPersonDefine);
        info.setConnectPerson(meetingConnectPerson.getText().toString());

        EditText meetingConnectPhone = (EditText) findViewById(R.id.meetingConnectPhoneDefine);
        info.setConnectPhone(meetingConnectPhone.getText().toString());

        EditText zuzhiDep = (EditText) findViewById(R.id.zuzhiDepDefine);
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

        EditText meetingRemark = (EditText) findViewById(R.id.meetingRemarkDefine);
        info.setRemark(meetingRemark.getText().toString());

        MyTask m = new MyTask();
        m.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, info);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            String content = data.getStringExtra("result");
            if (StringUtils.isNotBlank(content))
            {
                TextView dateTime = (TextView) findViewById(R.id.dateEtAddDefine);
                dateTime.setText(content);
            }

            String meetingRoomId = data.getStringExtra("id");
            String meetingRoomName = data.getStringExtra("name");
            if (StringUtils.isNotBlank(meetingRoomId))
            {
                TextView meetingRoomNameEtAddDefine = (TextView) findViewById(R.id.meetingRoomNameEtAddDefine);
                meetingRoomNameEtAddDefine.setText(meetingRoomName);

                TextView meetingRoomIdAddDefine = (TextView) findViewById(R.id.meetingRoomIdAddDefine);
                meetingRoomIdAddDefine.setText(meetingRoomId);
            }
        }
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

package com.huizhou.receptionbooking.afterLogin.tab1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.database.vo.BookMeetingDbInfoRecord;
import com.huizhou.receptionbooking.database.vo.MyMeetingInfoRecord;
import com.huizhou.receptionbooking.request.GetMyBeingMeetingReq;
import com.huizhou.receptionbooking.response.GetMyBeingMeetingResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ViewAndConfirmMeetingActivity extends AppCompatActivity
{

    private String id;
    private String meetingRoom;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_confirm_meeting);

        Intent i = getIntent();
        id = i.getStringExtra("id");
        meetingRoom = i.getStringExtra("meetingRoom");


    }


    /**
     * 待开会议
     */
    private class MyShowTask extends AsyncTask<String, Integer, BookMeetingDbInfoRecord>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected BookMeetingDbInfoRecord doInBackground(String... params)
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
        protected void onPostExecute(BookMeetingDbInfoRecord result)
        {
            if (!"OK".equals(result))
            {
                Toast tos = Toast.makeText(getActivity(), "查询会议信息失败，请检查网络或重试。", Toast.LENGTH_LONG);
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

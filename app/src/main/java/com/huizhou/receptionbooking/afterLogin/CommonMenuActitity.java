package com.huizhou.receptionbooking.afterLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.bookDining.BookDiningListActivity;
import com.huizhou.receptionbooking.afterLogin.definePublishMeeting.DefinePublishMeetingActivity;
import com.huizhou.receptionbooking.afterLogin.dinningMenu.CookBookListActivity;
import com.huizhou.receptionbooking.request.GetMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.request.SaveMeetingConfirmReq;
import com.huizhou.receptionbooking.request.UpdateMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.response.GetMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.response.SaveMeetingConfirmResp;
import com.huizhou.receptionbooking.response.UpdateMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.apache.commons.lang3.StringUtils;

public class CommonMenuActitity extends AppCompatActivity
{
    private String userName;
    private String loginShowName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_menu_actitity);

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName= userSettings.getString("loginUserName", "default");
        loginShowName = userSettings.getString("loginShowName", "default");

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.3);
        p.height = 450;   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.4);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.0f;      //设置黑暗度
        p.y = 40;
        //p.x=0;
        p.gravity = Gravity.RIGHT | Gravity.TOP;
        getWindow().setAttributes(p);     //设置生效
        //getWindow().setGravity(Gravity.TOP);       //设置靠右对齐
    }

    public void scan(View view)
    {
        Intent i = new Intent(this, CaptureActivity.class);
        i.putExtra("type","a");
        startActivityForResult(i, 0);

        //startActivityForResult(new Intent(this, CaptureActivity.class), 0);
        //finish();
    }

    public void definePublishMeeting(View view)
    {
        startActivityForResult(new Intent(this, DefinePublishMeetingActivity.class), 0);
        finish();
    }

    public void defineCookbook(View view)
    {
        startActivityForResult(new Intent(this, CookBookListActivity.class), 0);
        finish();
    }

    public void defineBookDining(View view)
    {
        startActivityForResult(new Intent(this, BookDiningListActivity.class), 0);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            if (bundle != null)
            {
                String id  = bundle.getString("result");
                //System.out.println("0000000000=" + id);

                MySignMeetingTask myTask = new MySignMeetingTask();
                myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
            }
        }

    }


    /**
     * 扫一扫签到会议
     */
    public class MySignMeetingTask extends AsyncTask<String, Integer, String>
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
                Toast tos = Toast.makeText(CommonMenuActitity.this, "保存数据失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }
            else
            {
                Toast tos = Toast.makeText(CommonMenuActitity.this, "签到成功!", Toast.LENGTH_LONG);
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

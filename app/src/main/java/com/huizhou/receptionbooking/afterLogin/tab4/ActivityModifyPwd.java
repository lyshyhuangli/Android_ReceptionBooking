package com.huizhou.receptionbooking.afterLogin.tab4;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.tab3.ActivityPublishMeetingEdit;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.UserInfoDAO;
import com.huizhou.receptionbooking.database.dao.impl.UserInfoDAOImpl;
import com.huizhou.receptionbooking.database.vo.BookMeetingDbInfoRecord;
import com.huizhou.receptionbooking.database.vo.UerInfoRecord;
import com.huizhou.receptionbooking.request.CheckUserByUserAndPwdReq;
import com.huizhou.receptionbooking.request.UpdateMeetingInfoByIdReq;
import com.huizhou.receptionbooking.response.ModifyPwdByUserNameResp;
import com.huizhou.receptionbooking.response.UpdateMeetingInfoByIdResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityModifyPwd extends AppCompatActivity
{
    private XTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);

        tv = (XTextView) this.findViewById(R.id.modifyPwdBack);
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

    public void modifyPwd(View view)
    {
        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        String userName = userSettings.getString("loginUserName", "default");

        TextInputLayout pwd1Wrapper = (TextInputLayout) findViewById(R.id.pwd1Wrapper);
        TextInputLayout pwd2Wrapper = (TextInputLayout) findViewById(R.id.pwd2Wrapper);

        String pwd1 = pwd1Wrapper.getEditText().getText().toString();
        String pwd2 = pwd2Wrapper.getEditText().getText().toString();

        if (!StringUtils.isBlank(pwd1) && !StringUtils.isEmpty(pwd1))
        {
            if (!pwd1.equals(pwd2))
            {
                Toast tos = Toast.makeText(this, "2次输入的密码不匹配!", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.TOP, 0, 200);
                tos.show();
                return;
            }
        }
        else
        {
            Toast tos = Toast.makeText(this, "密码不能为空!", Toast.LENGTH_LONG);
            tos.setGravity(Gravity.TOP, 0, 200);
            tos.show();
            return;
        }

        MyTask m = new MyTask();
        m.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, userName, pwd2);
    }

    private class MyTask extends AsyncTask<String, Integer, Boolean>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Boolean doInBackground(String... params)
        {
            CheckUserByUserAndPwdReq req = new CheckUserByUserAndPwdReq();
            req.setPwd(params[1]);
            req.setUserName(params[0]);
            req.setOperatorId(params[0]);

            String result = HttpClientClass.httpPost(req, "modifyPwdByUserName");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            ModifyPwdByUserNameResp info = gson.fromJson(result, ModifyPwdByUserNameResp.class);
            if (null != info)
            {
                if (0 == info.getResultCode())
                {
                    return info.getIsOK();
                }
            }

            return false;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses)
        {

        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(Boolean result)
        {

            if (! result)
            {
                Toast tos = Toast.makeText(ActivityModifyPwd.this, "密码修改失败，请重试!", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }
            else
            {
                Toast tos = Toast.makeText(ActivityModifyPwd.this, "密码修改成功!", Toast.LENGTH_SHORT);
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

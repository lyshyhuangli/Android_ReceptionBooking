package com.huizhou.receptionbooking.afterLogin.contactGroup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityCheckBoxContactList;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityContactEdit;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.GroupPersonDAO;
import com.huizhou.receptionbooking.database.dao.UserInfoDAO;
import com.huizhou.receptionbooking.database.dao.impl.GroupPersonDAOImpl;
import com.huizhou.receptionbooking.database.dao.impl.UserInfoDAOImpl;
import com.huizhou.receptionbooking.database.vo.GroupPersonInfoRecord;
import com.huizhou.receptionbooking.database.vo.UerInfoRecord;
import com.huizhou.receptionbooking.request.GetGroupPersonByIdReq;
import com.huizhou.receptionbooking.request.UpdateGroupPersonByIdReq;
import com.huizhou.receptionbooking.response.CheckUserByUserAndPwdResp;
import com.huizhou.receptionbooking.response.GetGroupPersonByIdResp;
import com.huizhou.receptionbooking.response.UpdateGroupPersonByIdResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroupEdit extends AppCompatActivity
{
    private MyEditTask mTask;
    private MyShowTask showTask;
    private XTextView tv;
    private String userName;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");

        Intent i = getIntent();
        id = i.getStringExtra("id");

        tv = (XTextView) this.findViewById(R.id.groupEditBack);
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
        showTask.execute();
    }


    public void saveGroupEdit(View view)
    {
        TextView groupEditMr = (TextView) findViewById(R.id.groupEditMr);
        String groupUserName = groupEditMr.getText().toString();
        if (StringUtils.isBlank(groupUserName))
        {
            Toast tos = Toast.makeText(this, "群组联系人不能为空", Toast.LENGTH_LONG);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
        }

        TextView groupEditdMrId = (TextView) findViewById(R.id.groupEditdMrId);
        String userIds = groupEditdMrId.getText().toString();

        EditText grouptNameAdd = (EditText) findViewById(R.id.grouptNameEdit);
        String grouptName = grouptNameAdd.getText().toString();

        if (StringUtils.isBlank(grouptName))
        {
            Toast tos = Toast.makeText(this, "群组名不能为空", Toast.LENGTH_LONG);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
        }

        mTask = new MyEditTask();
        mTask.execute(userName, groupUserName, userIds, grouptName);
    }

    public void getGroupPersonEdit(View view)
    {
        Intent intent = new Intent(ActivityGroupEdit.this, ActivityCheckBoxContactList.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            String id = data.getStringExtra("id");
            String name = data.getStringExtra("name");

            TextView groupEditMr = (TextView) findViewById(R.id.groupEditMr);
            groupEditMr.setText(name);

            TextView groupEditdMrId = (TextView) findViewById(R.id.groupEditdMrId);
            groupEditdMrId.setText(id);
        }
    }


    private class MyEditTask extends AsyncTask<String, Integer, Boolean>
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

            UpdateGroupPersonByIdReq req = new UpdateGroupPersonByIdReq();
            req.setUserPhone(params[0]);
            req.setGroupUserName(params[1]);
            req.setGroupUserId(params[2]);
            req.setGroupName(params[3]);
            req.setId(Integer.parseInt(id));
            req.setOperatorId(userName);
            String result = HttpClientClass.httpPost(req, "updateGroupPersonById");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            UpdateGroupPersonByIdResp info = gson.fromJson(result, UpdateGroupPersonByIdResp.class);
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
        protected void onPostExecute(Boolean list)
        {
            if (!list.booleanValue())
            {
                Toast tos = Toast.makeText(getApplicationContext(), "保存失败.", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }
            else
            {
                Toast tos = Toast.makeText(getApplicationContext(), "保存成功.", Toast.LENGTH_LONG);
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

    private class MyShowTask extends AsyncTask<String, Integer, GroupPersonInfoRecord>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected GroupPersonInfoRecord doInBackground(String... params)
        {
            GetGroupPersonByIdReq
                    req = new GetGroupPersonByIdReq();
            req.setId(id);
            req.setOperatorId(userName);
            String result = HttpClientClass.httpPost(req, "getGroupPersonById");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            GetGroupPersonByIdResp info = gson.fromJson(result, GetGroupPersonByIdResp.class);
            if (null != info)
            {
                if (0 == info.getResultCode())
                {
                    return info.getInfo();
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
        protected void onPostExecute(GroupPersonInfoRecord d)
        {
            if (null == d)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "查询群组信息失败", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }
            else
            {
                TextView groupEditMr = (TextView) findViewById(R.id.groupEditMr);
                groupEditMr.setText(d.getGroupUserName());


                TextView groupEditdMrId = (TextView) findViewById(R.id.groupEditdMrId);
                groupEditdMrId.setText(d.getGroupUserId());

                EditText grouptNameEdit = (EditText) findViewById(R.id.grouptNameEdit);
                grouptNameEdit.setText(d.getGroupName());

                TextView grouptNameEditId = (TextView) findViewById(R.id.grouptNameEditId);
                grouptNameEditId.setText(String.valueOf(d.getId()));
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }


}

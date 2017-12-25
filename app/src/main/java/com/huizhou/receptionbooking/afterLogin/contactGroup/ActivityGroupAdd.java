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
import android.widget.TextView;
import android.widget.Toast;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityCheckBoxContactList;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityContactAdd;
import com.huizhou.receptionbooking.afterLogin.department.ActivityDepartmentList;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.GroupPersonDAO;
import com.huizhou.receptionbooking.database.dao.UserInfoDAO;
import com.huizhou.receptionbooking.database.dao.impl.GroupPersonDAOImpl;
import com.huizhou.receptionbooking.database.dao.impl.UserInfoDAOImpl;
import com.huizhou.receptionbooking.database.vo.GroupPersonInfoRecord;
import com.huizhou.receptionbooking.database.vo.UerInfoRecord;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroupAdd extends AppCompatActivity
{

    private MyEditTask mTask;
    private XTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);


        tv = (XTextView) this.findViewById(R.id.groupAddBack);
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

    public void saveGroupAdd(View view)
    {
        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        String loginUserName = userSettings.getString("loginUserName", "default");

        TextView groupAddMr = (TextView) findViewById(R.id.groupAddMr);
        String groupUserName = groupAddMr.getText().toString();
        if(StringUtils.isBlank(groupUserName))
        {
            Toast tos = Toast.makeText(this,"请选择群组联系人", Toast.LENGTH_LONG);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }

        TextView groupAddMrId = (TextView) findViewById(R.id.groupAddMrId);
        String userIds = groupAddMrId.getText().toString();

        EditText grouptNameAdd = (EditText) findViewById(R.id.grouptNameAdd);
        String grouptName = grouptNameAdd.getText().toString();

        if(StringUtils.isBlank(grouptName))
        {
            Toast tos = Toast.makeText(this,"请填写群组名", Toast.LENGTH_LONG);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }

        mTask = new MyEditTask();
        mTask.execute(loginUserName, groupUserName, userIds, grouptName);
    }

    public void getGroupPerson(View view)
    {
        Intent intent = new Intent(ActivityGroupAdd.this, ActivityCheckBoxContactList.class);
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

            TextView groupAddMr = (TextView) findViewById(R.id.groupAddMr);
            groupAddMr.setText(name);

            TextView groupAddMrId = (TextView) findViewById(R.id.groupAddMrId);
            groupAddMrId.setText(id);
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
            GroupPersonInfoRecord g = new GroupPersonInfoRecord();
            g.setUserPhone(params[0]);
            g.setGroupUserName(params[1]);
            g.setGroupUserId(params[2]);
            g.setGroupName(params[3]);
            try
            {
                GroupPersonDAO dao = new GroupPersonDAOImpl();
                dao.saveGroupPerson(g, errorList);
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

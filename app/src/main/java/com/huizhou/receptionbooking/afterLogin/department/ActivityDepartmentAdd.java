package com.huizhou.receptionbooking.afterLogin.department;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.common.BaseTreeBean;
import com.huizhou.receptionbooking.common.Node;
import com.huizhou.receptionbooking.common.SimpleTreeAdapter;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.DepartmentDAO;
import com.huizhou.receptionbooking.database.dao.impl.DepartmentDAOImpl;
import com.huizhou.receptionbooking.database.vo.DepartmentInfoRecord;
import com.huizhou.receptionbooking.request.AddDepartmentRequest;
import com.huizhou.receptionbooking.request.AddEditBookDiningReq;
import com.huizhou.receptionbooking.response.AddDepartmentResp;
import com.huizhou.receptionbooking.response.AddEditBookDiningResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityDepartmentAdd extends AppCompatActivity
{
    private MyTask mTask;
    private XTextView tv;
    private String loginUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_add);

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        loginUserName = userSettings.getString("loginUserName", "default");

        tv = (XTextView) this.findViewById(R.id.depatmentAddBack);

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

    public void saveDepartment(View view)
    {
        TextView depatmentAddMr = (TextView) findViewById(R.id.depatmentAddMr);

        TextView depatmentAddMrId = (TextView) findViewById(R.id.depatmentAddMrId);
        int parentId = 0;
        if (StringUtils.isBlank(depatmentAddMrId.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请选择上级部门", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            parentId = Integer.valueOf((String) depatmentAddMrId.getText());
        }

        EditText depatmentNameAdd = (EditText) findViewById(R.id.depatmentNameAdd);
        String departmentName = null;
        if (StringUtils.isBlank(depatmentNameAdd.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写部门", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            departmentName = depatmentNameAdd.getText().toString();
        }

        EditText remarkDepartmentAdd = (EditText) findViewById(R.id.remarkDepartmentAdd);
        String remark = remarkDepartmentAdd.getText().toString();

        mTask = new MyTask();
        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,String.valueOf(parentId), departmentName, remark);
    }

    public void getParentDepartment(View view)
    {
        Intent intent = new Intent(ActivityDepartmentAdd.this, ActivityDepartmentList.class);
        intent.putExtra("type", "addDepartment");
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

            TextView depatmentAddMr = (TextView) findViewById(R.id.depatmentAddMr);
            depatmentAddMr.setText(name);

            TextView depatmentAddMrId = (TextView) findViewById(R.id.depatmentAddMrId);
            depatmentAddMrId.setText(id);
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
            AddDepartmentRequest req = new AddDepartmentRequest();
            req.setOperatorId(loginUserName);
            req.setName(params[1]);
            req.setParentId(Integer.valueOf(params[0]));
            req.setType(1);
            req.setRemark(params[2]);

            String result = HttpClientClass.httpPost(req, "addDepartment");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            AddDepartmentResp info = gson.fromJson(result, AddDepartmentResp.class);
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

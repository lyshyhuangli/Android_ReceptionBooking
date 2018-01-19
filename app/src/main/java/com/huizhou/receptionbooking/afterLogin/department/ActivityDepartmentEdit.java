package com.huizhou.receptionbooking.afterLogin.department;

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
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.DepartmentDAO;
import com.huizhou.receptionbooking.database.dao.impl.DepartmentDAOImpl;
import com.huizhou.receptionbooking.database.vo.DepartmentInfoRecord;
import com.huizhou.receptionbooking.request.EditDepartmentRequest;
import com.huizhou.receptionbooking.response.AddDepartmentResp;
import com.huizhou.receptionbooking.response.EditDepartmentResp;
import com.huizhou.receptionbooking.utils.CommonUtils;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityDepartmentEdit extends AppCompatActivity
{
    private MyEditTask mTask;
    private MyShowTask showTask;
    private XTextView tv;
    private String id;
    private String loginUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_edit);

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        loginUserName = userSettings.getString("loginUserName", "default");

        Intent i = getIntent();
        id = i.getStringExtra("id");

        tv = (XTextView) this.findViewById(R.id.depatmentEditBack);

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

    public void saveDepartmentEdit(View view)
    {
        TextView depatmentEditMr = (TextView) findViewById(R.id.depatmentEditMr);

        TextView depatmentEditMrId = (TextView) findViewById(R.id.depatmentEditMrId);
        int parentId = 0;
        if (StringUtils.isBlank(depatmentEditMrId.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请选择上级部门", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            parentId = Integer.valueOf((String) depatmentEditMrId.getText());
        }

        EditText depatmentNameEt = (EditText) findViewById(R.id.depatmentNameEt);
        String departmentName = null;
        if (StringUtils.isBlank(depatmentNameEt.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写部门", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            departmentName = depatmentNameEt.getText().toString();
        }

        EditText depatmentSortEdit = (EditText) findViewById(R.id.depatmentSortEdit);
        String depatmentSort = depatmentSortEdit.getText().toString();
        if (StringUtils.isBlank(depatmentSort) || !CommonUtils.isNumeric(depatmentSort))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写正确的序号", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }

        EditText remarkDepartmentEt = (EditText) findViewById(R.id.remarkDepartmentEt);
        String remark = remarkDepartmentEt.getText().toString();

        mTask = new MyEditTask();
        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(parentId), departmentName, remark, String.valueOf(depatmentSort));
    }

    public void getParentDepartmentEdit(View view)
    {
        Intent intent = new Intent(ActivityDepartmentEdit.this, ActivityDepartmentList.class);
        intent.putExtra("type", "editDepartment");
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

            TextView depatmentEditMr = (TextView) findViewById(R.id.depatmentEditMr);
            depatmentEditMr.setText(name);

            TextView depatmentEditMrId = (TextView) findViewById(R.id.depatmentEditMrId);
            depatmentEditMrId.setText(parentId);
        }
    }

    private class MyShowTask extends AsyncTask<String, Integer, DepartmentInfoRecord>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected DepartmentInfoRecord doInBackground(String... params)
        {
            List<String> errorList = new ArrayList<>();
            try
            {
                DepartmentDAO dao = new DepartmentDAOImpl();
                DepartmentInfoRecord d = dao.getDepartmentById(Integer.valueOf(params[0]), errorList);
                return d;
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
        protected void onPostExecute(DepartmentInfoRecord d)
        {
            if (null == d)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "查询部门信息失败", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }
            else
            {
                try
                {
                    TextView depatmentEditMr = (TextView) findViewById(R.id.depatmentEditMr);
                    depatmentEditMr.setText(d.getParentName());

                    TextView depatmentEditMrId = (TextView) findViewById(R.id.depatmentEditMrId);
                    depatmentEditMrId.setText(String.valueOf(d.getParentId()));

                    EditText depatmentNameEt = (EditText) findViewById(R.id.depatmentNameEt);
                    depatmentNameEt.setText(d.getName());

                    EditText remarkDepartmentEt = (EditText) findViewById(R.id.remarkDepartmentEt);
                    remarkDepartmentEt.setText(d.getRemark());

                    EditText depatmentSortEdit = (EditText) findViewById(R.id.depatmentSortEdit);
                    depatmentSortEdit.setText(String.valueOf(d.getDepSort()));

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

    private class MyEditTask extends AsyncTask<String, Integer, Integer>
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
            EditDepartmentRequest req = new EditDepartmentRequest();
            req.setOperatorId(loginUserName);
            req.setName(params[1]);
            req.setParentId(Integer.valueOf(params[0]));
            req.setType(1);
            req.setRemark(params[2]);
            req.setId(Integer.valueOf(id));
            req.setDepSort(Integer.valueOf(params[3]));

            String result = HttpClientClass.httpPost(req, "editDepartment");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            EditDepartmentResp info = gson.fromJson(result, EditDepartmentResp.class);
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

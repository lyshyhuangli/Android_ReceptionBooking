package com.huizhou.receptionbooking.afterLogin.contacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.department.ActivityDepartmentList;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.UserInfoDAO;
import com.huizhou.receptionbooking.database.dao.impl.UserInfoDAOImpl;
import com.huizhou.receptionbooking.database.vo.UerInfoRecord;
import com.huizhou.receptionbooking.request.AddUserInfoReq;
import com.huizhou.receptionbooking.request.EditUserInfoReq;
import com.huizhou.receptionbooking.response.AddUserInfoResp;
import com.huizhou.receptionbooking.response.EditUserInfoResp;
import com.huizhou.receptionbooking.utils.CommonUtils;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityContactEdit extends AppCompatActivity
{

    private MyEditTask mTask;
    private MyShowTask showTask;
    private XTextView tv;
    private String id;

    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    private Spinner spinnerSex;
    private List<String> data_listSex;
    private ArrayAdapter<String> arr_adapterSex;

    private String role;
    private String loginUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        role = userSettings.getString("role", "default");
        loginUserName = userSettings.getString("loginUserName", "default");

        spinner = (Spinner) findViewById(R.id.contactRoleSpinner);

        //数据
        data_list = new ArrayList<String>();
        data_list.add("普通");
        data_list.add("管理员");

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);

        ///////////////////
        spinnerSex = (Spinner) findViewById(R.id.contactSexSpinner);

        //数据
        data_listSex = new ArrayList<String>();
        data_listSex.add("男");
        data_listSex.add("女");

        //适配器
        arr_adapterSex = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data_listSex);
        //设置样式
        arr_adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinnerSex.setAdapter(arr_adapterSex);

        //////////////////

        Intent i = getIntent();
        id = i.getStringExtra("id");

        tv = (XTextView) this.findViewById(R.id.contactEditBack);
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


    public void saveUserEdit(View view)
    {
        TextView contactDepatmentMrEdit = (TextView) findViewById(R.id.contactDepatmentMrEdit);

        TextView contactDepatmentEditMrId = (TextView) findViewById(R.id.contactDepatmentEditMrId);
        int parentId = 0;
        if (StringUtils.isBlank(contactDepatmentEditMrId.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请选择部门", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            parentId = Integer.valueOf((String) contactDepatmentEditMrId.getText());
        }

        EditText contactNameEt = (EditText) findViewById(R.id.contactNameEt);
        String contactName = null;
        if (StringUtils.isBlank(contactNameEt.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写姓名", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            contactName = contactNameEt.getText().toString();
        }

        EditText contactRemarkEt = (EditText) findViewById(R.id.contactRemarkEt);
        String remark = contactRemarkEt.getText().toString();

        EditText contactPhoneEt = (EditText) findViewById(R.id.contactPhoneEt);
        String phone = null;
        if (StringUtils.isBlank(contactPhoneEt.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写手机号", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            phone = contactPhoneEt.getText().toString();
        }

        EditText contactSortEdit = (EditText) findViewById(R.id.contactSortEdit);
        String contactSort = contactSortEdit.getText().toString();
        if (StringUtils.isBlank(contactSort) || !CommonUtils.isNumeric(contactSort))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写正确的序号", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }

        Spinner contactSexSpinner = (Spinner) findViewById(R.id.contactSexSpinner);
        String sex = contactSexSpinner.getSelectedItem().toString();

        EditText contactIdcardEt = (EditText) findViewById(R.id.contactIdcardEt);
        String idCard = contactIdcardEt.getText().toString();

        Spinner contactRoleSpinner = (Spinner) findViewById(R.id.contactRoleSpinner);
        String role = contactRoleSpinner.getSelectedItem().toString();

        EditText contactPostEdit = (EditText) findViewById(R.id.contactPostEdit);
        String contactPost = contactPostEdit.getText().toString();

        EditText contactCarIdEdit = (EditText) findViewById(R.id.contactCarIdEdit);
        String contactCarId = contactCarIdEdit.getText().toString();

        mTask = new MyEditTask();
        mTask.execute(String.valueOf(parentId), contactName, remark, phone, sex, idCard, role, contactPost, contactCarId, contactSort);
    }

    public void getParentDepartmentEditForContact(View view)
    {
        Intent intent = new Intent(ActivityContactEdit.this, ActivityDepartmentList.class);
        intent.putExtra("type", "editContact");
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

            TextView contactDepatmentMr = (TextView) findViewById(R.id.contactDepatmentMrEdit);
            contactDepatmentMr.setText(name);

            TextView contactDepatmentEditMrId = (TextView) findViewById(R.id.contactDepatmentEditMrId);
            contactDepatmentEditMrId.setText(parentId);
        }
    }

    private class MyShowTask extends AsyncTask<String, Integer, UerInfoRecord>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected UerInfoRecord doInBackground(String... params)
        {
            List<String> errorList = new ArrayList<>();
            try
            {
                UserInfoDAO dao = new UserInfoDAOImpl();
                UerInfoRecord m = dao.getContactPersionById(Integer.valueOf(params[0]), errorList);
                return m;
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
        protected void onPostExecute(UerInfoRecord d)
        {
            if (null == d)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "查询通讯录信息失败", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }
            else
            {
                TextView contactDepatmentMrEdit = (TextView) findViewById(R.id.contactDepatmentMrEdit);
                contactDepatmentMrEdit.setText(d.getParentName());

                TextView contactDepatmentEditMrId = (TextView) findViewById(R.id.contactDepatmentEditMrId);
                contactDepatmentEditMrId.setText(String.valueOf(d.getParentId()));

                EditText contactNameEt = (EditText) findViewById(R.id.contactNameEt);
                contactNameEt.setText(d.getName());

                EditText contactPhoneEt = (EditText) findViewById(R.id.contactPhoneEt);
                contactPhoneEt.setText(d.getPhone());

                Spinner contactSexSpinner = (Spinner) findViewById(R.id.contactSexSpinner);
                SpinnerAdapter sexAdapter = contactSexSpinner.getAdapter(); //得到SpinnerAdapter对象
                int k = sexAdapter.getCount();
                for (int i = 0; i < k; i++)
                {
                    if (d.getSex() != null && d.getSex().equals(sexAdapter.getItem(i).toString()))
                    {
                        contactSexSpinner.setSelection(i, true);// 默认选中项
                        break;
                    }
                }

                EditText contactIdcardEt = (EditText) findViewById(R.id.contactIdcardEt);
                contactIdcardEt.setText(d.getIdcard());

                Spinner contactRoleSpinner = (Spinner) findViewById(R.id.contactRoleSpinner);
                SpinnerAdapter roleAdapter = contactRoleSpinner.getAdapter(); //得到SpinnerAdapter对象
                int j = roleAdapter.getCount();
                for (int i = 0; i < j; i++)
                {
                    if (d.getRole() != null && d.getRole().equals(roleAdapter.getItem(i).toString()))
                    {
                        contactRoleSpinner.setSelection(i, true);// 默认选中项
                        break;
                    }
                }

                EditText contactSortEdit = (EditText) findViewById(R.id.contactSortEdit);
                contactSortEdit.setText(String.valueOf(d.getPersonSort()));

                EditText contactPostEdit = (EditText) findViewById(R.id.contactPostEdit);
                contactPostEdit.setText(d.getPost());

                EditText contactRemarkEt = (EditText) findViewById(R.id.contactRemarkEt);
                contactRemarkEt.setText(d.getRemark());


                EditText contactCarIdEdit = (EditText) findViewById(R.id.contactCarIdEdit);
                contactCarIdEdit.setText(d.getCarId());

                if (!"管理员".equals(role))
                {
                    Button editUserConform = (Button) findViewById(R.id.editUserConform);
                    editUserConform.setVisibility(View.GONE);
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
            EditUserInfoReq req = new EditUserInfoReq();
            req.setOperatorId(loginUserName);
            req.setName(params[1]);
            req.setParentId(Integer.valueOf(params[0]));
            req.setType(3);
            req.setRemark(params[2]);
            req.setPhone(params[3]);
            req.setSex(params[4]);
            req.setIdCard(params[5]);
            req.setRole(params[6]);
            req.setPost(params[7]);
            req.setCarId(params[8]);
            req.setId(Integer.valueOf(id));
            req.setPersonSort(Integer.parseInt(params[9]));

            String result = HttpClientClass.httpPost(req, "editUserInfo");
            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            EditUserInfoResp info = gson.fromJson(result, EditUserInfoResp.class);
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

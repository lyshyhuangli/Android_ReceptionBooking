package com.huizhou.receptionbooking.afterLogin.contacts;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.department.ActivityDepartmentList;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.UserInfoDAO;
import com.huizhou.receptionbooking.database.dao.impl.UserInfoDAOImpl;
import com.huizhou.receptionbooking.database.vo.UerInfoRecord;

import java.util.ArrayList;
import java.util.List;

public class ActivityContactAdd extends AppCompatActivity
{

    private MyEditTask mTask;
    private XTextView tv;

    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    private Spinner spinnerSex;
    private List<String> data_listSex;
    private ArrayAdapter<String> arr_adapterSex;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);

        spinner = (Spinner) findViewById(R.id.contactRoleSpinnerAdd);

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
        spinnerSex = (Spinner) findViewById(R.id.contactSexSpinnerAdd);

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

        tv = (XTextView) this.findViewById(R.id.contactAddBack);
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

    public void saveUserAdd(View view)
    {
        TextView contactDepatmentMrAdd = (TextView) findViewById(R.id.contactDepatmentMrAdd);

        TextView contactDepatmentAddMrId = (TextView) findViewById(R.id.contactDepatmentAddMrId);
        int parentId = Integer.valueOf((String) contactDepatmentAddMrId.getText());

        EditText contactNameAdd = (EditText) findViewById(R.id.contactNameAdd);
        String contactName = contactNameAdd.getText().toString();

        EditText contactRemarkAdd = (EditText) findViewById(R.id.contactRemarkAdd);
        String remark = contactRemarkAdd.getText().toString();

        EditText contactPhoneAdd = (EditText) findViewById(R.id.contactPhoneAdd);
        String phone = contactPhoneAdd.getText().toString();

        Spinner contactSexSpinnerAdd = (Spinner) findViewById(R.id.contactSexSpinnerAdd);
        String sex = contactSexSpinnerAdd.getSelectedItem().toString();


        EditText contactIdcardAdd = (EditText) findViewById(R.id.contactIdcardAdd);
        String idCard = contactIdcardAdd.getText().toString();

        Spinner contactRoleSpinnerAdd = (Spinner) findViewById(R.id.contactRoleSpinnerAdd);
        String role = contactRoleSpinnerAdd.getSelectedItem().toString();

        mTask = new MyEditTask();
        mTask.execute(String.valueOf(parentId), contactName, remark, phone, sex, idCard, role);

    }

    public void getParentDepartmentAddForContact(View view)
    {
        Intent intent = new Intent(ActivityContactAdd.this, ActivityDepartmentList.class);
        intent.putExtra("type", "addContact");
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

            TextView contactDepatmentMrAdd = (TextView) findViewById(R.id.contactDepatmentMrAdd);
            contactDepatmentMrAdd.setText(name);

            TextView contactDepatmentAddMrId = (TextView) findViewById(R.id.contactDepatmentAddMrId);
            contactDepatmentAddMrId.setText(parentId);
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
            // mTask.execute(String.valueOf(parentId), contactName, remark, phone, sex, idCard, role);
            List<String> errorList = new ArrayList<>();
            UerInfoRecord d = new UerInfoRecord();
            d.setParentId(Integer.valueOf(params[0]));
            d.setName(params[1]);
            d.setRemark(params[2]);
            d.setPhone(params[3]);
            d.setSex(params[4]);
            d.setIdcard(params[5]);
            d.setRole(params[6]);
            try
            {
                UserInfoDAO dao = new UserInfoDAOImpl();
                dao.saveContactPersion(d, errorList);
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

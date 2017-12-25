package com.huizhou.receptionbooking.afterLogin.dinningMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.vo.CookBookRecord;
import com.huizhou.receptionbooking.request.AddEditCookBookReq;
import com.huizhou.receptionbooking.request.GetCookBookByIdReq;
import com.huizhou.receptionbooking.response.AddEditCookBookResp;
import com.huizhou.receptionbooking.response.GetCookBookByIdResp;
import com.huizhou.receptionbooking.utils.ActivityCalendarPickerView;
import com.huizhou.receptionbooking.utils.CommonUtils;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EditCookBookActivity extends AppCompatActivity
{
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    private MyShowTask showTask;
    private XTextView tv;
    private String userName;
    private String role;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cook_book);

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");
        role = userSettings.getString("role", "default");



        Intent i = getIntent();
        id = i.getStringExtra("id");

        tv = (XTextView) this.findViewById(R.id.cookBookEditBack);
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });

        spinner = (Spinner) findViewById(R.id.menuTypeEdit);

        //数据
        data_list = new ArrayList<String>();
        data_list.add("早餐");
        data_list.add("中餐");
        data_list.add("晚餐");

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);

        showTask = new MyShowTask();
        showTask.execute();
    }

    private void hideKeyboard()
    {
        View view = getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
    }


    public void getDateEdit(View view)
    {
        Intent intent = new Intent(EditCookBookActivity.this, ActivityCalendarPickerView.class);
        startActivityForResult(intent, 100);
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
                TextView menuTypeDateEdit = (TextView) findViewById(R.id.menuTypeDateEdit);
                menuTypeDateEdit.setText(content);
            }
        }
    }

    public void cookBookEditSaveDefine(View view)
    {
        CookBookRecord info = new CookBookRecord();
        TextView menuTypeDate = (TextView) findViewById(R.id.menuTypeDateEdit);
        info.setPublishDate(menuTypeDate.getText().toString());

        Spinner menuTypeAdd = (Spinner) findViewById(R.id.menuTypeEdit);
        info.setMenuType(menuTypeAdd.getSelectedItem().toString());

        info.setMenuTypeNumber(CommonUtils.getMenuTypeNumber(menuTypeAdd.getSelectedItem().toString()));

        EditText menuContentEdit = (EditText) findViewById(R.id.menuContentEdit);
        if (StringUtils.isBlank(menuContentEdit.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写菜谱内容", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            info.setMenuContent(menuContentEdit.getText().toString());
        }

        MyEditTask m = new MyEditTask();
        m.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, info);
    }

    private class MyEditTask extends AsyncTask<CookBookRecord, Integer, String>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(CookBookRecord... params)
        {
            AddEditCookBookReq req = new AddEditCookBookReq();
            req.setOperatorId(userName);
            req.setMenuContent(params[0].getMenuContent());
            req.setMenuType(params[0].getMenuType());
            req.setPublishDate(params[0].getPublishDate());
            req.setId(Integer.parseInt(id));
            req.setMenuTypeNumber(params[0].getMenuTypeNumber());

            String result = HttpClientClass.httpPost(req, "updateCookBookById");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            AddEditCookBookResp info = gson.fromJson(result, AddEditCookBookResp.class);
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
        protected void onPostExecute(String result)
        {

            if (!"OK".equals(result))
            {
                Toast tos = Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_LONG);
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

    private class MyShowTask extends AsyncTask<String, Integer, CookBookRecord>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected CookBookRecord doInBackground(String... params)
        {
            GetCookBookByIdReq
                    req = new GetCookBookByIdReq();
            req.setId(Integer.parseInt(id));
            req.setOperatorId(userName);
            String result = HttpClientClass.httpPost(req, "getCookBookById");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            GetCookBookByIdResp info = gson.fromJson(result, GetCookBookByIdResp.class);
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
        protected void onPostExecute(CookBookRecord d)
        {
            if (null == d)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "查询信息失败", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }
            else
            {
                TextView menuTypeDate = (TextView) findViewById(R.id.menuTypeDateEdit);
                menuTypeDate.setText(d.getPublishDate());

                Spinner menuTypeEdit = (Spinner) findViewById(R.id.menuTypeEdit);
                SpinnerAdapter menuTypeAdapter = menuTypeEdit.getAdapter(); //得到SpinnerAdapter对象
                int k = menuTypeAdapter.getCount();
                for (int i = 0; i < k; i++)
                {
                    if (d.getMenuType() != null && d.getMenuType().equals(menuTypeAdapter.getItem(i).toString()))
                    {
                        menuTypeEdit.setSelection(i, true);// 默认选中项
                        break;
                    }
                }

                EditText menuContentEdit = (EditText) findViewById(R.id.menuContentEdit);
                menuContentEdit.setText(d.getMenuContent());

                if (!"管理员".equals(role))
                {
                    Button cookBookEditSaveDefine = (Button) findViewById(R.id.cookBookEditSaveDefine);
                    cookBookEditSaveDefine.setVisibility(View.GONE);
                }

                hideKeyboard();
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

}

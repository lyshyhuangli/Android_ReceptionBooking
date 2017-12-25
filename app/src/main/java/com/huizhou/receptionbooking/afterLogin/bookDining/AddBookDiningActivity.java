package com.huizhou.receptionbooking.afterLogin.bookDining;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.vo.BookingDiningRecord;
import com.huizhou.receptionbooking.request.AddEditBookDiningReq;
import com.huizhou.receptionbooking.response.AddEditBookDiningResp;
import com.huizhou.receptionbooking.utils.ActivityCalendarPickerView;
import com.huizhou.receptionbooking.utils.CommonUtils;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AddBookDiningActivity extends AppCompatActivity
{
    String loginUserName;
    private XTextView tv;

    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_dining);
        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        loginUserName = userSettings.getString("loginUserName", "default");

        spinner = (Spinner) findViewById(R.id.diningTypeAdd);

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

        tv = (XTextView) this.findViewById(R.id.bookDiningAddBack);
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });

        TextView diningDate = (TextView) findViewById(R.id.diningDate);
        diningDate.setText(CommonUtils.getSpecifiedDayAfter(0));
    }

    public void getDate(View view)
    {
        Intent intent = new Intent(AddBookDiningActivity.this, ActivityCalendarPickerView.class);
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
                TextView diningDate = (TextView) findViewById(R.id.diningDate);
                diningDate.setText(content);
            }
        }
    }

    public void bookDiningAddSaveDefine(View view)
    {
        BookingDiningRecord info = new BookingDiningRecord();

        EditText diningRoomAdd = (EditText)findViewById(R.id.diningRoomAdd);
        if(StringUtils.isBlank(diningRoomAdd.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写包间名称", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            info.setDiningRoom(diningRoomAdd.getText().toString());
        }


        TextView diningDate = (TextView) findViewById(R.id.diningDate);
        info.setDiningDate(diningDate.getText().toString());

        info.setBookerUser(loginUserName);

        Spinner diningTypeAdd = (Spinner) findViewById(R.id.diningTypeAdd);
        info.setDiningType(diningTypeAdd.getSelectedItem().toString());

        info.setDiningTypeNumber(CommonUtils.getMenuTypeNumber(diningTypeAdd.getSelectedItem().toString()));

        EditText ContentAdd = (EditText) findViewById(R.id.ContentAdd);
        info.setContent(ContentAdd.getText().toString());



        MyTask m = new MyTask();
        m.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, info);

    }

    private class MyTask extends AsyncTask<BookingDiningRecord, Integer, String>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(BookingDiningRecord... params)
        {
            AddEditBookDiningReq req = new AddEditBookDiningReq();
            req.setOperatorId(loginUserName);
            req.setContent(params[0].getContent());
            req.setDiningType(params[0].getDiningType());
            req.setBookerUser(params[0].getBookerUser());
            req.setDiningDate(params[0].getDiningDate());
            req.setDiningTypeNumber(params[0].getDiningTypeNumber());
            req.setDiningRoom(params[0].getDiningRoom());

            String result = HttpClientClass.httpPost(req, "saveBookingDining");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            AddEditBookDiningResp info = gson.fromJson(result, AddEditBookDiningResp.class);
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

}

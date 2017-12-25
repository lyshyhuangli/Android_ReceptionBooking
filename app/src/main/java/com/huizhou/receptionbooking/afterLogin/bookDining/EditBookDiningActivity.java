package com.huizhou.receptionbooking.afterLogin.bookDining;

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
import com.huizhou.receptionbooking.afterLogin.dinningMenu.EditCookBookActivity;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.vo.BookingDiningRecord;
import com.huizhou.receptionbooking.database.vo.CookBookRecord;
import com.huizhou.receptionbooking.request.AddEditBookDiningReq;
import com.huizhou.receptionbooking.request.AddEditCookBookReq;
import com.huizhou.receptionbooking.request.GetBookDiningByIdReq;
import com.huizhou.receptionbooking.request.GetCookBookByIdReq;
import com.huizhou.receptionbooking.response.AddEditBookDiningResp;
import com.huizhou.receptionbooking.response.AddEditCookBookResp;
import com.huizhou.receptionbooking.response.GetBookDiningByIdResp;
import com.huizhou.receptionbooking.response.GetCookBookByIdResp;
import com.huizhou.receptionbooking.utils.ActivityCalendarPickerView;
import com.huizhou.receptionbooking.utils.CommonUtils;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EditBookDiningActivity extends AppCompatActivity
{
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    private MyShowTask showTask;
    private XTextView tv;
    private String role;
    private String userName;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book_dining);
        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");
        role = userSettings.getString("role", "default");

        Intent i = getIntent();
        id = i.getStringExtra("id");

        tv = (XTextView) this.findViewById(R.id.bookDiningEditBack);
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });

        spinner = (Spinner) findViewById(R.id.diningTypeEdit);

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

    public void getDateEdit(View view)
    {
        Intent intent = new Intent(EditBookDiningActivity.this, ActivityCalendarPickerView.class);
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
                TextView diningDateEdit = (TextView) findViewById(R.id.diningDateEdit);
                diningDateEdit.setText(content);
            }
        }
    }
    public void bookDiningEditSaveDefine(View view)
    {
        BookingDiningRecord info = new BookingDiningRecord();
        TextView diningDateEdit = (TextView) findViewById(R.id.diningDateEdit);
        info.setDiningDate(diningDateEdit.getText().toString());

        info.setBookerUser(userName);

        Spinner diningTypeEdit = (Spinner) findViewById(R.id.diningTypeEdit);
        info.setDiningType(diningTypeEdit.getSelectedItem().toString());

        info.setDiningTypeNumber(CommonUtils.getMenuTypeNumber(diningTypeEdit.getSelectedItem().toString()));

        EditText ContentEdit = (EditText) findViewById(R.id.ContentEdit);
        info.setContent(ContentEdit.getText().toString());

        EditText diningRoomEdit = (EditText)findViewById(R.id.diningRoomEdit);
        if(StringUtils.isBlank(diningRoomEdit.getText().toString()))
        {
            Toast tos = Toast.makeText(getApplicationContext(), "请填写包间名称", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }
        else
        {
            info.setDiningRoom(diningRoomEdit.getText().toString());
        }


        MyEditTask m = new MyEditTask();
        m.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, info);
    }

    private class MyEditTask extends AsyncTask<BookingDiningRecord, Integer, String>
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
            req.setOperatorId(userName);
            req.setContent(params[0].getContent());
            req.setDiningType(params[0].getDiningType());
            req.setDiningDate(params[0].getDiningDate());
            req.setDiningTypeNumber(params[0].getDiningTypeNumber());
            req.setDiningRoom(params[0].getDiningRoom());
            req.setId(Integer.parseInt(id));
            req.setBookerUser(params[0].getBookerUser());

            String result = HttpClientClass.httpPost(req, "updateBookingDiningById");

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

    private class MyShowTask extends AsyncTask<String, Integer, BookingDiningRecord>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected BookingDiningRecord doInBackground(String... params)
        {
            GetBookDiningByIdReq
                    req = new GetBookDiningByIdReq();
            req.setId(Integer.parseInt(id));
            req.setOperatorId(userName);
            String result = HttpClientClass.httpPost(req, "getBookingDiningById");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            GetBookDiningByIdResp info = gson.fromJson(result, GetBookDiningByIdResp.class);
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
        protected void onPostExecute(BookingDiningRecord d)
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
                TextView diningDateEdit = (TextView) findViewById(R.id.diningDateEdit);
                diningDateEdit.setText(d.getDiningDate());

                Spinner diningTypeEdit = (Spinner) findViewById(R.id.diningTypeEdit);
                SpinnerAdapter diningTypeAdapter = diningTypeEdit.getAdapter(); //得到SpinnerAdapter对象
                int k = diningTypeAdapter.getCount();
                for (int i = 0; i < k; i++)
                {
                    if (d.getDiningType() != null && d.getDiningType().equals(diningTypeAdapter.getItem(i).toString()))
                    {
                        diningTypeEdit.setSelection(i, true);// 默认选中项
                        break;
                    }
                }

                EditText ContentEdit = (EditText) findViewById(R.id.ContentEdit);
                ContentEdit.setText(d.getContent());

                EditText diningRoomEdit = (EditText) findViewById(R.id.diningRoomEdit);
                diningRoomEdit.setText(d.getDiningRoom());

                if(!"管理员".equals(role))
                {
                    Button bookDiingEditSaveDefine = (Button)findViewById(R.id.bookDiingEditSaveDefine);
                    bookDiingEditSaveDefine.setVisibility(View.GONE);
                }
            }
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

}

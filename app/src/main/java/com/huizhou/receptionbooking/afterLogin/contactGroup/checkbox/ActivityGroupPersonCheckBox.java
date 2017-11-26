package com.huizhou.receptionbooking.afterLogin.contactGroup.checkbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.tab3.ActivityPublishMeetingAdd;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.vo.GroupPersonInfoRecord;
import com.huizhou.receptionbooking.request.GetAllGroupPersonByUserPhoneReq;
import com.huizhou.receptionbooking.response.GetAllGroupPersonByUserPhoneResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroupPersonCheckBox extends AppCompatActivity
{
    private ListView mListView;
    private List<Model> models = new ArrayList<>();
    private CheckBox mMainCkb;
    private CheckboxAdapter checkboxAdapter;

    //监听来源
    public boolean mIsFromItem = false;

    private String userName;
    private XTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_person_check_box);

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");

        tv = (XTextView) this.findViewById(R.id.chooseGroupBack);
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });

        initView();
        initData();
        initViewOper();
    }

    /**
     * view初始化
     */
    private void initView()
    {
        mListView = (ListView) findViewById(R.id.list_main_checkboxGroupPerson);
        mMainCkb = (CheckBox) findViewById(R.id.ckb_groupPerson);
    }

    public void getAllGroupPerson(View view)
    {
        StringBuilder ids = new StringBuilder();
        StringBuilder name = new StringBuilder();
        for (Model m : models)
        {
            if (m.ischeck())
            {
                ids.append(m.getGroupUserId());
                name.append(m.getGetGroupUserName());
                ids.append(",");
                name.append(",");
            }
        }

        String idTemp = ids.toString();
        String nameTemp = name.toString();
        Intent intent = new Intent(ActivityGroupPersonCheckBox.this, ActivityPublishMeetingAdd.class);
        intent.putExtra("id", idTemp.substring(0, idTemp.lastIndexOf(",")));
        intent.putExtra("name", nameTemp.substring(0, nameTemp.lastIndexOf(",")));
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    /**
     * 数据加载
     */
    private void initData()
    {
        MyTask myTask = new MyTask();
        myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        // myTask.execute();
    }

    /**
     * 数据绑定
     */
    private void initViewOper()
    {


    }

    //对item导致maincheckbox改变做监听
    interface AllCheckListener
    {
        void onCheckedChanged(boolean b);
    }

    private class MyTask extends AsyncTask<String, Integer, List<GroupPersonInfoRecord>>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected List<GroupPersonInfoRecord> doInBackground(String... params)
        {
            GetAllGroupPersonByUserPhoneReq
                    req = new GetAllGroupPersonByUserPhoneReq();
            req.setOperatorId(userName);
            req.setUserPhone(userName);
            String result = HttpClientClass.httpPost(req, "getAllGroupPersonByUserPhone");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            GetAllGroupPersonByUserPhoneResp info = gson.fromJson(result, GetAllGroupPersonByUserPhoneResp.class);
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
        protected void onPostExecute(List<GroupPersonInfoRecord> result)
        {
            if (null == result)
            {
                return;
            }

            Model model;
            for (GroupPersonInfoRecord s : result)
            {
                model = new Model();
                model.setStr(s.getGroupName());
                model.setGroupUserId(s.getGroupUserId());
                model.setGetGroupUserName(s.getGroupUserName());
                model.setIscheck(false);
                models.add(model);
            }

            checkboxAdapter = new CheckboxAdapter(models, ActivityGroupPersonCheckBox.this, new ActivityGroupPersonCheckBox.AllCheckListener()
            {
                @Override
                public void onCheckedChanged(boolean b)
                {
                    //根据不同的情况对maincheckbox做处理
                    if (!b && !mMainCkb.isChecked())
                    {
                        return;
                    }
                    else if (!b && mMainCkb.isChecked())
                    {
                        mIsFromItem = true;
                        mMainCkb.setChecked(false);
                    }
                    else if (b)
                    {
                        mIsFromItem = true;
                        mMainCkb.setChecked(true);
                    }
                }
            });

            mListView.setAdapter(checkboxAdapter);
            //全选的点击监听
            mMainCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    //当监听来源为点击item改变maincbk状态时不在监听改变，防止死循环
                    if (mIsFromItem)
                    {
                        mIsFromItem = false;
                        return;
                    }

                    //改变数据
                    for (Model model : models)
                    {
                        model.setIscheck(b);
                    }
                    //刷新listview
                    checkboxAdapter.notifyDataSetChanged();
                }
            });

        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }


}

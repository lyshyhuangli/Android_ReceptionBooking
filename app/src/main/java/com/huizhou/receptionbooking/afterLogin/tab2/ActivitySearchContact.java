package com.huizhou.receptionbooking.afterLogin.tab2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityContactEdit;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.UserInfoDAO;
import com.huizhou.receptionbooking.database.dao.impl.UserInfoDAOImpl;
import com.huizhou.receptionbooking.database.vo.UerInfoRecord;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitySearchContact extends AppCompatActivity implements AbsListView.OnScrollListener
{
    private Map<Integer, String> mapNames = new HashMap<Integer, String>();
    private List<Integer> listIds = new ArrayList<Integer>(1000);
    private ListView listView;
    private int last_index;
    private int total_index;

    private boolean isLoading = false;//表示是否正处于加载状态
    private ListViewAdapterTab2 adapter;

    private int getDataNo = 1;
    private String searchParams;
    private SearchView searchContact;

    private MyTask myTask;
    private XTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contact);

        listView = (ListView) findViewById(R.id.searchDataList);
        listView.setOnScrollListener(this);

        searchContact = (SearchView) findViewById(R.id.searchContactEt);
        searchContact.setFocusable(true);
        searchContact.setFocusableInTouchMode(true);
        // 设置搜索文本监听
        searchContact.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchParams = query;
                loadFirstTime();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (StringUtils.isBlank(newText))
                {
                    searchParams = newText;
                    loadFirstTime();
                }

                return false;
            }
        });


        tv = (XTextView) this.findViewById(R.id.searchBack);
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });

        loadFirstTime();
    }

    /**
     * 第一次加载数据
     */
    private void loadFirstTime()
    {
        if (null != searchContact)
        {
            int id = searchContact.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = (TextView) searchContact.findViewById(id);
            textView.setTextSize(15);//字体、提示字体大小
        }

        listIds.clear();
        getDataNo = 1;
        myTask = new MyTask();
        myTask.execute(getDataNo);
    }

    //获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //移除搜索焦点
            searchContact.setFocusable(false);
            searchContact.setFocusableInTouchMode(false);
            searchContact.clearFocus();

            ListViewAdapterTab2.ViewHolder holder = (ListViewAdapterTab2.ViewHolder) view.getTag();
            String ids = holder.id.getText().toString();
            //跳转到详细页面
            Intent it = new Intent(ActivitySearchContact.this, ActivityContactEdit.class);
            it.putExtra("id", ids);
            startActivity(it);
        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        try
        {
            last_index = firstVisibleItem + visibleItemCount;
            total_index = totalItemCount;
        }
        catch (Exception e)
        {
            // CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), userName);
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        try
        {
            if (last_index == total_index && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE))
            {
                //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
                if (!isLoading)
                {
                    //不处于加载状态的话对其进行加载
                    isLoading = true;
                    //滑动次数加1
                    getDataNo += 1;
                    //设置刷新界面可见
                    //loadmoreView.setVisibility(View.VISIBLE);
                    //listView.addFooterView(loadmoreView, null, true);
                    onLoad();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), userName);
        }

    }

    /**
     * 刷新加载
     */
    public void onLoad()
    {
        try
        {
            if (adapter == null)
            {
                adapter = new ListViewAdapterTab2(this, mapNames, listIds);
                listView.setAdapter(adapter);
            }
            else
            {
                //获取更多数据
                mapNames.clear();
                listIds.clear();

                myTask = new MyTask();
                myTask.execute(getDataNo);

                adapter.updateView(mapNames, listIds
                );

            }

            loadComplete();//刷新结束
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), userName);
        }
    }

    /**
     * 加载完成
     */
    public void loadComplete()
    {
        try
        {
            isLoading = false;//设置正在刷新标志位false
            this.invalidateOptionsMenu();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //CommonUtils.sendLogsToServer(classInfo, "获取更多信息失败：" + e.getMessage(), userName);
        }
    }

    private class MyTask extends AsyncTask<Integer, Integer, Boolean>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Boolean doInBackground(Integer... params)
        {
            List<String> errorList = new ArrayList<>();
            List<UerInfoRecord> result = null;
            try
            {
                UserInfoDAO m = new UserInfoDAOImpl();
                result = m.getPersonByName(searchParams, errorList, params[0]);
                if (null == result)
                {
                    return false;
                }

                for (UerInfoRecord u : result)
                {
                    listIds.add(u.getId());
                    mapNames.put(u.getId(), u.getName());
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return true;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses)
        {

        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(Boolean result)
        {
            adapter = new ListViewAdapterTab2(ActivitySearchContact.this, mapNames, listIds);
            listView.setAdapter(adapter);

            //条目点击事件
            listView.setOnItemClickListener(new ActivitySearchContact.ItemClickListener());
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

    public void goBack(View view)
    {
        onBackPressed();
    }
}

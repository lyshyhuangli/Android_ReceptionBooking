package com.huizhou.receptionbooking.afterLogin.dinningMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityContactList;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.vo.CookBookRecord;
import com.huizhou.receptionbooking.request.GetAllCookBookReq;
import com.huizhou.receptionbooking.response.GetAllCookBookResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookBookListActivity extends AppCompatActivity implements AbsListView.OnScrollListener
{

    private List<Integer> listIds = new ArrayList<Integer>();
    private Map<Integer, String> cookbookDate = new HashMap<Integer, String>();
    private Map<Integer, String> menuType = new HashMap<Integer, String>();
    private Map<Integer, String> menuContent = new HashMap<Integer, String>();

    private ListView listView;
    private int last_index;
    private int total_index;

    private boolean isLoading = false;//表示是否正处于加载状态
    private CookbookAdapter adapter;

    private int getDataNo = 1;

    private MyTask myTask;

    private String userName;
    private XTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_book_list);
        listView = (ListView) this.findViewById(R.id.listviewCookbook);
        listView.setOnScrollListener(this);

        SharedPreferences userSettings = CookBookListActivity.this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");

        tv = (XTextView) this.findViewById(R.id.cookBookListBack);

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
            CookbookAdapter.ViewHolder holder = (CookbookAdapter.ViewHolder) view.getTag();
            String ids = holder.idCookbook.getText().toString();
            //跳转到详细页面
            Intent it = new Intent(CookBookListActivity.this, EditCookBookActivity.class);
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
                    onLoad();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
                adapter = new CookbookAdapter(this, cookbookDate,
                        menuType, menuContent, listIds
                );

                listView.setAdapter(adapter);
            }
            else
            {
                //获取更多数据
                cookbookDate.clear();
                menuType.clear();
                menuContent.clear();
                listIds.clear();

                myTask = new MyTask();
                myTask.execute(getDataNo);

                adapter.updateView(cookbookDate,
                        menuType, menuContent, listIds
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

    private class MyTask extends AsyncTask<Integer, Integer, List<CookBookRecord>>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected List<CookBookRecord> doInBackground(Integer... params)
        {
            GetAllCookBookReq req = new GetAllCookBookReq();
            req.setOperatorId(userName);
            req.setCount(getDataNo);

            String result = HttpClientClass.httpPost(req, "getAllCookBook");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            GetAllCookBookResp info = gson.fromJson(result, GetAllCookBookResp.class);
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
        protected void onPostExecute(List<CookBookRecord> result)
        {


            if (result == null)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }
            else
            {
                for (CookBookRecord c : result)
                {
                    listIds.add(c.getId());
                    cookbookDate.put(c.getId(), c.getPublishDate());
                    menuType.put(c.getId(), c.getMenuType());
                    menuContent.put(c.getId(), c.getMenuContent());
                }
            }

            adapter = new CookbookAdapter(CookBookListActivity.this, cookbookDate,
                    menuType, menuContent, listIds
            );

            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            //条目点击事件
            listView.setOnItemClickListener(new CookBookListActivity.ItemClickListener());
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

    @Override
    protected void onResume()
    {
        //重新刷新数据
        super.onResume();
        if (null != adapter)
        {
            cookbookDate.clear();
            menuType.clear();
            menuContent.clear();
            listIds.clear();
            getDataNo =1;
            myTask = new MyTask();
            myTask.execute(getDataNo);
        }
    }
}

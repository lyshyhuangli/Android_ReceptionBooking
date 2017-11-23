package com.huizhou.receptionbooking.afterLogin.contacts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contactGroup.ActivityGroupAdd;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.UserInfoDAO;
import com.huizhou.receptionbooking.database.dao.impl.UserInfoDAOImpl;
import com.huizhou.receptionbooking.multileveltreelist.Node;
import com.huizhou.receptionbooking.multileveltreelist.SimpleTreeRecyclerAdapter;
import com.huizhou.receptionbooking.multileveltreelist.TreeRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityCheckBoxContactList extends AppCompatActivity
{
    private TreeRecyclerAdapter mAdapter;
    private RecyclerView mTree;

    protected List<Node> mDatas = new ArrayList<Node>();

    private MyTask mTask;
    private XTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_box_contact_list);

        mTree = (RecyclerView) findViewById(R.id.recyclerview);

        tv = (XTextView) this.findViewById(R.id.chooseGroupContactBack);

        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });


        initDatas();


    }

    /**
     * 显示选中数据
     */
    public void getGroupPersionCheckBox(View v)
    {
        StringBuilder sbName = new StringBuilder();
        StringBuilder sbId = new StringBuilder();
        final List<Node> allNodes = mAdapter.getAllNodes();
        for (int i = 0; i < allNodes.size(); i++)
        {
            if (allNodes.get(i).isChecked() && allNodes.get(i).getType() ==3)
            {
                sbId.append(allNodes.get(i).getId() + ",");
                sbName.append(allNodes.get(i).getName() + ",");
            }
        }
        String strNodesName = sbName.toString();
        String strNodesId = sbId.toString();
        if (!TextUtils.isEmpty(strNodesName))
        {
            // Toast.makeText(this, strNodesName.substring(0, strNodesName.length() - 1), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivityCheckBoxContactList.this, ActivityGroupAdd.class);
            intent.putExtra("id", strNodesId.substring(0, strNodesId.length() - 1));
            intent.putExtra("name", strNodesName.substring(0, strNodesName.length() - 1));
            setResult(RESULT_OK, intent);
            onBackPressed();
        }
    }

    /**
     * 初始化数据
     */
    private void initDatas()
    {
        mTask = new MyTask();
        mTask.execute();
    }

    private class MyTask extends AsyncTask<String, Integer, List<Node>>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected List<Node> doInBackground(String... params)
        {
            List<String> errorList = new ArrayList<>();
            List<Node> result = null;
            try
            {
                UserInfoDAO m = new UserInfoDAOImpl();
                result = m.getAllContactPersionForCheckbox(errorList);
                if (null == result)
                {
                    return null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return result;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses)
        {

        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(List<Node> list)
        {
            if (null == list)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "查询通讯录信息失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }

            //初始化数据
            mDatas.addAll(list);

            //可以一直添加数据层
            try
            {
                mTree.setLayoutManager(new LinearLayoutManager(ActivityCheckBoxContactList.this));
                //第一个参数  RecyclerView
                //第二个参数  上下文
                //第三个参数  数据集
                //第四个参数  默认展开层级数 0为不展开
                //第五个参数  展开的图标
                //第六个参数  闭合的图标
                mAdapter = new SimpleTreeRecyclerAdapter(mTree, ActivityCheckBoxContactList.this,
                        mDatas, 1, R.mipmap.tree_ex, R.mipmap.tree_ec
                );

                mTree.setAdapter(mAdapter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }
}

package com.huizhou.receptionbooking.afterLogin.department;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityContactAdd;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityContactEdit;
import com.huizhou.receptionbooking.afterLogin.meetingRoom.ActivityMeetingRoomAdd;
import com.huizhou.receptionbooking.afterLogin.meetingRoom.ActivityMeetingRoomEdit;
import com.huizhou.receptionbooking.common.BaseTreeBean;
import com.huizhou.receptionbooking.common.Node;
import com.huizhou.receptionbooking.common.SimpleTreeAdapter;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.dao.DepartmentDAO;
import com.huizhou.receptionbooking.database.dao.impl.DepartmentDAOImpl;

import java.util.ArrayList;
import java.util.List;

public class ActivityDepartmentList extends AppCompatActivity
{

    private List<BaseTreeBean> mDatas = new ArrayList<BaseTreeBean>();
    private ListView mLvTree;
    private SimpleTreeAdapter mAdapter;
    private MyTask mTask;
    private XTextView tv;

    private String types;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_list);

        tv = (XTextView) this.findViewById(R.id.depatmentList);

        Intent i  = getIntent();
        if(i != null)
        {
            types  = i.getStringExtra("type");
        }

        //进入增加页面
        tv.setDrawableRightListener(new XTextView.DrawableRightListener()
        {
            @Override
            public void onDrawableRightClick(View view)
            {
                Intent intent = new Intent(ActivityDepartmentList.this, ActivityDepartmentAdd.class);
                startActivityForResult(intent, 100);
            }
        });

        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                //Toast.makeText(ActivityMeetingRoomList.this, "已收到点击指令", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        mLvTree = (ListView) findViewById(R.id.listviewDepartment);

        mTask = new MyTask();
        mTask.execute();

    }

    private class MyTask extends AsyncTask<String, Integer, List<BaseTreeBean>>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected List<BaseTreeBean> doInBackground(String... params)
        {
            List<String> errorList = new ArrayList<>();
            List<BaseTreeBean> result = null;
            try
            {
                DepartmentDAO d = new DepartmentDAOImpl();
                result = d.getAllDepartment(errorList);
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
        protected void onPostExecute(List<BaseTreeBean> list)
        {
            if (null == list)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "查询部门信息失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }

            //初始化数据
            mDatas.addAll(list);

            //可以一直添加数据层
            try
            {
                mAdapter = new SimpleTreeAdapter<BaseTreeBean>(mLvTree, ActivityDepartmentList.this, mDatas, 0);
                mAdapter.expandOrCollapse(0);

                mAdapter.setSubClickListener(new SimpleTreeAdapter.SubClickListener()
                {
                    @Override
                    public void onClick(Node node, int position)
                    {
                        Intent intent = null;
                        if(null != types)
                        {
                            if("addDepartment".equals(types))
                            {
                                intent = new Intent(ActivityDepartmentList.this, ActivityDepartmentAdd.class);

                            }
                            else  if("editDepartment".equals(types))
                            {
                                intent = new Intent(ActivityDepartmentList.this, ActivityDepartmentEdit.class);

                            }
                            else  if("addMeettingRoom".equals(types))
                            {
                                intent = new Intent(ActivityDepartmentList.this, ActivityMeetingRoomAdd.class);
                            }
                            else  if("editMeettingRoom".equals(types))
                            {
                                intent = new Intent(ActivityDepartmentList.this, ActivityMeetingRoomEdit.class);
                            }
                            else  if("addContact".equals(types))
                            {
                                intent = new Intent(ActivityDepartmentList.this, ActivityContactAdd.class);
                            }
                            else  if("editContact".equals(types))
                            {
                                intent = new Intent(ActivityDepartmentList.this, ActivityContactEdit.class);
                            }
                            else
                            {

                            }

                            intent.putExtra("parentId",node.getId());
                            intent.putExtra("name",node.getName());
                            setResult(RESULT_OK, intent);
                            onBackPressed();
                        }
                        else
                        {
                            intent = new Intent(ActivityDepartmentList.this, ActivityDepartmentEdit.class);
                            intent.putExtra("id",node.getId());
                            startActivityForResult(intent, 100);
                        }

                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            mLvTree.setAdapter(mAdapter);
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }
}

package com.huizhou.receptionbooking.afterLogin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityContactEdit;
import com.huizhou.receptionbooking.afterLogin.contactGroup.ActivityGroupList;
import com.huizhou.receptionbooking.afterLogin.tab2.ActivitySearchContact;
import com.huizhou.receptionbooking.common.BaseTreeBean;
import com.huizhou.receptionbooking.common.Node;
import com.huizhou.receptionbooking.common.SimpleTreeAdapter;
import com.huizhou.receptionbooking.database.dao.UserInfoDAO;
import com.huizhou.receptionbooking.database.dao.impl.UserInfoDAOImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有警讯列表界面
 */
public class ContacePersonFragment extends Fragment
{

    private View view;
    private TextView searchContact;
    private TextView groupTv;

    private List<BaseTreeBean> mDatas = new ArrayList<BaseTreeBean>();
    private ListView mLvTree;
    private SimpleTreeAdapter mAdapter;
    private MyTask mTask;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        view = inflater.inflate(R.layout.tab02, null);

        mLvTree = (ListView) view.findViewById(R.id.listviewContacts);
       searchContact = (TextView) view.findViewById(R.id.searchContact);
        groupTv = (TextView) view.findViewById(R.id.groupTv);

        mTask = new MyTask();
        mTask.execute();

        //引入布局
        return view;
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
                UserInfoDAO m = new UserInfoDAOImpl();
                result = m.getAllContactPersion(errorList);
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
                Toast tos = Toast.makeText(getActivity(), "查询通讯录信息失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }

            //初始化数据
            mDatas.addAll(list);

            //可以一直添加数据层
            try
            {
                mAdapter = new SimpleTreeAdapter<BaseTreeBean>(mLvTree, getActivity(), mDatas, 0);
                mAdapter.expandOrCollapse(0);

                mAdapter.setSubClickListener(new SimpleTreeAdapter.SubClickListener()
                {
                    @Override
                    public void onClick(Node node, int position)
                    {
                        Intent intent = new Intent(getActivity(), ActivityContactEdit.class);
                        intent.putExtra("id", node.getId());
                        startActivityForResult(intent, 100);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /**
         *跳转到搜索页面
         * @param view
         */
         //TextView searchContact = (TextView) getActivity().findViewById(R.id.searchContact);
        if (null != searchContact)
        {
            searchContact.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(), ActivitySearchContact.class);
                    startActivity(intent);
                }
            });
        }


        /**
         *跳转到群组
         * @param view
         */
        //TextView groupTv = (TextView) getActivity().findViewById(R.id.groupTv);
        if (null != groupTv)
        {
            groupTv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(), ActivityGroupList.class);
                    startActivity(intent);
                }
            });
        }

    }
}

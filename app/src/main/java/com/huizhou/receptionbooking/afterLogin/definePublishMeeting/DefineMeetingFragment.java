package com.huizhou.receptionbooking.afterLogin.definePublishMeeting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contactGroup.checkbox.Model;
import com.huizhou.receptionbooking.database.dao.UserInfoDAO;
import com.huizhou.receptionbooking.database.dao.impl.UserInfoDAOImpl;
import com.huizhou.receptionbooking.database.vo.GroupPersonInfoRecord;
import com.huizhou.receptionbooking.multileveltreelist.Node;
import com.huizhou.receptionbooking.multileveltreelist.SimpleTreeRecyclerAdapter;
import com.huizhou.receptionbooking.request.GetAllGroupPersonByUserPhoneReq;
import com.huizhou.receptionbooking.response.GetAllGroupPersonByUserPhoneResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/12.
 */

public class DefineMeetingFragment extends Fragment
{
    private String type;
    private String userName;
    private View view;

    private MyContactTask mTask;
    public SimpleTreeRecyclerAdapter mAdapter;

    private RecyclerView mTree;
    protected List<Node> mDatas = new ArrayList<Node>();

    private ListView mListView;
    private List<Model> models = new ArrayList<>();
    private CheckBox mMainCkb;
    private CheckboxAdapterForDefineMeeting checkboxAdapter;

    //监听来源
    public boolean mIsFromItem = false;


    public static DefineMeetingFragment newInstance(String str)
    {
        Bundle bundle = new Bundle();
        bundle.putString("type", str);
        DefineMeetingFragment fragment = new DefineMeetingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");

        type = getArguments().getString("type");
        if ("联系人".equals(type))
        {
            view = inflater.inflate(R.layout.activity_check_box_contact_list, container, false);
            Button bt = (Button) view.findViewById(R.id.getContact_bt);
            bt.setText("发布会议");
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_chooseContact);
            ll.setVisibility(View.GONE);

            mTree = (RecyclerView) view.findViewById(R.id.recyclerview);

            initDatas();
        }
        else
        {
            view = inflater.inflate(R.layout.activity_group_person_check_box, container, false);
            mListView = (ListView) view.findViewById(R.id.list_main_checkboxGroupPerson);
            mMainCkb = (CheckBox) view.findViewById(R.id.ckb_groupPerson);
            Button bt = (Button) view.findViewById(R.id.groupPerson_bt);
            bt.setText("发布会议");

            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_group);
            ll.setVisibility(View.GONE);

            initData();
            initViewOper();
        }

        return view;
    }

    /**
     * 初始化数据
     */
    private void initDatas()
    {
        mTask = new MyContactTask();
        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class MyContactTask extends AsyncTask<String, Integer, List<Node>>
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
                mTree.setLayoutManager(new LinearLayoutManager(getContext()));
                //第一个参数  RecyclerView
                //第二个参数  上下文
                //第三个参数  数据集
                //第四个参数  默认展开层级数 0为不展开
                //第五个参数  展开的图标
                //第六个参数  闭合的图标
                mAdapter = new SimpleTreeRecyclerAdapter(mTree, getActivity(),
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


    /**
     * 数据加载
     */
    private void initData()
    {
        MyGroupTask myTask = new MyGroupTask();
        myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    private class MyGroupTask extends AsyncTask<String, Integer, List<GroupPersonInfoRecord>>
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

            checkboxAdapter = new CheckboxAdapterForDefineMeeting(models, getContext(), new DefineMeetingFragment.AllCheckListener()
            {
                @Override
                public void onCheckedChanged(boolean b)
                {
                    //根据不同的情况对maincheckbox做处理
                    if (!b && !mMainCkb.isChecked())
                    {

                        SharedPreferences modelInfo = getActivity().getSharedPreferences("modelInfo", 0);
                        SharedPreferences.Editor editor = modelInfo.edit();
                        Gson gson = new Gson();
                        DefineMeetingModel define = new DefineMeetingModel();
                        define.setModels(models);
                        String jsonString = gson.toJson(define);
                        editor.putString("defineMeetingModelInfo", jsonString);
                        editor.commit();

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

                    SharedPreferences modelInfo = getActivity().getSharedPreferences("modelInfo", 0);
                    SharedPreferences.Editor editor = modelInfo.edit();
                    Gson gson = new Gson();
                    DefineMeetingModel define = new DefineMeetingModel();
                    define.setModels(models);
                    String jsonString = gson.toJson(define);
                    editor.putString("defineMeetingModelInfo", jsonString);
                    editor.commit();

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

                    SharedPreferences modelInfo = getActivity().getSharedPreferences("modelInfo", 0);
                    SharedPreferences.Editor editor = modelInfo.edit();
                    Gson gson = new Gson();
                    DefineMeetingModel define = new DefineMeetingModel();
                    define.setModels(models);
                    String jsonString = gson.toJson(define);
                    editor.putString("defineMeetingModelInfo", jsonString);
                    editor.commit();

                }
            });

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
         * 跳转会议发布页面
         *
         * @param view
         */
        Button getContact_bt = (Button) getActivity().findViewById(R.id.getContact_bt);
        if(null !=getContact_bt)
        {
            getContact_bt.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    StringBuilder sbName = new StringBuilder();
                    StringBuilder sbId = new StringBuilder();
                    List<Node> allNodes = mAdapter.getAllNodes();
                    for (int i = 0; i < allNodes.size(); i++)
                    {
                        if (allNodes.get(i).isChecked() && allNodes.get(i).getType() == 3)
                        {
                            sbId.append(allNodes.get(i).getId() + ",");
                            sbName.append(allNodes.get(i).getName() + ",");
                        }
                    }

                    String strNodesName = sbName.toString();
                    String strNodesId = sbId.toString();

                    if(StringUtils.isBlank(strNodesId))
                    {
                        Toast tos = Toast.makeText(getActivity(), "请选择与会人", Toast.LENGTH_LONG);
                        tos.setGravity(Gravity.CENTER, 0, 0);
                        tos.show();
                        return;
                    }

                    if (!TextUtils.isEmpty(strNodesName))
                    {
                        Intent intent = new Intent(getActivity(), DefineMeetingViewActivity.class);
                        intent.putExtra("id", strNodesId.substring(0, strNodesId.length() - 1));
                        intent.putExtra("name", strNodesName.substring(0, strNodesName.length() - 1));
                        startActivity(intent);
                    }
                }
            });
        }

        Button groupPerson_bt = (Button) getActivity().findViewById(R.id.groupPerson_bt);
        if (null != groupPerson_bt)
        {
            groupPerson_bt.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SharedPreferences modelInfo = getActivity().getSharedPreferences("modelInfo", 0);
                    String defineMeetingModelInfo = modelInfo.getString("defineMeetingModelInfo", "default");
                    Gson gson = new Gson();
                    DefineMeetingModel models = gson.fromJson(defineMeetingModelInfo,  DefineMeetingModel.class);

                    StringBuilder ids = new StringBuilder();
                    StringBuilder name = new StringBuilder();
                    Map<String,String> tempMap = new HashMap<String,String>();

                    for (Model m : models.getModels())
                    {
                        if (m.ischeck())
                        {
                            String[] idss = m.getGroupUserId().split(",");
                            String[] names = m.getGetGroupUserName().split(",");
                            for(int i =0;i<idss.length;i++)
                            {
                                tempMap.put(idss[i],names[i]);
                            }
                        }
                    }

                    if(tempMap.isEmpty())
                    {
                        Toast tos = Toast.makeText(getActivity(), "请选择与会人", Toast.LENGTH_LONG);
                        tos.setGravity(Gravity.CENTER, 0, 0);
                        tos.show();
                        return;
                    }

                    for(Iterator<String> ite = tempMap.keySet().iterator();ite.hasNext();)
                    {
                        String i = ite.next();
                        ids.append(i);
                        name.append(tempMap.get(i));
                        ids.append(",");
                        name.append(",");
                    }

                    String idTemp = ids.toString();
                    String nameTemp = name.toString();
                    Intent intent = new Intent(getActivity(), DefineMeetingViewActivity.class);
                    intent.putExtra("id", idTemp.substring(0, idTemp.lastIndexOf(",")));
                    intent.putExtra("name", nameTemp.substring(0, nameTemp.lastIndexOf(",")));
                    startActivity(intent);
                }
            });
        }
    }
}

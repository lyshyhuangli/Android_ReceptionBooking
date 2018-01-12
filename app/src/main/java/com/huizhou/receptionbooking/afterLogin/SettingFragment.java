package com.huizhou.receptionbooking.afterLogin;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.bookDining.AddBookDiningActivity;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityContactList;
import com.huizhou.receptionbooking.afterLogin.department.ActivityDepartmentList;
import com.huizhou.receptionbooking.afterLogin.dinningMenu.AddCookBookActivity;
import com.huizhou.receptionbooking.afterLogin.meetingRoom.ActivityMeetingRoomList;
import com.huizhou.receptionbooking.afterLogin.tab4.ActivityModifyPwd;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.request.GetMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.request.SaveMeetingConfirmReq;
import com.huizhou.receptionbooking.request.UpdateMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.response.GetMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.response.SaveMeetingConfirmResp;
import com.huizhou.receptionbooking.response.UpdateMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.apache.commons.lang3.StringUtils;

/**
 * 设置主页
 */
public class SettingFragment extends Fragment
{

    private ReceiveBroadCast receiveBroadCast;
    private String userName;
    private String loginShowName;
    private String role;
    private XTextView tv;

    class ReceiveBroadCast extends BroadcastReceiver
    {
        //接受二维码扫描后的会议Id
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String id = intent.getExtras().getString("result");
            //System.out.println("0000000000=" + id);

            MySignMeetingTask myTask = new MySignMeetingTask();
            myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,id);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {

        /** 注册广播 */
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.gasFragment");    //只有持有相同的action的接受者才能接收此广播
        activity.registerReceiver(receiveBroadCast, filter);
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(R.layout.tab04, container, false);

        SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
        userName= userSettings.getString("loginUserName", "default");
        loginShowName = userSettings.getString("loginShowName", "default");
        role = userSettings.getString("role", "default");

        tv = (XTextView) view.findViewById(R.id.topCommon);
        tv.setText("设置");
        //弹出menu菜单
        tv.setDrawableRightListener(new XTextView.DrawableRightListener()
        {
            @Override
            public void onDrawableRightClick(View view)
            {
                Intent intent = new Intent(getActivity(), CommonMenuActitity.class);
                startActivityForResult(intent, 100);
            }
        });


        Button button = (Button) view.findViewById(R.id.nameShow);
        button.setText(loginShowName);

        if(!"管理员".equals(role))
        {
            LinearLayout meetingRoomManager = (LinearLayout)view.findViewById(R.id.meetingRoomManagerLl);
            meetingRoomManager.setVisibility(View.GONE);

            LinearLayout addressBookManager = (LinearLayout)view.findViewById(R.id.addressBookManagerLl);
            addressBookManager.setVisibility(View.GONE);

            LinearLayout departmentManager = (LinearLayout)view.findViewById(R.id.departmentManagerLl);
            departmentManager.setVisibility(View.GONE);

            LinearLayout diningManager = (LinearLayout)view.findViewById(R.id.diningManagerLl);
            diningManager.setVisibility(View.GONE);

            LinearLayout diningRoomManager = (LinearLayout)view.findViewById(R.id.diningRoomManagerLl);
            diningRoomManager.setVisibility(View.GONE);
        }

        //引入布局
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /**
         * 跳转到修改密码
         */
        Button modifyPwd = (Button) getActivity().findViewById(R.id.modifyPwd);
        modifyPwd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityModifyPwd.class);
                startActivity(intent);
            }
        });


        /**
         * 跳转登录界面
         *
         * @param view
         */
        Button buttonLogout = (Button) getActivity().findViewById(R.id.logout);
        buttonLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //清楚缓存
                SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
                SharedPreferences.Editor editor = userSettings.edit();
                editor.clear();
                editor.commit();

                //清楚缓存
                SharedPreferences password = getActivity().getSharedPreferences("password", 0);
                SharedPreferences.Editor editorPassword = password.edit();
                editorPassword.clear();
                editorPassword.commit();

                getActivity().unregisterReceiver(receiveBroadCast);

                //Intent intent = new Intent(getActivity(), LoginActivity.class);
               // startActivity(intent);
                //getActivity().finish();
                onDestroy();
                //参数用作状态码；根据惯例，非 0 的状态码表示异常终止。
                System.exit(0);
            }
        });

        /**
         *  跳转到会议室管理界面
         */
        Button meetingRoomManagerBt = (Button) getActivity().findViewById(R.id.meetingRoomManager);
        meetingRoomManagerBt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityMeetingRoomList.class);
                intent.putExtra("flag","setting");
                startActivityForResult(intent, 100);
            }
        });

        /**
         *  跳转到部门管理界面
         */
        Button departmentManagerBt = (Button) getActivity().findViewById(R.id.departmentManager);
        departmentManagerBt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityDepartmentList.class);
                startActivityForResult(intent, 100);
            }
        });

        /**
         *  跳转到部门管理界面
         */
        Button addressBookManager = (Button) getActivity().findViewById(R.id.addressBookManager);
        addressBookManager.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityContactList.class);
                startActivityForResult(intent, 100);
            }
        });


        /**
         *  跳转到菜谱管理
         */
        Button diningManager = (Button) getActivity().findViewById(R.id.diningManager);
        diningManager.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), AddCookBookActivity.class);
                startActivityForResult(intent, 100);
            }
        });


        /**
         *  跳转到包间预定管理
         */
        Button diningRoomManager = (Button) getActivity().findViewById(R.id.diningRoomManager);
        diningRoomManager.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), AddBookDiningActivity.class);
                startActivityForResult(intent, 100);
            }
        });



        /**
         *  扫一扫
         */
        Button startScan = (Button) getActivity().findViewById(R.id.startScan);
        startScan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), CaptureActivity.class);
                i.putExtra("type","f");
                startActivityForResult(i, 0);
            }
        });
    }

    /**
     * 扫一扫签到会议
     */
    public class MySignMeetingTask extends AsyncTask<String, Integer, String>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params)
        {
            String id = params[0];
            GetMeetingConfirmByMeetingIdAndPhoneReq
                    req = new GetMeetingConfirmByMeetingIdAndPhoneReq();
            req.setOperatorId(userName);
            req.setMeetingId(Integer.parseInt(id));
            req.setPhone(userName);
            String result = HttpClientClass.httpPost(req, "getMeetingConfirmByMeetingIdAndPhone");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            GetMeetingConfirmByMeetingIdAndPhoneResp info = gson.fromJson(result, GetMeetingConfirmByMeetingIdAndPhoneResp.class);
            if (null != info)
            {
                if (0 == info.getResultCode())
                {
                    if (null != info.getInfo())
                    {
                        //表示之前保存一条参加会议的信息
                        //为空，表示新插入记录
                        UpdateMeetingConfirmByMeetingIdAndPhoneReq
                                req2 = new UpdateMeetingConfirmByMeetingIdAndPhoneReq();
                        req2.setOperatorId(userName);
                        req2.setMeetingId(Integer.parseInt(id));
                        req2.setPhone(userName);
                        req2.setAttendType(1);
                        req2.setIsSign(1);

                        String result2 = HttpClientClass.httpPost(req2, "updateMeetingConfirmByMeetingIdAndPhone");

                        if (StringUtils.isBlank(result2))
                        {
                            return null;
                        }

                        Gson gson2 = new Gson();
                        UpdateMeetingConfirmByMeetingIdAndPhoneResp info2 = gson2.fromJson(
                                result2,
                                UpdateMeetingConfirmByMeetingIdAndPhoneResp.class
                        );
                        if (null != info2)
                        {
                            if (0 == info2.getResultCode())
                            {
                                return "OK";
                            }
                        }
                    }
                    else
                    {
                        //为空，表示新插入记录
                        SaveMeetingConfirmReq
                                req2 = new SaveMeetingConfirmReq();
                        req2.setOperatorId(userName);
                        req2.setMeetingId(Integer.parseInt(id));
                        req2.setPhone(userName);
                        req2.setUserName(loginShowName);
                        req2.setAttendType(1);
                        req2.setIsSign(1);
                        String result2 = HttpClientClass.httpPost(req2, "saveMeetingConfirm");

                        if (StringUtils.isBlank(result2))
                        {
                            return null;
                        }

                        Gson gson2 = new Gson();
                        SaveMeetingConfirmResp info2 = gson2.fromJson(result2, SaveMeetingConfirmResp.class);
                        if (null != info2)
                        {
                            if (0 == info2.getResultCode())
                            {
                                return "OK";
                            }
                        }
                    }
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
                Toast tos = Toast.makeText(getActivity(), "保存数据失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }
            else
            {
                Toast tos = Toast.makeText(getActivity(), "签到成功!", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }

        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }


}



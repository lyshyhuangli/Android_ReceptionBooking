package com.huizhou.receptionbooking.afterLogin;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.huizhou.receptionbooking.LoginActivity;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityContactList;
import com.huizhou.receptionbooking.afterLogin.department.ActivityDepartmentList;
import com.huizhou.receptionbooking.afterLogin.meetingRoom.ActivityMeetingRoomList;

/**
 * 设置主页
 */
public class SettingFragment extends Fragment
{

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        SharedPreferences userSettings = getActivity().getSharedPreferences("userInfo", 0);
        String name = userSettings.getString("loginUserName", "default");

        View view = inflater.inflate(R.layout.tab04, container, false);
        Button button = (Button) view.findViewById(R.id.nameShow);
        button.setText(name.toLowerCase());
        //引入布局
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

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

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
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
                startActivityForResult(intent,100);
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
                startActivityForResult(intent,100);
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
                startActivityForResult(intent,100);
            }
        });
    }

}



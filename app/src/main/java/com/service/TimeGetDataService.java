package com.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.AfterLogin;
import com.huizhou.receptionbooking.afterLogin.tab1.UnreadMeetingInfoRecord;
import com.huizhou.receptionbooking.afterLogin.tab1.UnreadMeetingMoreRecord;
import com.huizhou.receptionbooking.database.vo.MyMeetingInfoRecord;
import com.huizhou.receptionbooking.request.GetMyBeingMeetingReq;
import com.huizhou.receptionbooking.response.GetMyBeingMeetingResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2018/1/7.
 */

public class TimeGetDataService extends Service
{
    private String userName;

    @Override
    public void onCreate()
    {
        //android.os.Debug.waitForDebugger();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        SharedPreferences userSettings = getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");

        //获取最新会议信息
        MyNewMeetingThread myNewMeetingThread = new MyNewMeetingThread();
        Thread t1 = new Thread(myNewMeetingThread);
        t1.start();

        UnreadMoreWakeupThread unreadMoreWakeupThread = new UnreadMoreWakeupThread();
        Thread t2 = new Thread(unreadMoreWakeupThread);
        t2.start();

        MeetingBeginWakeupThread meetingBeginWakeupThread = new MeetingBeginWakeupThread();
        Thread t3 = new Thread(meetingBeginWakeupThread);
        t3.start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Intent localIntent = new Intent();
        localIntent.setClass(this, TimeGetDataService.class);  //销毁时重新启动Service
        this.startService(localIntent);
    }


    /**
     * 获取最新会议信息线程
     */
    class MyNewMeetingThread extends Thread
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    Thread.sleep(2000);
                    //待开会议信息
                    getNewMeetingData();
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 还有未读会议定时提醒
     */
    class UnreadMoreWakeupThread extends Thread
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    //20分钟=1200000
                    Thread.sleep(1200000);
                    sendNoteficeForMoreUnread();
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 会前会议提醒
     */
    class MeetingBeginWakeupThread extends Thread
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    //要比MyNewMeetingThread延迟一点，要不然获取不到缓存信息
                    //20分钟=1200000
                    Thread.sleep(1200000);
                    MeetingBeginWakeup();
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void MeetingBeginWakeup()
    {
        UnreadMeetingInfoRecord unreadMeetingInfoRecord = null;
        SharedPreferences unreadingMeeting = getSharedPreferences("unreadingMeeting" + userName, Context.MODE_MULTI_PROCESS);
        if (unreadingMeeting != null)
        {
            Gson gson = new Gson();
            String ids = unreadingMeeting.getString("ids", "default");


            if (StringUtils.isNotBlank(ids) && !"default".equals(ids))
            {
                unreadMeetingInfoRecord = gson.fromJson(ids, UnreadMeetingInfoRecord.class);
                Map<Integer, UnreadMeetingMoreRecord> map = unreadMeetingInfoRecord.getUnreadMeetingIds();

                long currentTime = System.currentTimeMillis();

                for (Map.Entry<Integer, UnreadMeetingMoreRecord> entry : map.entrySet())
                {
                    Integer key = entry.getKey();
                    UnreadMeetingMoreRecord value = entry.getValue();

                    String meetingDate = value.getMeetingDate();
                    String startTime = value.getStartTime();

                    String dateTime = meetingDate + " " + startTime + ":00 000";
                    Calendar calendar = Calendar.getInstance();

                    try
                    {
                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").parse(dateTime));
                    }
                    catch (Exception e)
                    {

                    }

                    //20分钟=1200000
                    long meetingMillis = calendar.getTimeInMillis();

                    if (currentTime > meetingMillis)
                    {
                        //表示会议已经开完，已经开完的会议
                        continue;
                    }

                    //提前40分钟提醒=2400000
                    if (currentTime + 2400000 > meetingMillis)
                    {
                        //启动通知服务
                        sendNotification(String.valueOf(key), "开会提醒", "您即将有会参加!", 3);
                    }
                }
            }
        }
    }

    private void sendNoteficeForMoreUnread()
    {
        UnreadMeetingInfoRecord unreadMeetingInfoRecord = null;
        SharedPreferences unreadingMeeting = getSharedPreferences("unreadingMeeting" + userName, Context.MODE_MULTI_PROCESS);
        if (unreadingMeeting != null)
        {
            Gson gson = new Gson();
            String ids = unreadingMeeting.getString("ids", "default");
            if (StringUtils.isNotBlank(ids) && !"default".equals(ids))
            {
                unreadMeetingInfoRecord = gson.fromJson(ids, UnreadMeetingInfoRecord.class);
                Map<Integer, UnreadMeetingMoreRecord> map = unreadMeetingInfoRecord.getUnreadMeetingIds();

                for (Map.Entry<Integer, UnreadMeetingMoreRecord> entry : map.entrySet())
                {
                    Integer key = entry.getKey();
                    UnreadMeetingMoreRecord value = entry.getValue();
                    if (!value.isRead())
                    {
                        //启动通知服务
                        sendNotification(String.valueOf(key), "未读会议提醒", "您还有未读会议!", 2);
                        break;
                    }
                }

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                String dateTime = df.format(new Date());// new Date()为获取当前系统时间

                //清除过期的会议信息，清除缓存
                Iterator<Map.Entry<Integer, UnreadMeetingMoreRecord>> entries = map.entrySet().iterator();
                while (entries.hasNext())
                {
                    Map.Entry<Integer, UnreadMeetingMoreRecord> entry = entries.next();
                    Integer key = entry.getKey();
                    UnreadMeetingMoreRecord value = entry.getValue();
                    if (dateTime.compareTo(value.getMeetingDate()) > 0)
                    {
                        entries.remove();
                    }
                }

                String jsonString = gson.toJson(unreadMeetingInfoRecord);
                SharedPreferences.Editor editor = unreadingMeeting.edit();
                editor.putString("ids", jsonString);
                editor.commit();
            }
        }
    }

    private void getNewMeetingData()
    {
        SharedPreferences userSettings = getSharedPreferences("userInfo", 0);
        String userName = userSettings.getString("loginUserName", "default");

        GetMyBeingMeetingReq
                req = new GetMyBeingMeetingReq();
        req.setPhone(userName);
        req.setOperatorId(userName + "_TimeGetDataService");

        try
        {
            String result = HttpClientClass.httpPost(req, "getMyBingMeeting");

            if (StringUtils.isBlank(result))
            {
                return;
            }

            Gson gson = new Gson();
            GetMyBeingMeetingResp info = gson.fromJson(result, GetMyBeingMeetingResp.class);
            if (null != info)
            {
                if (0 == info.getResultCode())
                {
                    UnreadMeetingInfoRecord unreadMeetingInfoRecord = null;
                    SharedPreferences unreadingMeeting = getSharedPreferences("unreadingMeeting" + userName, Context.MODE_MULTI_PROCESS);
                    if (unreadingMeeting != null)
                    {
                        String ids = unreadingMeeting.getString("ids", "default");
                        if (StringUtils.isNotBlank(ids) && !"default".equals(ids))
                        {
                            unreadMeetingInfoRecord = gson.fromJson(ids, UnreadMeetingInfoRecord.class);
                        }
                    }

                    Map<Integer, UnreadMeetingMoreRecord> mapOld = null;
                    if (unreadMeetingInfoRecord != null)
                    {
                        mapOld = unreadMeetingInfoRecord.getUnreadMeetingIds();
                    }

                    SharedPreferences.Editor editor = unreadingMeeting.edit();
                    Map<Integer, UnreadMeetingMoreRecord> mapNew = new HashMap<>();

                    List<MyMeetingInfoRecord> myBeingMeetingInfo = info.getMyBeingMeetingInfo();
                    for (MyMeetingInfoRecord m : myBeingMeetingInfo)
                    {
                        if (mapOld != null)
                        {
                            if (!mapOld.containsKey(m.getId()))
                            {
                                UnreadMeetingMoreRecord u = new UnreadMeetingMoreRecord();
                                u.setCreateTime(m.getCreateTime());
                                u.setMeetingDate(m.getMeetingDate());
                                u.setRead(false);
                                u.setStartTime(m.getStartTime());

                                //如果不在列表里面，表示是新的会议,需要发出通知
                                mapOld.put(m.getId(), u);

                                //启动通知服务
                                sendNotification(String.valueOf(m.getId()), "新会议提醒", "您有新的会议信息!", 1);
                            }
                            else
                            {
                                //更新缓存中会议时间和日期
                                UnreadMeetingMoreRecord old = mapOld.get(m.getId());
                                old.setStartTime(m.getStartTime());
                                old.setMeetingDate(m.getMeetingDate());
                            }
                        }
                        else
                        {
                            UnreadMeetingMoreRecord u = new UnreadMeetingMoreRecord();
                            u.setCreateTime(m.getCreateTime());
                            u.setMeetingDate(m.getMeetingDate());
                            u.setRead(false);
                            u.setStartTime(m.getStartTime());

                            //表示缓存中没有未读会议数据，第一次缓存,也需要发出通知
                            mapNew.put(m.getId(), u);

                            //启动通知服务
                            sendNotification(String.valueOf(m.getId()), "新会议提醒", "您有新的会议信息!", 1);
                        }
                    }

                    if (mapOld == null)
                    {
                        //如果是第一次缓存
                        UnreadMeetingInfoRecord unread = new UnreadMeetingInfoRecord();
                        unread.setUnreadMeetingIds(mapNew);
                        String jsonString = gson.toJson(unread);
                        editor.putString("ids", jsonString);
                    }
                    else
                    {
                        //新增加的会议信息
                        String jsonString = gson.toJson(unreadMeetingInfoRecord);
                        editor.putString("ids", jsonString);
                    }

                    editor.commit();

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 发送最简单的通知,该通知的ID = 1
     */
    public void sendNotification(String noteficaId, String title, String content, int notifyId)
    {
        //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                //设置小图标
                .setSmallIcon(R.mipmap.notification)
                //设置通知标题
                .setContentTitle(title)
                //设置通知内容
                .setContentText(content);


        //定义一个PendingIntent，当用户点击通知时，跳转到某个Activity(也可以发送广播等)
        Intent intent = new Intent(getApplicationContext(), AfterLogin.class);
        intent.putExtra("notificationId", Integer.parseInt(noteficaId));
        int requestCode = new Random().nextInt();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//        {
//            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
//            // 关联PendingIntent
//            builder.setFullScreenIntent(pendingIntent, true);// 横幅
//        }

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR; // 点击清除按钮时就会清除消息通知,但是点击通知栏的通知时不会消失
        //notification.flags = Notification.FLAG_ONGOING_EVENT; // 点击清除按钮不会清除消息通知,可以用来表示在正在运行
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失
        //notification.flags |= Notification.FLAG_INSISTENT; // 一直进行，比如音乐一直播放，知道用户响应
        notification.defaults = Notification.DEFAULT_SOUND; // 调用系统自带声音
        notification.defaults = Notification.DEFAULT_VIBRATE;// 设置默认震动
        notification.defaults = Notification.DEFAULT_ALL; // 设置铃声震动

        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(notifyId, notification);

        // 多条通知
        // notifyManager.notify(Integer.parseInt(noteficaId), notification);
        //startForeground(1, notification);

    }

}

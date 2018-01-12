package com.huizhou.receptionbooking.afterLogin.tab1;

/**
 * Created by Administrator on 2018/1/7.
 */

public class UnreadMeetingMoreRecord
{
    private String createTime;
    private String meetingDate;
    private boolean isRead;
    private String startTime;
    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getMeetingDate()
    {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate)
    {
        this.meetingDate = meetingDate;
    }

    public boolean isRead()
    {
        return isRead;
    }

    public void setRead(boolean read)
    {
        isRead = read;
    }
}

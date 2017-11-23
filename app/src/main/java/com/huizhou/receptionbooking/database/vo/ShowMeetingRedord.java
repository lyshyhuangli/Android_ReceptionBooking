package com.huizhou.receptionbooking.database.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ShowMeetingRedord
{
    private int id;

    private String bookUser;

    private String name;

    private String startTime;

    private String endTime;

    private String amOrPm;

    private String threaf;

    private int mid;

    private String meetingDate;

    public String getAmOrPm()
    {
        return amOrPm;
    }

    public void setAmOrPm(String amOrPm)
    {
        this.amOrPm = amOrPm;
    }

    public String getMeetingDate()
    {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate)
    {
        this.meetingDate = meetingDate;
    }

    public int getMid()
    {
        return mid;
    }

    public void setMid(int mid)
    {
        this.mid = mid;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getBookUser()
    {
        return bookUser;
    }

    public void setBookUser(String bookUser)
    {
        this.bookUser = bookUser;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getThreaf()
    {
        return threaf;
    }

    public void setThreaf(String threaf)
    {
        this.threaf = threaf;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }
}

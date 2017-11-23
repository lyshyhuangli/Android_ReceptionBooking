package com.huizhou.receptionbooking.database.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class EveryMeeting
{
    private String bookUser;

    private String startTime;

    private String endTime;

    private String amOrPm;

    private String threaf;

    private int mid;

    public String getBookUser()
    {
        return bookUser;
    }

    public void setBookUser(String bookUser)
    {
        this.bookUser = bookUser;
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

    public String getAmOrPm()
    {
        return amOrPm;
    }

    public void setAmOrPm(String amOrPm)
    {
        this.amOrPm = amOrPm;
    }

    public String getThreaf()
    {
        return threaf;
    }

    public void setThreaf(String threaf)
    {
        this.threaf = threaf;
    }

    public int getMid()
    {
        return mid;
    }

    public void setMid(int mid)
    {
        this.mid = mid;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }
}

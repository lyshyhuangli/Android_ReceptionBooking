package com.huizhou.receptionbooking.database.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.LinkedList;
import java.util.List;

public class BookingMeetingRecord
{
    private int id;

    private String name;

    private String meetingDate;

    List<EveryMeeting> meetingList = new LinkedList<EveryMeeting>();

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMeetingDate()
    {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate)
    {
        this.meetingDate = meetingDate;
    }

    public List<EveryMeeting> getMeetingList()
    {
        return meetingList;
    }

    public void setMeetingList(List<EveryMeeting> meetingList)
    {
        this.meetingList = meetingList;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }
}

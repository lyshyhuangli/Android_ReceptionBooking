package com.huizhou.receptionbooking.database.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class MyMeetingInfoRecord
{
    private int id;
    private String bookUser;
    private String threaf;
    private String meetingDate;
    private int meetingRoom;
    private String startTime;
    private String endTime;
    private String person;
    private String name;
    private int parentId;
    private String department;
    private String createTime;

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
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

    public String getThreaf()
    {
        return threaf;
    }

    public void setThreaf(String threaf)
    {
        this.threaf = threaf;
    }

    public String getMeetingDate()
    {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate)
    {
        this.meetingDate = meetingDate;
    }

    public int getMeetingRoom()
    {
        return meetingRoom;
    }

    public void setMeetingRoom(int meetingRoom)
    {
        this.meetingRoom = meetingRoom;
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

    public String getPerson()
    {
        return person;
    }

    public void setPerson(String person)
    {
        this.person = person;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getParentId()
    {
        return parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }
}

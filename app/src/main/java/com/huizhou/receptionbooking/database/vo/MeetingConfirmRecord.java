package com.huizhou.receptionbooking.database.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class MeetingConfirmRecord
{
    private int id;
    private int meetingId;
    private String phone;
    private String userName;
    private int attendType;
    private int isSign;
    private String reason;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(int meetingId)
    {
        this.meetingId = meetingId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public int getAttendType()
    {
        return attendType;
    }

    public void setAttendType(int attendType)
    {
        this.attendType = attendType;
    }

    public int getIsSign()
    {
        return isSign;
    }

    public void setIsSign(int isSign)
    {
        this.isSign = isSign;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }

}

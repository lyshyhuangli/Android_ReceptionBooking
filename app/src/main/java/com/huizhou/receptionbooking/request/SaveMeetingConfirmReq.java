package com.huizhou.receptionbooking.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SaveMeetingConfirmReq extends CommonRequest
{
    private int meetingId;
    private String phone;
    private String userName;
    private int attendType;
    private String reason;
    private int isSign;

    public int getIsSign()
    {
        return isSign;
    }

    public void setIsSign(int isSign)
    {
        this.isSign = isSign;
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

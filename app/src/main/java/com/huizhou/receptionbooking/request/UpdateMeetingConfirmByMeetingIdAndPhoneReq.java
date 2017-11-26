package com.huizhou.receptionbooking.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class UpdateMeetingConfirmByMeetingIdAndPhoneReq extends CommonRequest
{
    int attendType;
    String reason;
    int isSign;
    int meetingId;
    String phone;


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

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }

}

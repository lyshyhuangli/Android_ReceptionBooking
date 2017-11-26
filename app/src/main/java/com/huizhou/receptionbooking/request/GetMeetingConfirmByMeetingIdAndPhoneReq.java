package com.huizhou.receptionbooking.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class GetMeetingConfirmByMeetingIdAndPhoneReq extends CommonRequest
{
    int meetingId;
    String phone;

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

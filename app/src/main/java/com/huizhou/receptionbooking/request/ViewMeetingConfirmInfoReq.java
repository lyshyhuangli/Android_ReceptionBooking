package com.huizhou.receptionbooking.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ViewMeetingConfirmInfoReq extends CommonRequest
{

    private int meetingId;

    public int getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(int meetingId)
    {
        this.meetingId = meetingId;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }

}

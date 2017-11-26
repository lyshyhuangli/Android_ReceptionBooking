package com.huizhou.receptionbooking.response;

import com.huizhou.receptionbooking.database.vo.MeetingConfirmRecord;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class GetMeetingConfirmByMeetingIdAndPhoneResp extends CommonResult
{

    MeetingConfirmRecord info = new MeetingConfirmRecord();

    public MeetingConfirmRecord getInfo()
    {
        return info;
    }

    public void setInfo(MeetingConfirmRecord info)
    {
        this.info = info;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }

}

package com.huizhou.receptionbooking.response;

import com.huizhou.receptionbooking.database.vo.MyMeetingInfoRecord;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.LinkedList;
import java.util.List;

public class GetMyBeingMeetingResp extends CommonResult
{

    private List<MyMeetingInfoRecord> myBeingMeetingInfo = new LinkedList<MyMeetingInfoRecord>();

    public List<MyMeetingInfoRecord> getMyBeingMeetingInfo()
    {
        return myBeingMeetingInfo;
    }

    public void setMyBeingMeetingInfo(List<MyMeetingInfoRecord> myBeingMeetingInfo)
    {
        this.myBeingMeetingInfo = myBeingMeetingInfo;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }

}

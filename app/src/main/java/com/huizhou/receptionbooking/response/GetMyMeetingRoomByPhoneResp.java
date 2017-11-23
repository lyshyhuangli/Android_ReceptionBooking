package com.huizhou.receptionbooking.response;

import com.huizhou.receptionbooking.database.vo.BookingMeetingRecord;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.LinkedList;
import java.util.List;

public class GetMyMeetingRoomByPhoneResp extends CommonResult
{

    List<BookingMeetingRecord> myMeetingRoom = new LinkedList<BookingMeetingRecord>();

    public List<BookingMeetingRecord> getMyMeetingRoom()
    {
        return myMeetingRoom;
    }

    public void setMyMeetingRoom(List<BookingMeetingRecord> myMeetingRoom)
    {
        this.myMeetingRoom = myMeetingRoom;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }


}

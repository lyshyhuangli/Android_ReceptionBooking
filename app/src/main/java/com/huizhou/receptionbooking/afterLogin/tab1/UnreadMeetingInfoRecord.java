package com.huizhou.receptionbooking.afterLogin.tab1;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/7.
 */

public class UnreadMeetingInfoRecord
{
    private Map<Integer, UnreadMeetingMoreRecord> unreadMeetingIds = new HashMap<>();

    public Map<Integer, UnreadMeetingMoreRecord> getUnreadMeetingIds()
    {
        return unreadMeetingIds;
    }

    public void setUnreadMeetingIds(Map<Integer, UnreadMeetingMoreRecord> unreadMeetingIds)
    {
        this.unreadMeetingIds = unreadMeetingIds;
    }
}

package com.huizhou.receptionbooking.database.vo;

/**
 * Created by Administrator on 2017/11/1.
 */

public class GroupPersonInfoRecord
{
    private int id;

    private String userPhone;

    private String groupName;

    private String groupUserId;

    private  String groupUserName;

    public String getGroupUserName()
    {
        return groupUserName;
    }

    public void setGroupUserName(String groupUserName)
    {
        this.groupUserName = groupUserName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUserPhone()
    {
        return userPhone;
    }

    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupUserId()
    {
        return groupUserId;
    }

    public void setGroupUserId(String groupUserId)
    {
        this.groupUserId = groupUserId;
    }
}

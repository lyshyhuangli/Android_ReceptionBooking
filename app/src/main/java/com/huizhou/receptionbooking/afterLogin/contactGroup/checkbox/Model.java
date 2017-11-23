package com.huizhou.receptionbooking.afterLogin.contactGroup.checkbox;

/**
 * Created by Administrator on 2017/8/23.
 */

public class Model
{
    private boolean ischeck;
    private String str;
    private int id;
    private String groupUserId;

    private String getGroupUserName;

    public String getGroupUserId()
    {
        return groupUserId;
    }

    public void setGroupUserId(String groupUserId)
    {
        this.groupUserId = groupUserId;
    }

    public String getGetGroupUserName()
    {
        return getGroupUserName;
    }

    public void setGetGroupUserName(String getGroupUserName)
    {
        this.getGroupUserName = getGroupUserName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public boolean ischeck()
    {
        return ischeck;
    }

    public void setIscheck(boolean ischeck)
    {
        this.ischeck = ischeck;
    }

    public String getStr()
    {
        return str;
    }

    public void setStr(String str)
    {
        this.str = str;
    }
}

package com.huizhou.receptionbooking.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class UpdateGroupPersonByIdReq extends CommonRequest
{
    private int id;

    private String userPhone;

    private String groupName;

    private String groupUserId;

    private String groupUserName;

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

    public String getGroupUserName()
    {
        return groupUserName;
    }

    public void setGroupUserName(String groupUserName)
    {
        this.groupUserName = groupUserName;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }
}

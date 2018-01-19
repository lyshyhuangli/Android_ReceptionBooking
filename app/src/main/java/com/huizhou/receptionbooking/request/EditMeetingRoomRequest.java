package com.huizhou.receptionbooking.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class EditMeetingRoomRequest extends CommonRequest
{
    private int id;
    private String name;
    private int parentId;
    private String remark;
    private int type;
    private int meetingroomSort;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getParentId()
    {
        return parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getMeetingroomSort()
    {
        return meetingroomSort;
    }

    public void setMeetingroomSort(int meetingroomSort)
    {
        this.meetingroomSort = meetingroomSort;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }


}

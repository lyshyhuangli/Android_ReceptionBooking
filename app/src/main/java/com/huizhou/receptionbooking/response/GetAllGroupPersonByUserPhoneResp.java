package com.huizhou.receptionbooking.response;

import com.huizhou.receptionbooking.database.vo.GroupPersonInfoRecord;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.LinkedList;
import java.util.List;

public class GetAllGroupPersonByUserPhoneResp extends CommonResult
{
    private List<GroupPersonInfoRecord> info = new LinkedList<GroupPersonInfoRecord>();

    public List<GroupPersonInfoRecord> getInfo()
    {
        return info;
    }

    public void setInfo(List<GroupPersonInfoRecord> info)
    {
        this.info = info;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }
}

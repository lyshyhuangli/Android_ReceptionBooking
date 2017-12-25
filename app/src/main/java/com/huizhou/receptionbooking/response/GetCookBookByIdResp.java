package com.huizhou.receptionbooking.response;

import com.huizhou.receptionbooking.database.vo.CookBookRecord;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class GetCookBookByIdResp extends CommonResult
{
    CookBookRecord info = new CookBookRecord();

    public CookBookRecord getInfo()
    {
        return info;
    }

    public void setInfo(CookBookRecord info)
    {
        this.info = info;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }

}

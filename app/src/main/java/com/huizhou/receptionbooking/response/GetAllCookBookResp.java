package com.huizhou.receptionbooking.response;

import com.huizhou.receptionbooking.database.vo.CookBookRecord;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

public class GetAllCookBookResp  extends CommonResult
{
    List<CookBookRecord> result;

    public List<CookBookRecord> getResult()
    {
        return result;
    }

    public void setResult(List<CookBookRecord> result)
    {
        this.result = result;
    }
    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }

}

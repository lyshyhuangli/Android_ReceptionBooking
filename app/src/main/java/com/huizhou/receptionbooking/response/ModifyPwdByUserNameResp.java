package com.huizhou.receptionbooking.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ModifyPwdByUserNameResp extends CommonResult
{
    private boolean isOK;

    public boolean getIsOK()
    {
        return isOK;
    }

    public void setIsOK(boolean OK)
    {
        isOK = OK;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }

}

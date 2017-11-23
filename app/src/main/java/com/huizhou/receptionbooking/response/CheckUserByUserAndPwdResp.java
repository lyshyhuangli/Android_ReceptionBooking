package com.huizhou.receptionbooking.response;

public class CheckUserByUserAndPwdResp extends CommonResult
{
    private boolean isExist;

    public boolean getIsExist()
    {
        return isExist;
    }

    public void setIsExist(boolean exist)
    {
        isExist = exist;
    }
}

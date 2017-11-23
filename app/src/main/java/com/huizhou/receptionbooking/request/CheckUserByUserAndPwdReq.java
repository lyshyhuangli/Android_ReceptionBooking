package com.huizhou.receptionbooking.request;

public class CheckUserByUserAndPwdReq extends CommonRequest
{
    private String userName;

    private String pwd;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }
}

package com.huizhou.receptionbooking.database.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CookBookRecord
{
    int id;
    String publishUser;
    String publishDate;
    String menuType;
    int menuTypeNumber;
    String menuContent;

    public int getMenuTypeNumber()
    {
        return menuTypeNumber;
    }

    public void setMenuTypeNumber(int menuTypeNumber)
    {
        this.menuTypeNumber = menuTypeNumber;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getPublishUser()
    {
        return publishUser;
    }

    public void setPublishUser(String publishUser)
    {
        this.publishUser = publishUser;
    }

    public String getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(String publishDate)
    {
        this.publishDate = publishDate;
    }

    public String getMenuType()
    {
        return menuType;
    }

    public void setMenuType(String menuType)
    {
        this.menuType = menuType;
    }

    public String getMenuContent()
    {
        return menuContent;
    }

    public void setMenuContent(String menuContent)
    {
        this.menuContent = menuContent;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }
    

}

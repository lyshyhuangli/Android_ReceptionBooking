package com.huizhou.receptionbooking.database.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BookingDiningRecord
{
    int id;
    String bookerUser;
    String diningRoom;
    String diningDate;
    int diningTypeNumber;
    String diningType;
    String content;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getBookerUser()
    {
        return bookerUser;
    }

    public void setBookerUser(String bookerUser)
    {
        this.bookerUser = bookerUser;
    }

    public String getDiningRoom()
    {
        return diningRoom;
    }

    public void setDiningRoom(String diningRoom)
    {
        this.diningRoom = diningRoom;
    }

    public String getDiningDate()
    {
        return diningDate;
    }

    public void setDiningDate(String diningDate)
    {
        this.diningDate = diningDate;
    }

    public int getDiningTypeNumber()
    {
        return diningTypeNumber;
    }

    public void setDiningTypeNumber(int diningTypeNumber)
    {
        this.diningTypeNumber = diningTypeNumber;
    }

    public String getDiningType()
    {
        return diningType;
    }

    public void setDiningType(String diningType)
    {
        this.diningType = diningType;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }

}

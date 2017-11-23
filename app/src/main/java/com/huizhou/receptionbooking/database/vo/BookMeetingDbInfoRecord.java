package com.huizhou.receptionbooking.database.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/14.
 */

public class BookMeetingDbInfoRecord
{
    private int id;
    private String bookUser;
    private String meetingDate;
    private String amOrPm;
    private String startTime;
    private String endTime;
    private String meetingroom;
    private String threaf;
    private String content;
    private String person;
    private String personName;
    private String clothes;
    private int wakeType;
    private String meetingDiscipline;
    private String connectPerson;
    private String connectPhone;
    private String files;
    private byte[] QRcode;
    private Date createTime;
    private String remark;

    public String getPersonName()
    {
        return personName;
    }

    public void setPersonName(String personName)
    {
        this.personName = personName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getBookUser()
    {
        return bookUser;
    }

    public void setBookUser(String bookUser)
    {
        this.bookUser = bookUser;
    }

    public String getMeetingDate()
    {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate)
    {
        this.meetingDate = meetingDate;
    }

    public String getAmOrPm()
    {
        return amOrPm;
    }

    public void setAmOrPm(String amOrPm)
    {
        this.amOrPm = amOrPm;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getMeetingroom()
    {
        return meetingroom;
    }

    public void setMeetingroom(String meetingroom)
    {
        this.meetingroom = meetingroom;
    }

    public String getThreaf()
    {
        return threaf;
    }

    public void setThreaf(String threaf)
    {
        this.threaf = threaf;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getPerson()
    {
        return person;
    }

    public void setPerson(String person)
    {
        this.person = person;
    }

    public String getClothes()
    {
        return clothes;
    }

    public void setClothes(String clothes)
    {
        this.clothes = clothes;
    }

    public int getWakeType()
    {
        return wakeType;
    }

    public void setWakeType(int wakeType)
    {
        this.wakeType = wakeType;
    }

    public String getMeetingDiscipline()
    {
        return meetingDiscipline;
    }

    public void setMeetingDiscipline(String meetingDiscipline)
    {
        this.meetingDiscipline = meetingDiscipline;
    }

    public String getConnectPerson()
    {
        return connectPerson;
    }

    public void setConnectPerson(String connectPerson)
    {
        this.connectPerson = connectPerson;
    }

    public String getConnectPhone()
    {
        return connectPhone;
    }

    public void setConnectPhone(String connectPhone)
    {
        this.connectPhone = connectPhone;
    }

    public String getFiles()
    {
        return files;
    }

    public void setFiles(String files)
    {
        this.files = files;
    }

    public byte[] getQRcode()
    {
        return QRcode;
    }

    public void setQRcode(byte[] QRcode)
    {
        this.QRcode = QRcode;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    public String toString()
    {
        String str = ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        return str;
    }

}

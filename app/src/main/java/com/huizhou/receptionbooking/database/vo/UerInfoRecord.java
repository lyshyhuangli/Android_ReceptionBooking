package com.huizhou.receptionbooking.database.vo;

/**
 * Created by Administrator on 2017/10/18.
 */

public class UerInfoRecord
{
    private int id;
    private String name;
    private String phone;
    private String pwd;
    private int parentId;
    private String sex;
    private String idcard;
    private String remark;
    private String role;
    private int type;
    private String parentName;
    private int personSort;
    private String post;
    private String carId;

    public String getPost()
    {
        return post;
    }

    public void setPost(String post)
    {
        this.post = post;
    }

    public String getCarId()
    {
        return carId;
    }

    public void setCarId(String carId)
    {
        this.carId = carId;
    }

    public int getPersonSort()
    {
        return personSort;
    }

    public void setPersonSort(int personSort)
    {
        this.personSort = personSort;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public String getIdcard()
    {
        return idcard;
    }

    public void setIdcard(String idcard)
    {
        this.idcard = idcard;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

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

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }

    public int getParentId()
    {
        return parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}

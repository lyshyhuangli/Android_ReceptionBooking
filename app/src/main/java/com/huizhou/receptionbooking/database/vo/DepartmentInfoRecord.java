package com.huizhou.receptionbooking.database.vo;

/**
 * Created by Administrator on 2017/10/19.
 */

public class DepartmentInfoRecord
{
    private int id;

    private String name;

    private int parentId;

    private String parentName;

    private String remark;

    private int depSort;

    public int getDepSort()
    {
        return depSort;
    }

    public void setDepSort(int depSort)
    {
        this.depSort = depSort;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getParentId()
    {
        return parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}

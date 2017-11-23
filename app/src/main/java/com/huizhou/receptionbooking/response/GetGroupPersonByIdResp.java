package com.huizhou.receptionbooking.response;

import com.huizhou.receptionbooking.database.vo.GroupPersonInfoRecord;

public class GetGroupPersonByIdResp extends CommonResult
{
    private GroupPersonInfoRecord info;

    public GroupPersonInfoRecord getInfo()
    {
        return info;
    }

    public void setInfo(GroupPersonInfoRecord info)
    {
        this.info = info;
    }

}

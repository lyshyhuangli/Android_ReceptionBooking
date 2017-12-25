package com.huizhou.receptionbooking.database.dao;

import com.huizhou.receptionbooking.database.vo.GroupPersonInfoRecord;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public interface GroupPersonDAO
{
    /**
     * 分页查询所有群组
     *
     * @return
     */
    List<GroupPersonInfoRecord> getAllGroup(List<String> errorList, int count, String userCode,String param);

    /**
     * 保存群组信息
     *
     * @param info
     * @return
     */
    boolean saveGroupPerson(GroupPersonInfoRecord info, List<String> errorList);
}

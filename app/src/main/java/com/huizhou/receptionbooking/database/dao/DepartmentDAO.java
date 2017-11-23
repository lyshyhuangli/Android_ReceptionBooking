package com.huizhou.receptionbooking.database.dao;

import com.huizhou.receptionbooking.common.BaseTreeBean;
import com.huizhou.receptionbooking.database.vo.DepartmentInfoRecord;
import com.huizhou.receptionbooking.database.vo.MeetingRoomInfoRecord;

import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */

public interface DepartmentDAO
{
    /**
     * 获取所有部门信息
     *
     * @param errorList
     * @return
     */
    List<BaseTreeBean> getAllDepartment(List<String> errorList);

    /**
     * 根据Id获取部门信息
     *
     * @param errorList
     * @return
     */
    DepartmentInfoRecord getDepartmentById(int id, List<String> errorList);

    /**
     * 保存部门信息
     *
     * @param info
     * @return
     */
    boolean saveDepartment(DepartmentInfoRecord info, List<String> errorList);

    /**
     * 编辑部门信息
     *
     * @param info
     * @return
     */
    boolean updateDepartment(DepartmentInfoRecord info, List<String> errorList);
}

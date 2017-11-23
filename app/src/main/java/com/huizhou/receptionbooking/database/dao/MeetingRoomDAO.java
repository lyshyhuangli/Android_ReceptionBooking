package com.huizhou.receptionbooking.database.dao;

import com.huizhou.receptionbooking.common.BaseTreeBean;
import com.huizhou.receptionbooking.database.vo.DepartmentInfoRecord;
import com.huizhou.receptionbooking.database.vo.MeetingRoomInfoRecord;

import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */

public interface MeetingRoomDAO
{

    /**
     * 获取所有会议室信息
     *
     * @param errorList
     * @return
     */
    List<BaseTreeBean> getAllMeetingRoom(List<String> errorList);

    /**
     * 根据Id获取会议室信息
     *
     * @param errorList
     * @return
     */
    MeetingRoomInfoRecord getMeetingRoomById(int id, List<String> errorList);

    /**
     * 编辑会议室信息
     *
     * @param info
     * @return
     */
     boolean updateMeetingRoom(MeetingRoomInfoRecord info, List<String> errorList);

    /**
     * 保存会议室信息
     *
     * @param info
     * @return
     */
     boolean saveMeetingRoom(MeetingRoomInfoRecord info, List<String> errorList);
}

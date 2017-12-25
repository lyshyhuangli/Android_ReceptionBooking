package com.huizhou.receptionbooking.database.dao.impl;

import com.huizhou.receptionbooking.common.BaseTreeBean;
import com.huizhou.receptionbooking.database.DbConnect;
import com.huizhou.receptionbooking.database.dao.MeetingRoomDAO;
import com.huizhou.receptionbooking.database.vo.DepartmentInfoRecord;
import com.huizhou.receptionbooking.database.vo.MeetingRoomInfoRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */

public class MeetingRoomDAOImpl implements MeetingRoomDAO
{

    /**
     * 获取所有会议室信息
     *
     * @param errorList
     * @return
     */
    public List<BaseTreeBean> getAllMeetingRoom(List<String> errorList)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return null;
        }

        //type=2代表会议室
        String strSql = "select * from tb_user_dep_meeting where type =2 or type =1 ";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BaseTreeBean> list = new LinkedList<>();

        try
        {
            pstmt = conn.prepareStatement(strSql);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                BaseTreeBean b = new BaseTreeBean();
                b.setId(String.valueOf(rs.getInt("id")));
                b.setName(rs.getString("name"));
                b.setParentId(String.valueOf(rs.getInt("parentId")));
                b.setType(String.valueOf(rs.getInt("type")));
                list.add(b);
            }
        }
        catch (Exception e)
        {
            errorList.add("查询会议室信息失败，请检查网络或重试。");
        }
        finally
        {
            try
            {
                if (null != rs)
                {
                    rs.close();
                }
                if (null != pstmt)
                {
                    pstmt.close();
                }
            }
            catch (Exception e)
            {
                errorList.add("查询会议室信息失败，请检查网络或重试。");
            }

            return list;
        }

    }

    /**
     * 根据Id获取会议室信息
     *
     * @param errorList
     * @return
     */
    public MeetingRoomInfoRecord getMeetingRoomById(int id, List<String> errorList)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return null;
        }

        String strSql = "select d.id,d.name,d.parentId,d.remark,t.name as pname from tb_user_dep_meeting d LEFT JOIN tb_user_dep_meeting t on d.parentId = t.id where  d.id =" + id;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        MeetingRoomInfoRecord m = new MeetingRoomInfoRecord();
        try
        {
            pstmt = conn.prepareStatement(strSql);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                m.setParentId(rs.getInt("parentId"));
                m.setRemark(rs.getString("remark"));
                m.setParentName(rs.getString("pname"));
            }
        }
        catch (Exception e)
        {
            errorList.add("查询会议室信息失败，请检查网络或重试。");
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (null != rs)
                {
                    rs.close();
                }
                if (null != pstmt)
                {
                    pstmt.close();
                }
            }
            catch (Exception e)
            {
                errorList.add("查询部门信息失败，请检查网络或重试。");
            }

            return m;
        }
    }

    /**
     * 保存会议室信息
     *
     * @param info
     * @return
     */
    public boolean saveMeetingRoom(MeetingRoomInfoRecord info, List<String> errorList)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return false;
        }

        String strSql = "insert tb_user_dep_meeting (name,parentId,remark,type) values(?,?,?,?) ";
        PreparedStatement pstmt = null;

        try
        {
            pstmt = conn.prepareStatement(strSql);
            pstmt.setString(1, info.getName());
            pstmt.setInt(2, info.getParentId());
            pstmt.setString(3, info.getRemark());
            pstmt.setInt(4, 2);
            return !pstmt.execute();
        }
        catch (Exception e)
        {
            errorList.add("增加会议室信息失败，请检查网络或重试。");
        }
        finally
        {
            try
            {
                if (null != pstmt)
                {
                    pstmt.close();
                }
            }
            catch (Exception e)
            {
                errorList.add("增加会议室信息失败，请检查网络或重试。");
            }

        }
        return false;
    }

    /**
     * 编辑会议室信息
     *
     * @param info
     * @return
     */
    public boolean updateMeetingRoom(MeetingRoomInfoRecord info, List<String> errorList)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return false;
        }

        String strSql = "update  tb_user_dep_meeting set name = ? ,parentId=?,remark=? where type =2 and id =? ";
        PreparedStatement pstmt = null;

        try
        {
            pstmt = conn.prepareStatement(strSql);
            pstmt.setString(1, info.getName());
            pstmt.setInt(2, info.getParentId());
            pstmt.setString(3, info.getRemark());
            pstmt.setInt(4,info.getId());
            return !pstmt.execute();
        }
        catch (Exception e)
        {
            errorList.add("编辑会议室信息失败，请检查网络或重试。");
        }
        finally
        {
            try
            {
                if (null != pstmt)
                {
                    pstmt.close();
                }
            }
            catch (Exception e)
            {
                errorList.add("编辑会议室信息失败，请检查网络或重试。");
            }

        }
        return false;
    }

}

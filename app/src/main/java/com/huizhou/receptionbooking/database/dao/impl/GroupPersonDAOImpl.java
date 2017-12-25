package com.huizhou.receptionbooking.database.dao.impl;

import com.huizhou.receptionbooking.database.DbConnect;
import com.huizhou.receptionbooking.database.dao.GroupPersonDAO;
import com.huizhou.receptionbooking.database.vo.DepartmentInfoRecord;
import com.huizhou.receptionbooking.database.vo.GroupPersonInfoRecord;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class GroupPersonDAOImpl implements GroupPersonDAO
{
    /**
     * 分页查询所有群组
     *
     * @return
     */
    public List<GroupPersonInfoRecord> getAllGroup(List<String> errorList, int count, String userCode, String param)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return null;
        }

        //type=1代表部门
        String strSql = null;
        if (StringUtils.isNotBlank(param))
        {
            strSql = "select * from tb_groupperson where userPhone ='" + userCode + "' and groupName like '%" + param + "%' order by groupName DESC LIMIT " + count * 20;
        }
        else
        {
            strSql = "select * from tb_groupperson where userPhone ='" + userCode + "' order by groupName DESC LIMIT " + count * 20;
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<GroupPersonInfoRecord> list = new LinkedList<>();

        try
        {
            pstmt = conn.prepareStatement(strSql);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                GroupPersonInfoRecord b = new GroupPersonInfoRecord();
                b.setId(rs.getInt("id"));
                b.setGroupName(rs.getString("groupName"));
                list.add(b);
            }
        }
        catch (Exception e)
        {
            errorList.add("查询群组信息失败，请检查网络或重试。");
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
                errorList.add("查询群组信息失败，请检查网络或重试。");
            }

            return list;
        }
    }

    /**
     * 保存群组信息
     *
     * @param info
     * @return
     */
    public boolean saveGroupPerson(GroupPersonInfoRecord info, List<String> errorList)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return false;
        }

        String strSql = "insert tb_groupperson (userPhone,groupName,groupUserId,groupUserName) values(?,?,?,?) ";
        PreparedStatement pstmt = null;

        try
        {
            pstmt = conn.prepareStatement(strSql);
            pstmt.setString(1, info.getUserPhone());
            pstmt.setString(2, info.getGroupName());
            pstmt.setString(3, info.getGroupUserId());
            pstmt.setString(4, info.getGroupUserName());
            return !pstmt.execute();
        }
        catch (Exception e)
        {
            errorList.add("增加群组信息失败，请检查网络或重试。");
            e.printStackTrace();
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
                errorList.add("增加群组信息失败，请检查网络或重试。");
            }

        }
        return false;
    }
}

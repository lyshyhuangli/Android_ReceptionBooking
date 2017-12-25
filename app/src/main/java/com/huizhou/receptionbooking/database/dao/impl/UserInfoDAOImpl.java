package com.huizhou.receptionbooking.database.dao.impl;

import com.huizhou.receptionbooking.common.BaseTreeBean;
import com.huizhou.receptionbooking.database.DbConnect;
import com.huizhou.receptionbooking.database.dao.UserInfoDAO;
import com.huizhou.receptionbooking.database.vo.MeetingRoomInfoRecord;
import com.huizhou.receptionbooking.database.vo.UerInfoRecord;
import com.huizhou.receptionbooking.multileveltreelist.Node;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/18.
 */

public class UserInfoDAOImpl implements UserInfoDAO
{

    /**
     * 根据用户名和密码校验用户真实性
     *
     * @param userName
     * @param pwd
     * @return
     */
    public boolean checkUserByUserAndPwd(String userName, String pwd, List<String> errorList,List<String> loginInfo)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return false;
        }

        String strsql = "select u.*,d.name as department from tb_user_dep_meeting u " +
                "LEFT JOIN tb_user_dep_meeting d ON u.parentId = d.id " +
                "where   u.phone  = ? and u.pwd = ? and u.type =3";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            pstmt = conn.prepareStatement(strsql);
            pstmt.setString(1, userName);
            pstmt.setString(2, pwd);
            rs = pstmt.executeQuery();

            if (rs.next())
            {
                loginInfo.add(rs.getString("name"));
                loginInfo.add(rs.getString("department"));
                loginInfo.add(rs.getString("parentId"));
                loginInfo.add(rs.getString("role"));
                return true;
            }
        }
        catch (Exception e)
        {
            errorList.add("查询登录用户是否存在失败，请检查网络或重试。");
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
                errorList.add("查询登录用户是否存在失败，请检查网络或重试。");
            }

        }

        return false;
    }

    /**
     * 修改用户密码
     *
     * @param userName
     * @param pwd
     * @return
     */
    public boolean modifyPwdByUserName(String userName, String pwd, List<String> errorList)
    {
        Connection conn = DbConnect.getConnection(errorList);
        String strsql = "update tb_user_dep_meeting set pwd = ? where phone  = ? and type =3";
        PreparedStatement pstmt = null;
        try
        {
            pstmt = conn.prepareStatement(strsql);
            pstmt.setString(1, pwd);
            pstmt.setString(2, userName);

            if (pstmt.executeUpdate() != 0)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            errorList.add("修改密码失败，请检查网络或重试。");
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
                errorList.add("修改密码失败，请检查网络或重试。");
            }
        }

        return false;

    }
    /**
     * 获取所有通讯录(多选联系人)
     *
     * @param errorList
     * @return
     */
    public List<Node> getAllContactPersionForCheckbox(List<String> errorList)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return null;
        }

        //type=3代表通讯录
        String strSql = "select * from tb_user_dep_meeting where type =3 or type =1 ";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Node> list = new ArrayList<>();

        try
        {
            pstmt = conn.prepareStatement(strSql);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                Node b = new Node();
                b.setId(String.valueOf(rs.getInt("id")));
                b.setName(rs.getString("name"));
                b.setpId(String.valueOf(rs.getInt("parentId")));
                b.setType(rs.getInt("type"));
                list.add(b);
            }
        }
        catch (Exception e)
        {
            errorList.add("查询通讯录信息失败，请检查网络或重试。");
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
                errorList.add("查询通讯录信息失败，请检查网络或重试。");
            }

            return list;
        }
    }

    /**
     * 获取所有通讯录
     *
     * @param errorList
     * @return
     */
    public List<BaseTreeBean> getAllContactPersion(List<String> errorList)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return null;
        }

        //type=3代表通讯录
        String strSql = "select * from tb_user_dep_meeting where type =3 or type =1 ";
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
            errorList.add("查询通讯录信息失败，请检查网络或重试。");
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
                errorList.add("查询通讯录信息失败，请检查网络或重试。");
            }

            return list;
        }

    }

    /**
     * 根据Id获取通讯录信息
     *
     * @param errorList
     * @return
     */
    public UerInfoRecord getContactPersionById(int id, List<String> errorList)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return null;
        }

        String strSql = "select d.id,d.name,d.parentId,d.remark,d.phone,d.sex,d.idcard,d.role,t.name as pname from tb_user_dep_meeting d LEFT JOIN tb_user_dep_meeting t on d.parentId = t.id where  d.id =" + id;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        UerInfoRecord m = new UerInfoRecord();
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
                m.setPhone(rs.getString("phone"));
                m.setSex(rs.getString("sex"));
                m.setRole(rs.getString("role"));
                m.setIdcard(rs.getString("idcard"));
            }
        }
        catch (Exception e)
        {
            errorList.add("查询通讯录信息失败，请检查网络或重试。");
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
                errorList.add("查询通讯录信息失败，请检查网络或重试。");
            }

            return m;
        }
    }

    /**
     * 编辑通讯录信息
     *
     * @param info
     * @return
     */
    public boolean updateContactPersion(UerInfoRecord info, List<String> errorList)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return false;
        }

        String strSql = "update  tb_user_dep_meeting set name = ?,parentId=?,remark=?,phone=?,sex=?,idcard=?,role=? where type =3 and id =? ";
        PreparedStatement pstmt = null;

        try
        {
            pstmt = conn.prepareStatement(strSql);
            pstmt.setString(1, info.getName());
            pstmt.setInt(2, info.getParentId());
            pstmt.setString(3, info.getRemark());
            pstmt.setString(4, info.getPhone());
            pstmt.setString(5, info.getSex());
            pstmt.setString(6, info.getIdcard());
            pstmt.setString(7, info.getRole());
            pstmt.setInt(8, info.getId());

            return !pstmt.execute();
        }
        catch (Exception e)
        {
            errorList.add("编辑通讯录信息失败，请检查网络或重试。");
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
                errorList.add("编辑通讯录信息失败，请检查网络或重试。");
            }

        }
        return false;
    }

    /**
     * 保存通讯录信息
     *
     * @param info
     * @return
     */
    public boolean saveContactPersion(UerInfoRecord info, List<String> errorList)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return false;
        }

        String strSql = "insert tb_user_dep_meeting (name,parentId,remark,type,phone,sex,idcard,role) values(?,?,?,?,?,?,?,?) ";
        PreparedStatement pstmt = null;

        try
        {
            pstmt = conn.prepareStatement(strSql);
            pstmt.setString(1, info.getName());
            pstmt.setInt(2, info.getParentId());
            pstmt.setString(3, info.getRemark());
            pstmt.setInt(4, 3);
            pstmt.setString(5, info.getPhone());
            pstmt.setString(6, info.getSex());
            pstmt.setString(7, info.getIdcard());
            pstmt.setString(8, info.getRole());

            return !pstmt.execute();
        }
        catch (Exception e)
        {
            errorList.add("增加通讯录信息失败，请检查网络或重试。");
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
                errorList.add("增加通讯录信息失败，请检查网络或重试。");
            }

        }
        return false;
    }

    /**
     * 根据名字查询通讯录
     *
     * @param param
     * @return
     */
    public List<UerInfoRecord> getPersonByName(String param, List<String> errorList,int count)
    {
        Connection conn = DbConnect.getConnection(errorList);

        if (!errorList.isEmpty())
        {
            return null;
        }

        StringBuilder strsql = new StringBuilder();
        strsql.append("select * from tb_user_dep_meeting where 1=1 and  type =3 ");

        if (StringUtils.isNotBlank(param))
        {
            strsql.append("  and name like '%" + param + "%'");
        }

        strsql.append(" order by name DESC LIMIT " + count * 20);

        List<UerInfoRecord> list = new LinkedList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            pstmt = conn.prepareStatement(strsql.toString());
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                UerInfoRecord u = new UerInfoRecord();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                list.add(u);
            }
        }
        catch (Exception e)
        {
            errorList.add("查询登录用户是否存在失败，请检查网络或重试。");
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
                errorList.add("查询登录用户是否存在失败，请检查网络或重试。");
            }

        }

        return list;
    }
}

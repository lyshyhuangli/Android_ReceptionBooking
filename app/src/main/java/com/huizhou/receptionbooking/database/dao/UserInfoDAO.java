package com.huizhou.receptionbooking.database.dao;

import com.huizhou.receptionbooking.common.BaseTreeBean;
import com.huizhou.receptionbooking.database.vo.UerInfoRecord;
import com.huizhou.receptionbooking.multileveltreelist.Node;

import java.util.List;

/**
 * Created by Administrator on 2017/10/18.
 */

public interface UserInfoDAO
{
    /**
     * 根据用户名和密码校验用户真实性
     * @param userName
     * @param pwd
     * @return
     */
      boolean checkUserByUserAndPwd(String userName,String pwd,List<String> errorList,List<String> loginShowName);

    /**
     * 修改用户密码
     * @param userName
     * @param pwd
     * @return
     */
      boolean modifyPwdByUserName(String userName,String pwd,List<String> errorList);

    /**
     * 获取所有通讯录
     *
     * @param errorList
     * @return
     */
    List<BaseTreeBean> getAllContactPersion(List<String> errorList);

    /**
     * 根据Id获取通讯录信息
     *
     * @param errorList
     * @return
     */
    UerInfoRecord getContactPersionById(int id, List<String> errorList);

    /**
     * 编辑通讯录信息
     *
     * @param info
     * @return
     */
    boolean updateContactPersion(UerInfoRecord info, List<String> errorList);

    /**
     * 保存通讯录信息
     *
     * @param info
     * @return
     */
    boolean saveContactPersion(UerInfoRecord info, List<String> errorList);

    /**
     * 根据名字或部门查询通讯录
     * @param param
     * @return
     */
    List<UerInfoRecord> getPersonByName(String param, List<String> errorList,int count);

    /**
     * 获取所有通讯录(多选联系人)
     *
     * @param errorList
     * @return
     */
    List<Node> getAllContactPersionForCheckbox(List<String> errorList);
}

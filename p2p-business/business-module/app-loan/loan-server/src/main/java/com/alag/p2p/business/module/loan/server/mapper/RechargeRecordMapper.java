package com.alag.p2p.business.module.loan.server.mapper;

import com.alag.p2p.business.module.loan.api.model.RechargeRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RechargeRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    int insert(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    int insertSelective(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    RechargeRecord selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    int updateByPrimaryKeySelective(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    int updateByPrimaryKey(RechargeRecord record);
}
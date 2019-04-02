package com.alag.p2p.business.module.loan.server.mapper;

import com.alag.p2p.business.module.loan.api.model.IncomeRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IncomeRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    int insert(IncomeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    int insertSelective(IncomeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    IncomeRecord selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    int updateByPrimaryKeySelective(IncomeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Tue Apr 02 02:28:02 CST 2019
     */
    int updateByPrimaryKey(IncomeRecord record);
}
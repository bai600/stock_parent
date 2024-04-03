package com.xbb.stock.mapper;

import com.xbb.stock.pojo.entity.SysLog;

/**
 * @Entity com.xbb.stock.pojo.entity.SysLog
 */
public interface SysLogMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

}

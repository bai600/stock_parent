package com.xbb.stock.mapper;

import com.xbb.stock.pojo.entity.SysPermission;

/**
 * @Entity com.xbb.stock.pojo.entity.SysPermission
 */
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

}

package com.xbb.stock.mapper;

import com.xbb.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity com.xbb.stock.pojo.entity.SysUser
 */

public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findUserInfoByName(@Param("userName") String userName);

}

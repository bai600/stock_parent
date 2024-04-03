package com.xbb.stock.service;

import com.xbb.stock.pojo.entity.SysUser;
import com.xbb.stock.vo.req.LoginReqVo;
import com.xbb.stock.vo.resp.LoginRespVo;
import com.xbb.stock.vo.resp.R;


public interface UserService {

    SysUser findByUserName(String userName);

    R<LoginRespVo> login(LoginReqVo vo);

}

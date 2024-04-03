package com.xbb.stock.service.impl;



import com.mysql.jdbc.StringUtils;
import com.xbb.stock.mapper.SysUserMapper;
import com.xbb.stock.pojo.entity.SysUser;
import com.xbb.stock.service.UserService;
import com.xbb.stock.vo.req.LoginReqVo;
import com.xbb.stock.vo.resp.LoginRespVo;
import com.xbb.stock.vo.resp.R;
import com.xbb.stock.vo.resp.ResponseCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SysUser findByUserName(String userName) {
        return sysUserMapper.findUserInfoByName(userName);
    }

    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
        //1 判断参数是否合法
        if (vo==null || StringUtils.isNullOrEmpty(vo.getUsername())
                || StringUtils.isNullOrEmpty(vo.getPassword())
                ||StringUtils.isNullOrEmpty(vo.getCode())){
            return R.error(ResponseCode.DATA_ERROR);
        }
        //2 根据用户名查询用户信息，获取密码密文
        SysUser dbUser = sysUserMapper.findUserInfoByName(vo.getUsername());
        //3 密码校验比对
        if (dbUser==null){
            return R.error(ResponseCode.ACCOUNT_EXISTS_ERROR);
        }
        if (passwordEncoder.matches(dbUser.getPassword(), vo.getPassword())){
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //4 响应
        LoginRespVo respVo = new LoginRespVo();
        BeanUtils.copyProperties(dbUser,respVo);    //必须保证属性名称一致
        return R.ok(respVo);
    }
}

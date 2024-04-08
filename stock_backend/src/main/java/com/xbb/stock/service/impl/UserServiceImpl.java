package com.xbb.stock.service.impl;



import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.mysql.jdbc.StringUtils;
import com.xbb.stock.constant.StockConstant;
import com.xbb.stock.mapper.SysUserMapper;
import com.xbb.stock.pojo.entity.SysUser;
import com.xbb.stock.service.UserService;
import com.xbb.stock.utils.IdWorker;
import com.xbb.stock.vo.req.LoginReqVo;
import com.xbb.stock.vo.resp.LoginRespVo;
import com.xbb.stock.vo.resp.R;
import com.xbb.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;

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
        //2.校验验证码和sessionId是否有效
        if (StringUtils.isNullOrEmpty(vo.getCode()) || StringUtils.isNullOrEmpty(vo.getSessionId())){
            return R.error(ResponseCode.CHECK_CODE_NOT_EMPTY);
        }
        //3.判断redis中保存的验证码是否存在，以及是否与输入的验证码相同(比较时忽略大小写)
        String redisCode= (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX +vo.getSessionId());
        if (StringUtils.isNullOrEmpty(redisCode)) {
            //验证码过期
            return R.error(ResponseCode.CHECK_CODE_TIMEOUT);
        }
        if (! redisCode.equalsIgnoreCase(vo.getCode())){
            //验证码错误
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //4 根据用户名查询用户信息，获取密码密文
        SysUser dbUser = sysUserMapper.findUserInfoByName(vo.getUsername());
        //5 密码校验比对
        if (dbUser==null){
            return R.error(ResponseCode.ACCOUNT_EXISTS_ERROR);
        }
        if (passwordEncoder.matches(dbUser.getPassword(), vo.getPassword())){
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //6 响应
        LoginRespVo respVo = new LoginRespVo();
        BeanUtils.copyProperties(dbUser,respVo);    //必须保证属性名称一致
        return R.ok(respVo);
    }

    @Override
    public R<Map> getCaptchaCode() {
        //参数分别是宽、高、验证码长度、干扰线数量
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        //设置背景颜色清灰
        captcha.setBackground(Color.lightGray);
        //获取图片中的验证码，默认生成的校验码包含文字和数字，长度为4
        String checkCode = captcha.getCode();
        //获取base64格式的图片数据
        String imageData = captcha.getImageBase64();
        //生成sessionId,转化为String避免精度丢失
        String sessionId = String.valueOf(idWorker.nextId());
        log.info("生成校验码:{},会话id:{}",checkCode,sessionId);
        //将sessionId作为key和校验码保存在redis下，并设置缓存中数据存活时间一分钟
        //方便管理加入业务前缀“CK”
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX+sessionId,checkCode,5, TimeUnit.MINUTES);
        //组装响应数据
        HashMap<String, String> data = new HashMap<>();
        data.put("sessionId",sessionId);
        data.put("imageData",imageData);
        //设置响应数据格式
        return R.ok(data);
    }
}

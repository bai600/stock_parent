package com.xbb.stock.controller;

import com.xbb.stock.pojo.entity.SysUser;
import com.xbb.stock.service.UserService;
import com.xbb.stock.vo.req.LoginReqVo;
import com.xbb.stock.vo.resp.LoginRespVo;
import com.xbb.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(tags = "用户相关接口处理器")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "根据用户名查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", dataType = "String", required = true, type = "path")
    })
    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String userName){
        return userService.findByUserName(userName);
    }

    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        return userService.login(vo);

    }

    //生成图片验证码功能
    @GetMapping("/captcha")
    public R<Map> getCaptchaCode(){
        return userService.getCaptchaCode();
    }

}

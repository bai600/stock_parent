package com.xbb.stock.controller;

        import com.xbb.stock.pojo.entity.SysUser;
        import com.xbb.stock.service.UserService;
        import com.xbb.stock.vo.req.LoginReqVo;
        import com.xbb.stock.vo.resp.LoginRespVo;
        import com.xbb.stock.vo.resp.R;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String userName){
        return userService.findByUserName(userName);
    }

    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        return userService.login(vo);

    }

}

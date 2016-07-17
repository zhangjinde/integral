package com.vip.integral.controller;

import com.vip.integral.base.BaseController;
import com.vip.integral.base.Result;
import com.vip.integral.model.Goods;
import com.vip.integral.model.User;
import com.vip.integral.service.GoodsService;
import com.vip.integral.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by lihuajun on 16-7-6.
 */
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/get")
    public String getUser(User user) {
        Result<User> result = new Result<>();

        result.set(0, userService.getByOpenid(user.getOpenid()));

        return result.toString();
    }

}
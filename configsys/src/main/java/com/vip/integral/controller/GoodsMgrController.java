package com.vip.integral.controller;

import com.alibaba.fastjson.JSON;
import com.vip.integral.base.BaseController;
import com.vip.integral.base.Result;
import com.vip.integral.base.ResultDg;
import com.vip.integral.model.Goods;
import com.vip.integral.model.TreeNode;
import com.vip.integral.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by lihuajun on 16-6-14.
 */
@Controller
@RequestMapping("/sys/goods")
public class GoodsMgrController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/fix")
    @ResponseBody
    public String saveOrUpdate(Goods goods) {

        Result<Boolean> result = new Result<>();

        if (goods.getId() == 0) {
            goodsService.save(goods);
        } else if (goods.getId() > 0) {
            goodsService.update(goods);
        }
        result.set(1, true);
        return result.toString();
    }

    @RequestMapping("/get")
    @ResponseBody
    public String get(Goods goods) {

        Result<Goods> result = new Result<>();

        goods = goodsService.get(goods);

        result.set("添加成功", goods);
        return result.toString();
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list() {

        ResultDg<List<Goods>> resultDg = new ResultDg<>();

        List<Goods> list = goodsService.listAll(null);
        resultDg.setTotal(list == null ? 0 : list.size());
        resultDg.setRows(list);

        return JSON.toJSONString(resultDg);
    }

    @RequestMapping("/status/update")
    @ResponseBody
    public String updateStatus(Goods goods) {

        Result<Boolean> result = new Result<>();

        goodsService.update(goods);

        result.set("成功", true);
        return result.toString();
    }
}
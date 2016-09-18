package com.vip.integral.controller;

import com.vip.integral.base.BaseController;
import com.vip.integral.base.Result;
import com.vip.integral.model.Goods;
import com.vip.integral.util.AppConfig;
import com.vip.integral.util.StrUtil;
import com.vip.integral.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.DigestException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lihuajun on 16-7-6.
 */
@Controller
@RequestMapping("/integral")
public class IntegralController extends BaseController {

    @Autowired
    private AppConfig appConfig;

    /**
     * 分享到朋友圈获得积分奖励
     *
     * @param userid
     * @param count
     * @return
     */
    @ResponseBody
    @RequestMapping("/share/encourage")
    public String encourageFromShare(Integer userid, Integer count) {


        Result<Boolean> result = new Result<>();

        //result.set(0, userService.getByOpenid(user.getOpenid()));

        return result.toString();
    }


    @RequestMapping("/rec")
    public ModelAndView integralRec() {

        ModelAndView modelAndView = new ModelAndView("integral_rec");
        Goods goods = new Goods();
        goods.setStatus(1);
        //modelAndView.addObject("goodsList", goodsService.listByCondition(goods));
        return modelAndView;
    }

    /**
     * 赚积分
     *
     * @return
     */
    @RequestMapping("/earn")
    public ModelAndView integralEarn() throws DigestException {
        ModelAndView modelAndView = new ModelAndView("integral_earn");

        Map param = this.getParam(appConfig.appDomain + "/integral/earn");

        modelAndView.addObject("appId", appConfig.wechatAppid);
        modelAndView.addObject("timestamp", param.get("time"));
        modelAndView.addObject("nonceStr", param.get("noncestr"));
        modelAndView.addObject("signature", param.get("signature"));

        return modelAndView;
    }


    private Map<String, String> getParam(String url) throws DigestException {

        String noncestr = StrUtil.getNoncestr();
        Timestamp d = new Timestamp(System.currentTimeMillis());
        String time = Long.toString(d.getTime() / 1000);

        String str = "jsapi_ticket=" + Constant.JSAPI_TICKET + "&noncestr=" + noncestr + "&timestamp=" + time + "&url=" + url;
        logger.info(str);
        String signature = StrUtil.sha1(str);
        logger.info(signature);
        Map<String, String> data = new HashMap<>();
        data.put("noncestr", noncestr);
        data.put("signature", signature);
        data.put("time", time);

        return data;
    }

}

package com.vip.spider;

import com.vip.spider.attack.tengxun.task.CommentAttackTask;
import com.vip.spider.bean.SpringContext;

/**
 * Created by lihuajun on 16-7-27.
 */
public class AttackApp {

    public static void main(String[] args) {
        //初始化
        SpringContext.init("classpath:spring/spring.xml");
        new Thread(new CommentAttackTask()).start();
        //new Thread(new ReplyAttackTask()).start();

    }

}
package com.vip.integral.service;

import com.vip.integral.model.AttackPage;

import java.util.List;

/**
 * Created by lihuajun on 16-7-27.
 */
public interface AttackPageService {

    int save(AttackPage attackPage);

    List<AttackPage> listByBelong(String belong);

}

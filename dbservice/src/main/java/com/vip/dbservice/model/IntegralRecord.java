package com.vip.dbservice.model;

import java.util.Date;

/**
 * Created by lihuajun on 16-7-6.
 */
public class IntegralRecord {

    private Integer id;
    private Integer userid;
    private Integer vipAccountId;
    private Integer goodsid;
    private Integer count;
    /**
     * 10:奖励(关注公众号)
     * 11:奖励(分享到微信朋友圈)
     * 12:奖励(分享到qq空间)
     * 13:奖励(推广)
     * 20:消费
     * 30:充值
     */
    private Integer type;
    private String tag;
    private String des;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public Integer getVipAccountId() {
        return vipAccountId;
    }

    public void setVipAccountId(Integer vipAccountId) {
        this.vipAccountId = vipAccountId;
    }
}

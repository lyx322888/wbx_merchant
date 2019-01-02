package com.wbx.merchant.bean;

import java.io.Serializable;

/**
 * @author Zero
 * @date 2018/7/19
 */
public class NoticeBean implements Serializable {

    /**
     * title : 商家奖励活动
     * id : 1531843200
     * content : 1.关注店铺： 1元/用户 奖励商家；
     * <p>
     * 2.用户首单： 满20元，商家奖励1元/单；
     * <p>
     * 3.取消活动： 满30奖励1元,满60奖励2元 截止日期：2018-07-20；
     * <p>
     * 4.限量赠送：原签约商家赠送升级永久版+小程序（永久版）请联系销售或客服；
     * <p>
     * 客服联系：4008-838-019
     * <p>
     * 5.推荐开店：点击分享赚钱，推荐商家开店可活动300元/店+300积分奖励；
     * <p>
     * 以上活动均可在商家后台查看情况并提现；
     * <p>
     * 活动最终解释权归微百姓（厦门）科技有限公司所有。
     */

    private String title;
    private String id;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

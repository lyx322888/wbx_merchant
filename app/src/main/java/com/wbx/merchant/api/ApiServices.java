package com.wbx.merchant.api;


import com.alibaba.fastjson.JSONObject;
import com.wbx.merchant.bean.BindAllUserBean;
import com.wbx.merchant.bean.BindUserBean;
import com.wbx.merchant.bean.CateBean;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.bean.GoodsDetailsInfo;
import com.wbx.merchant.bean.GradeInfoBean;
import com.wbx.merchant.bean.OpenRadarBean;
import com.wbx.merchant.bean.OrderBean;
import com.wbx.merchant.bean.ProprietaryGoodsBean;
import com.wbx.merchant.bean.ShopGradeInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiServices {
    //获取店铺等级
    @POST("/sjapi/apply/get_shop_grade")
    Observable<GradeInfoBean> getShopGrade();

    //获取店铺分类
    @FormUrlEncoded
    @POST("/sjapi/apply/get_shop_cate")
    Observable<CateBean> getShopCate(@Field("sj_login_token") String login_token, @Field("grade_id") int gradeId);

    //扫描雷达
    @FormUrlEncoded
    @POST("/sjapi/user/open_radar")
    Observable<OpenRadarBean> getRadar(@Field("sj_login_token") String login_token);

    //绑定用户
    @FormUrlEncoded
    @POST("/sjapi/user/bind_radar_user")
    Observable<BindUserBean> getBindUser(@Field("sj_login_token") String login_token, @Field("user_id") String user_id);

    //绑定全部用户
    @FormUrlEncoded
    @POST("/sjapi/user/bind_all_radar_user")
    Observable<BindAllUserBean> getBindAllUser(@Field("sj_login_token") String login_token);

    //自营商品列表
    @FormUrlEncoded
    @POST("/sjapi/interiorshop/list_goods")
    Observable<ProprietaryGoodsBean> getProprietaryGoods(@Field("sj_login_token") String login_token, @Field("page") int page, @Field("num") int num);

    @FormUrlEncoded
    @POST("/sjapi/interiorshop/get_goods_details")
    Observable<GoodsDetailsInfo> getGoodsDetails(@Field("sj_login_token") String login_token, @Field("goods_id") int goods_id);

    @FormUrlEncoded
    @POST("/sjapi/interiorshop/order")
    Observable<OrderBean> getOeder(@Field("sj_login_token") String login_token);


}

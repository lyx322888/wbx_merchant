package com.wbx.merchant.api;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * des:ApiService
 * Created by wushenghui on 2017/4/23.
 */
public interface ApiService {
    //获取版本号
    @FormUrlEncoded
    @POST("/sjapi/version/get_version_info")
    Observable<JSONObject> getVersion(@Field("app_type") String appType,@Field("version_code") String versionCode);

    //登录
    @FormUrlEncoded
    @POST("/sjapi/user/login")
    Observable<JSONObject> login(@FieldMap Map<String, Object> params);

    //退出
    @FormUrlEncoded
    @POST("/sjapi/user/logout")
    Observable<JSONObject> logout(@Field("sj_login_token") String loginToken, @Field("type") String type, @Field("registration_id") String registration_id);

    //注册
    @FormUrlEncoded
    @POST("/sjapi/user/register")
    Observable<JSONObject> register(@Field("mobile") String mobile, @Field("code") String code, @Field("password") String password);

    //发送验证码
    @FormUrlEncoded
    @POST("/sjapi/user/sendsms")
    Observable<JSONObject> sendCode(@Field("mobile") String mobile);

    //忘记密码
    @FormUrlEncoded
    @POST("/sjapi/user/forget_password")
    Observable<JSONObject> forgetPsw(@Field("mobile") String mobile, @Field("code") String code, @Field("new_password") String psw);

    //修改密码
    @FormUrlEncoded
    @POST("/sjapi/user/update_password")
    Observable<JSONObject> modifyPsw(@Field("sj_login_token") String loginToken, @Field("old_password") String oldPsw, @Field("new_password") String newPsw);

    //获取店铺分类
    @FormUrlEncoded
    @POST("/sjapi/apply/get_shop_cate")
    Observable<JSONObject> getShopCate(@Field("grade_id") int gradeId,@Field("type") String type );

    //获取店铺等级
    @POST("/sjapi/apply/get_shop_grade")
    Observable<JSONObject> getShopGrade();

    //获取城市商圈等数据
    @POST("/sjapi/apply/list_city")
    Observable<JSONObject> getCityList();

    //添加入驻信息
    @FormUrlEncoded
    @POST("/sjapi/apply/add_apply_info")
    Observable<JSONObject> addShopInfo(@FieldMap Map<String, Object> params);

    //添加入驻信息 开店宝版
    @FormUrlEncoded
    @POST("/sjapi/Kaidianbao/add_apply_info")
    Observable<JSONObject> add_apply_info(@FieldMap Map<String, Object> params);

    //版本选择信息 开店宝版
    @FormUrlEncoded
    @POST("/sjapi/Kaidianbao/get_shop_grade_info")
    Observable<JSONObject> get_shop_grade_info(@Field("sj_login_token") String loginToken);

    //版本选择信息 开店宝版
    @FormUrlEncoded
    @POST("/sjapi/Kaidianbao/get_shop_grade_info_two")
    Observable<JSONObject> get_shop_grade_info_two(@Field("sj_login_token") String loginToken);

    //支付码
    @FormUrlEncoded
    @POST("/sjapi/Kaidianbao/payment_code_pay")
    Observable<JSONObject> payment_code_pay(@FieldMap Map<String, Object> params);

    //设置店铺分类
    @FormUrlEncoded
    @POST("/sjapi/apply/update_shop_cate")
    Observable<JSONObject> upDateShopCate(@Field("sj_login_token") String loginToken, @Field("shop_id") int shopId, @Field("cate_id") String cateId);

    //支付
    @FormUrlEncoded
    @POST("/sjapi/apply/pay")
    Observable<JSONObject> goPay(@Field("sj_login_token") String loginToken, @Field("grade_id") int gradeId, @Field("code") String payCode, @Field("type") String payType, @Field("shop_grade") int shopGrade);

    //支付
    @FormUrlEncoded
    @POST("/sjapi/Kaidianbao/pay")
    Observable<JSONObject> goPayKdp(@Field("sj_login_token") String loginToken, @Field("code") String payCode, @Field("type") String payType, @Field("shop_grade") String shopGrade, @Field("grade_type") int gradeType);

    //获取服务到期时间
    @FormUrlEncoded
    @POST("/sjapi/apply/get_end_date")
    Observable<JSONObject> getServiceEndTime(@Field("sj_login_token") String loginToken, @Field("shop_id") int shopId);

    //获取首页数据
    @FormUrlEncoded
    @POST("/sjapi/user/get_shop_info")
    Observable<JSONObject> getShopInfo(@Field("sj_login_token") String loginToken);

    //添加资格认证
    @FormUrlEncoded
    @POST("/sjapi/user/add_shop_audit")
    Observable<JSONObject> addShopAudit(@FieldMap Map<String, Object> params);

    /**
     * 添加食品经营许可证
     */
    @FormUrlEncoded
    @POST("/sjapi/user/add_shop_hygiene_photo")
    Observable<JSONObject> addGoodsAccreditation(@Field("sj_login_token") String loginToken, @Field("hygiene_photo") String hygiene_photo, @Field("has_hygiene_photo") int has_hygiene_photo);

    /**
     * 查看许可证
     */
    @FormUrlEncoded
    @POST("/sjapi/user/get_shop_audit")
    Observable<JSONObject> getAccreditation(@Field("sj_login_token") String loginToken);

    //修改营业状态
    @FormUrlEncoded
    @POST("/sjapi/user/update_shop_is_open")
    Observable<JSONObject> updateOperatingState(@Field("sj_login_token") String loginToken, @Field("is_open") int isOpen);

    //获取商品列表
    @FormUrlEncoded
    @POST("/sjapi/goods/list_goods")
    Observable<JSONObject> goodsList(@FieldMap Map<String, Object> params);

    //发布商品
    @FormUrlEncoded
    @POST("/sjapi/goods/add_goods")
    Observable<JSONObject> releaseGoods(@FieldMap Map<String, Object> params);

    //获取分类列表数据
    @FormUrlEncoded
    @POST("/sjapi/goods/list_cate")
    Observable<JSONObject> getCateList(@Field("sj_login_token") String loginToken);

    //添加分类
    @FormUrlEncoded
    @POST("/sjapi/goods/add_cate")
    Observable<JSONObject> addCate(@FieldMap Map<String, Object> params);

    //删除分类
    @FormUrlEncoded
    @POST("/sjapi/goods/delete_cate")
    Observable<JSONObject> deleteCate(@Field("sj_login_token") String loginToken, @Field("cate_id") int cateId);

    //商品批量修改分类
    @FormUrlEncoded
    @POST("/sjapi/goods/move_batch_cate")
    Observable<JSONObject> goodsBatchClassify(@Field("sj_login_token") String loginToken, @Field("goods_ids") String goods_ids, @Field("cate_id") int cateId);

    //商品下架
    @FormUrlEncoded
    @POST("/sjapi/goods/goods_stop")
    Observable<JSONObject> soldOut(@FieldMap Map<String, Object> params);

    //商品上架
    @FormUrlEncoded
    @POST("/sjapi/goods/goods_start")
    Observable<JSONObject> soldUp(@FieldMap Map<String, Object> params);

    //商品删除
    @FormUrlEncoded
    @POST("/sjapi/goods/delete_goods")
    Observable<JSONObject> goodsDelete(@FieldMap Map<String, Object> params);

    //订单列表
    @FormUrlEncoded
    @POST("/sjapi/shoporder/list_order")
    Observable<JSONObject> orderList(@FieldMap Map<String, Object> params);

    //订单详情
    @FormUrlEncoded
    @POST("/sjapi/goods/delete_cate")
    Observable<JSONObject> orderDetail(@Field("sj_login_token") String loginToken, @Field("order_id") int orderId);

    //发货
    @FormUrlEncoded
    @POST("/sjapi/shoporder/send_goods")
    Observable<JSONObject> sendGoods(@Field("sj_login_token") String loginToken, @Field("order_id") int orderId);

    //拒绝接单
    @FormUrlEncoded
    @POST("/sjapi/shoporder/refuse_order")
    Observable<JSONObject> refuseOrder(@Field("sj_login_token") String loginToken, @Field("order_id") int orderId);

    //同意退款
    @FormUrlEncoded
    @POST("/sjapi/shoporder/agree_refund")
    Observable<JSONObject> agreeRefund(@Field("sj_login_token") String loginToken, @Field("order_id") int orderId);

    //拒绝退款
    @FormUrlEncoded
    @POST("/sjapi/shoporder/refuse_refund")
    Observable<JSONObject> refuseRefund(@Field("sj_login_token") String loginToken, @Field("order_id") int orderId);

    //申请提现信息
    @FormUrlEncoded
    @POST("/sjapi/user/get_cash_info")
    Observable<JSONObject> getCashInfo(@Field("sj_login_token") String loginToken);

    //收入管理信息
    @FormUrlEncoded
    @POST("/sjapi/user/get_cash_money")
    Observable<JSONObject> getIncomeInfo(@Field("sj_login_token") String loginToken);

    //修改支付密码
    @FormUrlEncoded
    @POST("/sjapi/user/update_pay_password")
    Observable<JSONObject> resetPayPassword(@Field("sj_login_token") String loginToken, @Field("code") String SMS, @Field("mobile") String mobile, @Field("new_pay_password") String newPsw);

    //申请提现
    @FormUrlEncoded
    @POST("/sjapi/user/add_cash_apply_new")
    Observable<JSONObject> applyCash(@FieldMap Map<String, Object> params);

     //邀请开店
    @FormUrlEncoded
    @POST("/sjapi/Merchantinvitation/merchant_invitation")
    Observable<JSONObject> invitation(@Field("sj_login_token") String loginToken);

    //获取店铺详情信息
    @FormUrlEncoded
    @POST("/sjapi/user/get_shop_detail")
    Observable<JSONObject> getShopDetail(@Field("sj_login_token") String loginToken);

    //更新店铺信息
    @FormUrlEncoded
    @POST("/sjapi/user/update_shop_detail")
    Observable<JSONObject> updateShopDetail(@FieldMap Map<String, Object> params);

    //设置促销商品
    @FormUrlEncoded
    @POST("/sjapi/goods/update_promotion")
    Observable<JSONObject> setPromotion(@FieldMap Map<String, Object> params);

    //查询提现记录
    @FormUrlEncoded
    @POST("/sjapi/user/list_cash_log")
    Observable<JSONObject> getCashRecord(@FieldMap Map<String, Object> params);

    //获取会员数量和会员商品数量
    @FormUrlEncoded
    @POST("/sjapi/shopmember/get_shop_member_goods_num")
    Observable<JSONObject> getShopMemberGoodsNum(@Field("sj_login_token") String loginToken);

    //获取我的会员列表
    @FormUrlEncoded
    @POST("/sjapi/user/customer_manage")
    Observable<JSONObject> getMyMemberList(@Field("sj_login_token") String loginToken, @Field("type") int type
            , @Field("page") int page, @Field("num") int num);

    //设置会员商品价商品
    @FormUrlEncoded
    @POST("/sjapi/shopmember/add_shop_member_goods")
    Observable<JSONObject> addMemberGoods(@Field("sj_login_token") String loginToken, @Field("goods") String goods);

    //获取会员商品列表
    @FormUrlEncoded
    @POST("/sjapi/shopmember/list_goods")
    Observable<JSONObject> getMemberGoodsList(@Field("sj_login_token") String loginToken);

    //添加与取消会员
    @FormUrlEncoded
    @POST("/sjapi/shopmember/update_shop_member")
    Observable<JSONObject> updateShopMember(@Field("sj_login_token") String loginToken, @Field("user_id") String user_id, @Field("closed") int closed);

    //会员条件设置
    @FormUrlEncoded
    @POST("/sjapi/shopmember/set_consumption_money")
    Observable<JSONObject> setMemberCondition(@Field("sj_login_token") String loginToken, @Field("consumption_money") String consumption_money, @Field("closed") int closed);

    //获取会员条件
    @FormUrlEncoded
    @POST("/sjapi/shopmember/get_consumption_money")
    Observable<JSONObject> getMemberCondition(@Field("sj_login_token") String loginToken);

    //获取会员详情
    @FormUrlEncoded
    @POST("/sjapi/shopmember/get_shop_member_details")
    Observable<JSONObject> getMemberDetail(@Field("user_id") String user_id, @Field("sj_login_token") String loginToken);

    //会员商品价商品列表
    @FormUrlEncoded
    @POST("/sjapi/shopmember/list_shop_member_goods")
    Observable<JSONObject> getMemberGoods(@Field("sj_login_token") String loginToken, @Field("page") int page, @Field("num") int num);

    //删除会员商品
    @FormUrlEncoded
    @POST("/sjapi/shopmember/delete_shop_member_goods")
    Observable<JSONObject> deleteMemberGoods(@Field("sj_login_token") String loginToken, @Field("goods") String goods);

    //编辑更新公告
    @FormUrlEncoded
    @POST("/sjapi/user/update_notice")
    Observable<JSONObject> updateNotice(@Field("sj_login_token") String loginToken, @Field("notice") String notice);

    //库存分析
    @FormUrlEncoded
    @POST("/sjapi/goods/list_goods_stock")
    Observable<JSONObject> inventoryAnalyze(@FieldMap Map<String, Object> params);

    //经营分析
    @FormUrlEncoded
    @POST("/sjapi/finance/list_manage_analyse")
    Observable<JSONObject> runAnalyze(@FieldMap Map<String, Object> params);

    //修改库存
    @FormUrlEncoded
    @POST("/sjapi/goods/update_goods_num")
    Observable<JSONObject> updateGoodsNum(@FieldMap Map<String, Object> params);

    //新增优惠券
    @FormUrlEncoded
    @POST("/sjapi/coupon/add_coupon")
    Observable<JSONObject> addCoupon(@FieldMap Map<String, Object> params);

    //优惠券列表
    @FormUrlEncoded
    @POST("/sjapi/coupon/list_coupon")
    Observable<JSONObject> getCouponList(@FieldMap Map<String, Object> params);

    //优惠券列表
    @FormUrlEncoded
    @POST("/sjapi/coupon/delete_coupon")
    Observable<JSONObject> deleteCoupon(@Field("sj_login_token") String loginToken, @Field("coupon_id") int couponId);

    //创建达达订单
    @FormUrlEncoded
    @POST("/sjapi/dada/create_order")
    Observable<JSONObject> sendByDaDa(@Field("sj_login_token") String loginToken, @Field("order_id") String orderId);

    //获取达达预估价格
    @FormUrlEncoded
    @POST("/sjapi/dada/dada_estimate_price")
    Observable<JSONObject> getDaDaEstimatePrice(@Field("sj_login_token") String loginToken, @Field("order_id") String orderId);

    //获得第三方配送订单
    @FormUrlEncoded
    @POST("/sjapi/dada/list_dispatching")
    Observable<JSONObject> getThirdOrderList(@Field("sj_login_token") String loginToken);

    //达达充值
    @FormUrlEncoded
    @POST("/sjapi/fengniao/recharge")
    Observable<JSONObject> daDaRecharge(@Field("sj_login_token") String loginToken, @Field("code") String payCode, @Field("money") double money);

    //达达提现
    @FormUrlEncoded
    @POST("/api/fengniao/add_cash_apply")
    Observable<JSONObject> daDaCash(@Field("sj_login_token") String loginToken, @Field("code") String payCode, @Field("money") double money);

    //上传商家视频
    @FormUrlEncoded
    @POST("/sjapi/user/update_video")
    Observable<JSONObject> upLoadVideo(@Field("sj_login_token") String loginToken, @Field("video") String video);

    //获取视频
    @FormUrlEncoded
    @POST("/sjapi/user/get_video")
    Observable<JSONObject> getVideo(@Field("sj_login_token") String loginToken);

    //获得满减活动数据
    @FormUrlEncoded
    @POST("/sjapi/user/list_full_money_reduce")
    Observable<JSONObject> getfull(@Field("sj_login_token") String loginToken);

    //添加满减活动数据
    @FormUrlEncoded
    @POST("/sjapi/user/full_money_reduce")
    Observable<JSONObject> addfull(@Field("sj_login_token") String loginToken, @Field("full_money_reduce") String json);


    //删除满减活动数据
    @FormUrlEncoded
    @POST("/sjapi/user/delete_full_money")
    Observable<JSONObject> delfull(@Field("sj_login_token") String loginToken, @Field("id") int id);

    //获取订单详情
    @FormUrlEncoded
    @POST("/sjapi/shoporder/get_order_detail")
    Observable<JSONObject> getThirdOrderDetail(@Field("sj_login_token") String loginToken, @Field("order_id") String id);

    //获取商品库类型
    @FormUrlEncoded
    @POST("/sjapi/productlibrary/list_menu")
    Observable<JSONObject> getproduct(@Field("sj_login_token") String loginToken);

    //获取素材列表
    @FormUrlEncoded
    @POST("/sjapi/productlibrary/list_product_new")
    Observable<JSONObject> getMaterialList(@Field("sj_login_token") String loginToken, @Field("product_cate_id") int id);

    //商品发布
    @FormUrlEncoded
    @POST("/sjapi/productlibrary/copy_product")
    Observable<JSONObject> getproductrelease(@Field("sj_login_token") String loginToken, @Field("products") String json);

    //添加支付宝信息
    @FormUrlEncoded
    @POST("/sjapi/withdraw/add_alipay")
    Observable<JSONObject> addAliPay(@Field("sj_login_token") String loginToken, @Field("alipay_account") String alipay_account,
                                     @Field("alipay_name") String weixinpay_name, @Field("mobile") String mobile,
                                     @Field("code") String code, @Field("widthdraw_id") String widthdraw_id);

    //添加微信信息
    @FormUrlEncoded
    @POST("/sjapi/withdraw/add_weixinpay")
    Observable<JSONObject> addWXPay(@Field("sj_login_token") String loginToken, @Field("nick_name") String nick_name,
                                    @Field("weixinpay_name") String weixinpay_name, @Field("openid") String openid, @Field("mobile") String mobile,
                                    @Field("code") String code, @Field("widthdraw_id") String widthdraw_id);

    //获取支付宝账号信息
    @FormUrlEncoded
    @POST("/sjapi/withdraw/get_alipay_info")
    Observable<JSONObject> getAliPayInfo(@Field("sj_login_token") String loginToken);

    //获取微信账号信息
    @FormUrlEncoded
    @POST("/sjapi/withdraw/get_weixinpay_info")
    Observable<JSONObject> getWXPayInfo(@Field("sj_login_token") String loginToken);

    //获取提现申请时需要的信息
    @FormUrlEncoded
    @POST("sjapi/user/get_cash_info")
    Observable<JSONObject> getBindPayInfo(@Field("sj_login_token") String loginToken,@Field("cash_type") String cashType);

    //获取提现申请时需要的信息
    @FormUrlEncoded
    @POST("/sjapi/withdraw/add_bank")
    Observable<JSONObject> addBank(@FieldMap Map<String, Object> params);

    //获取银行卡帐户信息
    @FormUrlEncoded
    @POST("/sjapi/withdraw/get_bank_info")
    Observable<JSONObject> getBankInfo(@Field("sj_login_token") String loginToken);

    // 获取余额日志
    @FormUrlEncoded
    @POST("/sjapi/user/list_yue_logs")
    Observable<JSONObject> getBalanceLog(@FieldMap Map<String, Object> params);

    // 添加/修改打印机
    @FormUrlEncoded
    @POST
    Observable<JSONObject> addPrinter(@Url String url, @FieldMap Map<String, Object> params);

    // 删除主打印机
    @FormUrlEncoded
    @POST("/sjapi/user/delete_print")
    Observable<JSONObject> deletePrinter(@Field("sj_login_token") String loginToken, @Field("print_brand") String print_brand);

    // 删除副打印机
    @FormUrlEncoded
    @POST("/sjapi/user/delete_assistant_print")
    Observable<JSONObject> deleteAssistantPrinter(@Field("sj_login_token") String loginToken, @Field("print_id") String print_id);

    // 获取打印机
    @FormUrlEncoded
    @POST("/sjapi/user/get_print_info")
    Observable<JSONObject> getPrinter(@Field("sj_login_token") String loginToken);

    //1.34.	获取打印机品牌
    @FormUrlEncoded
    @POST("/sjapi/user/get_print_brand")
    Observable<JSONObject> getPrintBrand(@Field("sj_login_token") String loginToken);

    // 获取打印菜单
    @FormUrlEncoded
    @POST("/sjapi/goods/list_print_goods_cate")
    Observable<JSONObject> getPrintGoodsCate(@Field("sj_login_token") String loginToken, @Field("print_id") String print_id);

    //获取活动管理信息
    @FormUrlEncoded
    @POST("/sjapi/goods/get_activity")
    Observable<JSONObject> getActivitySettingsInfo(@Field("sj_login_token") String loginToken);

    //修改活动管理信息
    @FormUrlEncoded
    @POST("/sjapi/goods/update_activity")
    Observable<JSONObject> updateActivityInfo(@FieldMap Map<String, Object> params);

    //获取版本等级 通用版/旗舰版
    @FormUrlEncoded
    @POST("/sjapi/applyh5/service_fee")
    Observable<JSONObject> getShopVersionInfo(@Field("grade_id") int gradeId);

    //设置店铺版本
    @FormUrlEncoded
    @POST("/sjapi/apply/update_shop_shop_grade")
    Observable<JSONObject> settingsShopGrade(@Field("sj_login_token") String loginToken, @Field("shop_grade") int gradeId);

    //添加电子合同信息
    @FormUrlEncoded
    @POST("/sjapi/apply/add_shop_pacit")
    Observable<JSONObject> addShopContract(@Field("sj_login_token") String loginToken, @Field("identity_card_front") String card_head_image,
                                           @Field("identity_card_con") String card_back_image, @Field("identity_card_no") String id_card_num, @Field("name") String name);

    //电子签名图片提交
    @FormUrlEncoded
    @POST("/sjapi/apply/add_sign_photo")
    Observable<JSONObject> addSignPhoto(@Field("sj_login_token") String loginToken, @Field("sign_photo") String sign_photo);

    //获取座位列表
    @FormUrlEncoded
    @POST("/sjapi/user/list_table_number")
    Observable<JSONObject> getSeatInfo(@Field("sj_login_token") String loginToken);

    //添加座位
    @FormUrlEncoded
    @POST("/sjapi/user/add_table_number")
    Observable<JSONObject> addSeat(@FieldMap Map<String, Object> params);

    //删除座位
    @FormUrlEncoded
    @POST("/sjapi/user/delete_table_number")
    Observable<JSONObject> deleteSeat(@Field("sj_login_token") String loginToken, @Field("id") String id);

    //获取秒杀商品列表
    @FormUrlEncoded
    @POST("/sjapi/seckillgoods/list_goods")
    Observable<JSONObject> getSecKillGoodsList(@FieldMap Map<String, Object> params);

    //添加秒杀商品
    @FormUrlEncoded
    @POST("/sjapi/seckillgoods/add_seckill_goods")
    Observable<JSONObject> addSecKillGoods(@FieldMap Map<String, Object> params);

    //取消秒杀
    @FormUrlEncoded
    @POST("/sjapi/seckillgoods/delete_seckill_goods")
    Observable<JSONObject> cancelSecKillGoods(@Field("sj_login_token") String loginToken, @Field("goods_id") int goodsId);

    //预定订单列表
    @FormUrlEncoded
    @POST("/sjapi/subscribe/list_subscribe")
    Observable<JSONObject> getBookSeatList(@FieldMap Map<String, Object> params);

    //预定订单 拒单
    @FormUrlEncoded
    @POST("/sjapi/subscribe/refuse_subscribe")
    Observable<JSONObject> refuseBookSeat(@Field("sj_login_token") String loginToken, @Field("reserve_table_id") int seatId);

    //接单分配座位
    @FormUrlEncoded
    @POST("/sjapi/subscribe/allot_seat")
    Observable<JSONObject> receiveBookSeat(@Field("sj_login_token") String loginToken, @Field("reserve_table_id") int seatId, @Field("id") int id);

    //10.3.获取待退款的预定
    @FormUrlEncoded
    @POST("/sjapi/subscribe/list_refunds_pending_subscribe")
    Observable<JSONObject> getBookSeatRefund(@FieldMap Map<String, Object> params);

    //10.6.同意退款
    @FormUrlEncoded
    @POST("/sjapi/subscribe/agree_refund")
    Observable<JSONObject> bookSeatRefundAgree(@Field("sj_login_token") String loginToken, @Field("reserve_table_id") int seatId);

    //10.7.拒绝退款
    @FormUrlEncoded
    @POST("/sjapi/subscribe/refuse_refund")
    Observable<JSONObject> bookSeatRefundRefuse(@Field("sj_login_token") String loginToken, @Field("reserve_table_id") int seatId);

    //10.1.预定功能的开发与定金设置
    @FormUrlEncoded
    @POST("/sjapi/subscribe/update_subscribe")
    Observable<JSONObject> openOrCloseBook(@Field("sj_login_token") String loginToken, @Field("is_subscribe") int isBook, @Field("subscribe_money") String price);


    //根据关键词显示对应问题标题
    @FormUrlEncoded
    @POST("/api/airobot/list_question_title")
    Observable<JSONObject> getQuestion(@Field("keyword") String keyword);

    //根据问题id获取问题的答案
    @FormUrlEncoded
    @POST("/api/airobot/get_question_answer")
    Observable<JSONObject> getAnswer(@Field("question_id") int id);

    //常见问题
    @POST("/api/airobot/list_often_question")
    Observable<JSONObject> getOftenQuestion();

    //入驻 获取城市地区
    @POST("/sjapi/apply/list_province_city")
    Observable<JSONObject> getJoinSelectAddress();

    //获取商家补贴
    @FormUrlEncoded
    @POST("/sjapi/subsidyincentive/list_subsidy_incentive")
    Observable<JSONObject> getMerchantSubsidy(@Field("sj_login_token") String loginToken, @Field("page") int page, @Field("num") int num);

    //智能支付列表
    @FormUrlEncoded
    @POST("sjapi/user/list_noopstchepay")
    Observable<JSONObject> getIntelligentReceiveList(@Field("sj_login_token") String login_token, @Field("page") int page, @Field("num") int num);

    //领取达达金额补贴
    @FormUrlEncoded
    @POST("/sjapi/fengniao/gain_fengniao_money")
    Observable<JSONObject> gainDaDaMoney(@Field("sj_login_token") String login_token);

    //扫码订单列表
    @FormUrlEncoded
    @POST("/sjapi/scanorder/scan_order_list")
    Observable<JSONObject> getScanOrderList(@Field("sj_login_token") String login_token, @Field("status") int status, @Field("page") int page, @Field("num") int num);

    //扫码订单详情
    @FormUrlEncoded
    @POST("/sjapi/scanorder/scan_order_detail")
    Observable<JSONObject> getScanOrderDetail(@Field("sj_login_token") String login_token, @Field("out_trade_no") String out_trade_no);

    //删除扫码订单
    @FormUrlEncoded
    @POST("/sjapi/scanorder/delete_order")
    Observable<JSONObject> delScanOrder(@Field("sj_login_token") String login_token, @Field("out_trade_no") String out_trade_no);

    //现金支付
    @FormUrlEncoded
    @POST("/sjapi/scanorder/cash_pay")
    Observable<JSONObject> cashPay(@Field("sj_login_token") String login_token, @Field("out_trade_no") String out_trade_no);

    //打印订单
    @FormUrlEncoded
    @POST("/sjapi/scanorder/print_order")
    Observable<JSONObject> printScanOrder(@Field("sj_login_token") String login_token, @Field("out_trade_no") String out_trade_no);

    //增加菜的数量
    @FormUrlEncoded
    @POST("/sjapi/scanorder/add_food_num")
    Observable<JSONObject> addFoodNum(@Field("sj_login_token") String login_token, @Field("out_trade_no") String out_trade_no, @Field("goods") String goods);

    //减去菜的数量
    @FormUrlEncoded
    @POST("/sjapi/scanorder/minus_food_num")
    Observable<JSONObject> reduceFoodNum(@Field("sj_login_token") String login_token, @Field("out_trade_no") String out_trade_no, @Field("goods") String goods);

    //加菜
    @FormUrlEncoded
    @POST("/sjapi/scanorder/add_food")
    Observable<JSONObject> addFood(@Field("sj_login_token") String login_token, @Field("out_trade_no") String out_trade_no, @Field("goods") String goods);

    //加菜
    @FormUrlEncoded
    @POST("/sjapi/scanorder/list_goods")
    Observable<JSONObject> getScanOrderGoodsList(@Field("sj_login_token") String login_token);

    /**
     * 获取百度客服聊天链接
     */
    @FormUrlEncoded
    @POST("/api/airobot/get_baidutim_url")
    Observable<JSONObject> getBaiduTimUrl(@Field("type") int type);

    /**
     * 添加发现内容
     */
    @FormUrlEncoded
    @POST("/sjapi/discover/add_discover")
    Observable<JSONObject> addDiscovery(@Field("sj_login_token") String login_token, @Field("text") String text, @Field("photos") String photos);

    /**
     * 发现内容列表
     */
    @FormUrlEncoded
    @POST("/sjapi/discover/list_discover")
    Observable<JSONObject> getDiscoveryList(@Field("sj_login_token") String login_token);

    /**
     * 删除发现内容
     */
    @FormUrlEncoded
    @POST("/sjapi/discover/delete_discover")
    Observable<JSONObject> deleteDiscovery(@Field("sj_login_token") String login_token, @Field("discover_ids") int discover_ids);

    /**
     * 获取商家最后一次设置的红包
     */
    @FormUrlEncoded
    @POST("/sjapi/redpacket/get_end_red_packet")
    Observable<JSONObject> getLastRedPacket(@Field("sj_login_token") String login_token);

    /**
     * 获取商家最后一次设置的红包
     */
    @FormUrlEncoded
    @POST("/sjapi/redpacket/add_red_packet")
    Observable<JSONObject> addRedPacket(@Field("sj_login_token") String login_token, @Field("money") String money, @Field("min_money") String min_money, @Field("max_money") String max_money);

    /**
     * 修改商家红包
     */
    @FormUrlEncoded
    @POST("/sjapi/redpacket/update_red_packet")
    Observable<JSONObject> updateRedPacket(@Field("sj_login_token") String login_token, @Field("money") String money, @Field("min_money") String min_money, @Field("max_money") String max_money, @Field("red_packet_id") String red_packet_id);

    /**
     * 获取通知内容
     */
    @FormUrlEncoded
    @POST("/sjapi/message/get_message")
    Observable<JSONObject> getNotice(@Field("sj_login_token") String login_token);

    /**
     * 更新营业时间段
     */
    @FormUrlEncoded
    @POST("/sjapi/businesstime/update_business_time")
    Observable<JSONObject> updateBusinessTime(@Field("sj_login_token") String login_token, @Field("business_time") String business_time);

    /**
     * 商家红包领取列表
     */
    @FormUrlEncoded
    @POST("/sjapi/redpacket/list_red_packet")
    Observable<JSONObject> redPacketList(@Field("sj_login_token") String login_token, @Field("is_use") int is_use, @Field("page") int page, @Field("num") int num);

    /**
     * 更新扫码订单类型
     */
    @FormUrlEncoded
    @POST("/sjapi/scanorder/update_scan_order_type")
    Observable<JSONObject> updateScanOrderType(@Field("sj_login_token") String login_token, @Field("scan_order_type") int scan_order_type);

    /**
     * 获取取餐码订单
     */
    @FormUrlEncoded
    @POST("/sjapi/takenumberorder/list_take_number_order")
    Observable<JSONObject> getNumberOrderList(@Field("sj_login_token") String login_token, @Field("page") int page, @Field("num") int num);

    /**
     * 获取取餐码设置信息
     */
    @FormUrlEncoded
    @POST("/sjapi/takenumberorder/get_take_number")
    Observable<JSONObject> getNumberOrderSetting(@Field("sj_login_token") String login_token);

    /**
     * 设置取餐码信息
     */
    @FormUrlEncoded
    @POST("/sjapi/takenumberorder/update_take_number")
    Observable<JSONObject> setNumberOrder(@Field("sj_login_token") String login_token, @Field("take_number_prefix") String take_number_prefix, @Field("take_number_start") String take_number_start, @Field("is_take_number") int is_take_number);

    /**
     * 删除叫号订单
     */
    @FormUrlEncoded
    @POST("/sjapi/shoporder/delete_order")
    Observable<JSONObject> deleteNumberOrder(@Field("sj_login_token") String login_token, @Field("order_id") String order_id);

    /**
     * 设置默认提现方式
     */
    @FormUrlEncoded
    @POST("/sjapi/user/set_default_cash_type")
    Observable<JSONObject> setDefaultCashType(@Field("sj_login_token") String login_token, @Field("pay_code") String pay_code);

    /**
     * 修改超出配送距离配送费
     */
    @FormUrlEncoded
    @POST("/sjapi/user/update_distance_logistics")
    Observable<JSONObject> updateDistanceSendFee(@Field("sj_login_token") String login_token, @Field("peisong_fanwei") String peisong_fanwei, @Field("since_money") float since_money, @Field("logistics") float logistics, @Field("every_exceed_kilometre") int every_exceed_kilometre, @Field("exceed_every_kilometre_logistics") float exceed_every_kilometre_logistics);

    /**
     * 获取规格项目或属性列表
     */
    @FormUrlEncoded
    @POST("/sjapi/goods/list_nature")
    Observable<JSONObject> getNatureList(@Field("sj_login_token") String login_token, @Field("level") int level, @Field("pid") String pid, @Field("page") int page, @Field("num") int num);

    /**
     * 新增规格项目或属性
     */
    @FormUrlEncoded
    @POST("/sjapi/goods/add_nature")
    Observable<JSONObject> addNature(@Field("sj_login_token") String login_token, @Field("level") int level, @Field("nature_name") String nature_name, @Field("pid") String pid);

    /**
     * 修改规格项目
     */
    @FormUrlEncoded
    @POST("/sjapi/goods/update_nature")
    Observable<JSONObject> updateNature(@Field("sj_login_token") String login_token, @Field("nature_name") String nature_name, @Field("nature_id") String nature_id);

    /**
     * 删除规格项目或属性
     */
    @FormUrlEncoded
    @POST("/sjapi/goods/delete_nature")
    Observable<JSONObject> deleteNature(@Field("sj_login_token") String login_token, @Field("nature_id") String nature_id);

    /**
     * 打印订单
     */
    @FormUrlEncoded
    @POST("/sjapi/shoporder/print_order")
    Observable<JSONObject> printOrder(@Field("sj_login_token") String login_token, @Field("order_id") String order_id);

    /**
     * 获取达达取消原因
     */
    @FormUrlEncoded
    @POST("/sjapi/dada/get_order_cancel_reasons")
    Observable<JSONObject> getDaDaCancelReasons(@Field("sj_login_token") String login_token);

    /**
     * 确认取消达达
     */
    @FormUrlEncoded
    @POST("/sjapi/dada/cancel_order")
    Observable<JSONObject> cancelDaDaOrder(@Field("sj_login_token") String login_token, @Field("order_id") String order_id
            , @Field("reason_id") String reason_id, @Field("cancel_reason") String cancel_reason);

    /**
     * 获取消费免单的商品
     */
    @FormUrlEncoded
    @POST("/sjapi/consumefreegoods/list_consume_free_goods")
    Observable<JSONObject> getConsumeFreeGoodsList(@Field("sj_login_token") String login_token, @Field("page") int page, @Field("num") int num);

    /**
     * 获取积累免单的商品
     */
    @FormUrlEncoded
    @POST("/sjapi/accumulatefreegoods/list_accumulate_free_goods")
    Observable<JSONObject> getIntegralFreeGoodsList(@Field("sj_login_token") String login_token, @Field("page") int page, @Field("num") int num);

    /**
     * 删除免单商品
     */
    @FormUrlEncoded
    @POST("/sjapi/freegoods/delete_free_goods")
    Observable<JSONObject> deleteFreeGoods(@Field("sj_login_token") String login_token, @Field("goods_id") String goodsId, @Field("free_goods_type") String types);

    /**
     * 获取可以进行免单活动的商品
     */
    @FormUrlEncoded
    @POST("/sjapi/freegoods/list_goods")
    Observable<JSONObject> getFreeActivityGoodsList(@Field("sj_login_token") String login_token, @Field("page") int page, @Field("num") int num, @Field("free_goods_type") String free_goods_type);

    /**
     * 搜索可以进行免单活动的商品
     */
    @FormUrlEncoded
    @POST("/sjapi/freegoods/search_goods")
    Observable<JSONObject> searchFreeActivityGoodsList(@Field("sj_login_token") String login_token, @Field("keyword") String keyword, @Field("free_goods_type") String free_goods_type);

    /**
     * 添加消费免单的商品
     */
    @FormUrlEncoded
    @POST("/sjapi/consumefreegoods/add_consume_free_goods")
    Observable<JSONObject> addConsumeFreeGoods(@Field("sj_login_token") String login_token, @Field("consume_free_num") int consume_free_num, @Field("consume_free_amount") int consume_free_amount, @Field("consume_free_price") float consume_free_price, @Field("consume_free_duration") long consume_free_duration, @Field("goods_id") String goods_id);

    /**
     * 添加积累免单的商品
     */
    @FormUrlEncoded
    @POST("/sjapi/accumulatefreegoods/add_accumulate_free_goods")
    Observable<JSONObject> addIntegralFreeGoods(@Field("sj_login_token") String login_token, @Field("accumulate_free_need_num") int accumulate_free_need_num, @Field("accumulate_free_get_num") int accumulate_free_get_num, @Field("goods_id") String goods_id);

    /**
     * 获取分享免单的商品
     */
    @FormUrlEncoded
    @POST("/sjapi/sharefreegoods/list_share_free_goods")
    Observable<JSONObject> getShareFreeGoodsList(@Field("sj_login_token") String login_token, @Field("page") int page, @Field("num") int num);

    /**
     * 添加分享免单的商品
     */
    @FormUrlEncoded
    @POST("/sjapi/sharefreegoods/add_share_free_goods")
    Observable<JSONObject> addShareFreeGoods(@Field("sj_login_token") String login_token, @Field("share_free_num") int share_free_num, @Field("share_free_amount") int share_free_amount, @Field("share_free_duration") long share_free_duration, @Field("goods_id") String goods_id);

    /**
     * 26.3.	修改数量
     *
     * @param login_token
     * @param goods_id    商品id
     * @param type        plus加  minus减
     * @return
     */
    @FormUrlEncoded
    @POST("/sjapi/interiorshop/update_order_num")
    Observable<JSONObject> getUpdate(@Field("sj_login_token") String login_token, @Field("goods_id") int goods_id, @Field("type") String type);

    @FormUrlEncoded
    @POST("/sjapi/interiorshop/edit_order_info")
    Observable<JSONObject> getOrderInfo(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("/sjapi/interiorshop/pay")
    Observable<JSONObject> getOrderPay(@Field("sj_login_token") String login_token, @Field("order_id") String order_id, @Field("code") String code);

    @FormUrlEncoded
    @POST("/sjapi/interiorshop/list_order")
    Observable<JSONObject> getListOrder(@Field("sj_login_token") String login_token, @Field("page") int page, @Field("num") int num);

    //1.40.	设置满多少免配送费
    @FormUrlEncoded
    @POST("/sjapi/user/update_full_minus_shipping_fee")
    Observable<JSONObject> getNoDeliveryFee(@Field("sj_login_token") String login_token, @Field("is_full_minus_shipping_fee") int is_check, @Field("full_minus_shipping_fee") String money);

    //27.1.	特供商品列表
    @FormUrlEncoded
    @POST("/sjapi/Specialsupplygoods/list_special_supply_goods")
    Observable<JSONObject> getSpecialSupply(@Field("sj_login_token") String login_token);

    //27.2.	一键发布
    @FormUrlEncoded
    @POST("/sjapi/Specialsupplygoods/send_goods")
    Observable<JSONObject> sendSpecial(@Field("sj_login_token") String login_token, @Field("goods_ids") String goodIDs);


    //获取评价信息
    @FormUrlEncoded
    @POST("/sjapi/user/get_shop_salesman_comment")
    Observable<JSONObject> getSalesmanCommentInfo(@Field("sj_login_token") String login_token,@Field("type") String type);

  //添加评价信息
    @FormUrlEncoded
    @POST("/sjapi/user/add_shop_salesman_comment")
    Observable<JSONObject> addSalesmanComment(@FieldMap Map<String, Object> params);

    //满金额免配送费
    @FormUrlEncoded
    @POST("/sjapi/user/get_full_minus_shipping_fee")
    Observable<JSONObject> getShippingFeeInfo(@Field("sj_login_token") String login_token);

    //三公里内的社区
    @FormUrlEncoded
    @POST("/sjapi/apply/list_city_community")
    Observable<JSONObject> getcitycommunity(@FieldMap Map<String,Object> params);

    //商家推荐列表
    @FormUrlEncoded
    @POST("/sjapi/recommendgoods/list_goods")
    Observable<JSONObject> getRecommend_goods(@FieldMap Map<String,Object> params);

    //推荐商家商品
    @FormUrlEncoded
    @POST("/sjapi/recommendgoods/update_is_recommend")
    Observable<JSONObject> getUpdateRecommned(@FieldMap Map<String,Object> params);

    //删除推荐商家商品
    @FormUrlEncoded
    @POST("/sjapi/recommendgoods/delete_is_recommend")
    Observable<JSONObject> getDeleteRecommned(@FieldMap Map<String,Object> params);

    //订单角标
    @FormUrlEncoded
    @POST("/sjapi/shoporder/count_order")
    Observable<JSONObject> getCountOrder(@FieldMap Map<String,Object> params);

    //获取礼包
    @FormUrlEncoded
    @POST("/sjapi/giftbag/get_gift_bag")
    Observable<JSONObject> getGiftBag(@FieldMap Map<String,Object> params);

    //添加libao红包
    @FormUrlEncoded
    @POST("/sjapi/giftbag/add_red_packet")
    Observable<JSONObject> addRedPacket(@FieldMap Map<String,Object> params);

    //添加赠品
    @FormUrlEncoded
    @POST("/sjapi/giftbag/add_give_goods")
    Observable<JSONObject> addGiveGoods(@FieldMap Map<String,Object> params);

    //删除红包
    @FormUrlEncoded
    @POST("/sjapi/giftbag/delete_red_packet")
    Observable<JSONObject> deleteRedPacket(@FieldMap Map<String,Object> params);

    //删除赠品
    @FormUrlEncoded
    @POST("/sjapi/giftbag/delete_give_goods")
    Observable<JSONObject> deleteGiveGoods(@FieldMap Map<String,Object> params);

    //发送礼包
    @FormUrlEncoded
    @POST("/sjapi/giftbag/affirm_gift_bag")
    Observable<JSONObject> affirmGiftBag(@FieldMap Map<String,Object> params);

    //选择赠送商品
    @FormUrlEncoded
    @POST("/sjapi/giftbag/list_give_goods")
    Observable<JSONObject> listGiveGoods(@FieldMap Map<String,Object> params);

    // 奖励明细
    @FormUrlEncoded
    @POST("/sjapi/Merchantinvitation/list_share_user")
    Observable<JSONObject> listShareUser(@Field("sj_login_token") String login_token);

    // 登录判断是否已经付过款
    @FormUrlEncoded
    @POST("/sjapi/apply/get_shop_is_pay")
    Observable<JSONObject> getShopIsPay(@Field("sj_login_token") String login_token);

    //分享店铺信息
    @FormUrlEncoded
    @POST("/sjapi/user/share_shop")
    Observable<JSONObject> shareShop(@Field("sj_login_token") String login_token);

    //排行榜
    @FormUrlEncoded
    @POST("/sjapi/Merchantinvitation/ranking_list")
    Observable<JSONObject> ranKingList(@Field("sj_login_token") String login_token);
    //状态切换
    @FormUrlEncoded
    @POST("/sjapi/Kaidianbao/update_is_open")
    Observable<JSONObject> updateIsOpen(@Field("sj_login_token") String login_token,@Field("is_open") int is_open);
    //获取状态
    @FormUrlEncoded
    @POST("/sjapi/Kaidianbao/get_shop_identity")
    Observable<JSONObject> getShopIdentity(@Field("sj_login_token") String login_token);

    //添加店铺审核信息
    @FormUrlEncoded
    @POST("/sjapi/Kaidianbao/add_shop_identity")
    Observable<JSONObject> addShopIdentity(@FieldMap Map<String,Object> params);

    //活动管理
    @FormUrlEncoded
    @POST("/sjapi/Shopactivity/get_shop_activity_v2")
    Observable<JSONObject> getShopActivity(@Field("sj_login_token") String login_token);

    //是否退出登录
    @FormUrlEncoded
    @POST("/sjapi/user/get_need_login")
    Observable<JSONObject> getNeedLogin(@Field("sj_login_token") String login_token,@Field("version_code") String versionCode);

    //行业类型
    @FormUrlEncoded
    @POST("/sjapi/duolabao/list_industry")
    Observable<JSONObject> list_industry(@Field("sj_login_token") String login_token);

    //二级行业
    @FormUrlEncoded
    @POST("/sjapi/duolabao/list_industry_second")
    Observable<JSONObject> list_industry_second(@Field("sj_login_token") String login_token,@Field("industryNum") String industryNum);

    //京东信息提交
    @FormUrlEncoded
    @POST("/sjapi/duolabao/add_customerinfo")
    Observable<JSONObject> add_customerinfo(@FieldMap Map<String,Object> params);

    //京东信息修改
    @FormUrlEncoded
    @POST("/sjapi/duolabao/update_customerinfo")
    Observable<JSONObject> update_customerinfo(@FieldMap Map<String,Object> params);

    //订单列表
    @FormUrlEncoded
    @POST("/sjapi/duolabao/list_order")
    Observable<JSONObject> list_order(@FieldMap Map<String,Object> params);

    //银行列表
    @FormUrlEncoded
    @POST("/sjapi/duolabao/list_bank")
    Observable<JSONObject> list_bank(@Field("sj_login_token") String sj_login_token,@Field("keyword") String keyword);

    //支行列表
    @FormUrlEncoded
    @POST("/sjapi/duolabao/list_bankSub")
    Observable<JSONObject> list_bankSub(@Field("sj_login_token") String sj_login_token,@Field("keyword") String keyword,@Field("bankCode") String bankCode);

     //获取省份
    @FormUrlEncoded
    @POST("/sjapi/duolabao/list_province")
    Observable<JSONObject> list_province(@Field("sj_login_token") String sj_login_token);

    //获取城市
    @FormUrlEncoded
    @POST("/sjapi/duolabao/list_city")
    Observable<JSONObject> list_city(@Field("sj_login_token") String sj_login_token,@Field("code") String code);

    //支付银行
    @FormUrlEncoded
    @POST("/sjapi/duolabao/list_bankinfo")
    Observable<JSONObject> list_bankinfo(@Field("sj_login_token") String sj_login_token);

    //设置结算信息
    @FormUrlEncoded
    @POST("/sjapi/duolabao/add_customerinfo_declare")
    Observable<JSONObject> add_customerinfo_declare(@FieldMap Map<String,Object> params);

    //店铺信息
    @FormUrlEncoded
    @POST("/sjapi/duolabao/add_declare_shopinfo")
    Observable<JSONObject> create(@FieldMap Map<String,Object> params);

    //图片上传
    @FormUrlEncoded
    @POST("/sjapi/duolabao/upload_attach")
    Observable<JSONObject> upload_attach(@Field("sj_login_token") String sj_login_token,@Field("attachType") String attachType,@Field("file") String file);

    //提交报单
    @FormUrlEncoded
    @POST("/sjapi/duolabao/declare_complete")
    Observable<JSONObject> declare_complete(@Field("sj_login_token") String sj_login_token);

    //确认报单
    @FormUrlEncoded
    @POST("/sjapi/duolabao/declare_complete_confirm")
    Observable<JSONObject> declare_complete_confirm(@Field("sj_login_token") String sj_login_token);

    //获取附件
    @FormUrlEncoded
    @POST("/sjapi/duolabao/get_attach")
    Observable<JSONObject> get_attach(@Field("sj_login_token") String sj_login_token);

    //修改附件
    @FormUrlEncoded
    @POST("/sjapi/duolabao/update_attach")
    Observable<JSONObject> update_attach(@Field("sj_login_token") String sj_login_token,@Field("attachType") String attachType,@Field("file") String file,@Field("attachNum") String attachNum);

    //获取单个附件
    @FormUrlEncoded
    @POST("/sjapi/duolabao/get_attach_one")
    Observable<JSONObject> get_attach_one(@Field("sj_login_token") String sj_login_token,@Field("attachType") String attachType);

    //行业类型
    @FormUrlEncoded
    @POST("/sjapi/Kaidianbao/get_shop_grade_v2")
    Observable<JSONObject> get_shop_grade(@Field("sj_login_token") String sj_login_token,@Field("shop_edition") int shop_edition);

    //区域
    @FormUrlEncoded
    @POST("/sjapi/duolabao/list_district")
    Observable<JSONObject> list_district(@Field("sj_login_token") String sj_login_token,@Field("code") String code);


    //获取配送管理
    @FormUrlEncoded
    @POST("/sjapi/user/get_delivery_manage")
    Observable<JSONObject> get_delivery_manage(@Field("sj_login_token") String sj_login_token);

    //配送管理
    @FormUrlEncoded
    @POST("/sjapi/user/update_delivery_manage")
    Observable<JSONObject> update_delivery_manage(@FieldMap Map<String,Object> params);


    //获取商户信息
    @FormUrlEncoded
    @POST("/sjapi/duolabao/get_customerinfo")
    Observable<JSONObject> get_customerinfo(@Field("sj_login_token") String sj_login_token);

    //获取审核结果
    @FormUrlEncoded
    @POST("/sjapi/duolabao/get_audit_result")
    Observable<JSONObject> get_audit_result(@Field("sj_login_token") String sj_login_token);

    //获取结算信息
    @FormUrlEncoded
    @POST("/sjapi/duolabao/get_customerinfo_declare")
    Observable<JSONObject> get_customerinfo_declare(@Field("sj_login_token") String sj_login_token);

    //获取结算信息
    @FormUrlEncoded
    @POST("/sjapi/duolabao/update_customerinfo_declare")
    Observable<JSONObject> update_customerinfo_declare(@FieldMap Map<String,Object> params);


    //获取店铺信息
    @FormUrlEncoded
    @POST("/sjapi/duolabao/list_shopinfo")
    Observable<JSONObject> list_shopinfo(@Field("sj_login_token") String sj_login_token);


    //修改店铺信息
    @FormUrlEncoded
    @POST("/sjapi/duolabao/update_declare_shopinfo")
    Observable<JSONObject> update_declare_shopinfo(@FieldMap Map<String,Object> params);

    //获取红包码
    @FormUrlEncoded
    @POST("/sjapi/duolabao/get_red_packet_code")
    Observable<JSONObject> get_red_packet_code(@Field("sj_login_token") String sj_login_token);


    //充值
    @FormUrlEncoded
    @POST("/sjapi/discover/recharge")
    Observable<JSONObject> recharge(@Field("sj_login_token") String sj_login_token,@Field("code") String code,@Field("money") String money);


    //红包码
    @FormUrlEncoded
    @POST("/sjapi/duolabao/add_red_packet_code")
    Observable<JSONObject> add_red_packet_code(@Field("sj_login_token") String sj_login_token,@Field("red_packet_code") String red_packet_code);

    //圈粉充值明细
    @FormUrlEncoded
    @POST("/sjapi/discover/list_draw_fans_money_log")
    Observable<JSONObject> list_draw_fans_money_log(@FieldMap Map<String,Object> params);

    //圈粉
    @FormUrlEncoded
    @POST("/sjapi/discover/get_draw_fans_money")
    Observable<JSONObject> get_draw_fans_money(@Field("sj_login_token") String sj_login_token);

    //圈粉引流info
    @FormUrlEncoded
    @POST("/sjapi/discover/get_draw_fans_info")
    Observable<JSONObject> get_draw_fans_info(@Field("sj_login_token") String sj_login_token);

    //圈粉引流
    @FormUrlEncoded
    @POST("/sjapi/discover/add_discover_v2")
    Observable<JSONObject> add_discove_v2(@FieldMap Map<String,Object> params);

    //圈粉引流
    @FormUrlEncoded
    @POST("/sjapi/discover/update_discover_draw_fans_settings")
    Observable<JSONObject> update_discover_draw_fans_settings(@FieldMap Map<String,Object> params);

    //充值
    @FormUrlEncoded
    @POST("/sjapi/discover/pay")
    Observable<JSONObject> pay(@Field("sj_login_token") String sj_login_token,@Field("code") String code,@Field("money") String money,@Field("discover_id") String discover_id,@Field("pay_password") String pay_password);

    //到店套餐商品选择列表
    @FormUrlEncoded
    @POST("/sjapi/Shopsetmeal/list_goods")
    Observable<JSONObject> list_goods(@FieldMap Map<String,Object> params);

    //添加到店套餐
    @FormUrlEncoded
    @POST("/sjapi/shopsetmeal/add_shop_set_meal")
    Observable<JSONObject> add_shop_set_meal(@FieldMap Map<String,Object> params);

    //到店套餐列表
    @FormUrlEncoded
    @POST("/sjapi/shopsetmeal/list_shop_set_meal")
    Observable<JSONObject> list_shop_set_meal(@FieldMap Map<String,Object> params);



    //到店套餐
    @FormUrlEncoded
    @POST("/sjapi/shopsetmeal/delete_shop_set_meal")
    Observable<JSONObject> delete_shop_set_meal(@Field("sj_login_token") String sj_login_token,@Field("shop_set_meal_id") String shop_set_meal_id);

    //到店套餐
    @FormUrlEncoded
    @POST("/sjapi/shopsetmeal/pause_shop_set_meal")
    Observable<JSONObject> pause_shop_set_meal(@Field("sj_login_token") String sj_login_token,@Field("shop_set_meal_id") String shop_set_meal_id,@Field("is_pause") String is_pause);

    //33.7.	套餐出售使用统计
    @FormUrlEncoded
    @POST("/sjapi/shopsetmeal/count_user_set_meal")
    Observable<JSONObject> count_user_set_meal(@Field("sj_login_token") String sj_login_token,@Field("shop_set_meal_id") String shop_set_meal_id);

    //	33.6.	套餐售出、使用列表
    @FormUrlEncoded
    @POST("/sjapi/shopsetmeal/list_user_set_meal")
    Observable<JSONObject> list_user_set_meal(@FieldMap Map<String,Object> params);

    //添加私密商品
    @FormUrlEncoded
    @POST("/sjapi/shopsetmeal/add_secret_goods")
    Observable<JSONObject> add_secret_goods(@FieldMap Map<String, Object> params);

    //获取套餐
    @FormUrlEncoded
    @POST("/sjapi/Shopsetmeal/get_shop_set_meal")
    Observable<JSONObject> get_shop_set_meal(@Field("sj_login_token") String loginToken, @Field("shop_set_meal_id") String shop_set_meal_id);

    //修改套餐
    @FormUrlEncoded
    @POST("/sjapi/Shopsetmeal/update_shop_set_meal")
    Observable<JSONObject> update_shop_set_meal(@FieldMap Map<String, Object> params);

    //33.3.	获取技术服务费（百分比）
    @FormUrlEncoded
    @POST("/sjapi/shopsetmeal/get_set_meal_skill_fee")
    Observable<JSONObject> get_set_meal_skill_fee(@Field("sj_login_token") String sj_login_token);


    //32.1.	获取视频抵扣券分类
    @FormUrlEncoded
    @POST("/sjapi/Videopromotion/get_video_promotion_classify")
    Observable<JSONObject> get_video_promotion_classify(@Field("sj_login_token") String sj_login_token);

    //32.2.	视频抵扣券新增与支付
    @FormUrlEncoded
    @POST("/sjapi/Videopromotion/add_video_promotion")
    Observable<JSONObject> add_video_deduction_coupon(@FieldMap Map<String, Object> params);

    //32.2.	视频抵扣券与支付
    @FormUrlEncoded
    @POST("/sjapi/Videopromotion/update_video_promotion")
    Observable<JSONObject> update_video_promotion(@FieldMap Map<String, Object> params);

    //视频列表
    @FormUrlEncoded
    @POST("/sjapi/Videopromotion/list_video_promotion")
    Observable<JSONObject> list_video_promotion(@Field("sj_login_token") String sj_login_token);


    //视频列表
    @FormUrlEncoded
    @POST("/sjapi/Videopromotion/delete_video_promotion")
    Observable<JSONObject> delete_video_promotion(@Field("sj_login_token") String sj_login_token,@Field("video_promotion_id") String video_promotion_id);

    //视频
    @FormUrlEncoded
    @POST("/sjapi/Videopromotion/get_video_promotion")
    Observable<JSONObject> get_video_promotion(@Field("sj_login_token") String sj_login_token,@Field("video_promotion_id") String video_promotion_id);
}

package com.wbx.merchant.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wushenghui on 2017/6/27.
 */

public class OrderInfo implements Serializable {
    private OrderAddressInfo addr;
    private int addr_id;
    private int create_time;
    private long dispatching_time;//配送时间
    private List<GoodsInfo> goods;
    private int is_daofu;
    private int logistics;
    private int num;
    private int need_pay;
    private int casing_price;
    private int user_subsidy_money;
    private int red_packet_money;
    private int service_money;
    private int coupon_money;
    private int full_money_reduce;
    private int order_id;
    private int status;
    private String message;
    private int is_fengniao;//!=0 代表提交到蜂鸟
    private int is_dada;//!=0 代表提交到达达
    //实体店
    private int total_price;//订单总价
    private int express_price;//配送费（运费）
    private List<FengNiaoOrderTrackBean> fengniao;
    private List<DaDaOrderTrackBean> dada;
    private int is_afhalen;//是否到店自提
    private int order_status;//请求的页面状态值

    public int getService_money() {
        return service_money;
    }

    public void setService_money(int service_money) {
        this.service_money = service_money;
    }
    public int getUser_subsidy_money() {
        return user_subsidy_money;
    }

    public void setUser_subsidy_money(int user_subsidy_money) {
        this.user_subsidy_money = user_subsidy_money;
    }

    public int getIs_fengniao() {
        return is_fengniao;
    }

    public void setIs_fengniao(int is_fengniao) {
        this.is_fengniao = is_fengniao;
    }

    public int getRed_packet_money() {
        return red_packet_money;
    }

    public void setRed_packet_money(int red_packet_money) {
        this.red_packet_money = red_packet_money;
    }

    public int getCoupon_money() {
        return coupon_money;
    }

    public void setCoupon_money(int coupon_money) {
        this.coupon_money = coupon_money;
    }

    public int getFull_money_reduce() {
        return full_money_reduce;
    }

    public void setFull_money_reduce(int full_money_reduce) {
        this.full_money_reduce = full_money_reduce;
    }

    public List<FengNiaoOrderTrackBean> getFengniao() {
        return fengniao;
    }

    public void setFengniao(List<FengNiaoOrderTrackBean> fengniao) {
        this.fengniao = fengniao;
    }

    public int getCasing_price() {
        return casing_price;
    }

    public void setCasing_price(int casing_price) {
        this.casing_price = casing_price;
    }

    public int getIs_dada() {
        return is_dada;
    }

    public void setIs_dada(int is_dada) {
        this.is_dada = is_dada;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getExpress_price() {
        return express_price;
    }

    public void setExpress_price(int express_price) {
        this.express_price = express_price;
    }

    public OrderAddressInfo getAddr() {
        return addr;
    }

    public void setAddr(OrderAddressInfo addr) {
        this.addr = addr;
    }

    public int getAddr_id() {
        return addr_id;
    }

    public void setAddr_id(int addr_id) {
        this.addr_id = addr_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public List<GoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsInfo> goods) {
        this.goods = goods;
    }

    public int getIs_daofu() {
        return is_daofu;
    }

    public void setIs_daofu(int is_daofu) {
        this.is_daofu = is_daofu;
    }

    public int getLogistics() {
        return logistics;
    }

    public void setLogistics(int logistics) {
        this.logistics = logistics;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNeed_pay() {
        return need_pay;
    }

    public void setNeed_pay(int need_pay) {
        this.need_pay = need_pay;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DaDaOrderTrackBean> getDada() {
        return dada;
    }

    public void setDada(List<DaDaOrderTrackBean> dada) {
        this.dada = dada;
    }

    public long getDispatching_time() {
        return dispatching_time;
    }

    public void setDispatching_time(long dispatching_time) {
        this.dispatching_time = dispatching_time;
    }

    public int getIs_afhalen() {
        return is_afhalen;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public void setIs_afhalen(int is_afhalen) {
        this.is_afhalen = is_afhalen;
    }
}

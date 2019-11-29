package com.wbx.merchant.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushenghui on 2017/6/26.
 */

public class GoodsInfo implements Serializable {

    /**
     * product_id : 4163
     * photo : http://www.wbx365.com/attachs/2017/04/29/thumb_5904092accc7d.jpg
     * product_name : 只发粉1包454克
     * price : 500
     * cate_name : 配料
     */

    private int product_id;
    private String photo;
    private List<String> goods_photo;
    private String product_name;
    private float price;
    private String cate_name;
    private boolean isSelect;
    private int closed;
    private String desc;
    private int cate_id;
    private int audit;//0审核中 //1审核通过
    private int num;
    private int create_time;
    private int sold_num;
    private int loss;//耗损

    //实体店返回的数据
    private int goods_id;
    private String title;//名称
    private float mall_price;//金额
    private String intro;//介绍
    private int sales_promotion_is;//是否促销
    private float sales_promotion_price;//促销价
    private List<String> photos;//商品详情图
    private int shopcate_id;
    private int is_attr;//是否启用多规格
    private int is_use_num;//是否开启库存
    private double min_price;//最小价格
    private String attr_name;//规格名称
    private String details;
    private String seckill_start_time;//秒杀开始时间
    private String seckill_end_time;//秒杀结束时间
    private int is_seckill;//是否开启秒杀
    private String seckill_num;//秒杀库存
    private String limitations_num;//秒杀限购数量


    public int getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

    private int is_recommend;//是否开启推荐
    private float seckill_price;//秒杀价格
    private float casing_price;//包装费
    private float shop_member_price;//会员价
    private ArrayList<Nature> nature;
    private String nature_name;
    private String subhead;
    private int special_supply_goods_id;//大于0就是特供

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public String getNature_name() {
        return nature_name;
    }

    public void setNature_name(String nature_name) {
        this.nature_name = nature_name;
    }


    public ArrayList<Nature> getNature() {
        return nature;
    }

    public void setNature(ArrayList<Nature> nature) {
        this.nature = nature;
    }

    public float getShop_member_price() {
        return shop_member_price;
    }

    public void setShop_member_price(float shop_member_price) {
        this.shop_member_price = shop_member_price;
    }

    public List<String> getGoods_photo() {
        return goods_photo;
    }

    public void setGoods_photo(List<String> goods_photo) {
        this.goods_photo = goods_photo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSeckill_start_time() {
        return seckill_start_time;
    }

    public void setSeckill_start_time(String seckill_start_time) {
        this.seckill_start_time = seckill_start_time;
    }

    public String getSeckill_end_time() {
        return seckill_end_time;
    }

    public void setSeckill_end_time(String seckill_end_time) {
        this.seckill_end_time = seckill_end_time;
    }

    public int getIs_seckill() {
        return is_seckill;
    }

    public void setIs_seckill(int is_seckill) {
        this.is_seckill = is_seckill;
    }

    public String getSeckill_num() {
        return seckill_num;
    }

    public void setSeckill_num(String seckill_num) {
        this.seckill_num = seckill_num;
    }

    public String getLimitations_num() {
        return limitations_num;
    }

    public void setLimitations_num(String limitations_num) {
        this.limitations_num = limitations_num;
    }

    public float getSeckill_price() {
        return seckill_price;
    }

    public void setSeckill_price(float seckill_price) {
        this.seckill_price = (float) (seckill_price / 100.00);
    }

    public void setFloatSeckill_price(float seckill_price) {
        this.seckill_price = seckill_price;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }

    public double getMin_price() {
        return min_price;
    }

    public void setMin_price(double min_price) {
        this.min_price = min_price;
    }

    public int getIs_attr() {
        return is_attr;
    }

    public void setIs_attr(int is_attr) {
        this.is_attr = is_attr;
    }

    public int getIs_use_num() {
        return is_use_num;
    }

    public void setIs_use_num(int is_use_num) {
        this.is_use_num = is_use_num;
    }

    private List<SpecInfo> goods_attr;

    public List<SpecInfo> getGoods_attr() {
        return goods_attr;
    }

    public void setGoods_attr(List<SpecInfo> goods_attr) {
        this.goods_attr = goods_attr;
    }

    public int getShopcate_id() {
        return shopcate_id;
    }

    public void setShopcate_id(int shopcate_id) {
        this.shopcate_id = shopcate_id;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public int getSales_promotion_is() {
        return sales_promotion_is;
    }

    public void setSales_promotion_is(int sales_promotion_is) {
        this.sales_promotion_is = sales_promotion_is;
    }

    public float getSales_promotion_price() {
        return sales_promotion_price;
    }

    public void setSales_promotion_price(float sales_promotion_price) {
        this.sales_promotion_price = sales_promotion_price;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getMall_price() {
        return mall_price;
    }

    public void setMall_price(float mall_price) {
        this.mall_price = mall_price;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getSold_num() {
        return sold_num;
    }

    public void setSold_num(int sold_num) {
        this.sold_num = sold_num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public float getCasing_price() {
        return casing_price;
    }

    public void setCasing_price(float casing_price) {
        this.casing_price = casing_price;
    }

    public int getSpecial_supply_goods_id() {
        return special_supply_goods_id;
    }

    public void setSpecial_supply_goods_id(int special_supply_goods_id) {
        this.special_supply_goods_id = special_supply_goods_id;
    }

    public static class Nature implements Serializable {
        private String item_id;
        private String item_name;
        private int is_use;
        private List<Nature_attr> nature_arr;

        public int getIs_use() {
            return is_use;
        }

        public void setIs_use(int is_use) {
            this.is_use = is_use;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public List<Nature_attr> getNature_arr() {
            return nature_arr;
        }

        public void setNature_arr(List<Nature_attr> nature_arr) {
            this.nature_arr = nature_arr;
        }

    }

    public static class Nature_attr implements Serializable {
        private String nature_id;
        private String nature_name;
        private int is_use;
        private boolean isSelect;

        public int getIs_use() {
            return is_use;
        }

        public void setIs_use(int is_use) {
            this.is_use = is_use;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getNature_id() {
            return nature_id;
        }

        public void setNature_id(String nature_id) {
            this.nature_id = nature_id;
        }

        public String getNature_name() {
            return nature_name;
        }

        public void setNature_name(String nature_name) {
            this.nature_name = nature_name;
        }

    }
}

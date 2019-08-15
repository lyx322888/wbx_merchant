package com.wbx.merchant.baseapp;


import com.wbx.merchant.base.BaseApplication;


public class AppConfig {

    public static final String SP_DOWNLOAD_PATH = "download.path";
    public static final String DEBUG_TAG = "logger";// LogCat的标记
    public static final String VERSION = "VERSION";//保存服务端版本号
    public static final String WX_APP_ID = "wx07867153bc1d88fa";//  wx07867153bc1d88fa
    public static final String WX_APP_SECRET = "3e2f0221cf045ab9d7775165afc2f4e4";
    public static final String IMAGES = "http://imgs.wbx365.com/";
    public static final String LOGIN_STATE = "loginState";//登录状态
    public static final String LOGIN_MOBILE = "loginMobile";//保存手机号码
    public static final String REFRESH_UI = "refreshUl";//刷新UI
    public static final String NO_ASK_AGAIN_ACCREDITATION = "NO_ASK_AGAIN_ACCREDITATION";//不再弹出资质认证提醒
    public static final int TAKE_PHOTO_CODE = 10066;
    public static boolean RESULT_PAY_TYPE;

    public static boolean isBuy;
    public static final int MAX_COUNT = 6;

    public static final int REQUEST_CONTACT = 10003;

    public static final int PERMISSIONS_CODE = 10004;

    public static final int pageNum = 1;

    public static final int pageSize = 10;


    public static final String PUSH_APP_ID = "loginToken";//保存登录Token

    public interface CashCode {
        String wxpay = "weixinpay";//微信支付
        String alipay = "alipay";//支付宝
        String bank = "bank";//银行卡

        String wxpayapp = "weixinapp";
        String alipayapp = "alipayapp";
    }

    public interface CollectionType {
        int GOODS = 0;
        int STORE = 1;
    }

    public interface StoreGrade {
        int STORE = 16;//注册店铺
        int MARKET = 15;//菜市场
        int EVINE = 32;//实体店
    }

    public interface StoreType {
        int VEGETABLE_MARKET = 15;//菜市场
        int PHYSICAL_STORE = 19;//实体店
        int FOOD_STREET = 20;//美食街
    }

    public interface addressIsDefault {
        int isDefault = 1;
        int unDefault = 0;
    }

    public interface CashMode {
        int ALI = 1;//支付宝
        int UNION = 2;//银联
    }

    //搜索type
    public interface ShopType {
        int MALL = 1;
        int SHOP = 2;
    }

    //搜索type
    public interface SearchType {
        int GOODS = 1;
        int STORE = 2;
    }

    public interface PayMode {
        String money = "money";//余额支付
        String wxpay = "weixinapp";//微信支付
        String alipay = "alipayapp";//支付宝
    }

    public static String orderStateStr(int state) {
        String stateStr = "";
        switch (state) {
            case 0:
                stateStr = "待付款";
                break;
            case 1:
                stateStr = "待配送";
                break;
            case 2:
                stateStr = "待收货";
                break;
            case 3:
                stateStr = "待退款";
                break;
            case 4:
                if (BaseApplication.getInstance().readLoginUser().getGrade_id() == StoreType.VEGETABLE_MARKET) {
                    stateStr = "已退款";
                } else {
                    stateStr = "待退款";
                }
                break;
            case 5:
                stateStr = "已退款";
                break;
            case 8:
                stateStr = "已完成";
                break;
            case 9:
                stateStr = "已拒单";
                break;

        }
        return stateStr;
    }

    public static int orderState(int state, boolean isEm) {
        int stateInt = 0;
        switch (state) {
            case 0:
                stateInt = 0;//待付款
                break;
            case 1:
                stateInt = 1;//待发货
                break;
            case 2:
                stateInt = 2;//已发货
                break;
            case 3:
                stateInt = isEm ? 4 : 3;//买菜待退款3 实体店4
                break;
            case 4:
                stateInt = 8;//已完成
                break;
        }
        return stateInt;
    }

    public interface ERROR_STATE {
        int NULLDATA = 1001;//无数据
        int NO_NETWORK = 1002;//网络错误
        int SERVICE_ERROR = 1003;//加载错误
        int NULL_PAY_PSW = 1005;//未设置支付密码
        int SEND_FEE_NO_ENOUGH = 1006;//配送金额不足

    }

    public interface PAY_TYPE {
        String RENEW = "renewals";
        String APPLY = "apply";
    }

    public static String thirdOrderStateStr(int state) {
        String stateStr = "";
        switch (state) {
            case 1:
                stateStr = "系统已接单";
                break;
            case 20:
                stateStr = "已分配骑手";
                break;
            case 80:
                stateStr = "骑手已到店";
                break;
            case 2:
                stateStr = "配送中";
                break;
            case 3:
                stateStr = "已送达";
                break;
            case 5:
                stateStr = "系统拒单/配送异常";
                break;

        }
        return stateStr;
    }

    //秒杀
    public interface SECKILL {
        int SECKILL = 1;
        int UNSECKILL = 0;
    }

    public static String getWeekStr(int week) {
        String weekStr = "";
        switch (week) {
            case 1:
                weekStr = "星期一";
                break;
            case 2:
                weekStr = "星期二";
                break;
            case 3:
                weekStr = "星期三";
                break;
            case 4:
                weekStr = "星期四";
                break;
            case 5:
                weekStr = "星期五";
                break;
            case 6:
                weekStr = "星期六";
                break;
            case 0:
                weekStr = "星期日";
                break;

        }
        return weekStr;
    }
}

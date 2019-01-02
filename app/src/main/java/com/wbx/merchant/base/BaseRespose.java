package com.wbx.merchant.base;

import java.io.Serializable;

/**
 * des:封装服务器返回数据
 * Created by wushenghui on 2017/4/23.
 */
public class BaseRespose<T> implements Serializable {
    public int state;
    public String msg;
    public T data;

    public boolean success() {
        return 1==state;
    }
    @Override
    public String toString() {
        return "BaseRespose{" +
                "state='" + state + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

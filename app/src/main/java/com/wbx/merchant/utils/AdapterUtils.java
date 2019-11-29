package com.wbx.merchant.utils;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wbx.merchant.common.Loadtype;

import java.util.List;


/**
 * 列表页适配器的 加载更多数据 刷新数据 封装
 * Created by Lyx on 2017/11/21.
 */
public class AdapterUtils {
    /**
     * @param myadapter 适配器
     * @param list      请求回调后的数组
     * @param action    请求类型
     * @param <T> 泛型
     */
    public static <T>void  setData(BaseQuickAdapter<T, BaseViewHolder> myadapter, List<T> list, int action) {
        switch (action){
            case Loadtype.ACTION_PULL_DOWN:
                //第一次加载 或刷新
                myadapter.setNewData(list);
                //服务器最多每页返回10条数据 如果第一次返回的数据不够十条 就关闭适配器的加载更多
                if (list.size()<10){
                    myadapter.setEnableLoadMore(false);
                }else {
                    myadapter.setEnableLoadMore(true);
                }
                break;
            case Loadtype.ACTION_LOAD_MORE:
                //加载更多
                if (list.size() > 0) {
                    myadapter.addData(list);
                    //加载完成（注意不是加载结束，而是本次数据加载结束并且还有下页数据）
                    myadapter.loadMoreComplete();
                } else {
                    //加载结束 显示没有更多footview
                    myadapter.loadMoreEnd();
                }
                break;
            default:break;
        }
    }

}

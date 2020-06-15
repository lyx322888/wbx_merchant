package com.wbx.merchant.utils;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * 列表页适配器的 加载更多数据 刷新数据 封装
 * Created by Lyx on 2017/11/21.
 */
public class AdapterUtilsNew {
    /**
     * @param myadapter 适配器
     * @param list      请求回调后的数组
     * @param <T> 泛型
     */
    public static <T>void  setData(BaseQuickAdapter<T, BaseViewHolder> myadapter, List<T> list, int pageNum,int pageSize) {
        if (pageNum ==1){
            //第一次加载 或刷新
            myadapter.setNewData(list);
            //服务器最多每页返回10条数据 如果第一次返回的数据不够十条 就关闭适配器的加载更多
            if (list.size()<pageSize){
                myadapter.setEnableLoadMore(false);
            }else {
                myadapter.setEnableLoadMore(true);
            }
        }else {
            //加载更多
            if (list.size() > 0) {
                myadapter.addData(list);
                //加载完成（注意不是加载结束，而是本次数据加载结束并且还有下页数据）
                myadapter.loadMoreComplete();
            } else {
                //加载结束 显示没有更多footview
                myadapter.loadMoreEnd();
            }
        }
    }

}

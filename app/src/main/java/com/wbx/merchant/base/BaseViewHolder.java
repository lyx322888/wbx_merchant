package com.wbx.merchant.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by wushenghui on 2017/4/27.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    protected SparseArray<View> views;
    protected View convertView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.convertView = itemView;
        this.views = new SparseArray<View>();
    }

    public BaseViewHolder setImageResource(int viewId, int resource) {
        ImageView view = getView(viewId);
        view.setImageResource(resource);
        return this;
    }


    /**
     * 设置TextView内容
     *
     * @param viewId
     * @param value
     * @return
     */
    public BaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
//        if (!TextUtils.isEmpty(value)) {
        view.setText(value);
//        }
        return this;
    }

    public BaseViewHolder setRating(int viewId, int rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }


    /**
     * 设置开/关两种状态的按钮
     *
     * @param viewId
     * @param checked
     * @return
     */
    public BaseViewHolder setCheck(int viewId, boolean checked) {
        View view = getView(viewId);
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setChecked(checked);
        } else if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(checked);
        }
        return this;
    }

    /**
     * 设置adapter(ListView/GridView都继承AdapterView)
     *
     * @param viewId
     * @param adapter
     * @return
     */
    public BaseViewHolder setAdapter(int viewId, BaseAdapter adapter) {
        AdapterView view = getView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    /**
     * 设置Rcv的adapter
     *
     * @param viewId
     * @param adapter
     * @return
     */
    public BaseViewHolder setRcvAdapter(int viewId, RecyclerView.Adapter adapter) {
        RecyclerView view = getView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    /**
     * 得到相信的view
     *
     * @param viewId view的id
     * @param <T>    view本身
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 加载layoutId视图并用LGViewHolder保存
     *
     * @param parent
     * @param layoutId
     * @return
     */
    protected static BaseViewHolder getViewHolder(ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new BaseViewHolder(itemView);
    }

}

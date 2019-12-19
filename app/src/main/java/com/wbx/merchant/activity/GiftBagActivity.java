package com.wbx.merchant.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.GiftbagHBAdapter;
import com.wbx.merchant.adapter.GiftbagZsAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.GiftBagInfo;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.GiftbagDiolog;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * 新人专享礼包
 */
public class GiftBagActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_hb)
    TextView tvHb;
    @Bind(R.id.ll_tjhb)
    LinearLayout llTjhb;
    @Bind(R.id.tv_ts_hb)
    TextView tvTsHb;
    @Bind(R.id.ll_xzts_hb)
    LinearLayout llXztsHb;
    @Bind(R.id.rv_hb)
    RecyclerView rvHb;
    @Bind(R.id.tv_zs)
    TextView tvZs;
    @Bind(R.id.ll_tjzs)
    LinearLayout llTjzs;
    @Bind(R.id.tv_ts_zs)
    TextView tvTsZs;
    @Bind(R.id.ll_xzts_zs)
    LinearLayout llXztsZs;
    @Bind(R.id.rv_zs)
    RecyclerView rvZs;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    private GiftbagHBAdapter giftbagHBAdapter;
    private GiftbagZsAdapter giftbagZsAdapter;
    private List<String> yxtlist = new ArrayList<>();
    private static final int RESULTCODE = 1234;
    @Override
    public int getLayoutId() {
        FormatUtil.setStatubarColor(mActivity, R.color.app_color);
        return R.layout.activity_gift_bag;
    }

    @Override
    public void initPresenter() {
        for (int i = 1; i <= 30; i++) {
            yxtlist.add(String.valueOf(i));
        }
    }

    @Override
    public void initView() {
        initAdapter();
    }



    //适配器
    private void initAdapter() {
        //红包
        rvHb.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        giftbagHBAdapter = new GiftbagHBAdapter();
        rvHb.setAdapter(giftbagHBAdapter);
        giftbagHBAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.tv_delete:
                        //删除
                        giftbagHBAdapter.getData().remove(position);
                        giftbagHBAdapter.notifyItemRemoved(position);
                        break;
                }
            }
        });
        //赠送
        rvZs.setLayoutManager(new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        giftbagZsAdapter = new GiftbagZsAdapter();
        rvZs.setAdapter(giftbagZsAdapter);
        giftbagZsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.fl_del:
                        //删除
                        giftbagZsAdapter.getData().remove(position);
                        giftbagZsAdapter.notifyItemRemoved(position);
                        break;
                }
            }
        });
    }

    @Override
    public void fillData() {
        getGiftBag();
    }


    //获取礼包
    private void getGiftBag() {
        LoadingDialog.showDialogForLoading(mActivity);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        new MyHttp().doPost(Api.getDefault().getGiftBag(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                GiftBagInfo bagInfo = new Gson().fromJson(result.toString(), GiftBagInfo.class);
                //面额除以100保留小数
                for (int i = 0; i <bagInfo.getData().getRed_packet().size() ; i++) {
                    bagInfo.getData().getRed_packet().get(i).setMoney(bagInfo.getData().getRed_packet().get(i).getMoney()/100);
                }
                giftbagHBAdapter.setNewData(bagInfo.getData().getRed_packet());
                giftbagZsAdapter.setNewData(bagInfo.getData().getGive_goods());
                tvTsHb.setText(bagInfo.getData().getRed_packet_valid_day()+"天");
                tvTsZs.setText(bagInfo.getData().getGive_goods_valid_day()+"天");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }


    //红包面额弹窗
    private void showHbDialog(){
        GiftbagDiolog giftbagDiolog = new GiftbagDiolog();
        giftbagDiolog.setDialogListener(new GiftbagDiolog.DialogListener() {
            @Override
            public void dialogClickListener(String content) {
                GiftBagInfo.DataBean.RedPacketBean redPacketBean = new GiftBagInfo.DataBean.RedPacketBean();
                redPacketBean.setMoney(Float.valueOf(content));
                redPacketBean.setRed_packet_id("0");
                giftbagHBAdapter.addData(redPacketBean);
            }
        });
        giftbagDiolog.show(getFragmentManager(),"");
    }
    //选择有效天数
    private void showPopYxts(final TextView textView){
         final OptionsPickerView pvOptions = new OptionsPickerView(new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                textView.setText(String.format("%s天",yxtlist.get(options1)));
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("选择天数")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(ContextCompat.getColor(mContext, R.color.black))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(mContext, R.color.app_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(mContext, R.color.black))//取消按钮文字颜色
//                .setTitleBgColor(0xFF333333)//标题背景颜色 Night mode
//                .setBgColor(0xFF000000)//滚轮背景颜色 Night mode
                .setContentTextSize(25)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
        );

        pvOptions.setPicker(yxtlist);
        pvOptions.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULTCODE) {
            //选择赠品返回
            if (data!=null){
              List<GoodsInfo> selectGoodsList = (List<GoodsInfo>) data.getSerializableExtra("goods");
                for (int i = 0; i < selectGoodsList.size(); i++) {
                    GiftBagInfo.DataBean.GiveGoodsBean bean = new GiftBagInfo.DataBean.GiveGoodsBean();
                    bean.setGive_goods_id("0");
                    bean.setPhoto(selectGoodsList.get(i).getPhoto());
                    bean.setTitle(selectGoodsList.get(i).getProduct_name());
                    bean.setGoods_id(String.valueOf(selectGoodsList.get(i).getGoods_id()));
                    giftbagZsAdapter.addData(bean);
                }

            }
        }
    }

    @OnClick({R.id.ll_tjhb, R.id.ll_xzts_hb, R.id.ll_tjzs, R.id.ll_xzts_zs, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_tjhb:
                //添加红包
                showHbDialog();
                break;
            case R.id.ll_xzts_hb:
                //选择红包有效期
                showPopYxts(tvTsHb);
                break;
            case R.id.ll_tjzs:
                //选择赠品
                startActivityForResult(new Intent(mActivity,GiftBagSelectGoodsActivity.class),RESULTCODE);
                break;
            case R.id.ll_xzts_zs:
                //选择赠品有效期
                showPopYxts(tvTsZs);
                break;
            case R.id.tv_submit:
                 //发布礼包
                affirmGiftBag();
                break;
        }
    }
    //发送礼包
    private void affirmGiftBag(){
        LoadingDialog.showDialogForLoading(mActivity);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        hashMap.put("red_packet_valid_day", tvTsHb.getText().toString().replace("天",""));
        hashMap.put("give_goods_valid_day", tvTsZs.getText().toString().replace("天",""));
        hashMap.put("red_packet", JSONArray.toJSON(giftbagHBAdapter.getData()));
        hashMap.put("give_goods",JSONArray.toJSON(giftbagZsAdapter.getData()) );
        new MyHttp().doPost(Api.getDefault().affirmGiftBag(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("礼包已发送");
            }

            @Override
            public void onError(int code) {

            }
        });
    }
}

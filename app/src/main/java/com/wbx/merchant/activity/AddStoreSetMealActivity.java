package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.wbx.merchant.AddSecreGoodsActivity;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.BusinessHoursAdapter;
import com.wbx.merchant.adapter.PhotoGeneralAdapter;
import com.wbx.merchant.adapter.ddtc.CuisineAdapter;
import com.wbx.merchant.adapter.ddtc.SelectCuisineAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.bean.BusinessHoursBaen;
import com.wbx.merchant.bean.CuisineBean;
import com.wbx.merchant.bean.CuisineOhterBean;
import com.wbx.merchant.bean.MealSkillFee;
import com.wbx.merchant.bean.SelectGoodsInfo;
import com.wbx.merchant.bean.ShopSetMealBean;
import com.wbx.merchant.common.LoginUtil;
import com.wbx.merchant.dialog.SelectGoodsDialog;
import com.wbx.merchant.utils.ChoosePictureUtils;
import com.wbx.merchant.utils.FileUtils;
import com.wbx.merchant.utils.FormatUtil;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.PriceEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

//添加到店套餐
public class AddStoreSetMealActivity extends BaseActivity {
    @Bind(R.id.tv_tcnr_tjcp)
    TextView tvTcnrTjcp;
    @Bind(R.id.rv_need)
    RecyclerView rvNeed;
    @Bind(R.id.tv_nxy_tjcp)
    TextView tvNxyTjcp;
    @Bind(R.id.rv_elect_kind)
    RecyclerView rvElectKind;
    @Bind(R.id.rv_other)
    RecyclerView rvOther;
    @Bind(R.id.et_ddcs_price)
    PriceEditText etDdcsPrice;
    @Bind(R.id.tv_ddcs_zk)
    TextView tvDdcsZk;
    @Bind(R.id.et_ptcs_price)
    PriceEditText etPtcsPrice;
    @Bind(R.id.tv_ptcs_zk)
    TextView tvPtcsZk;
    @Bind(R.id.et_vip_price)
    PriceEditText etVipPrice;
    @Bind(R.id.tv_jsfwf_bl)
    TextView tvJsfwfBl;
    @Bind(R.id.recycler_yysj)
    RecyclerView recyclerYysj;
    @Bind(R.id.ll_select_ky_time)
    LinearLayout llSelectKyTime;
    @Bind(R.id.tv_hxrq)
    TextView tvHxrq;
    @Bind(R.id.ll_select_hxjz_time)
    LinearLayout llSelectHxjzTime;
    @Bind(R.id.tv_jztime)
    TextView tvJztime;
    @Bind(R.id.ll_select_csjz_time)
    LinearLayout llSelectCsjzTime;
    @Bind(R.id.ll_fb)
    LinearLayout llFb;
    @Bind(R.id.rv_cp)
    RecyclerView rvCp;
    @Bind(R.id.tv_submit)
    RoundTextView tvSubmit;
    @Bind(R.id.title_name_tv)
    TextView titleNameTv;
    @Bind(R.id.tv_vip_zk)
    TextView tvVipZk;
    @Bind(R.id.tv_ky_time)
    TextView tvKyTime;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.et_tc_name)
    EditText etTcName;
    @Bind(R.id.et_sygz)
    EditText etsygz;
    private CuisineAdapter needKindAdapte;//必选商品
    private CuisineAdapter electKindAdapter;//n选1商品
    private SelectCuisineAdapter selectCuisineAdapter;
    private BusinessHoursAdapter businessHoursAdapter;
    private PhotoGeneralAdapter photoGeneralAdapter;//图片
    private final String ELECTKIND = "electkind";
    private final String NEEDKIND = "needkind";
    public static final int REQUEST__TIME = 1001;//时间
    public static final int REQUEST__SECRE_GOODS = 1002;//隐私商品
    private String can_hours;//可用时间
    private final int  MAXPHOTO = 6;
    private float original_price =0;//原价
    private float one_price =0;//单人购买价格价格
    private float two_price =0;//2人拼团价格
    private float vip_price =0;//vip价格
    private SelectGoodsDialog selectGoodsDialog;

    public int type; //0 新建  1 修改  2再次发布 3//查看

    public String shop_set_meal_id = "";

    String use_end_time;
    String sell_end_time ;

    //跳转
    public static void actionStart(Activity activity, int type,String shop_set_meal_id) {
        Intent intent = new Intent(activity, AddStoreSetMealActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("shop_set_meal_id", shop_set_meal_id);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_store_set_meal;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        needKindAdapte = new CuisineAdapter(this);
        rvNeed.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvNeed.setAdapter(needKindAdapte);

        electKindAdapter = new CuisineAdapter(this);
        rvElectKind.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvElectKind.setAdapter(electKindAdapter);

        selectCuisineAdapter = new SelectCuisineAdapter(this);
        rvOther.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvOther.setAdapter(selectCuisineAdapter);

        //其他
        ArrayList<CuisineOhterBean> list = new ArrayList<>();
        list.add(new CuisineOhterBean("米饭/份", "", 1, false));
        list.add(new CuisineOhterBean("餐位费/位", "", 1, false));
        list.add(new CuisineOhterBean("餐厅纸/份", "", 1, false));
        list.add(new CuisineOhterBean("水果自助/份", "", 1, false));
        selectCuisineAdapter.setNewData(list);

        //时间
        ArrayList<BusinessHoursBaen> businessHoursBaenList = new ArrayList<>();
        businessHoursBaenList.add(new BusinessHoursBaen(true, "周日"));
        businessHoursBaenList.add(new BusinessHoursBaen(true, "周一"));
        businessHoursBaenList.add(new BusinessHoursBaen(true, "周二"));
        businessHoursBaenList.add(new BusinessHoursBaen(true, "周三"));
        businessHoursBaenList.add(new BusinessHoursBaen(true, "周四"));
        businessHoursBaenList.add(new BusinessHoursBaen(true, "周五"));
        businessHoursBaenList.add(new BusinessHoursBaen(true, "周六"));
        businessHoursAdapter = new BusinessHoursAdapter();

        //时间
        recyclerYysj.setLayoutManager(new GridLayoutManager(mContext, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerYysj.setAdapter(businessHoursAdapter);
        businessHoursAdapter.setNewData(businessHoursBaenList);

        //图片
        photoGeneralAdapter = new PhotoGeneralAdapter();
        rvCp.setAdapter(photoGeneralAdapter);
        photoGeneralAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (photoGeneralAdapter.getData().size()<MAXPHOTO){
                    chooseImage(MAXPHOTO-photoGeneralAdapter.getData().size()-1);
                }else {
                    showShortToast("超过最大张数");
                }
            }
        });


        addCsPriceChangeCallback();

        type =getIntent().getIntExtra("type",0);
        switch (type){
            case 0:
                //新建
                titleNameTv.setText("添加到店套餐");
                break;
            case 1:
                //修改
                shop_set_meal_id =getIntent().getStringExtra("shop_set_meal_id");
                titleNameTv.setText("编辑到店套餐");
                break;
            case 2:
                //再次发布
                shop_set_meal_id =getIntent().getStringExtra("shop_set_meal_id");
                titleNameTv.setText("添加到店套餐");
                break;
            case 3:
                //查看
                shop_set_meal_id =getIntent().getStringExtra("shop_set_meal_id");
                titleNameTv.setText("到店套餐");
                tvTcnrTjcp.setVisibility(View.GONE);
                tvNxyTjcp.setVisibility(View.GONE);
                llFb.setVisibility(View.GONE);
                break;
        }
    }

    //出售价格折扣
    private void addCsPriceChangeCallback() {
        //单独购买出售价格
        etDdcsPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float zk = 0;
                if (TextUtils.isEmpty(s)){
                    one_price  = (float) 0.0;
                }else {
                    one_price  = Float.parseFloat(s.toString());
                }

                if (original_price!=0){
                    zk =   (one_price/original_price)*10;
                }
                tvDdcsZk.setText(String.format("%.1f", zk));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //拼团购买出售价格
        etPtcsPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float zk = 0;
                if (TextUtils.isEmpty(s)){
                    two_price  = (float) 0.0;
                }else {
                    two_price  = Float.parseFloat(s.toString());
                }

                if (original_price!=0){
                    zk =   (two_price/original_price)*10;
                }
                tvPtcsZk.setText(String.format("%.1f", zk));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //拼团购买出售价格
        etVipPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float zk = 0;
                if (TextUtils.isEmpty(s)){
                    vip_price  = (float) 0.0;
                }else {
                    vip_price  = Float.parseFloat(s.toString());
                }

                if (original_price!=0){
                    zk =   (vip_price/original_price)*10;
                }
                tvVipZk.setText(String.format("%.1f", zk));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void fillData() {
        get_set_meal_skill_fee();
        if (type!=0){
            getData();
        }
    }

    //技术服务费
    private void get_set_meal_skill_fee(){
        new MyHttp().doPost(Api.getDefault().get_set_meal_skill_fee(LoginUtil.getLoginToken()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                MealSkillFee bean = new Gson().fromJson(result.toString(),MealSkillFee.class);
                //技术服务费
                tvJsfwfBl.setText(bean.getData().getSet_meal_skill_fee()+"%");
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //获取数据
    private void getData() {
        new MyHttp().doPost(Api.getDefault().get_shop_set_meal(LoginUtil.getLoginToken(), shop_set_meal_id), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                ShopSetMealBean shopSetMealBeanBaseResult = new Gson().fromJson(result.toString(),ShopSetMealBean.class);
                //n选1
                List<CuisineBean> cuisineBeanList = new ArrayList<>();
                for (int i = 0; i < shopSetMealBeanBaseResult.getData().getElect_kind().size(); i++) {
                    ShopSetMealBean.DataBean.ElectKindBean kindBean = shopSetMealBeanBaseResult.getData().getElect_kind().get(i);
                    cuisineBeanList.add(new CuisineBean(kindBean.getTitle(),kindBean.getPrice(),Integer.valueOf(kindBean.getGoods_num()),kindBean.getGoods_id()));
                }
                electKindAdapter.setNewData(cuisineBeanList);

                //必选
                List<CuisineBean> needKindList = new ArrayList<>();
                for (int i = 0; i < shopSetMealBeanBaseResult.getData().getNeed_kind().size(); i++) {
                    ShopSetMealBean.DataBean.NeedKindBean needKindBean = shopSetMealBeanBaseResult.getData().getNeed_kind().get(i);
                    needKindList.add(new CuisineBean(needKindBean.getTitle(),needKindBean.getPrice(),Integer.valueOf(needKindBean.getGoods_num()),needKindBean.getGoods_id()));
                }
                needKindAdapte.setNewData(needKindList);

                //其他套餐
                List<CuisineOhterBean> list = new ArrayList<>();
                list.add(0,new CuisineOhterBean("米饭/份",shopSetMealBeanBaseResult.getData().getRice_price(),shopSetMealBeanBaseResult.getData().getRice_num(),shopSetMealBeanBaseResult.getData().getHas_rice()==1));
                list.add(1,new CuisineOhterBean("餐位费/位",shopSetMealBeanBaseResult.getData().getTable_price(),shopSetMealBeanBaseResult.getData().getTable_num(),shopSetMealBeanBaseResult.getData().getHas_table()==1));
                list.add(2,new CuisineOhterBean("餐厅纸/份",shopSetMealBeanBaseResult.getData().getNapkin_price(),shopSetMealBeanBaseResult.getData().getNapkin_num(),shopSetMealBeanBaseResult.getData().getHas_napkin()==1));
                list.add(3,new CuisineOhterBean("水果自助/份",shopSetMealBeanBaseResult.getData().getSelf_fruits_price(),shopSetMealBeanBaseResult.getData().getSelf_fruits_num(),shopSetMealBeanBaseResult.getData().getHas_self_fruits()==1));
                selectCuisineAdapter.setNewData(list);

                //套餐名称
                etTcName.setText(shopSetMealBeanBaseResult.getData().getSet_meal_name());
                original_price = shopSetMealBeanBaseResult.getData().getOriginal_price();
                tvPrice.setText(String.valueOf(original_price));
                one_price = shopSetMealBeanBaseResult.getData().getOne_price();
                two_price = shopSetMealBeanBaseResult.getData().getTwo_price();
                vip_price = shopSetMealBeanBaseResult.getData().getVip_price();
                etDdcsPrice.setText(one_price+"");
                etPtcsPrice.setText(two_price+"");
                etVipPrice.setText(vip_price+"");

                //可用时间
                can_hours = shopSetMealBeanBaseResult.getData().getCan_hours();
                tvKyTime.setText(can_hours);
                can_hours = shopSetMealBeanBaseResult.getData().getCan_hours();
                tvKyTime.setText(can_hours);
                sell_end_time = shopSetMealBeanBaseResult.getData().getSell_end_time();
                use_end_time = shopSetMealBeanBaseResult.getData().getUse_end_time();
                tvHxrq.setText(FormatUtil.stampToDate1(shopSetMealBeanBaseResult.getData().getUse_end_time()));
                tvJztime.setText(FormatUtil.stampToDate1(shopSetMealBeanBaseResult.getData().getSell_end_time()));
                etsygz.setText(shopSetMealBeanBaseResult.getData().getUse_rule_info());

                //星期
                for (int i = 0; i < shopSetMealBeanBaseResult.getData().getBusiness_week_days().size(); i++) {
                        businessHoursAdapter.getItem(i).setChoice(TextUtils.equals("1",shopSetMealBeanBaseResult.getData().getBusiness_week_days().get(i)));
                }
                businessHoursAdapter.notifyDataSetChanged();

                photoGeneralAdapter.addData(shopSetMealBeanBaseResult.getData().getPhotos());
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    @Override
    public void setListener() {

    }

    //选择商品
    private void shopGoodsListDailog(String type) {
        selectGoodsDialog = new SelectGoodsDialog(mContext);
        selectGoodsDialog.show();
        selectGoodsDialog.setDialogListener(new SelectGoodsDialog.DialogListener() {
            @Override
            public void dialogBeanClickListener(SelectGoodsInfo.DataBean dataBean) {
                switch (type) {
                    case NEEDKIND:
                        needKindAdapte.addData(new CuisineBean(dataBean.getTitle(), dataBean.getMall_price(), 1, dataBean.getGoods_id()));
                        break;
                    case ELECTKIND:
                        electKindAdapter.addData(new CuisineBean(dataBean.getTitle(), dataBean.getMall_price(), 1, dataBean.getGoods_id()));
                        break;
                }
                dataChangedCallback();
            }

            @Override
            public void add_yssp() {
                AddSecreGoodsActivity.actionStart(mActivity);
            }
        });
    }

    //原价数据变化调用
    public void dataChangedCallback() {
        original_price = 0;
        if (electKindAdapter.getData().size()>0){
            original_price +=electKindAdapter.getTopPrice();
        }
        if (needKindAdapte.getData().size()>0){
            original_price +=needKindAdapte.getPrice();
        }
        //选择商品price
        original_price +=selectCuisineAdapter.getPrice();

        tvPrice.setText(String.valueOf(original_price));

        //计算出售折扣
        float ddzk = 0;
        if (original_price!=0){
            ddzk =   (one_price/original_price)*10;
        }
        tvDdcsZk.setText(String.format("%.1f", ddzk));

        float ptzk = 0;
        if (original_price!=0){
            ptzk =   (two_price/original_price)*10;
        }
        tvPtcsZk.setText(String.format("%.1f", ptzk));

        float vipzk = 0;
        if (original_price!=0){
            vipzk =   (vip_price/original_price)*10;
        }
        tvVipZk.setText(String.format("%.1f", vipzk));
    }

    @OnClick({R.id.tv_tcnr_tjcp, R.id.tv_nxy_tjcp, R.id.ll_select_ky_time, R.id.ll_select_hxjz_time, R.id.ll_select_csjz_time, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tcnr_tjcp:
                //添加套餐内容菜品
                shopGoodsListDailog(NEEDKIND);
                break;
            case R.id.tv_nxy_tjcp:
                //添加n选一菜品
                shopGoodsListDailog(ELECTKIND);
                break;
            case R.id.ll_select_ky_time:
                //可用时间
                SelectTimeActivity.actionStart(mActivity, can_hours);
                break;
            case R.id.ll_select_hxjz_time:
                //核销截止时间
                chooseTime(tvHxrq,0);
                break;
            case R.id.ll_select_csjz_time:
                //出售时间
                chooseTime(tvJztime,1);
                break;
            case R.id.tv_submit:
                //发布
                if (needKindAdapte.getData().size()==0){
                    showShortToast("请添加套餐菜品");
                    return;
                }
                if (photoGeneralAdapter.getData().size()<2){
                    showShortToast("请选择菜品图片");
                    return;
                }
                if (TextUtils.isEmpty(etTcName.getText())){
                    showShortToast("请输入套餐名称");
                    return;
                }

                if (type==1){
                    update_shop_set_meal();
                }else {
                    add_shop_set_meal();
                }
                break;
        }
    }

    //选择图片
    private void chooseImage( int maxSelectable){
        ChoosePictureUtils.choosePictureCommon(mActivity, maxSelectable, result -> {
            //图片压缩
            LoadingDialog.showDialogForLoading(mActivity);
            Observable.just(result)
                    .observeOn(Schedulers.io())
                    .map(list -> {
                        // 同步方法直接返回压缩后的文件
                        ArrayList<String> arrayList = new ArrayList<>();
                        List<File> files= null;
                        try {
                            files = Luban.with(mContext).load(list).setTargetDir(FileUtils.getCacheDir(mContext,"image").getAbsolutePath()).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < files.size(); i++) {
                            arrayList.add(files.get(i).getPath());
                        }
                        return arrayList;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(arrayList -> upLoadPic( arrayList));
        });
    }

    //上传图片七牛
    private void upLoadPic( ArrayList<String> arrayList){
        UpLoadPicUtils.batchUpload(arrayList, new UpLoadPicUtils.BatchUpLoadPicListener() {
            @Override
            public void success(List<String> qiNiuPath) {
                photoGeneralAdapter.addData(qiNiuPath);
                LoadingDialog.cancelDialogForLoading();
            }
            @Override
            public void error() {
                showShortToast("上传失败，请稍后重试");
                LoadingDialog.cancelDialogForLoading();
            }
        });
    }

    //选择时间
    private void chooseTime(final TextView textView,int type) {
        TimePickerView pvTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String dataStr = String.valueOf(date.getTime()/1000);
                if (type==0){
                    use_end_time = dataStr;
                }else {
                    sell_end_time = dataStr;
                }
                textView.setText(FormatUtil.stampToDate1(dataStr));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})//年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setTitleColor(ContextCompat.getColor(mContext, R.color.black))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(mContext, R.color.app_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(mContext, R.color.black))//取消按钮文字颜色
                .setDate(Calendar.getInstance())
                .setDecorView(null)
                .build();
        pvTime.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == REQUEST__TIME) {
            if (intent != null) {
                can_hours = intent.getStringExtra("businessTime");
                tvKyTime.setText(can_hours);
            }
        } else if (resultCode == RESULT_OK&&requestCode ==REQUEST__SECRE_GOODS){
            if (selectGoodsDialog!=null){
                selectGoodsDialog.refresh();
            }
        }
    }

    //添加到店套餐
    private void add_shop_set_meal(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        String needKind = "";
        String elect_kind = "";
        if (needKindAdapte.getData().size()>0){
            needKind  = JSONObject.toJSONString(needKindAdapte.getData());
        }
        if (electKindAdapter.getData().size()>0){
            elect_kind  = JSONObject.toJSONString(electKindAdapter.getData());
        }
        hashMap.put("need_kind",needKind);
        hashMap.put("elect_kind",elect_kind);
        hashMap.put("set_meal_name",etTcName.getText().toString().replaceAll(" ",""));
        hashMap.put("business_week_days",getBusiness_week_days());
        hashMap.put("original_price",original_price);
        hashMap.put("one_price",one_price);
        hashMap.put("two_price",two_price);
        hashMap.put("vip_price",vip_price);
        hashMap.put("can_hours",can_hours);
        hashMap.put("use_end_time",use_end_time);
        hashMap.put("sell_end_time",sell_end_time);
        hashMap.put("use_rule_info",etsygz.getText());
        hashMap.put("photos",getKeyStr(photoGeneralAdapter.getPhotoList()));

        //是否含米饭
        hashMap.put("has_rice",selectCuisineAdapter.getData().get(0).isChoice()?"1":"0");
        if (selectCuisineAdapter.getData().get(0).isChoice()){
            hashMap.put("rice_price",selectCuisineAdapter.getData().get(0).getPrice());
            hashMap.put("rice_num",selectCuisineAdapter.getData().get(0).getNum());
        }
        //是否餐位费
        hashMap.put("has_table",selectCuisineAdapter.getData().get(1).isChoice()?"1":"0");
        if (selectCuisineAdapter.getData().get(1).isChoice()){
            hashMap.put("table_price",selectCuisineAdapter.getData().get(1).getPrice());
            hashMap.put("table_num",selectCuisineAdapter.getData().get(1).getNum());
        }
        //是否餐巾纸
        hashMap.put("has_napkin",selectCuisineAdapter.getData().get(2).isChoice()?"1":"0");
        if (selectCuisineAdapter.getData().get(2).isChoice()){
            hashMap.put("napkin_price",selectCuisineAdapter.getData().get(2).getPrice());
            hashMap.put("napkin_num",selectCuisineAdapter.getData().get(2).getNum());
        }
        //是否水果
        hashMap.put("has_self_fruits",selectCuisineAdapter.getData().get(3).isChoice()?"1":"0");
        if (selectCuisineAdapter.getData().get(3).isChoice()){
            hashMap.put("self_fruits_price",selectCuisineAdapter.getData().get(3).getPrice());
            hashMap.put("self_fruits_num",selectCuisineAdapter.getData().get(3).getNum());
        }

        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().add_shop_set_meal(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("添加成功");
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //修改到店套餐
    private void update_shop_set_meal(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sj_login_token", LoginUtil.getLoginToken());
        hashMap.put("shop_set_meal_id", shop_set_meal_id);
        String needKind = "";
        String elect_kind = "";
        if (needKindAdapte.getData().size()>0){
            needKind  = JSONObject.toJSONString(needKindAdapte.getData());
        }
        if (electKindAdapter.getData().size()>0){
            elect_kind  = JSONObject.toJSONString(electKindAdapter.getData());
        }
        hashMap.put("need_kind",needKind);
        hashMap.put("elect_kind",elect_kind);
        hashMap.put("set_meal_name",etTcName.getText().toString().replaceAll(" ",""));
        hashMap.put("business_week_days",getBusiness_week_days());
        hashMap.put("original_price",original_price);
        hashMap.put("one_price",one_price);
        hashMap.put("two_price",two_price);
        hashMap.put("vip_price",vip_price);
        hashMap.put("can_hours",can_hours);
        hashMap.put("use_end_time",use_end_time);
        hashMap.put("sell_end_time",sell_end_time);
        hashMap.put("use_rule_info",etsygz.getText());
        hashMap.put("photos",getKeyStr(photoGeneralAdapter.getPhotoList()));

        //是否含米饭
        hashMap.put("has_rice",selectCuisineAdapter.getData().get(0).isChoice()?"1":"0");
        if (selectCuisineAdapter.getData().get(0).isChoice()){
            hashMap.put("rice_price",selectCuisineAdapter.getData().get(0).getPrice());
            hashMap.put("rice_num",selectCuisineAdapter.getData().get(0).getNum());
        }
        //是否餐位费
        hashMap.put("has_table",selectCuisineAdapter.getData().get(1).isChoice()?"1":"0");
        if (selectCuisineAdapter.getData().get(1).isChoice()){
            hashMap.put("table_price",selectCuisineAdapter.getData().get(1).getPrice());
            hashMap.put("table_num",selectCuisineAdapter.getData().get(1).getNum());
        }
        //是否餐巾纸
        hashMap.put("has_napkin",selectCuisineAdapter.getData().get(2).isChoice()?"1":"0");
        if (selectCuisineAdapter.getData().get(2).isChoice()){
            hashMap.put("napkin_price",selectCuisineAdapter.getData().get(2).getPrice());
            hashMap.put("napkin_num",selectCuisineAdapter.getData().get(2).getNum());
        }
        //是否水果
        hashMap.put("has_self_fruits",selectCuisineAdapter.getData().get(3).isChoice()?"1":"0");
        if (selectCuisineAdapter.getData().get(3).isChoice()){
            hashMap.put("self_fruits_price",selectCuisineAdapter.getData().get(3).getPrice());
            hashMap.put("self_fruits_num",selectCuisineAdapter.getData().get(3).getNum());
        }

        LoadingDialog.showDialogForLoading(mActivity);
        new MyHttp().doPost(Api.getDefault().update_shop_set_meal(hashMap), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast("修改成功");
                finish();
            }

            @Override
            public void onError(int code) {

            }
        });
    }

    //分类Business_week_days
    private String getBusiness_week_days(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < businessHoursAdapter.getData().size(); i++) {
            stringBuilder.append(businessHoursAdapter.getData().get(i).getChoice()?"1":"0").append(",");
        }
        if (stringBuilder.length()>1){
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder.toString();
    }

    //数组以，分裂
    private String getKeyStr(List<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i)).append(",");
        }
        if (stringBuilder.length()>1){
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder.toString();
    }
}
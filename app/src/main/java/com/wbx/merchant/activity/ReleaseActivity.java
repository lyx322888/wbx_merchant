package com.wbx.merchant.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.kyleduo.switchbutton.SwitchButton;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.PhotoAdapter;
import com.wbx.merchant.api.Api;
import com.wbx.merchant.api.HttpListener;
import com.wbx.merchant.api.MyHttp;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.base.BaseAdapter;
import com.wbx.merchant.base.BaseApplication;
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.bean.SpecInfo;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.utils.ToastUitl;
import com.wbx.merchant.utils.UpLoadPicUtils;
import com.wbx.merchant.widget.LoadingDialog;
import com.wbx.merchant.widget.decoration.SpacesItemDecoration;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * 发布商品/更新商品
 */

public class ReleaseActivity extends BaseActivity implements OptionsPickerView.OnOptionsSelectListener {
    @Bind(R.id.take_photo_recycler_view)
    RecyclerView takePhotoRecyclerView;//商品详情图(底部)
    @Bind(R.id.goods_name_edit)
    EditText goodsNameEdit;
    @Bind(R.id.selling_price_edit)
    EditText sellingPriceEdit;
    @Bind(R.id.goods_desc_edit)
    EditText goodsDescEdit;
    @Bind(R.id.goods_cate_tv)
    TextView goodsCateTv;
    @Bind(R.id.release_btn)
    Button releaseBtn;
    @Bind(R.id.price_edit)
    EditText priceEdit;//商城价格
    @Bind(R.id.market_price_layout)
    LinearLayout marketPriceLayout;
    @Bind(R.id.goods_detail_pics_layout)
    LinearLayout goodsDetailPicsLayout;
    @Bind(R.id.photos_select_num_tv)
    TextView photoSelectNumTv;//商品详情图数量(底部)
    @Bind(R.id.ll_packing_fee)
    LinearLayout llPackingFee;
    @Bind(R.id.et_packing_fee)
    EditText etPackingFee;//包装费
    @Bind(R.id.et_sub_title)
    EditText etSubTitle;//副标题
    @Bind(R.id.rl_add_pic)
    ImageView rlAddPic;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.tv_index_pic)
    TextView tvIndexPic;
    @Bind(R.id.is_sales_layout)
    LinearLayout isSalesLayout;//开启促销价
    @Bind(R.id.open_sales_sb)
    SwitchButton openSalesSb;//是否促销
    @Bind(R.id.is_inventory_layout)
    LinearLayout isInventoryLayout;//开启库存
    @Bind(R.id.open_inventory_sb)
    SwitchButton openInventorySb;//是否开启库存
    @Bind(R.id.is_spec_layout)
    LinearLayout isSpecLayout;//添加多规格
    @Bind(R.id.ll_spec_attr)
    LinearLayout llSpecAttr;//多属性
    @Bind(R.id.open_spec_sb)
    SwitchButton openSpecSb;//是否多规格
    @Bind(R.id.settings_spec_layout)
    LinearLayout settingsSpecLayout;
    @Bind(R.id.open_spec_to_gone_layout)
    LinearLayout openSpecToGoneLayout;
    @Bind(R.id.num_edit)
    EditText numEdit;
    @Bind(R.id.sales_price_edit)
    EditText salesPriceEdit;

    private GoodsInfo mGoodInfo;
    private PhotoAdapter mPhotoAdapter;//商品详情图(底部)
    private OptionsPickerView catePickerView;//商品分类
    private static final int REQUEST_GOODS_PIC = 1001;//商品图
    private static final int REQUEST_GOODS_INTRODUCE_PIC = 1002;//商品介绍图l
    public static final int REQUEST_GOODS_ATTR = 1004;//多规格
    public static final String RESULT_GOODS = "result_goods";
    public static final String GOOD_INFO = "Good_Info";
    public static final int MAX_GOODS_PIC_NUM = 5; //商品图最多5张
    private static final int MAX_INTRODUCE_PIC_NUM = 6; //介绍图最多6张
    private List<CateInfo> cateInfoList = new ArrayList<>();//商品分类
    private List<String> lstGoodsIntroducePic = new ArrayList<>();//商品详情图(底部)
    private ArrayList<String> lstGoodsPic = new ArrayList<>(); //商品图(头部)


    @Override
    public int getLayoutId() {
        return R.layout.activity_release;
    }

    @Override
    public void initPresenter() {

    }

    private boolean isRelease;

    @Override
    public void initView() {
        mGoodInfo = (GoodsInfo) getIntent().getSerializableExtra(GOOD_INFO);
        if (mGoodInfo == null) {
            isRelease = true;
            mGoodInfo = new GoodsInfo();
        }
        if (userInfo.getGrade_id() != AppConfig.StoreGrade.MARKET) {
            marketPriceLayout.setVisibility(View.VISIBLE);
        } else {
            goodsDetailPicsLayout.setVisibility(View.GONE);
        }
        if (userInfo.getGrade_id() == AppConfig.StoreType.FOOD_STREET) {
            llSpecAttr.setVisibility(View.VISIBLE);
        } else {
            llSpecAttr.setVisibility(View.GONE);
        }
        lstGoodsIntroducePic.add("");
        //商品分类
        catePickerView = new OptionsPickerView.Builder(mContext, ReleaseActivity.this).build();
        //商品图片(头部)
        viewPager.setAdapter(picPagerAdapter);
        //商品详情图(底部)
        takePhotoRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        takePhotoRecyclerView.addItemDecoration(new SpacesItemDecoration(8));
        takePhotoRecyclerView.setHasFixedSize(true);
        takePhotoRecyclerView.setNestedScrollingEnabled(false);
        mPhotoAdapter = new PhotoAdapter(lstGoodsIntroducePic, mContext);
        takePhotoRecyclerView.setAdapter(mPhotoAdapter);
    }

    @Override
    public void fillData() {
        if (!isRelease) { //更新商品
            releaseBtn.setText("更新");
            rlAddPic.setVisibility(View.GONE);
            ((View) viewPager.getParent()).setVisibility(View.VISIBLE);
            if (mGoodInfo.getGoods_photo() == null || mGoodInfo.getGoods_photo().size() == 0) {
                lstGoodsPic.add(mGoodInfo.getPhoto());
            } else {
                lstGoodsPic.addAll(mGoodInfo.getGoods_photo());
            }
            tvIndexPic.setText(1 + "/" + lstGoodsPic.size());
            picPagerAdapter.notifyDataSetChanged();
            goodsNameEdit.setText(mGoodInfo.getProduct_name());
            goodsCateTv.setText(mGoodInfo.getCate_name());
            if (mGoodInfo.getSales_promotion_is() == 1) {
                salesPriceEdit.setText(String.format("%.2f", mGoodInfo.getSales_promotion_price() / 100.00));
            }
            if (mGoodInfo.getIs_use_num() == 1) {
                numEdit.setText("" + mGoodInfo.getNum());
            }
            sellingPriceEdit.setText(String.format("%.2f", mGoodInfo.getPrice() / 100.00));

            etSubTitle.setText(mGoodInfo.getSubhead());
            goodsDescEdit.setText(mGoodInfo.getDesc());

        }
        if (userInfo.getGrade_id() != AppConfig.StoreGrade.MARKET) {
            priceEdit.setText(String.format("%.2f", mGoodInfo.getMall_price() / 100.00));
            if (null != mGoodInfo.getPhotos()) {
                lstGoodsIntroducePic.addAll(mGoodInfo.getPhotos());
                photoSelectNumTv.setText(lstGoodsIntroducePic.size() - 1 + "/" + MAX_INTRODUCE_PIC_NUM);
                mPhotoAdapter.notifyDataSetChanged();
            }
        }
        if (userInfo.getGrade_id() == AppConfig.StoreType.FOOD_STREET) {//美食街
            llPackingFee.setVisibility(View.VISIBLE);
            etPackingFee.setText(String.format("%.2f", mGoodInfo.getCasing_price() / 100.00));
        }else {
            llPackingFee.setVisibility(View.GONE);
        }
    }

    @Override
    public void setListener() {
        //是否促销
        openSalesSb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isSalesLayout.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });
        //是否开启库存
        openInventorySb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isInventoryLayout.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });
        //是否多规格
        openSpecSb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isSpecLayout.setVisibility(b ? View.VISIBLE : View.GONE);
                openSpecToGoneLayout.setVisibility(b ? View.GONE : View.VISIBLE);
            }
        });
        //商品详情图(底部)
        mPhotoAdapter.setOnItemClickListener(R.id.iv_delete, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, final int position) {
                new AlertDialog(mContext).builder()
                        .setTitle("提示")
                        .setMsg("删除此照片？")
                        .setNegativeButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lstGoodsIntroducePic.remove(mPhotoAdapter.getItem(position));
                                mPhotoAdapter.notifyDataSetChanged();
                                photoSelectNumTv.setText(lstGoodsIntroducePic.size() - 1 + "/" + MAX_INTRODUCE_PIC_NUM);
                            }
                        }).show();
            }
        });
        //商品详情图(底部)
        mPhotoAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                if (position == 0) {
                    if (lstGoodsIntroducePic.size() - 1 == MAX_INTRODUCE_PIC_NUM) {
                        showShortToast("最多只能选6张哦！");
                        return;
                    }
                    PhotoPicker.builder()
                            .setPhotoCount(MAX_INTRODUCE_PIC_NUM - lstGoodsIntroducePic.size() + 1)
                            .setShowCamera(true)
                            .setPreviewEnabled(false)
                            .start(mActivity, REQUEST_GOODS_INTRODUCE_PIC);
                } else {
                    if (lstGoodsIntroducePic.size() > 0) {
                        ArrayList lstPic = new ArrayList();
                        for (String s : lstGoodsIntroducePic) {
                            if (!TextUtils.isEmpty(s)) {
                                lstPic.add(s);
                            }
                        }
                        PhotoPreview.builder()
                                .setPhotos(lstPic)
                                .setCurrentItem(position - 1)
                                .setShowDeleteButton(false)
                                .start(mActivity);
                    }
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvIndexPic.setText(position + 1 + "/" + lstGoodsPic.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        openSpecSb.setChecked(mGoodInfo.getIs_attr() == 1);//是否开启多规格
        openInventorySb.setChecked(mGoodInfo.getIs_use_num() == 1);//是否开启库存
        openSalesSb.setChecked(mGoodInfo.getSales_promotion_is() == 1);//是否开启促销
    }

    //商品图(头部)
    protected PagerAdapter picPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return lstGoodsPic.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsPictureActivity.actionStart(ReleaseActivity.this, lstGoodsPic, position);
                }
            });
            String goodsPath = lstGoodsPic.get(position);
            if (goodsPath.startsWith("http")) {
                GlideUtils.showBigPic(mContext, imageView, goodsPath);
            } else {
                GlideUtils.displayUri(mContext, imageView, Uri.fromFile(new File(goodsPath)));
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    };

    @OnClick({R.id.rl_add_pic, R.id.iv_add_pic, R.id.release_btn, R.id.add_classify_layout, R.id.choose_cate_layout, R.id.is_spec_layout, R.id.ll_spec_attr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_add_pic:
                PhotoPicker.builder()
                        .setPhotoCount(MAX_GOODS_PIC_NUM)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .start(mActivity, REQUEST_GOODS_PIC);
                break;
            case R.id.iv_add_pic:
                if (lstGoodsPic.size() >= MAX_GOODS_PIC_NUM) {
                    ToastUitl.showShort("最多只能添加" + MAX_GOODS_PIC_NUM + "张图片");
                    return;
                }
                PhotoPicker.builder()
                        .setPhotoCount(MAX_GOODS_PIC_NUM - lstGoodsPic.size())
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .start(mActivity, REQUEST_GOODS_PIC);
                break;
            case R.id.add_classify_layout:
                startActivity(new Intent(mContext, GoodsClassifyActivity.class));
                break;
            case R.id.choose_cate_layout:
                getCateListInfo();
                break;
            case R.id.release_btn:
                if (canRelease())
                    upLoadGoodsPic();
                break;
            case R.id.is_spec_layout:
                Intent intent = new Intent(mContext, AddSpecActivity.class);
                intent.putExtra("isInventory", openInventorySb.isChecked());//是否启用库存
                intent.putExtra("isSales", openSalesSb.isChecked());//是否启用多规格
                intent.putExtra("isShop", userInfo.getGrade_id() != AppConfig.StoreGrade.MARKET);//是否是实体店
                intent.putExtra("isFoodStreet", userInfo.getGrade_id() == AppConfig.StoreType.FOOD_STREET);//是否是美食街
                List<SpecInfo> goodsAttr = mGoodInfo.getGoods_attr();
                if (null != goodsAttr && goodsAttr.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("specList", (Serializable) goodsAttr);
                    intent.putExtras(bundle);
                }
                startActivityForResult(intent, REQUEST_GOODS_ATTR);
                break;
            case R.id.ll_spec_attr:
                ArrayList<GoodsInfo.Nature> goodsNature = mGoodInfo.getNature();
                SelectSubSpecActivity.actionStart(mActivity, goodsNature);
                break;
        }
    }

    //获取分类数据
    private void getCateListInfo() {
        LoadingDialog.showDialogForLoading(mActivity, "加载中...", true);
        new MyHttp().doPost(Api.getDefault().getCateList(userInfo.getSj_login_token()), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                cateInfoList = JSONArray.parseArray(result.getString("data"), CateInfo.class);
                if (null == cateInfoList) {
                    new AlertDialog(mContext).builder()
                            .setTitle("提示")
                            .setMsg("还未添加分类，是否前往添加")
                            .setNegativeButton("不了", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setPositiveButton("前往", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(mContext, GoodsClassifyActivity.class));
                                }
                            }).show();
                } else {
                    catePickerView.setPicker(cateInfoList);//添加数据
                    if (!catePickerView.isShowing()) {
                        catePickerView.show();
                    }
                }
            }

            @Override
            public void onError(int code) {
                if (code == AppConfig.ERROR_STATE.NULLDATA) {
                    new AlertDialog(mContext).builder()
                            .setTitle("提示")
                            .setMsg("尚未添加分类，是否前往添加")
                            .setPositiveButton("前往", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(mContext, GoodsClassifyActivity.class));
                                }
                            }).show();
                }
            }
        });
    }

    /**
     * 上传商品图
     */
    private void upLoadGoodsPic() {
        LoadingDialog.showDialogForLoading(mActivity, "商品上传中...", true);
        UpLoadPicUtils.batchUpload(lstGoodsPic, new UpLoadPicUtils.BatchUpLoadPicListener() {
            @Override
            public void success(List<String> qiNiuPath) {
                mGoodInfo.setPhoto(qiNiuPath.get(0));
                mGoodInfo.setGoods_photo(qiNiuPath);
                if (userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET) {
                    doRelease();
                } else {
                    uploadGoodsDetailPic();
                }
            }

            @Override
            public void error() {
                LoadingDialog.cancelDialogForLoading();
                showShortToast("图片上传失败，请重试。");
            }
        });
    }

    /**
     * 上传商品介绍图
     */
    private void uploadGoodsDetailPic() {
        ArrayList<String> temp = new ArrayList<>();
        temp.addAll(lstGoodsIntroducePic);
        temp.remove(0);
        if (temp.size() == 0) {
            doRelease();//开始发布
        } else {//上传商品详情介绍图
            UpLoadPicUtils.batchUpload(temp, new UpLoadPicUtils.BatchUpLoadPicListener() {
                @Override
                public void success(List<String> qiNiuPath) {
                    mGoodInfo.setPhotos(qiNiuPath);
                    doRelease();//开始发布
                }

                @Override
                public void error() {
                    LoadingDialog.cancelDialogForLoading();
                    showShortToast("图片上传失败，请重试。");
                }
            });
        }
    }

    private void doRelease() {
        Map<String, Object> mParams = new HashMap<>();
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("is_attr", openSpecSb.isChecked() ? 1 : 0);//是否开启多规格
        mGoodInfo.setIs_attr(openSpecSb.isChecked() ? 1 : 0);
        mParams.put("nature", addNature());

        mParams.put("sales_promotion_is", openSalesSb.isChecked() ? 1 : 0);//是否开启促销
        mGoodInfo.setSales_promotion_is(openSalesSb.isChecked() ? 1 : 0);
        float sales = TextUtils.isEmpty(salesPriceEdit.getText().toString().trim()) ? 0.0f : Float.valueOf(salesPriceEdit.getText().toString().trim());//促销价格
        mParams.put("sales_promotion_price", sales);//促销价格
        mGoodInfo.setSales_promotion_price(sales * 100);

        mParams.put("is_use_num", openInventorySb.isChecked() ? 1 : 0);//是否启用库存
        mGoodInfo.setIs_use_num(openInventorySb.isChecked() ? 1 : 0);
        int num = TextUtils.isEmpty(numEdit.getText().toString().trim()) ? 0 : Integer.valueOf(numEdit.getText().toString().trim());//库存数目
        mParams.put("num", num);//库存数目
        mGoodInfo.setNum(num);

        String subHead = etSubTitle.getText().toString().trim();//副标题
        mParams.put("subhead", subHead);
        mGoodInfo.setSubhead(subHead);

        if (userInfo.getGrade_id() == AppConfig.StoreType.FOOD_STREET) {//美食街
            float CasingPrice = TextUtils.isEmpty(etPackingFee.getText().toString().trim()) ? 0.0f : Float.valueOf(etPackingFee.getText().toString().trim());//包装费
            mParams.put("casing_price", CasingPrice);//包装费
            mGoodInfo.setCasing_price(CasingPrice * 100);
        }

        float price = TextUtils.isEmpty(sellingPriceEdit.getText().toString().trim()) ? 0.0f : Float.valueOf(sellingPriceEdit.getText().toString().trim());//包装费用
        mParams.put("price", price);//市场价格
        mGoodInfo.setPrice(price * 100);

        String title = goodsNameEdit.getText().toString().trim();//商品名称
        mParams.put(userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET ? "product_name" : "title", title);
        mGoodInfo.setProduct_name(title);

        if (userInfo.getGrade_id() != AppConfig.StoreGrade.MARKET) {
            float mallPrice = TextUtils.isEmpty(priceEdit.getText().toString().trim()) ? 0.0f : Float.valueOf(priceEdit.getText().toString().trim());//实体店商城价格
            mParams.put("mall_price", mallPrice);
            mGoodInfo.setMall_price(mallPrice * 100);
        }

        String desc = goodsDescEdit.getText().toString().trim();//商品描述
        mParams.put(userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET ? "desc" : "details", desc);
        mGoodInfo.setDesc(desc);

        mParams.put("photo", mGoodInfo.getGoods_photo() == null ? "" : TextUtils.join(",", mGoodInfo.getGoods_photo()));//商品图片(头部)
        mParams.put("photos", mGoodInfo.getPhotos() == null ? "" : TextUtils.join(",", mGoodInfo.getPhotos()));//商品详情图(底部)
        mParams.put("goods_attr", JSONArray.toJSON(mGoodInfo.getGoods_attr()));

        mParams.put(userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET ? "cate_id" : "shopcate_id", mGoodInfo.getCate_id());//商品分类
        mParams.put(userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET ? "product_id" : "goods_id", mGoodInfo.getProduct_id());//产品ID
        new MyHttp().doPost(Api.getDefault().releaseGoods(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                String da = result.getString("data");
                if (!TextUtils.isEmpty(da)) {
                    List<SpecInfo> data = JSONArray.parseArray(result.getString("data"), SpecInfo.class);
                    if (data.get(0).getMin_price() != 0) {
                        if (BaseApplication.getInstance().readLoginUser().getGrade_id() == AppConfig.StoreGrade.MARKET) {
                            mGoodInfo.setPrice(data.get(0).getMin_price());
                        } else {
                            mGoodInfo.setMall_price(data.get(0).getMin_price());
                        }
                        if (mGoodInfo.getSales_promotion_is() == 1) {
                            mGoodInfo.setSales_promotion_price(data.get(0).getMin_price());
                        }
                    }
                    for (SpecInfo info : data) {
                        info.setPrice(info.getPrice() / 100);
                        info.setCasing_price(info.getCasing_price() / 100);
                        info.setSeckill_price(info.getSeckill_price() / 100);
                        info.setMall_price(info.getMall_price() / 100);
                        info.setSales_promotion_price(info.getSales_promotion_price() / 100);
                        info.setShop_member_price(info.getShop_member_price() / 100);
                        info.setMin_price(info.getMin_price() / 100);
                    }
                    mGoodInfo.setGoods_attr(data);
                }
                Intent intent = new Intent();
                intent.putExtra(RESULT_GOODS, mGoodInfo);
                setResult(RESULT_OK, intent);
                showShortToast(isRelease ? "商品添加成功！" : "更改成功！");
                finish();
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    private String addNature() {
        if (mGoodInfo.getNature() == null) {
            return "";
        }
        if (mGoodInfo.getNature().size() > 0) {
            ArrayList<GoodsInfo.Nature> natureList = mGoodInfo.getNature();
            StringBuilder sbSubSpec = new StringBuilder();
            sbSubSpec.append("[");
            for (GoodsInfo.Nature nature : natureList) {
                sbSubSpec.append("{");
                sbSubSpec.append("\"");
                sbSubSpec.append(nature.getItem_id());
                sbSubSpec.append("\"");
                sbSubSpec.append(":");
                sbSubSpec.append("\"");
                for (GoodsInfo.Nature_attr nature_attr : nature.getNature_arr()) {
                    sbSubSpec.append(nature_attr.getNature_id());
                    sbSubSpec.append(",");
                }
                sbSubSpec.deleteCharAt(sbSubSpec.toString().length() - 1);
                sbSubSpec.append("\"");
                sbSubSpec.append("}");
                sbSubSpec.append(",");
            }
            sbSubSpec.deleteCharAt(sbSubSpec.toString().length() - 1);
            sbSubSpec.append("]");
            return sbSubSpec.toString();
        } else {
            return "";
        }
    }

    private boolean canRelease() {
        if (lstGoodsPic.size() == 0) {
            showShortToast("请添加产品图");
            return false;
        }
        if (TextUtils.isEmpty(goodsNameEdit.getText().toString().trim())) {
            showShortToast("请输入标题");
            return false;
        }
        if (TextUtils.isEmpty(sellingPriceEdit.getText().toString().trim()) && !openSpecSb.isChecked()) {
            showShortToast("请输入商品价格");
            return false;
        }
        if (TextUtils.isEmpty(goodsCateTv.getText().toString().trim())) {
            showShortToast("请选择商品分类");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_GOODS_PIC) {//商品图(头部)
                if (((View) viewPager.getParent()).getVisibility() != View.VISIBLE) {
                    ((View) viewPager.getParent()).setVisibility(View.VISIBLE);
                    rlAddPic.setVisibility(View.GONE);
                }
                ArrayList<String> pics = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                for (String pic : pics) {
                    if (!lstGoodsPic.contains(pic)) {
                        lstGoodsPic.add(pic);
                    }
                }
                tvIndexPic.setText(viewPager.getCurrentItem() + 1 + "/" + lstGoodsPic.size());
                picPagerAdapter.notifyDataSetChanged();
            } else if (requestCode == REQUEST_GOODS_INTRODUCE_PIC) {//商品详情图(底部)
                ArrayList<String> stringArrayListExtra = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                lstGoodsIntroducePic.addAll(stringArrayListExtra);
                photoSelectNumTv.setText(lstGoodsIntroducePic.size() - 1 + "/" + MAX_INTRODUCE_PIC_NUM);
                mPhotoAdapter.notifyDataSetChanged();
            } else if (requestCode == GoodsPictureActivity.REQUEST_CODE_SHOW_AND_CHANGE_PICTURE) {//拍照返回
                ArrayList<String> lstPhoto = data.getStringArrayListExtra("photos");
                lstGoodsPic.clear();
                lstGoodsPic.addAll(lstPhoto);
                picPagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0, true);
                tvIndexPic.setText(1 + "/" + lstGoodsPic.size());
                if (lstGoodsPic.size() == 0) {
                    rlAddPic.setVisibility(View.VISIBLE);
                    ((View) viewPager.getParent()).setVisibility(View.GONE);
                } else {
                    rlAddPic.setVisibility(View.GONE);
                    ((View) viewPager.getParent()).setVisibility(View.VISIBLE);
                }
            } else if (requestCode == SelectSubSpecActivity.REQUEST_SELECT_SUB_SPEC) {//多属性
                ArrayList<GoodsInfo.Nature> nature = (ArrayList<GoodsInfo.Nature>) data.getSerializableExtra("nature");
                mGoodInfo.setNature(nature);
            } else if (requestCode == REQUEST_GOODS_ATTR) {//多规格返回
                List<SpecInfo> infos = (List<SpecInfo>) data.getSerializableExtra("specList");
                mGoodInfo.setGoods_attr(infos);
            }
        }
    }

    /**
     * 添加商品分类回调,更新分类文本
     */
    @Override
    public void onOptionsSelect(int options1, int options2, int options3, View v) {
        mGoodInfo.setCate_id(cateInfoList.get(options1).getCate_id());
        mGoodInfo.setCate_name(cateInfoList.get(options1).getCate_name());
        goodsCateTv.setText(cateInfoList.get(options1).getCate_name());
    }
}

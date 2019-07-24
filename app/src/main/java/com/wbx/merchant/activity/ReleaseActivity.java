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
import android.widget.RelativeLayout;
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
import com.wbx.merchant.baseapp.AppConfig;
import com.wbx.merchant.bean.CateInfo;
import com.wbx.merchant.bean.GoodsInfo;
import com.wbx.merchant.bean.SpecInfo;
import com.wbx.merchant.utils.ArithUtils;
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

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by wushenghui on 2017/6/21.
 */

public class ReleaseActivity extends BaseActivity implements OptionsPickerView.OnOptionsSelectListener {
    @Bind(R.id.take_photo_recycler_view)
    RecyclerView takePhotoRecyclerView;
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
    TextView photoSelectNumTv;
    @Bind(R.id.ll_packing_fee)
    LinearLayout llPackingFee;
    @Bind(R.id.et_packing_fee)
    EditText etPackingFee;
    @Bind(R.id.rl_add_pic)
    RelativeLayout rlAddPic;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.tv_index_pic)
    TextView tvIndexPic;
    @Bind(R.id.is_sales_layout)
    LinearLayout isSalesLayout;//开启促销价
    @Bind(R.id.open_sales_sb)
    SwitchButton openSalesSb;
    @Bind(R.id.is_inventory_layout)
    LinearLayout isInventoryLayout;//开启库存
    @Bind(R.id.open_inventory_sb)
    SwitchButton openInventorySb;
    @Bind(R.id.is_spec_layout)
    LinearLayout isSpecLayout;//添加多规格
    @Bind(R.id.ll_spec_attr)
    LinearLayout llSpecAttr;
    @Bind(R.id.open_spec_sb)
    SwitchButton openSpecSb;
    @Bind(R.id.settings_spec_layout)
    LinearLayout settingsSpecLayout;
    @Bind(R.id.open_spec_to_gone_layout)
    LinearLayout openSpecToGoneLayout;
    @Bind(R.id.num_edit)
    EditText numEdit;
    @Bind(R.id.sales_price_edit)
    EditText salesPriceEdit;
    private static final int REQUEST_GOODS_PIC = 1001;//商品图
    private static final int REQUEST_GOODS_INTRODUCE_PIC = 1002;//商品介绍图l
    public static final int MAX_GOODS_PIC_NUM = 5; //商品图最多5张
    private static final int MAX_INTRODUCE_PIC_NUM = 6; //介绍图最多6张
    private PhotoAdapter mAdapter;
    private HashMap<String, Object> mParams = new HashMap<>();
    private List<CateInfo> cateInfoList = new ArrayList<>();
    private OptionsPickerView catePickerView;
    private GoodsInfo goods;
    private ArrayList<String> lstGoodsIntroducePic = new ArrayList<>();//商品介绍图
    private ArrayList<String> lstGoodsPic = new ArrayList<>();
    ArrayList<GoodsInfo.Nature> lstSelectNature = new ArrayList<>();//多属性
    private boolean isInventory;
    private boolean isSales;
    private boolean isSpec;
    private List<SpecInfo> specList;
    public static String RESULT_GOODS = "result_goods";

    @Override
    public int getLayoutId() {
        return R.layout.activity_release;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        if (userInfo.getGrade_id() != AppConfig.StoreGrade.MARKET) {
            marketPriceLayout.setVisibility(View.VISIBLE);
        } else {
            goodsDetailPicsLayout.setVisibility(View.GONE);
        }
        if (userInfo.getGrade_id() == AppConfig.StoreType.FOOD_STREET) {
            llPackingFee.setVisibility(View.VISIBLE);
            llSpecAttr.setVisibility(View.VISIBLE);
        } else {
            llPackingFee.setVisibility(View.GONE);
            llSpecAttr.setVisibility(View.GONE);
        }
        catePickerView = new OptionsPickerView.Builder(mContext, ReleaseActivity.this).build();
        viewPager.setAdapter(picPagerAdapter);
        lstGoodsIntroducePic.add("");
        takePhotoRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        takePhotoRecyclerView.addItemDecoration(new SpacesItemDecoration(8));
        takePhotoRecyclerView.setHasFixedSize(true);
        takePhotoRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new PhotoAdapter(lstGoodsIntroducePic, mContext);
        takePhotoRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fillData() {
        goods = (GoodsInfo) getIntent().getSerializableExtra("goods");
        if (null != goods) {
            rlAddPic.setVisibility(View.GONE);
            ((View) viewPager.getParent()).setVisibility(View.VISIBLE);
            if (goods.getGoods_photo() == null || goods.getGoods_photo().size() == 0) {
                lstGoodsPic.add(goods.getPhoto());
            } else {
                lstGoodsPic.addAll(goods.getGoods_photo());
            }
            tvIndexPic.setText(1 + "/" + lstGoodsPic.size());
            picPagerAdapter.notifyDataSetChanged();
            goodsNameEdit.setText(goods.getProduct_name());
            goodsCateTv.setText(goods.getCate_name());
            if (goods.getSales_promotion_is() == 1) {
                salesPriceEdit.setText(String.format("%.2f", goods.getSales_promotion_price() / 100.00));
            }
            if (goods.getIs_use_num() == 1) {
                numEdit.setText("" + goods.getNum());
            }
            mParams.put("sj_login_token", userInfo.getSj_login_token());//token
            mParams.put("photo", TextUtils.join(",", lstGoodsPic));
            sellingPriceEdit.setText(String.format("%.2f", goods.getPrice() / 100.00));
            etPackingFee.setText(String.format("%.2f", goods.getCasing_price() / 100.00));
            //实体店    mParams.put("photos",join);//商品详情图
            if (null != goods.getGoods_attr()) {
                List<SpecInfo> goods_attr = new ArrayList<>();
                goods_attr.addAll(goods.getGoods_attr());
                for (SpecInfo specInfo : goods_attr) {
                    specInfo.setMall_price(specInfo.getMall_price() / 100.00);
                    specInfo.setPrice(specInfo.getPrice() / 100.00);
                    specInfo.setSales_promotion_price(specInfo.getSales_promotion_price() / 100.00);
                    specInfo.setCasing_price(specInfo.getCasing_price() / 100.00);
                }
                mParams.put("goods_attr", JSONArray.toJSON(goods.getGoods_attr()));
            }
            if (userInfo.getGrade_id() == AppConfig.StoreType.FOOD_STREET) {
                if (goods.getNature() != null && goods.getNature().size() > 0) {
                    lstSelectNature.clear();
                    lstSelectNature.addAll(goods.getNature());
                }
            }
            goodsDescEdit.setText(goods.getDesc());
            if (userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET) {
                //菜市场
                mParams.put("product_name", goods.getProduct_name());
                mParams.put("cate_id", goods.getCate_id());
                mParams.put("product_id", goods.getProduct_id());
                mParams.put("price", goods.getPrice());
            } else {
                mParams.put("title", goods.getTitle());
                mParams.put("shopcate_id", goods.getCate_id());
                mParams.put("goods_id", goods.getProduct_id());
//                priceEdit.setText(String.format("%.2f", goods.getMall_price() / 100.00));
                priceEdit.setText((float) ArithUtils.round(goods.getMall_price() / 100, 2) + "");
                sellingPriceEdit.setText(String.format("%.2f", goods.getPrice() / 100.00));
                if (null != goods.getPhotos()) {
                    lstGoodsIntroducePic.addAll(goods.getPhotos());
                    photoSelectNumTv.setText(lstGoodsIntroducePic.size() - 1 + "/" + MAX_INTRODUCE_PIC_NUM);
                    mAdapter.notifyDataSetChanged();
                    mParams.put("photos", TextUtils.join(",", goods.getPhotos()));
                }
            }
            releaseBtn.setText("更新");
        }
    }

    @Override
    public void setListener() {
        mAdapter.setOnItemClickListener(R.id.iv_delete, new BaseAdapter.ItemClickListener() {
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
                                lstGoodsIntroducePic.remove(mAdapter.getItem(position));
                                mAdapter.notifyDataSetChanged();
                                photoSelectNumTv.setText(lstGoodsIntroducePic.size() - 1 + "/" + MAX_INTRODUCE_PIC_NUM);
                            }
                        }).show();
            }
        });
        isSpecLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddSpecActivity.class);
                intent.putExtra("isInventory", isInventory);//是否启用库存
                intent.putExtra("isSales", isSales);//是否启用多规格
                intent.putExtra("isShop", userInfo.getGrade_id() != AppConfig.StoreGrade.MARKET);//是否是实体店
                intent.putExtra("isFoodStreet", userInfo.getGrade_id() == AppConfig.StoreType.FOOD_STREET);//是否是美食街
                if (null != specList) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("specList", (Serializable) specList);
                    intent.putExtras(bundle);
                } else if (null != goods) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("specList", (Serializable) goods.getGoods_attr());
                    intent.putExtras(bundle);
                }
                startActivityForResult(intent, 1004);
            }
        });
        llSpecAttr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectSubSpecActivity.actionStart(mActivity, lstSelectNature);
            }
        });
        openSpecSb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isSpec = b;
                if (b) {
                    isSpecLayout.setVisibility(View.VISIBLE);
                    openSpecToGoneLayout.setVisibility(View.GONE);
                } else {
                    isSpecLayout.setVisibility(View.GONE);
                    openSpecToGoneLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        openInventorySb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isInventory = b;
                if (b) {
                    isInventoryLayout.setVisibility(View.VISIBLE);
                } else {
                    isInventoryLayout.setVisibility(View.GONE);
                }
            }
        });
        openSalesSb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isSales = b;
                if (b) {
                    isSalesLayout.setVisibility(View.VISIBLE);
                } else {
                    isSalesLayout.setVisibility(View.GONE);
                }
            }
        });
        mAdapter.setOnItemClickListener(R.id.root_view, new BaseAdapter.ItemClickListener() {
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
        if (null != goods) {
            openSpecSb.setChecked(goods.getIs_attr() != 0);//是否开启多规格
            openInventorySb.setChecked(goods.getIs_use_num() != 0);//是否开启库存
            openSalesSb.setChecked(goods.getSales_promotion_is() != 0);//是否开启促销
        }
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
    }

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

    @OnClick({R.id.rl_add_pic, R.id.iv_add_pic, R.id.release_btn, R.id.add_classify_layout, R.id.choose_cate_layout})
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
            case R.id.release_btn:
                if (!canRelease()) {
                    return;
                }
                upLoadGoodsPic();
                break;
            case R.id.add_classify_layout:
                startActivity(new Intent(mContext, GoodsClassifyActivity.class));
                break;
            case R.id.choose_cate_layout:
                getCateListInfo();
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
                //通用参数
                mParams.put("photo", TextUtils.join(",", qiNiuPath));//商品图
                mParams.put("sj_login_token", userInfo.getSj_login_token());//token
                if (null != goods) {
                    goods.setPhoto(qiNiuPath.get(0));
                    goods.setGoods_photo(qiNiuPath);
                    goods.setProduct_name(goodsNameEdit.getText().toString());
                }
                if (userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET) {
                    //菜市场所需参数
                    mParams.put("product_name", goodsNameEdit.getText().toString());
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
            mParams.put("title", goodsNameEdit.getText().toString());//商品名称
            mParams.put("mall_price", priceEdit.getText().toString());//商城价格
            mParams.put("details", goodsDescEdit.getText().toString());//商品描述
            if (null != goods) {
                if (TextUtils.isEmpty(priceEdit.getText().toString())) {
                    goods.setMall_price(0);
                } else {
                    float aDouble = Float.valueOf(priceEdit.getText().toString()) * 100;
                    goods.setMall_price((int) aDouble);
                }
                goods.setDetails(goodsDescEdit.getText().toString());
            }
            doRelease();//开始发布
        } else {
            UpLoadPicUtils.batchUpload(temp, new UpLoadPicUtils.BatchUpLoadPicListener() {
                @Override
                public void success(List<String> qiNiuPath) {
                    String strGoodsDetailPics = TextUtils.join(",", qiNiuPath);
                    mParams.put("title", goodsNameEdit.getText().toString());//商品名称
                    mParams.put("photos", strGoodsDetailPics);//商品详情图
                    mParams.put("mall_price", priceEdit.getText().toString());//商城价格
                    mParams.put("details", goodsDescEdit.getText().toString());//商品描述
                    if (null != goods) {
                        goods.setPhotos(qiNiuPath);
                        if (TextUtils.isEmpty(priceEdit.getText().toString())) {
                            goods.setMall_price(0);
                        } else {
                            float aDouble = Float.valueOf(priceEdit.getText().toString()) * 100;
                            goods.setMall_price((int) aDouble);
                        }
                        goods.setDetails(goodsDescEdit.getText().toString());
                    }
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
        if (!isSpec) {
            mParams.remove("goods_attr");
        }
        mParams.put("is_attr", isSpec ? 1 : 0);
        addNature();
        mParams.put("sales_promotion_is", isSales ? 1 : 0);//是否开启促销
        goods.setSales_promotion_is(isSales ? 1 : 0);
        float sales = TextUtils.isEmpty(salesPriceEdit.getText().toString().trim()) ? 0.0f : Float.valueOf(salesPriceEdit.getText().toString().trim()) * 100;//促销价格
        goods.setSales_promotion_price(sales);
        mParams.put("sales_promotion_price", salesPriceEdit.getText().toString());

        mParams.put("is_use_num", isInventory ? 1 : 0);//是否启用库存
        goods.setIs_use_num(isInventory ? 1 : 0);
        float num = TextUtils.isEmpty(numEdit.getText().toString().trim()) ? 0.0f : Float.valueOf(numEdit.getText().toString().trim());//库存
        goods.setNum(num);
        mParams.put("num", numEdit.getText().toString());

        mParams.put("casing_price", etPackingFee.getText().toString());//包装费
        float casing = TextUtils.isEmpty(etPackingFee.getText().toString().trim()) ? 0.0f : Float.valueOf(etPackingFee.getText().toString().trim()) * 100;//包装费用
        goods.setCasing_price(casing);

        float price = TextUtils.isEmpty(sellingPriceEdit.getText().toString().trim()) ? 0.0f : Float.valueOf(sellingPriceEdit.getText().toString().trim()) * 100;//包装费用
        goods.setPrice(price);//市场价

        goods.setDesc(goodsDescEdit.getText().toString());

        if (!isSpec) {
            //启用多规格
            if (isSales) {
                if (TextUtils.isEmpty(salesPriceEdit.getText().toString())) {
                    showShortToast("请输入促销价");
                    LoadingDialog.cancelDialogForLoading();
                    return;
                } else {
                    if (Double.valueOf(salesPriceEdit.getText().toString()) <= 0) {
                        showShortToast("促销价格不能小于0");
                        LoadingDialog.cancelDialogForLoading();
                        return;
                    }
                }
            }
        }
        mParams.put("sj_login_token", userInfo.getSj_login_token());
        mParams.put("price", sellingPriceEdit.getText().toString());//市场价格
        mParams.put(userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET ? "product_name" : "title", goodsNameEdit.getText().toString());
        if (userInfo.getGrade_id() != AppConfig.StoreGrade.MARKET) {
            mParams.put("mall_price", priceEdit.getText().toString());//实体店商城价格}
        }
        mParams.put(userInfo.getGrade_id() != AppConfig.StoreGrade.MARKET ? "details" : "desc", goodsDescEdit.getText().toString());//商品描述
        new MyHttp().doPost(Api.getDefault().releaseGoods(mParams), new HttpListener() {
            @Override
            public void onSuccess(JSONObject result) {
                showShortToast(goods == null ? "商品添加成功！" : "更改成功！");
                Intent intent = new Intent();
                intent.putExtra(RESULT_GOODS, goods);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(int code) {
            }
        });
    }

    private void addNature() {
        if (lstSelectNature != null && lstSelectNature.size() > 0) {
            StringBuilder sbSubSpec = new StringBuilder();
            sbSubSpec.append("[");
            for (GoodsInfo.Nature nature : lstSelectNature) {
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
            mParams.put("nature", sbSubSpec.toString());
        } else {
            mParams.put("nature", "");
        }
    }

    private boolean canRelease() {
        if (lstGoodsPic.size() == 0 && null == goods) {
            showShortToast("请添加产品图");
            return false;
        }
        if (TextUtils.isEmpty(goodsNameEdit.getText().toString().trim())) {
            showShortToast("请输入商品名称");
            return false;
        }
        if (TextUtils.isEmpty(sellingPriceEdit.getText().toString()) && !isSpec) {
            showShortToast("请输入商品价格");
            return false;
        }
        if (TextUtils.isEmpty(goodsCateTv.getText().toString())) {
            showShortToast("请选择商品分类");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_GOODS_PIC) {
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
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_GOODS_INTRODUCE_PIC) {
            ArrayList<String> stringArrayListExtra = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            lstGoodsIntroducePic.addAll(stringArrayListExtra);
            photoSelectNumTv.setText(lstGoodsIntroducePic.size() - 1 + "/" + MAX_INTRODUCE_PIC_NUM);
            mAdapter.notifyDataSetChanged();
        } else if (resultCode == RESULT_OK && requestCode == GoodsPictureActivity.REQUEST_CODE_SHOW_AND_CHANGE_PICTURE) {
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
        } else if (resultCode == RESULT_OK && requestCode == SelectSubSpecActivity.REQUEST_SELECT_SUB_SPEC) {
            ArrayList<GoodsInfo.Nature> nature = (ArrayList<GoodsInfo.Nature>) data.getSerializableExtra("nature");
            lstSelectNature.clear();
            if (nature != null && nature.size() > 0) {
                lstSelectNature.addAll(nature);
            }
        } else {
            //多规格返回
            specList = (List<SpecInfo>) data.getSerializableExtra("specList");
            if (null != specList) {
                mParams.put("goods_attr", JSONArray.toJSON(specList));
                if (null != goods) {
                    goods.setGoods_attr(specList);
                }
            }
        }
    }

    @Override
    public void onOptionsSelect(int options1, int options2, int options3, View v) {
        if (userInfo.getGrade_id() == AppConfig.StoreGrade.MARKET) {
            mParams.put("cate_id", cateInfoList.get(options1).getCate_id());
        } else {
            mParams.put("shopcate_id", cateInfoList.get(options1).getCate_id());
        }
        goodsCateTv.setText(cateInfoList.get(options1).getCate_name());
        goods.setCate_name(cateInfoList.get(options1).getCate_name());
    }
}

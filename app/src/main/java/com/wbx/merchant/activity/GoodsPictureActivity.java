package com.wbx.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.wbx.merchant.R;
import com.wbx.merchant.adapter.GoodsPictureAdapter;
import com.wbx.merchant.base.BaseActivity;
import com.wbx.merchant.utils.GlideUtils;
import com.wbx.merchant.widget.decoration.SpacesItemDecoration;
import com.wbx.merchant.widget.iosdialog.AlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

public class GoodsPictureActivity extends BaseActivity {
    public static final int REQUEST_CODE_SHOW_AND_CHANGE_PICTURE = 1008;
    private static final int REQUEST_GET_BUSINESS_CIRCLE_PHOTO = 1001;
    @Bind(R.id.iv_pic)
    ImageView ivPic;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private GoodsPictureAdapter adapter;
    private ArrayList<String> lstPhoto;
    private int currentPosition;

    public static void actionStart(Activity context, ArrayList<String> lstPhoto, int currentPosition) {
        Intent intent = new Intent(context, GoodsPictureActivity.class);
        intent.putExtra("photos", lstPhoto);
        intent.putExtra("currentPosition", currentPosition);
        context.startActivityForResult(intent, REQUEST_CODE_SHOW_AND_CHANGE_PICTURE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_picture;
    }

    @Override
    public void initPresenter() {
        lstPhoto = (ArrayList<String>) getIntent().getSerializableExtra("photos");
        currentPosition = getIntent().getIntExtra("currentPosition", 0);
    }

    @Override
    public void initView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.addItemDecoration(new SpacesItemDecoration(8));
        recyclerView.setLayoutManager(layoutManager);
        if (lstPhoto.size() != ReleaseActivity2.MAX_GOODS_PIC_NUM) {
            lstPhoto.add("");
        }
        adapter = new GoodsPictureAdapter(this, lstPhoto);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        if (lstPhoto.get(currentPosition).startsWith("http")) {
            GlideUtils.showBigPic(mContext, ivPic, lstPhoto.get(currentPosition));
        } else {
            GlideUtils.displayUri(mContext, ivPic, Uri.fromFile(new File(lstPhoto.get(currentPosition))));
        }
    }

    @Override
    public void fillData() {

    }

    @Override
    public void setListener() {

    }

    @OnClick(R.id.rl_right)
    public void onViewClicked() {
        if (TextUtils.isEmpty(lstPhoto.get(lstPhoto.size() - 1))) {
            lstPhoto.remove(lstPhoto.size() - 1);
        }
        Intent intent = new Intent();
        intent.putExtra("photos", lstPhoto);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void back(View view) {
        new AlertDialog(mContext).builder().setTitle("提示").setMsg("退出此次编辑？").setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).setPositiveButton("退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).show();
    }

    public void addPhoto() {
        PhotoPicker.builder().setShowCamera(true).setPhotoCount(ReleaseActivity2.MAX_GOODS_PIC_NUM - lstPhoto.size() + 1).setPreviewEnabled(true).start(this, REQUEST_GET_BUSINESS_CIRCLE_PHOTO);
    }

    public void showPicDetail(int position) {
        String path = lstPhoto.get(position);
        if (path.startsWith("http")) {
            GlideUtils.showBigPic(mContext, ivPic, path);
        } else {
            GlideUtils.displayUri(mContext, ivPic, Uri.fromFile(new File(path)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQUEST_GET_BUSINESS_CIRCLE_PHOTO) {
            ArrayList<String> pics = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            for (String pic : pics) {
                if (!lstPhoto.contains(pic)) {
                    lstPhoto.add(lstPhoto.size() - 1, pic);
                }
            }
            if (lstPhoto.size() == ReleaseActivity2.MAX_GOODS_PIC_NUM + 1) {
                lstPhoto.remove(ReleaseActivity2.MAX_GOODS_PIC_NUM);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int toPosition = target.getAdapterPosition();
            //拖动到加号的后面
            if (target.getAdapterPosition() == lstPhoto.size() - 1 && TextUtils.isEmpty(lstPhoto.get(lstPhoto.size() - 1))) {
                toPosition--;
            }
            //得到当拖拽的viewHolder的Position
            int fromPosition = viewHolder.getAdapterPosition();
            //拿到当前拖拽到的item的viewHolder
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(lstPhoto, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(lstPhoto, i, i - 1);
                }
            }
            adapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            adapter.notifyDataSetChanged();
        }
    };
}

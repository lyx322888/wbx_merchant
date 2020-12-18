package com.wbx.merchant.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wbx.merchant.R;
import com.wbx.merchant.adapter.CriclePayAdapter;
import com.wbx.merchant.bean.CricePayBaen;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**

 */
public class TkfwDialog extends DialogFragment {

    @Bind(R.id.rv_price)
    RecyclerView rvPrice;
    private DialogListener listener;
    CriclePayAdapter payAdapter;

    public static TkfwDialog newInstance() {
        TkfwDialog uploadAccreditationDialog = new TkfwDialog();
        Bundle bundle = new Bundle();
        uploadAccreditationDialog.setArguments(bundle);
        return uploadAccreditationDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tkfw, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        rvPrice.setLayoutManager(new GridLayoutManager(getContext(),3));
        payAdapter = new CriclePayAdapter();
        rvPrice.setAdapter(payAdapter);
        payAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                payAdapter.Select(position);
               if (listener!=null){
                   listener.dialogClickListener(payAdapter.getItem(position));
                   dismiss();
               }
            }
        });

        ArrayList<CricePayBaen> arrayList = new ArrayList<>();
        arrayList.add(new CricePayBaen("100",false));
        arrayList.add(new CricePayBaen("300",false));
        arrayList.add(new CricePayBaen("500",false));
        arrayList.add(new CricePayBaen("1000",false));
        arrayList.add(new CricePayBaen("3000",false));

        payAdapter.setNewData(arrayList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
    }

    public interface DialogListener {
        void dialogClickListener(CricePayBaen cricePayBaen);
    }
}

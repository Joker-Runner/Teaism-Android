package com.luy.teaism.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luy.teaism.R;
import com.luy.teaism.bean.TeaFoodBean;

import java.util.List;

/**
 * Created by joker on 6/27 0027.
 */

public class TeaFoodModuleAdapter extends BaseQuickAdapter<TeaFoodBean,BaseViewHolder> {
    public TeaFoodModuleAdapter(@Nullable List<TeaFoodBean> data) {
        super(R.layout.item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeaFoodBean item) {
        helper.setText(R.id.text,item.getTitle());
        Glide.with(mContext).load(R.drawable.google).centerCrop().
                into((ImageView) helper.getView(R.id.image));
    }
}

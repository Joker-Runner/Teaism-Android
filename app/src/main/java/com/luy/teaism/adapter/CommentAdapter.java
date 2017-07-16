package com.luy.teaism.adapter;

import android.support.annotation.Nullable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luy.teaism.R;
import com.luy.teaism.bean.CommentUserBean;
import com.luy.teaism.utils.Utils;

import java.util.List;

/**
 * 评论列表的适配器
 * Created by joker on 7/11 0011.
 */

public class CommentAdapter extends BaseQuickAdapter<CommentUserBean, BaseViewHolder> {
    CompoundButton goodToggle;
    TextView goodCount;

    public CommentAdapter(@Nullable List<CommentUserBean> data) {
        super(R.layout.comment_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentUserBean item) {
        goodToggle = helper.getView(R.id.good_toggle_button);
        goodCount = helper.getView(R.id.comment_good_count);
        helper.setText(R.id.commentator_name, item.getCommentatorName())
                .setText(R.id.comment_good_count, item.getGoodCount() + "")
                .setText(R.id.comment_content, item.getComment())
                .setText(R.id.create_time, Utils.parseToTime(item.getCreateTime()))// TODO: 7/11 0011 格式
                // TODO: 7/11 0011 是否已点赞
                .setOnCheckedChangeListener(R.id.good_toggle_button, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView == goodToggle) {
                            int count = Integer.parseInt(goodCount.getText().toString());
                            if (isChecked)// TODO: 7/10 0010 点赞
                                goodCount.setText(count + 1 + "");
                            else
                                goodCount.setText(count - 1 + "");
                        }
                    }
                });
        Glide.with(mContext).load(R.drawable.google).centerCrop().
                into((ImageView) helper.getView(R.id.commentator_icon));
    }
}

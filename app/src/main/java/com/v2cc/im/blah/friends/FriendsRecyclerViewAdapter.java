package com.v2cc.im.blah.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.v2cc.im.blah.R;
import com.v2cc.im.blah.base.app.App;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/22.
 * If this class works, I created it. If not, I didn't.
 */
public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> {
    public OnItemClickListener mOnItemClickListener;
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<FriendsBean> list;

    public FriendsRecyclerViewAdapter(Context context, List<FriendsBean> list) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        this.list = list;
    }

    // Provide a reference to the type of views that you are using
    // (custom viewholder)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView alphaView;
        public final ImageView img_avatar;
        public final TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            alphaView = (TextView) itemView.findViewById(R.id.alpha);
            img_avatar = (ImageView) itemView.findViewById(R.id.friends_item_avatar);
            nameView = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friends, parent, false);

        // 增加点击水波纹动画
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        FriendsBean contact = list.get(position);
        String name = contact.getName();
        viewHolder.nameView.setText(name);

        // TODO Friends photos

        // 当前字母
        String currentStr = getAlpha(contact.getSortKey());
        // 前面的字母
        String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
                position - 1).getSortKey()) : " ";
        if (!previewStr.equals(currentStr)) {
            viewHolder.alphaView.setVisibility(View.VISIBLE);
            viewHolder.alphaView.setText(currentStr);
        } else {
            viewHolder.alphaView.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(viewHolder.itemView, position);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemClickListener.onItemLongClick(viewHolder.itemView, position);
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    /**
     * 获取首字母
     *
     * @return Todo 分离提取首字母功能到单独的 Util 类中
     */
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式匹配
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase(); // 将小写字母转换为大写
        } else {
            return "#";
        }
    }
}

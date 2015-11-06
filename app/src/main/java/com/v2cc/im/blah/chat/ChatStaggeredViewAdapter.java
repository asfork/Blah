package com.v2cc.im.blah.chat;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v2cc.im.blah.R;
import com.v2cc.im.blah.base.utils.TimeFormatUtil;
import com.v2cc.im.blah.message.MessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/16.
 * If this class works, I created it. If not, I didn't.
 */
public class ChatStaggeredViewAdapter extends RecyclerView.Adapter<ChatStaggeredViewAdapter.ViewHolder> {

    Context context;
    private ArrayList<MessageBean> list;
    private List<Integer> heights;
    private ArrayList<String> colors;

    // Provide a reference to the type of views that you are using
    // (custom viewholder)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvTime;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    public ChatStaggeredViewAdapter(Context context, ArrayList<MessageBean> list) {
        this.context = context;
        this.list = list;

        heights = new ArrayList<>();
        colors = new ArrayList<>();
        Log.d("ChatStaggered", "start" + list.size());
//        if (list != null) {
//            for (int i = 0; i < list.size(); i++) {
//                heights.add((int) (Math.random() * 300) + 300);
//            }
//        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ChatStaggeredViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }

        if (list.size() != heights.size()) {
            Log.d("ChatStaggered", "repeat" + list.size());
            for (int i = heights.size(); i < list.size(); i++) {
                heights.add((int) (Math.random() * 300) + 200);

                // TODO add more colors
                switch (i % 5) {
                    case 0:
                        colors.add("#004D40");
                        break;
                    case 1:
                        colors.add("#880E4F");
                        break;
                    case 2:
                        colors.add("#b71c1c");
                        break;
                    case 3:
                        colors.add("#5D4037");
                        break;
                    case 4:
                        colors.add("#F57F17");
                        break;
                }
            }
        }
        holder.cardView.setCardBackgroundColor(Color.parseColor(colors.get(position)));

        ViewGroup.LayoutParams mLayoutParams = holder.tvName.getLayoutParams();
        mLayoutParams.height = heights.get(position);
        holder.tvName.setLayoutParams(mLayoutParams);
        holder.tvName.setText(list.get(position).getName());
        holder.tvTime.setText(TimeFormatUtil.displayTime(list.get(position).getTime()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}

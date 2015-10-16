package com.v2cc.im.blah.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v2cc.im.blah.R;
import com.v2cc.im.blah.message.MessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/16.
 * If this class works, I created it. If not, I didn't.
 */
public class ChatStaggeredViewAdapter extends RecyclerView.Adapter<ChatStaggeredViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MessageBean> mList;
    private List<Integer> mHeights;
    private LayoutInflater mLayoutInflater;

    // Provide a reference to the type of views that you are using
    // (custom viewholder)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.id_textview);
        }
    }

    public ChatStaggeredViewAdapter(Context context, ArrayList<MessageBean> list) {
        this.mContext = context;
        this.mList = list;
        mLayoutInflater = LayoutInflater.from(context);

        mHeights = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                mHeights.add((int) (Math.random() * 300) + 200);
            }
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ChatStaggeredViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_chat, parent, false);
        ViewHolder mViewHolder = new ViewHolder(mView);
        return mViewHolder;
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

        if (mList.size() != mHeights.size()) {
            for (int i = mHeights.size(); i < mList.size(); i++) {
                mHeights.add((int) (Math.random() * 300) + 200);
            }
        }

        ViewGroup.LayoutParams mLayoutParams = holder.mTextView.getLayoutParams();
        mLayoutParams.height = mHeights.get(position);
        holder.mTextView.setLayoutParams(mLayoutParams);
        holder.mTextView.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
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

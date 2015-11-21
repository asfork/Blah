package com.v2cc.im.blah.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2cc.im.blah.R;
import com.v2cc.im.blah.models.MessageBean;
import com.v2cc.im.blah.utils.TimeFormatUtil;

import java.util.ArrayList;

/**
 * 聊天内容界面的ListView适配器
 *
 * @author yeliangliang
 * @ClassName: ChatViewListAdapter
 * @date 2015-7-31 上午10:44:02
 */
public class MessageListViewAdapter extends BaseAdapter {
    private ArrayList<MessageBean> mList;
    Context mContext;
    private LayoutInflater inflater;
    ViewHolder holder;
//    private FaceUtil faceUtil;

    public MessageListViewAdapter(Context context, ArrayList<MessageBean> list) {
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
//        faceUtil = FaceUtil.getInstance(mContext);
    }

    public void refresh(ArrayList<MessageBean> datas) {
        if (datas == null) {
            datas = new ArrayList<MessageBean>(0);
        }
        this.mList = datas;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        MessageBean messageBean = mList.get(position);

        if (convertView == null) {
            // 判断是接收还是发送
            if (messageBean.getSource().equals(MessageBean.MSG_SOURCE_RECEIVE)) {
                convertView = inflater.inflate(R.layout.item_message_left, null);
                holder.img_avatar = (ImageView) convertView.findViewById(R.id.message_item_avatar);
            } else {
                convertView = inflater.inflate(R.layout.item_message_right, null);
            }

            holder.layout_content = (RelativeLayout) convertView.findViewById(R.id.message_item_layout_content);
            holder.img_sendfail = (ImageView) convertView.findViewById(R.id.message_item_fail);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.message_item_progress);
            holder.tv_msg_content = (TextView) convertView.findViewById(R.id.message_item_content_text);
            holder.tv_date = (TextView) convertView.findViewById(R.id.message_item_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_date.setText(TimeFormatUtil.displayTime(messageBean.getTime()));
        holder.tv_date.setVisibility(View.VISIBLE);

        holder.tv_msg_content.setText(messageBean.getContent());

        //判断是发送还是接收
        if (messageBean.getSource().equals(MessageBean.MSG_SOURCE_RECEIVE)) {
            holder.layout_content.setBackgroundResource(R.drawable.message_left_bg_selector);
        } else {
            holder.layout_content.setBackgroundResource(R.drawable.message_right_bg_selector);
        }

        //显示头像
//        holder.img_avatar.setImageResource();

        //消息发送的状态
        holder.progress.setVisibility(View.GONE);
        holder.img_sendfail.setVisibility(View.GONE);

        return convertView;
    }

    private class ViewHolder {
        TextView tv_date;  // 消息日期
        ImageView img_avatar; // 头像
        TextView tv_msg_content; // 文字消息
        ImageView img_sendfail;
        ProgressBar progress;
        RelativeLayout layout_content; // 聊天信息布局
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

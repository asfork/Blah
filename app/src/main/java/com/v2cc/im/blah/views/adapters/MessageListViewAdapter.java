package com.v2cc.im.blah.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.v2cc.im.blah.R;
import com.v2cc.im.blah.bean.ApplicationData;
import com.v2cc.im.blah.bean.ChatEntity;
import com.v2cc.im.blah.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天内容界面的ListView适配器
 *
 * @author yeliangliang
 * @ClassName: ChatViewListAdapter
 * @date 2015-7-31 上午10:44:02
 */
public class MessageListViewAdapter extends BaseAdapter {
    private List<ChatEntity> mMessageList;
    private Context mContext;
    private LayoutInflater mInflater;
    private ViewHolder holder;

    public MessageListViewAdapter(Context context, List<ChatEntity> list) {
        this.mContext = context;
        this.mMessageList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        ChatEntity messageEntity = mMessageList.get(position);
        User user = ApplicationData.getInstance().getUserInfo();

        if (convertView == null) {
            // 判断是接收还是发送
            if (messageEntity.getMessageType() == ChatEntity.RECEIVE) {
                convertView = mInflater.inflate(R.layout.item_message_left, null);
                Bitmap photo = ApplicationData.getInstance().getFriendPhotoMap()
                        .get(messageEntity.getSenderId());
                holder.img_avatar = (ImageView) convertView.findViewById(R.id.message_item_avatar);
                if (photo != null) {
                    holder.img_avatar.setImageBitmap(photo);
                }
            } else {
                convertView = mInflater.inflate(R.layout.item_message_right, null);
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

        holder.tv_date.setText(messageEntity.getSendTime());
        holder.tv_date.setVisibility(View.VISIBLE);

        holder.tv_msg_content.setText(messageEntity.getContent());

        //判断是发送还是接收
        if (messageEntity.getMessageType() == ChatEntity.RECEIVE) {
            holder.layout_content.setBackgroundResource(R.drawable.message_left_bg_selector);
        } else {
            holder.layout_content.setBackgroundResource(R.drawable.message_right_bg_selector);
        }

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
        return mMessageList == null ? 0 : mMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageList == null ? null : mMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void refresh(List<ChatEntity> data) {
        if (data == null) {
            data = new ArrayList<ChatEntity>(0);
        }
        this.mMessageList = data;
        notifyDataSetChanged();
    }

}

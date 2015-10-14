package com.v2cc.im.blah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class ChatListViewAdapter extends BaseAdapter {
    private ArrayList<MessageBean> mList;
    private Context mContext;
    private LayoutInflater inflater;

    //    private HolderView holderView;
    //    private FaceUtil faceUtil;
    public ChatListViewAdapter(Context context, ArrayList<MessageBean> list) {
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
//        faceUtil = FaceUtil.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chat_list, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_chat_name);
            holder.tv_record = (TextView) convertView.findViewById(R.id.tv_chat_record);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_chat_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MessageBean messageBean = mList.get(position);
        holder.tv_name.setText(messageBean.getName());
        holder.tv_record.setText(messageBean.getContent());
        holder.tv_time.setText(TimeFormattingUtil.displayTime(messageBean.getTime()));
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_name;
        private TextView tv_record;
        private TextView tv_time;
    }
}

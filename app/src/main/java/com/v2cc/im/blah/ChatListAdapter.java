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
public class ChatListAdapter extends BaseAdapter {
    private ArrayList<ChatListBean> mList;
    private Context mContext;
    private LayoutInflater inflater;

    //    private HolderView holderView;
    //    private FaceUtil faceUtil;
    public ChatListAdapter(Context context, ArrayList<ChatListBean> list) {
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
        ChatListBean chatListBean = mList.get(position);
        holder.tv_name.setText(chatListBean.getName());
        holder.tv_record.setText(chatListBean.getContent());
        holder.tv_time.setText(displayTime(chatListBean.getTime()));
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_name;
        private TextView tv_record;
        private TextView tv_time;
    }

    /**
     * 计算最近记录中应该显示的时间
     *
     * @return String
     * 2015-8-6 下午6:09:02
     * <p/>
     * Todo 分离格式化时间功能到单独的 Util 类
     */
    private String displayTime(String time) {
        long ti = Long.valueOf(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String t1 = dateFormat.format(ti);
        String t2 = dateFormat.format(System.currentTimeMillis());
        if (t1.equals(t2)) {
            // 时间为今天
            dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.format(ti);
        }
        dateFormat = new SimpleDateFormat("yyyy");
        String t3 = dateFormat.format(ti);
        String t4 = dateFormat.format(System.currentTimeMillis());
        if (t3.equals(t4)) {
            // 今年
            dateFormat = new SimpleDateFormat("MM-dd");
            return dateFormat.format(ti);
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(ti);
        }
    }
}

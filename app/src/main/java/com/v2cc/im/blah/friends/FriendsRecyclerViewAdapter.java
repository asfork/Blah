package com.v2cc.im.blah.friends;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.v2cc.im.blah.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/22.
 * If this class works, I created it. If not, I didn't.
 */
public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> {
    private List<FriendsBean> list;
    HashMap<String, Integer> alphaIndexer; // 字母索引
    String[] sections; // 存储每个章节
    private Context ctx; // 上下文

    public FriendsRecyclerViewAdapter(Context context, List<FriendsBean> list) {
        this.ctx = context;
        this.list = list;

        alphaIndexer = new HashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++) {
            // 得到字母
            String name = getAlpha(list.get(i).getSortKey());
            if (!alphaIndexer.containsKey(name)) {
                alphaIndexer.put(name, i);
            }
        }

        Set<String> sectionLetters = alphaIndexer.keySet();
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
        Collections.sort(sectionList); // 根据首字母进行排序
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
    }

    // Provide a reference to the type of views that you are using
    // (custom viewholder)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final QuickContactBadge quickContactBadge;
        public final TextView alphaView;
        public final TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            quickContactBadge = (QuickContactBadge) itemView.findViewById(R.id.qcb);
            alphaView = (TextView) itemView.findViewById(R.id.alpha);
            nameView = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friends, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        FriendsBean contact = list.get(position);
        String name = contact.getDesplayName();
        viewHolder.nameView.setText(name);

        viewHolder.quickContactBadge.assignContactUri(ContactsContract.Contacts.getLookupUri(
                contact.getContactId(), contact.getLookUpKey()));
        if (0 == contact.getPhotoId()) {
            viewHolder.quickContactBadge.setImageResource(R.drawable.icons_head_00);
        } else {
            Uri uri = ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI,
                    contact.getContactId());
            InputStream input = ContactsContract.Contacts
                    .openContactPhotoInputStream(ctx.getContentResolver(), uri);
            Bitmap contactPhoto = BitmapFactory.decodeStream(input);
            viewHolder.quickContactBadge.setImageBitmap(contactPhoto);
        }

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

        if (mOnItemClickListener != null) {
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

    /**
     * 获取首字母
     * @return
     * Todo 分离提取首字母功能到单独的 Util 类中
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

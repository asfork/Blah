package com.v2cc.im.blah;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class FriendsFragAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<FriendsBean> list;
    private HashMap<String, Integer> alphaIndexer; // 字母索引
    private String[] sections; // 存储每个章节
    private Context ctx; // 上下文

    public FriendsFragAdapter(Context context, List<FriendsBean> list) {
        this.ctx = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.alphaIndexer = new HashMap<String, Integer>();
        this.sections = new String[list.size()];

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

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position) {
        list.remove(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_friends_list, null);
            holder = new ViewHolder();
            holder.quickContactBadge = (QuickContactBadge) convertView
                    .findViewById(R.id.qcb);
            holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FriendsBean contact = list.get(position);
        String name = contact.getDesplayName();
//        String number = contact.getPhoneNum();
        holder.name.setText(name);
//        holder.number.setText(number);
        holder.quickContactBadge.assignContactUri(ContactsContract.Contacts.getLookupUri(
                contact.getContactId(), contact.getLookUpKey()));
        if (0 == contact.getPhotoId()) {
            holder.quickContactBadge.setImageResource(R.drawable.icon_head_03);
        } else {
            Uri uri = ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI,
                    contact.getContactId());
            InputStream input = ContactsContract.Contacts
                    .openContactPhotoInputStream(ctx.getContentResolver(), uri);
            Bitmap contactPhoto = BitmapFactory.decodeStream(input);
            holder.quickContactBadge.setImageBitmap(contactPhoto);
        }
        // 当前字母
        String currentStr = getAlpha(contact.getSortKey());
        // 前面的字母
        String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
                position - 1).getSortKey()) : " ";

        if (!previewStr.equals(currentStr)) {
            holder.alpha.setVisibility(View.VISIBLE);
            holder.alpha.setText(currentStr);
        } else {
            holder.alpha.setVisibility(View.GONE);
        }
        addListener(convertView);
        return convertView;
    }

    private static class ViewHolder {
        QuickContactBadge quickContactBadge;
        TextView alpha;
        TextView name;
    }

    /**
     * 获取首字母
     *
     * @param str
     * @return
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

    /**
     * 设置监听事件的组件写在下面这方法里
     */
    public void addListener(View convertView) {

    }
}

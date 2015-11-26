package com.v2cc.im.blah.action;

import com.v2cc.im.blah.global.Result;
import com.v2cc.im.blah.bean.ApplicationData;
import com.v2cc.im.blah.bean.ChatEntity;
import com.v2cc.im.blah.bean.TranObject;
import com.v2cc.im.blah.bean.TranObjectType;
import com.v2cc.im.blah.bean.User;
import com.v2cc.im.blah.network.NetService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Steve Zhang
 * 15-11-25
 * <p>
 * If it works, I created it. If not, I didn't.
 */
public class UserAction {
    private static NetService mNetService = NetService.getInstance();

    public static void accountVerify(String account) throws IOException {

        TranObject t = new TranObject(account, TranObjectType.REGISTER_ACCOUNT);
        mNetService.send(t);

    }

    public static void register(User user) throws IOException {

        TranObject t = new TranObject(user, TranObjectType.REGISTER);
        mNetService.send(t);

    }

    public static void loginVerify(User user) throws IOException {
        TranObject t = new TranObject(user, TranObjectType.LOGIN);
        mNetService.send(t);
    }

    public static void searchFriend(String message) throws IOException {
        TranObject t = new TranObject(message, TranObjectType.SEARCH_FRIEND);
        mNetService.send(t);

    }

    public static void sendFriendRequest(Result result, Integer id) {
        TranObject t = new TranObject();
        t.setReceiveId(id);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss");
        String sendTime = sdf.format(date);
        t.setSendTime(sendTime);
        User user = ApplicationData.getInstance().getUserInfo();
        t.setResult(result);
        t.setSendId(user.getId());
        t.setTranType(TranObjectType.FRIEND_REQUEST);
        t.setSendName(user.getUserName());
        try {
            mNetService.send(t);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void sendMessage(ChatEntity message) {

        TranObject t = new TranObject();
        t.setTranType(TranObjectType.MESSAGE);
        t.setReceiveId(message.getReceiverId());
        t.setSendName(ApplicationData.getInstance().getUserInfo().getUserName());
        t.setObject(message);
        try {
            mNetService.send(t);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

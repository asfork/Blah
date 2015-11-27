package com.v2cc.im.blah.network;

import android.content.Context;

import com.v2cc.im.blah.RegistrationActivity;
import com.v2cc.im.blah.bean.ApplicationData;
import com.v2cc.im.blah.bean.TranObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

public class ClientListenThread extends Thread {
    private Socket mSocket = null;
    private Context mContext = null;
    private ObjectInputStream mOis;

    private boolean isStart = true;

    public ClientListenThread(Context context, Socket socket) {
        this.mContext = context;
        this.mSocket = socket;
        try {
            mOis = new ObjectInputStream(mSocket.getInputStream());
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        this.mSocket = socket;
    }

    @Override
    public void run() {
        try {
            isStart = true;
            while (isStart) {
                TranObject mReceived;
                //System.out.println("开始接受服务器");
                mReceived = (TranObject) mOis.readObject();
                System.out.println("接受成功");
                System.out.println(mReceived.getTranType());
                switch (mReceived.getTranType()) {
                    case REGISTER_ACCOUNT:
                        RegistrationActivity.setRegisterInfo(mReceived, true);
                        System.out.println("账号可用");
                        break;
                    case REGISTER:
                        RegistrationActivity.setRegisterInfo(mReceived, true);
                        System.out.println("账号成功");
                        break;
                    case LOGIN:
                        ApplicationData.getInstance().loginMessageArrived(mReceived);
                        break;
                    case SEARCH_FRIEND:
                        System.out.println("收到朋友查找结果");
                        // TODO SearchFriendActivity
//                        SearchFriendActivity.messageArrived(mReceived);
                        break;
                    case FRIEND_REQUEST:
                        ApplicationData.getInstance().friendRequestArrived(mReceived);
                        break;
                    case MESSAGE:
                        ApplicationData.getInstance().messageArrived(mReceived);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void close() {
        isStart = false;
    }
}
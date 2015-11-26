package com.v2cc.im.blah.network;

import android.content.Context;

import com.v2cc.im.blah.bean.TranObject;

import java.io.IOException;
import java.net.Socket;

public class NetService {
    private static NetService mInstance = null;

    private ClientListenThread mClientListenThread = null;
    private ClientSendThread mClientSendThread = null;
    private NetConnect mConnect = null;
    private Socket mClientSocket = null;
    private boolean mIsConnected = false;
    private Context mContext = null;

    private NetService() {

    }

    public void onInit(Context context) {
        this.mContext = context;
    }

    public static NetService getInstance() {
        if (mInstance == null) {
            mInstance = new NetService();
        }
        return mInstance;
    }

    public void setupConnection() {
        mConnect = new NetConnect();
        mConnect.startConnect();
        if (mConnect.getIsConnected()) {
            mIsConnected = true;
            mClientSocket = mConnect.getSocket();
            startListen(mClientSocket);
        } else {
            mIsConnected = false;
        }
    }

    public boolean isConnected() {
        return mIsConnected;
    }

    public void startListen(Socket socket) {
        mClientSendThread = new ClientSendThread(socket);
        mClientListenThread = new ClientListenThread(mContext, socket);
        mClientListenThread.start();
    }

    public void send(TranObject t) throws IOException {
        mClientSendThread.sendMessage(t);
    }

    public void closeConnection() {
        if (mClientListenThread != null) {
            mClientListenThread.close();
        }
        try {
            if (mClientSocket != null)
                mClientSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        mIsConnected = false;
        mClientSocket = null;
    }
}
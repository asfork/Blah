package com.v2cc.im.blah.network;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetConnect {

    private Socket mClientSocket = null;
    private static final String SERVER_IP = "192.168.3.5";
    private static final int SERVER_PORT = 8399;
    private boolean mIsConnected = false;

    public NetConnect() {
    }

    public void startConnect() {
        try {
            mClientSocket = new Socket();
            mClientSocket.connect(
                    new InetSocketAddress(SERVER_IP, SERVER_PORT), 3000);
            Log.d("Network", "Successed to connect to server");
            mIsConnected = mClientSocket.isConnected();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.d("Network", "Server address cannot be resolved");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Network", "Socket io error");
        }
    }

    public boolean getIsConnected() {
        return mIsConnected;
    }

    public Socket getSocket() {
        return mClientSocket;
    }
}
package com.v2cc.im.blah.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.v2cc.im.blah.db.DB;
import com.v2cc.im.blah.global.Result;
import com.v2cc.im.blah.utils.PhotoUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationData {

	private static ApplicationData mInitData;

	private User mUser;
	private boolean mIsReceived;
	private List<User> mFriendList;
	private TranObject mReceivedMessage;
	private Map<Integer, Bitmap> mFriendPhotoMap;
	private Handler messageHandler;
	private Handler chatMessageHandler;
	private Handler friendListHandler;
	private Context mContext;
	private List<User> mFriendSearched;
	private Bitmap mUserPhoto;
	private List<MessageTabEntity> mMessageEntities;// messageFragment显示的列表
	private Map<Integer, List<ChatEntity>> mChatMessagesMap;

    private ApplicationData() {
    }
	public static ApplicationData getInstance() {
		if (mInitData == null) {
			mInitData = new ApplicationData();
		}
		return mInitData;
	}

    public void initData(Context context) {
        System.out.println("initdata");
        mContext = context;
        mIsReceived = false;
        mFriendList = null;
        mUser = null;
        mReceivedMessage = null;
    }

	public void start() {
		while (!(mIsReceived)) {
			// TODO add start
		}
	}

	public void loginMessageArrived(Object tranObject) {
		mReceivedMessage = (TranObject) tranObject;
		Result loginResult = mReceivedMessage.getResult();
		if (loginResult == Result.LOGIN_SUCCESS) {
			mUser = (User) mReceivedMessage.getObject();
			mFriendList = mUser.getFriendList();// 根据从服务器得到的信息，设置好友是否在线
			mUserPhoto = PhotoUtil.getBitmap(mUser.getPhoto());
			List<User> friendListLocal = DB.getInstance(mContext)
					.getAllFriend();
			mFriendPhotoMap = new HashMap<Integer, Bitmap>();
			for (int i = 0; i < friendListLocal.size(); i++) {
				User friend = friendListLocal.get(i);
				Bitmap photo = PhotoUtil.getBitmap(friend.getPhoto());
				mFriendPhotoMap.put(friend.getId(), photo);
			}
			mMessageEntities = DB.getInstance(mContext).getAllMessage();
		} else {

			mUser = null;
			mFriendList = null;
		}
		mChatMessagesMap = new HashMap<Integer, List<ChatEntity>>();
		mIsReceived = true;
	}

	public Map<Integer, List<ChatEntity>> getChatMessagesMap() {
		return mChatMessagesMap;
	}

	public void setChatMessagesMap(
			Map<Integer, List<ChatEntity>> mChatMessagesMap) {
		this.mChatMessagesMap = mChatMessagesMap;
	}

	public Map<Integer, Bitmap> getFriendPhotoMap() {
		return mFriendPhotoMap;
	}

	public void setFriendPhotoList(Map<Integer, Bitmap> mFriendPhotoMap) {
		this.mFriendPhotoMap = mFriendPhotoMap;
	}

	public User getUserInfo() {
		return mUser;
	}

	public List<User> getFriendList() {
		return mFriendList;
	}

	public TranObject getReceivedMessage() {
		return mReceivedMessage;
	}

	public void setReceivedMessage(TranObject mReceivedMessage) {
		this.mReceivedMessage = mReceivedMessage;
	}

	public List<User> getFriendSearched() {
		return mFriendSearched;
	}

	public void setFriendSearched(List<User> mFriendSearched) {
		this.mFriendSearched = mFriendSearched;
	}

	public void friendRequestArrived(TranObject mReceivedRequest) {
		MessageTabEntity messageEntity = new MessageTabEntity();
		if (mReceivedRequest.getResult() == Result.MAKE_FRIEND_REQUEST) {
			messageEntity.setMessageType(MessageTabEntity.MAKE_FRIEND_REQUEST);
			messageEntity.setContent("希望加你为好友");
		} else if (mReceivedRequest.getResult() == Result.FRIEND_REQUEST_RESPONSE_ACCEPT) {
			messageEntity
					.setMessageType(MessageTabEntity.MAKE_FRIEND_RESPONSE_ACCEPT);
			messageEntity.setContent("接受了你的好友请求");
			User newFriend = (User) mReceivedRequest.getObject();
			if (!mFriendList.contains(newFriend)) {

				mFriendList.add(newFriend);
			}

			mFriendPhotoMap.put(newFriend.getId(),
					PhotoUtil.getBitmap(newFriend.getPhoto()));
			if (friendListHandler != null) {
				Message message = new Message();
				message.what = 1;
				friendListHandler.sendMessage(message);
			}
			DB.getInstance(mContext).saveFriend(newFriend);
		} else {
			messageEntity
					.setMessageType(MessageTabEntity.MAKE_FRIEND_RESPONSE_REJECT);
			messageEntity.setContent("拒绝了你的好友请求");
		}
		messageEntity.setName(mReceivedRequest.getSendName());
		messageEntity.setSendTime(mReceivedRequest.getSendTime());
		messageEntity.setSenderId(mReceivedRequest.getSendId());
		messageEntity.setUnReadCount(1);
		DB.getInstance(mContext).saveMessage(messageEntity);
		mMessageEntities.add(messageEntity);
		if (messageHandler != null) {
			Message message = new Message();
			message.what = 1;
			messageHandler.sendMessage(message);
		}
	}

	public void messageArrived(TranObject tran) {
		ChatEntity chat = (ChatEntity) tran.getObject();
		int senderId = chat.getSenderId();
		System.out.println("senderId" + senderId);
		boolean hasMessageTab = false;
		for (int i = 0; i < mMessageEntities.size(); i++) {
			MessageTabEntity messageTab = mMessageEntities.get(i);
			if (messageTab.getSenderId() == senderId
					&& messageTab.getMessageType() == MessageTabEntity.FRIEND_MESSAGE) {
				messageTab.setUnReadCount(messageTab.getUnReadCount() + 1);
				messageTab.setContent(chat.getContent());
				messageTab.setSendTime(chat.getSendTime());
				DB.getInstance(mContext).updateMessages(messageTab);
				hasMessageTab = true;
			}
		}
		if (!hasMessageTab) {
			MessageTabEntity messageTab = new MessageTabEntity();
			messageTab.setContent(chat.getContent());
			messageTab.setMessageType(MessageTabEntity.FRIEND_MESSAGE);
			messageTab.setName(tran.getSendName());
			messageTab.setSenderId(senderId);
			messageTab.setSendTime(chat.getSendTime());
			messageTab.setUnReadCount(1);
			mMessageEntities.add(messageTab);
			DB.getInstance(mContext).saveMessage(messageTab);
		}
		chat.setMessageType(ChatEntity.RECEIVE);
		List<ChatEntity> chatList = mChatMessagesMap.get(chat.getSenderId());
		if (chatList == null) {
			chatList = DB.getInstance(mContext).getChatMessage(
					chat.getSenderId());
			getChatMessagesMap().put(chat.getSenderId(), chatList);
		}
		chatList.add(chat);
		DB.getInstance(mContext).saveChatMessage(chat);
		if (messageHandler != null) {
			Message message = new Message();
			message.what = 1;
			messageHandler.sendMessage(message);
		}
		if (chatMessageHandler != null) {
			Message message = new Message();
			message.what = 1;
			chatMessageHandler.sendMessage(message);
		}
	}

	public Bitmap getUserPhoto() {
		return mUserPhoto;
	}

	public void setUserPhoto(Bitmap mUserPhoto) {
		this.mUserPhoto = mUserPhoto;
	}

	public List<MessageTabEntity> getMessageEntities() {
		return mMessageEntities;
	}

	public void setMessageEntities(List<MessageTabEntity> mMessageEntities) {
		this.mMessageEntities = mMessageEntities;
	}

	public void setMessageHandler(Handler handler) {
		this.messageHandler = handler;
	}

	public void setChatHandler(Handler handler) {
		this.chatMessageHandler = handler;
	}

	public void setfriendListHandler(Handler handler) {
		this.friendListHandler = handler;
	}
}
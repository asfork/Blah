package com.v2cc.im.blah.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.v2cc.im.blah.base.activity.MainActivity;
import com.v2cc.im.blah.base.app.Constants;
import com.v2cc.im.blah.base.utils.SystemUtil;
import com.v2cc.im.blah.message.MessageActivity;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/21.
 * If this class works, I created it. If not, I didn't.
 */
public class NotificationReceiver extends BroadcastReceiver {

    private String phoneNum;

    public void onReceive(Context context, Intent intent) {

        phoneNum = intent.getStringExtra("phoneNum");

        //判断app进程是否存活
        if (SystemUtil.isAppAlive(context, "com.v2cc.im.blah")) {
            //如果存活的话，就直接启动MessageActivity，但要考虑一种情况，就是app的进程虽然仍然在
            //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
            //MessageActivity,再按Back键就不会返回MainActivity了。所以在启动
            //MessageActivity前，要先启动MainActivity。
            Log.d("NotificationReceiver", "the app process is alive");
            Intent mainIntent = new Intent(context, MainActivity.class);
            //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
            //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
            //如果Task栈不存在MainActivity实例，则在栈顶创建
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Intent mIntent = new Intent(context, MessageActivity.class);
            mIntent.putExtra("phoneNum", phoneNum);
            Intent[] intents = {mainIntent, mIntent};
            context.startActivities(intents);
        } else {
            //如果app进程已经被杀死，先重新启动app，将MessageActivity的启动参数传入Intent中，参数经过
            //SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入
            // 参数跳转到MessageActivity中去了
            Log.d("NotificationReceiver", "the app process is dead");
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.v2cc.im.blah");
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            Bundle args = new Bundle();
            args.putString("phoneNum", phoneNum);
            launchIntent.putExtra(Constants.EXTRA_BUNDLE, args);
            context.startActivity(launchIntent);
        }
    }
}

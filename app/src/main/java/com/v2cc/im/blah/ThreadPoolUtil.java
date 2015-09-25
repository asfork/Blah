package com.v2cc.im.blah;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理工具类
 *
 * @author yeliangliang
 *         ThreadPoolUtil
 *         2015-7-24 下午4:17:27
 */
public class ThreadPoolUtil {
    public static ExecutorService catchPool = null;
    public static ExecutorService singlePool = null;

    public static void insertTaskToCatchPool(Runnable command) {
        if (catchPool == null) {
            catchPool = Executors.newCachedThreadPool();
        }
        catchPool.execute(command);
    }

    public static void insertTaskToSinglePool(Runnable command) {
        if (singlePool == null) {
            singlePool = Executors.newSingleThreadExecutor();
        }
        singlePool.execute(command);
    }

    /**
     * 关闭所有正在执行的线程
     * <p/>
     * 2015-7-24 下午4:33:19
     */
    public static void closeAllThreadPool() {
        if (catchPool != null) {
            catchPool.shutdownNow();
        }
        if (singlePool != null) {
            singlePool.shutdownNow();
        }
    }
}

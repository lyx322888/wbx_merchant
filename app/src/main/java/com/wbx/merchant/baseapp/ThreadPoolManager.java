package com.wbx.merchant.baseapp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理器
 *
 * @author Zero
 * @date 2018/2/10
 */
public class ThreadPoolManager {
    private static ThreadPoolExecutor executorService;
    private static final int CORE_POOL_SIZE = 4;
    private static final int MAXIMUM_POOL_SIZE = 9;
    private static final int KEEP_ALIVE_TIME = 10;

    public static ExecutorService getInstance() {
        if (executorService == null) {
            synchronized (ThreadPoolManager.class) {
                if (executorService == null) {
                    executorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
                }
            }
        }
        return executorService;
    }


    public static void execute(Runnable runnable) {
        getInstance().execute(runnable);
    }
}

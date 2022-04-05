package org.csp.rpc.remoting.handler;

import org.csp.rpc.remoting.api.ChannelHandler;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 所有的事件，都交由业务线程处理
 */
public class AllChannelHandler implements ChannelHandler {

    private ExecutorService executorService;

    public AllChannelHandler() {
        this.executorService = new ThreadPoolExecutor(
                200,
                200,
                60,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadFactory() {
                    private AtomicInteger count = new AtomicInteger();
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "rpc_execute_biz_thread" + count.getAndIncrement());
                    }
                },
                new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public void read(Object msg) {
        Future future = this.executorService.submit(new ChannelEventHandler(msg));

        try {
            Object o = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

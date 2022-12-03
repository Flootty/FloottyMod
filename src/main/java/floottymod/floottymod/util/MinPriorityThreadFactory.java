package floottymod.floottymod.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MinPriorityThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public MinPriorityThreadFactory() {
        group = Thread.currentThread().getThreadGroup();
        namePrefix = "pool-min-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(), 0);
        if(t.isDaemon()) t.setDaemon(false);
        if(t.getPriority() != Thread.MIN_PRIORITY) t.setPriority(Thread.MIN_PRIORITY);
        return t;
    }

    public static ExecutorService newFixedThreadPool() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new MinPriorityThreadFactory());
    }
}

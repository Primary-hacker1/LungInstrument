package com.common.base;

import java.lang.System;

/**
 * @author lollipop
 * @date 2020/6/9 23:17
 * 通用的全局工具方法
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001!B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u0014\u001a\u00020\u0015\"\u0004\b\u0000\u0010\u00162\u0006\u0010\u0014\u001a\u00020\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0019J\u001a\u0010\u001a\u001a\u00020\u0015\"\u0004\b\u0000\u0010\u00162\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0019J\u001a\u0010\u001b\u001a\u00020\u0015\"\u0004\b\u0000\u0010\u00162\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0019J\u001b\u0010\u001c\u001a\u00020\u00042\u000e\u0010\u001d\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010\u001e\u00a2\u0006\u0002\u0010\u001fJ\u001a\u0010 \u001a\u00020\u0015\"\u0004\b\u0000\u0010\u00162\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0019R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u000e\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\""}, d2 = {"Lcom/common/base/CommonUtil;", "", "()V", "logTag", "", "getLogTag", "()Ljava/lang/String;", "setLogTag", "(Ljava/lang/String;)V", "mainThread", "Landroid/os/Handler;", "getMainThread", "()Landroid/os/Handler;", "mainThread$delegate", "Lkotlin/Lazy;", "threadPool", "Ljava/util/concurrent/Executor;", "getThreadPool", "()Ljava/util/concurrent/Executor;", "threadPool$delegate", "delay", "", "T", "", "task", "Lcom/common/base/CommonUtil$Task;", "doAsync", "onUI", "print", "value", "", "([Ljava/lang/Object;)Ljava/lang/String;", "remove", "Task", "common_debug"})
public final class CommonUtil {
    @org.jetbrains.annotations.NotNull
    public static final com.common.base.CommonUtil INSTANCE = null;
    
    /**
     * 全局的打印日志的关键字
     */
    @org.jetbrains.annotations.NotNull
    private static java.lang.String logTag = "WindowsLauncher";
    
    /**
     * 异步线程池
     */
    private static final kotlin.Lazy threadPool$delegate = null;
    
    /**
     * 主线程的handler
     */
    private static final kotlin.Lazy mainThread$delegate = null;
    
    private CommonUtil() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLogTag() {
        return null;
    }
    
    public final void setLogTag(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    /**
     * 异步线程池
     */
    private final java.util.concurrent.Executor getThreadPool() {
        return null;
    }
    
    /**
     * 主线程的handler
     */
    private final android.os.Handler getMainThread() {
        return null;
    }
    
    /**
     * 异步任务
     */
    public final <T extends java.lang.Object>void doAsync(@org.jetbrains.annotations.NotNull
    com.common.base.CommonUtil.Task<T> task) {
    }
    
    /**
     * 主线程
     */
    public final <T extends java.lang.Object>void onUI(@org.jetbrains.annotations.NotNull
    com.common.base.CommonUtil.Task<T> task) {
    }
    
    /**
     * 延迟任务
     */
    public final <T extends java.lang.Object>void delay(long delay, @org.jetbrains.annotations.NotNull
    com.common.base.CommonUtil.Task<T> task) {
    }
    
    /**
     * 移除任务
     */
    public final <T extends java.lang.Object>void remove(@org.jetbrains.annotations.NotNull
    com.common.base.CommonUtil.Task<T> task) {
    }
    
    /**
     * 将一组对象打印合并为一个字符串
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String print(@org.jetbrains.annotations.NotNull
    java.lang.Object[] value) {
        return null;
    }
    
    /**
     * 包装的任务类
     * 包装的意义在于复用和移除任务
     * 由于Handler任务可能造成内存泄漏，因此在生命周期结束时，有必要移除任务
     * 由于主线程的Handler使用了全局的对象，移除不必要的任务显得更为重要
     * 因此包装了任务类，以任务类为对象来保留任务和移除任务
     */
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B<\u0012\u0006\u0010\u0003\u001a\u00028\u0000\u0012\u0014\b\u0002\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u0012\u0017\u0010\b\u001a\u0013\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\b\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010\u0010\u001a\u00020\u0007J\u000e\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\b\u001a\u00020\u0007J\u0006\u0010\u0014\u001a\u00020\u0007R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001f\u0010\b\u001a\u0013\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00070\u0005\u00a2\u0006\u0002\b\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0010\u0010\u0003\u001a\u00028\u0000X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000f\u00a8\u0006\u0015"}, d2 = {"Lcom/common/base/CommonUtil$Task;", "T", "", "target", "err", "Lkotlin/Function1;", "", "", "run", "Lkotlin/ExtensionFunctionType;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "runnable", "Ljava/lang/Runnable;", "getRunnable", "()Ljava/lang/Runnable;", "Ljava/lang/Object;", "cancel", "delay", "time", "", "sync", "common_debug"})
    public static final class Task<T extends java.lang.Object> {
        private final T target = null;
        private final kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> err = null;
        private final kotlin.jvm.functions.Function1<T, kotlin.Unit> run = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.Runnable runnable = null;
        
        public Task(T target, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> err, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super T, kotlin.Unit> run) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.Runnable getRunnable() {
            return null;
        }
        
        public final void cancel() {
        }
        
        public final void run() {
        }
        
        public final void sync() {
        }
        
        public final void delay(long time) {
        }
    }
}
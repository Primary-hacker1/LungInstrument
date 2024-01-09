package com.common.network;

import java.lang.System;

/**
 * create by 2020/9/12
 * rxjavaObserver
 * @author zt
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0007\u0018\u0000 \u0019*\u0004\b\u0000\u0010\u00012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00042\u00020\u0003:\u0001\u0019B-\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000e\u001a\u00020\u0007H\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u0003H\u0016J\u0015\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u0018R\u001d\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001d\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\f\u00a8\u0006\u001a"}, d2 = {"Lcom/common/network/RequestObserver;", "T", "Ljava/util/concurrent/atomic/AtomicReference;", "Lio/reactivex/disposables/Disposable;", "Lio/reactivex/SingleObserver;", "onSuccesses", "Lkotlin/Function1;", "", "onErrors", "Lcom/common/throwe/BaseResponseThrowable;", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "getOnErrors", "()Lkotlin/jvm/functions/Function1;", "getOnSuccesses", "dispose", "isDisposed", "", "onError", "e", "", "onSubscribe", "d", "onSuccess", "value", "(Ljava/lang/Object;)V", "Companion", "common_debug"})
public final class RequestObserver<T extends java.lang.Object> extends java.util.concurrent.atomic.AtomicReference<io.reactivex.disposables.Disposable> implements io.reactivex.SingleObserver<T>, io.reactivex.disposables.Disposable {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<T, kotlin.Unit> onSuccesses = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.common.throwe.BaseResponseThrowable, kotlin.Unit> onErrors = null;
    @org.jetbrains.annotations.NotNull
    public static final com.common.network.RequestObserver.Companion Companion = null;
    private static final long serialVersionUID = -7012088219455310786L;
    
    public RequestObserver(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> onSuccesses, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.common.throwe.BaseResponseThrowable, kotlin.Unit> onErrors) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlin.jvm.functions.Function1<T, kotlin.Unit> getOnSuccesses() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlin.jvm.functions.Function1<com.common.throwe.BaseResponseThrowable, kotlin.Unit> getOnErrors() {
        return null;
    }
    
    @java.lang.Override
    public void onError(@org.jetbrains.annotations.NotNull
    java.lang.Throwable e) {
    }
    
    @java.lang.Override
    public void onSubscribe(@org.jetbrains.annotations.NotNull
    io.reactivex.disposables.Disposable d) {
    }
    
    @java.lang.Override
    public void onSuccess(T value) {
    }
    
    @java.lang.Override
    public void dispose() {
    }
    
    @java.lang.Override
    public boolean isDisposed() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/common/network/RequestObserver$Companion;", "", "()V", "serialVersionUID", "", "common_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
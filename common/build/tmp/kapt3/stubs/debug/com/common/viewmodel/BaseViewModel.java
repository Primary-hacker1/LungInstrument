package com.common.viewmodel;

import java.lang.System;

/**
 * create by 2024/2/27
 * ViewModel 基础类
 * @author zt
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u00b5\u0001\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u00142\'\u0010\u0015\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00140\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0016\u00a2\u0006\u0002\b\u001a2\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u0002H\u0014\u0012\u0004\u0012\u00020\u00130\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u001e2-\u0010\u001f\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020!\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190 \u00a2\u0006\u0002\b\u001a2)\b\u0002\u0010\"\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0016\u00a2\u0006\u0002\b\u001a\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J\u00ab\u0001\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u00142\'\u0010\u0015\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00140\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0016\u00a2\u0006\u0002\b\u001a2\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u0002H\u0014\u0012\u0004\u0012\u00020\u00130\u001c2-\u0010\u001f\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020!\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190 \u00a2\u0006\u0002\b\u001a2)\b\u0002\u0010\"\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0016\u00a2\u0006\u0002\b\u001a\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010$J\u00b1\u0001\u0010%\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u00142-\u0010\u0015\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00140&0\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0016\u00a2\u0006\u0002\b\u001a2\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u0002H\u0014\u0012\u0004\u0012\u00020\u00130\u001c2-\u0010\u001f\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020!\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190 \u00a2\u0006\u0002\b\u001a2)\b\u0002\u0010\"\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0016\u00a2\u0006\u0002\b\u001a\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010$JT\u0010\'\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u00142\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H\u00140&2-\u0010\u001b\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u0002H\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190 \u00a2\u0006\u0002\b\u001aH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)J\u00c7\u0001\u0010*\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u00142\'\u0010+\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00140\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0016\u00a2\u0006\u0002\b\u001a2-\u0010\u001b\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u0002H\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190 \u00a2\u0006\u0002\b\u001a2-\u0010\u001f\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020!\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190 \u00a2\u0006\u0002\b\u001a2\'\u0010\"\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0016\u00a2\u0006\u0002\b\u001aH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010,J9\u0010-\u001a\u00020.2\'\u0010+\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00190\u0016\u00a2\u0006\u0002\b\u001aH\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010/R \u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0004\u0018\u00010\rX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u00060"}, d2 = {"Lcom/common/viewmodel/BaseViewModel;", "Lcom/common/viewmodel/BaseLifeViewModel;", "()V", "mEventHub", "Lcom/common/viewmodel/SingleLiveEvent;", "Lcom/common/viewmodel/LiveDataEvent;", "getMEventHub", "()Lcom/common/viewmodel/SingleLiveEvent;", "setMEventHub", "(Lcom/common/viewmodel/SingleLiveEvent;)V", "stateView", "Lcom/common/viewmodel/StateView;", "tag", "", "getTag", "()Ljava/lang/String;", "setTag", "(Ljava/lang/String;)V", "async", "", "T", "request", "Lkotlin/Function2;", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "success", "Lkotlin/Function1;", "showDialog", "", "error", "Lkotlin/Function3;", "Lcom/common/throwe/BaseResponseThrowable;", "complete", "(Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function1;ZLkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function2;)V", "(Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function2;)V", "asyncExecute", "Lcom/common/BaseResponse;", "executeResponse", "response", "(Lcom/common/BaseResponse;Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "handleRequest", "block", "(Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "launchUi", "Lkotlinx/coroutines/Job;", "(Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/Job;", "common_debug"})
public class BaseViewModel extends com.common.viewmodel.BaseLifeViewModel {
    @org.jetbrains.annotations.Nullable
    private java.lang.String tag;
    private com.common.viewmodel.StateView stateView;
    @org.jetbrains.annotations.NotNull
    private com.common.viewmodel.SingleLiveEvent<com.common.viewmodel.LiveDataEvent> mEventHub;
    
    public BaseViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    protected final java.lang.String getTag() {
        return null;
    }
    
    protected final void setTag(@org.jetbrains.annotations.Nullable
    java.lang.String p0) {
    }
    
    private final kotlinx.coroutines.Job launchUi(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        return null;
    }
    
    /**
     * 直接获取结果的
     */
    public final <T extends java.lang.Object>void async(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> request, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> success, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function3<? super kotlinx.coroutines.CoroutineScope, ? super com.common.throwe.BaseResponseThrowable, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> error, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> complete) {
    }
    
    public final <T extends java.lang.Object>void asyncExecute(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super com.common.BaseResponse<T>>, ? extends java.lang.Object> request, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> success, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function3<? super kotlinx.coroutines.CoroutineScope, ? super com.common.throwe.BaseResponseThrowable, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> error, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> complete) {
    }
    
    /**
     * 带loading的请求
     */
    public final <T extends java.lang.Object>void async(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> request, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> success, boolean showDialog, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function3<? super kotlinx.coroutines.CoroutineScope, ? super com.common.throwe.BaseResponseThrowable, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> error, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> complete) {
    }
    
    private final <T extends java.lang.Object>java.lang.Object handleRequest(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> block, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.CoroutineScope, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> success, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.CoroutineScope, ? super com.common.throwe.BaseResponseThrowable, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> error, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> complete, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    private final <T extends java.lang.Object>java.lang.Object executeResponse(com.common.BaseResponse<T> response, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.CoroutineScope, ? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> success, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.common.viewmodel.SingleLiveEvent<com.common.viewmodel.LiveDataEvent> getMEventHub() {
        return null;
    }
    
    public final void setMEventHub(@org.jetbrains.annotations.NotNull
    com.common.viewmodel.SingleLiveEvent<com.common.viewmodel.LiveDataEvent> p0) {
    }
}
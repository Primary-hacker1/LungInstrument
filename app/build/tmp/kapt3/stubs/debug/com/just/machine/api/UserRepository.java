package com.just.machine.api;

import java.lang.System;

/**
 * create by 2020/3/19
 * @author zt
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u001c\u0010\n\u001a\u0010\u0012\f\u0012\n \f*\u0004\u0018\u00010\u00060\u00060\u000b2\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\r"}, d2 = {"Lcom/just/machine/api/UserRepository;", "", "apiService", "Lcom/just/machine/api/BaseApiService;", "(Lcom/just/machine/api/BaseApiService;)V", "getNews", "Lcom/just/machine/model/NewResponse;", "type", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRxNews", "Lio/reactivex/Single;", "kotlin.jvm.PlatformType", "app_debug"})
public final class UserRepository {
    private final com.just.machine.api.BaseApiService apiService = null;
    
    @javax.inject.Inject
    public UserRepository(@org.jetbrains.annotations.NotNull
    com.just.machine.api.BaseApiService apiService) {
        super();
    }
    
    /**
     * 协程请求
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getNews(@org.jetbrains.annotations.NotNull
    java.lang.String type, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.just.machine.model.NewResponse> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final io.reactivex.Single<com.just.machine.model.NewResponse> getRxNews(@org.jetbrains.annotations.NotNull
    java.lang.String type) {
        return null;
    }
}
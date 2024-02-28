package com.just.machine.api;

import java.lang.System;

/**
 * create by 2020/3/19
 * @author zt
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u001d\u0010\u0002\u001a\u00020\u00032\n\b\u0001\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\n\b\u0001\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\'\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\t"}, d2 = {"Lcom/just/machine/api/BaseApiService;", "", "getNews", "Lcom/just/machine/model/NewResponse;", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRxNews", "Lio/reactivex/Single;", "app_debug"})
public abstract interface BaseApiService {
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "/nc/article/headline/{id}/0-10.html")
    public abstract java.lang.Object getNews(@org.jetbrains.annotations.Nullable
    @retrofit2.http.Path(value = "id")
    java.lang.String id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.just.machine.model.NewResponse> continuation);
    
    @org.jetbrains.annotations.NotNull
    @retrofit2.http.GET(value = "/nc/article/headline/{id}/0-10.html")
    public abstract io.reactivex.Single<com.just.machine.model.NewResponse> getRxNews(@org.jetbrains.annotations.Nullable
    @retrofit2.http.Path(value = "id")
    java.lang.String id);
}
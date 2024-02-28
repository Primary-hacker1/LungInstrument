package com.common.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\bH\u0002J\u0018\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\fR\u0014\u0010\u0003\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/common/network/Net;", "", "()V", "getLogger", "Lokhttp3/logging/HttpLoggingInterceptor;", "getGetLogger", "()Lokhttp3/logging/HttpLoggingInterceptor;", "okHttpClient", "Lokhttp3/OkHttpClient;", "retrofit", "Lretrofit2/Retrofit;", "timeOut", "", "getOkHttpClient", "getRetrofit", "baseUrl", "", "time", "common_debug"})
public final class Net {
    @org.jetbrains.annotations.NotNull
    public static final com.common.network.Net INSTANCE = null;
    private static retrofit2.Retrofit retrofit;
    private static okhttp3.OkHttpClient okHttpClient;
    private static long timeOut = 6000L;
    
    private Net() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final retrofit2.Retrofit getRetrofit(@org.jetbrains.annotations.NotNull
    java.lang.String baseUrl, long time) {
        return null;
    }
    
    private final okhttp3.OkHttpClient getOkHttpClient() {
        return null;
    }
    
    private final okhttp3.logging.HttpLoggingInterceptor getGetLogger() {
        return null;
    }
}
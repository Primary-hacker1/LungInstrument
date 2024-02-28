package com.just.machine.helper;

import java.lang.System;

/**
 * create by 2020/6/19
 * @author zt
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/just/machine/helper/UriConfig;", "", "()V", "BASE_URL", "", "DATABASE_NAME", "LOG_PATH", "getLOG_PATH", "()Ljava/lang/String;", "PLANT_DATA_FILENAME", "app_debug"})
public final class UriConfig {
    @org.jetbrains.annotations.NotNull
    public static final com.just.machine.helper.UriConfig INSTANCE = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BASE_URL = "http://192.168.1.105:8080/inspect/";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String DATABASE_NAME = "news-db";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PLANT_DATA_FILENAME = "plants.json";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String LOG_PATH = null;
    
    private UriConfig() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLOG_PATH() {
        return null;
    }
}
package com.just.machine.model;

import java.lang.System;

/**
 * create by 2022/6/30
 * @author zt
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\n\u001a\u00020\u000bR*\u0010\u0005\u001a\u0004\u0018\u00010\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u00048F@FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\t\u00a8\u0006\r"}, d2 = {"Lcom/just/machine/model/SharedPreferencesUtils;", "", "()V", "serialNo", "", "phone", "getPhone", "()Ljava/lang/String;", "setPhone", "(Ljava/lang/String;)V", "logout", "", "Companion", "app_debug"})
public final class SharedPreferencesUtils {
    @org.jetbrains.annotations.NotNull
    public static final com.just.machine.model.SharedPreferencesUtils.Companion Companion = null;
    @org.jetbrains.annotations.NotNull
    private static final com.just.machine.model.SharedPreferencesUtils instance = null;
    private static final java.lang.String PER_USERNAME = "per_username";
    private static final java.lang.String NAME = "per_user_name";
    @org.jetbrains.annotations.Nullable
    private java.lang.String phone;
    
    private SharedPreferencesUtils() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getPhone() {
        return null;
    }
    
    public final void setPhone(@org.jetbrains.annotations.Nullable
    java.lang.String serialNo) {
    }
    
    /**
     * 登录信息销毁
     */
    public final void logout() {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2 = {"Lcom/just/machine/model/SharedPreferencesUtils$Companion;", "", "()V", "NAME", "", "PER_USERNAME", "instance", "Lcom/just/machine/model/SharedPreferencesUtils;", "getInstance", "()Lcom/just/machine/model/SharedPreferencesUtils;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.just.machine.model.SharedPreferencesUtils getInstance() {
            return null;
        }
    }
}
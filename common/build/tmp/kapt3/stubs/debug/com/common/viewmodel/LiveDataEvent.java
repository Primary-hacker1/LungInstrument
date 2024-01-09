package com.common.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0005B!\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0001\u00a2\u0006\u0002\u0010\u0007R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0001X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0001X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\r\"\u0004\b\u0011\u0010\u000f\u00a8\u0006\u0013"}, d2 = {"Lcom/common/viewmodel/LiveDataEvent;", "", "action", "", "any", "(ILjava/lang/Object;)V", "anyOne", "(ILjava/lang/Object;Ljava/lang/Object;)V", "getAction", "()I", "setAction", "(I)V", "getAny", "()Ljava/lang/Object;", "setAny", "(Ljava/lang/Object;)V", "getAnyOne", "setAnyOne", "Companion", "common_debug"})
public final class LiveDataEvent {
    @org.jetbrains.annotations.NotNull
    public static final com.common.viewmodel.LiveDataEvent.Companion Companion = null;
    public static final int SUCCESS = 200;
    
    /**
     * 登录状态
     *
     * @param LOGIN_SUCCESS
     * @return
     */
    public static final int LOGIN_SUCCESS = 1;
    
    /**
     * 登录错误
     *
     * @param LOGIN_FAIL
     * @return -
     */
    public static final int LOGIN_FAIL = 2;
    
    /**
     * 可判断所有错误
     */
    private static final int JUST_ERROR_FAIL = 55;
    private int action = 0;
    @org.jetbrains.annotations.Nullable
    private java.lang.Object any;
    @org.jetbrains.annotations.Nullable
    private java.lang.Object anyOne;
    
    public final int getAction() {
        return 0;
    }
    
    public final void setAction(int p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getAny() {
        return null;
    }
    
    public final void setAny(@org.jetbrains.annotations.Nullable
    java.lang.Object p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getAnyOne() {
        return null;
    }
    
    public final void setAnyOne(@org.jetbrains.annotations.Nullable
    java.lang.Object p0) {
    }
    
    public LiveDataEvent(int action, @org.jetbrains.annotations.NotNull
    java.lang.Object any) {
        super();
    }
    
    public LiveDataEvent(int action, @org.jetbrains.annotations.NotNull
    java.lang.Object any, @org.jetbrains.annotations.Nullable
    java.lang.Object anyOne) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/common/viewmodel/LiveDataEvent$Companion;", "", "()V", "JUST_ERROR_FAIL", "", "getJUST_ERROR_FAIL", "()I", "LOGIN_FAIL", "LOGIN_SUCCESS", "SUCCESS", "common_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final int getJUST_ERROR_FAIL() {
            return 0;
        }
    }
}
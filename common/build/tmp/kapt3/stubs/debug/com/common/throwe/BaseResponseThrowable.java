package com.common.throwe;

import java.lang.System;

/**
 * create by 2020/5/15
 *
 * @author yx
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\u0018\u00002\u00020\u0001B#\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0001\u00a2\u0006\u0002\u0010\u0007B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\tR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\b\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0012"}, d2 = {"Lcom/common/throwe/BaseResponseThrowable;", "", "code", "", "msg", "", "e", "(ILjava/lang/String;Ljava/lang/Throwable;)V", "errMsg", "(ILjava/lang/String;)V", "getCode", "()I", "setCode", "(I)V", "getErrMsg", "()Ljava/lang/String;", "setErrMsg", "(Ljava/lang/String;)V", "common_debug"})
public final class BaseResponseThrowable extends java.lang.Throwable {
    private int code;
    @org.jetbrains.annotations.NotNull
    private java.lang.String errMsg;
    
    public final int getCode() {
        return 0;
    }
    
    public final void setCode(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getErrMsg() {
        return null;
    }
    
    public final void setErrMsg(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    public BaseResponseThrowable(int code, @org.jetbrains.annotations.NotNull
    java.lang.String msg, @org.jetbrains.annotations.Nullable
    java.lang.Throwable e) {
        super(null);
    }
    
    public BaseResponseThrowable(int code, @org.jetbrains.annotations.NotNull
    java.lang.String errMsg) {
        super(null);
    }
}
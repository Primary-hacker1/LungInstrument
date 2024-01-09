package com.common;

import java.lang.System;

/**
 * create by 2020/9/10
 * @author yx
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\bJ\t\u0010\u0010\u001a\u00020\u0004H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0006H\u00c6\u0003J\u000e\u0010\u0012\u001a\u00028\u0000H\u00c6\u0003\u00a2\u0006\u0002\u0010\fJ2\u0010\u0013\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00028\u0000H\u00c6\u0001\u00a2\u0006\u0002\u0010\u0014J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0002H\u00d6\u0003J\t\u0010\u0018\u001a\u00020\u0006H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u0004H\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0013\u0010\u0007\u001a\u00028\u0000\u00a2\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001a"}, d2 = {"Lcom/common/BaseResponse;", "T", "", "errorMsg", "", "code", "", "data", "(Ljava/lang/String;ILjava/lang/Object;)V", "getCode", "()I", "getData", "()Ljava/lang/Object;", "Ljava/lang/Object;", "getErrorMsg", "()Ljava/lang/String;", "component1", "component2", "component3", "copy", "(Ljava/lang/String;ILjava/lang/Object;)Lcom/common/BaseResponse;", "equals", "", "other", "hashCode", "toString", "common_debug"})
public final class BaseResponse<T extends java.lang.Object> {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String errorMsg = null;
    private final int code = 0;
    private final T data = null;
    
    /**
     * create by 2020/9/10
     * @author yx
     */
    @org.jetbrains.annotations.NotNull
    public final com.common.BaseResponse<T> copy(@org.jetbrains.annotations.NotNull
    java.lang.String errorMsg, int code, T data) {
        return null;
    }
    
    /**
     * create by 2020/9/10
     * @author yx
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * create by 2020/9/10
     * @author yx
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * create by 2020/9/10
     * @author yx
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public BaseResponse(@org.jetbrains.annotations.NotNull
    java.lang.String errorMsg, int code, T data) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getErrorMsg() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final int getCode() {
        return 0;
    }
    
    public final T component3() {
        return null;
    }
    
    public final T getData() {
        return null;
    }
}
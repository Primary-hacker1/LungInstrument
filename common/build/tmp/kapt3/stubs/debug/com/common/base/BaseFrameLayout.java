package com.common.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0000\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\u000f\b\u0016\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B\u0017\b\u0016\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tB\u001f\b\u0016\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\b\u0010\u001c\u001a\u00020\u000bH%J\b\u0010\u001d\u001a\u00020\u001eH&R\u001c\u0010\r\u001a\n \u000f*\u0004\u0018\u00010\u000e0\u000eX\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0012\u001a\u00028\u0000X\u0086.\u00a2\u0006\u0010\n\u0002\u0010\u0017\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0018\u001a\u00020\u0005X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u0006\u00a8\u0006\u001f"}, d2 = {"Lcom/common/base/BaseFrameLayout;", "Vb", "Landroidx/databinding/ViewDataBinding;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attributes", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "int", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "TAG", "", "kotlin.jvm.PlatformType", "getTAG", "()Ljava/lang/String;", "binding", "getBinding", "()Landroidx/databinding/ViewDataBinding;", "setBinding", "(Landroidx/databinding/ViewDataBinding;)V", "Landroidx/databinding/ViewDataBinding;", "mContext", "getMContext", "()Landroid/content/Context;", "setMContext", "getLayout", "initView", "", "common_debug"})
public abstract class BaseFrameLayout<Vb extends androidx.databinding.ViewDataBinding> extends android.widget.FrameLayout {
    private final java.lang.String TAG = null;
    public Vb binding;
    public android.content.Context mContext;
    private java.util.HashMap _$_findViewCache;
    
    protected final java.lang.String getTAG() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final Vb getBinding() {
        return null;
    }
    
    public final void setBinding(@org.jetbrains.annotations.NotNull
    Vb p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final android.content.Context getMContext() {
        return null;
    }
    
    public final void setMContext(@org.jetbrains.annotations.NotNull
    android.content.Context p0) {
    }
    
    public BaseFrameLayout(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super(null);
    }
    
    public BaseFrameLayout(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.util.AttributeSet attributes) {
        super(null);
    }
    
    public BaseFrameLayout(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.util.AttributeSet attributes, int p2_52215) {
        super(null);
    }
    
    public abstract void initView();
    
    @androidx.annotation.LayoutRes
    protected abstract int getLayout();
}
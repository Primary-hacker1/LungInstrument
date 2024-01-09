package com.common.base;

import java.lang.System;

/**
 * create by 2021/3/2
 *
 * @author zt
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\r\u0010\u000e\u001a\u00028\u0000H$\u00a2\u0006\u0002\u0010\tJ\b\u0010\u000f\u001a\u00020\u0010H$J\u0012\u0010\u0011\u001a\u00020\u00102\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014R\u0010\u0010\u0005\u001a\u00028\u0000X\u0082.\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u0014\u0010\u0007\u001a\u00028\u00008DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u000bX\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0014"}, d2 = {"Lcom/common/base/CommonBaseActivity;", "VB", "Landroidx/databinding/ViewDataBinding;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "_binding", "Landroidx/databinding/ViewDataBinding;", "binding", "getBinding", "()Landroidx/databinding/ViewDataBinding;", "tag", "", "getTag", "()Ljava/lang/String;", "getViewBinding", "initView", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "common_debug"})
public abstract class CommonBaseActivity<VB extends androidx.databinding.ViewDataBinding> extends androidx.appcompat.app.AppCompatActivity {
    private VB _binding;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String tag = null;
    private java.util.HashMap _$_findViewCache;
    
    public CommonBaseActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    protected final VB getBinding() {
        return null;
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.NotNull
    protected abstract VB getViewBinding();
    
    @org.jetbrains.annotations.NotNull
    protected final java.lang.String getTag() {
        return null;
    }
    
    protected abstract void initView();
}
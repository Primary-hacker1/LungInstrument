package com.common.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0014H%J\b\u0010\u0015\u001a\u00020\u0012H&J\b\u0010\u0016\u001a\u00020\u0012H&J\b\u0010\u0017\u001a\u00020\u0012H$J&\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010 \u001a\u00020\u0012H\u0016J\b\u0010!\u001a\u00020\u0012H\u0016J\u001a\u0010\"\u001a\u00020\u00122\u0006\u0010#\u001a\u00020\u00192\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010$\u001a\u00020\u0012H\u0005J\u001a\u0010%\u001a\u00020\u00122\u0006\u0010&\u001a\u00020\'2\b\u0010(\u001a\u0004\u0018\u00010\u0006H\u0016J\u0012\u0010)\u001a\u00020\u00122\b\u0010*\u001a\u0004\u0018\u00010+H&J5\u0010,\u001a\u00020\u0012\"\n\b\u0001\u0010-\u0018\u0001*\u00020.*\u00020/2\u0017\u00100\u001a\u0013\u0012\u0004\u0012\u000202\u0012\u0004\u0012\u00020\u001201\u00a2\u0006\u0002\b3H\u0086\b\u00f8\u0001\u0000R\u0014\u0010\u0005\u001a\u00020\u0006X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u00028\u0000X\u0086.\u00a2\u0006\u0010\n\u0002\u0010\u000e\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u00064"}, d2 = {"Lcom/common/base/BaseDialogFragment;", "VB", "Landroidx/databinding/ViewDataBinding;", "Landroidx/fragment/app/DialogFragment;", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "binding", "getBinding", "()Landroidx/databinding/ViewDataBinding;", "setBinding", "(Landroidx/databinding/ViewDataBinding;)V", "Landroidx/databinding/ViewDataBinding;", "isShow", "", "dismiss", "", "getLayout", "", "initData", "initListener", "initView", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "onStart", "onViewCreated", "view", "setTitleBar", "show", "manager", "Landroidx/fragment/app/FragmentManager;", "tag", "start", "dialog", "Landroid/app/Dialog;", "startActivity", "T", "Landroid/app/Activity;", "Landroid/content/Context;", "action", "Lkotlin/Function1;", "Landroid/content/Intent;", "Lkotlin/ExtensionFunctionType;", "common_debug"})
public abstract class BaseDialogFragment<VB extends androidx.databinding.ViewDataBinding> extends androidx.fragment.app.DialogFragment {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String TAG = null;
    public VB binding;
    private boolean isShow = false;
    private java.util.HashMap _$_findViewCache;
    
    public BaseDialogFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    protected final java.lang.String getTAG() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final VB getBinding() {
        return null;
    }
    
    public final void setBinding(@org.jetbrains.annotations.NotNull
    VB p0) {
    }
    
    @java.lang.Override
    public void onStart() {
    }
    
    @androidx.annotation.RequiresApi(value = android.os.Build.VERSION_CODES.LOLLIPOP)
    protected final void setTitleBar() {
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    public abstract void start(@org.jetbrains.annotations.Nullable
    android.app.Dialog dialog);
    
    protected abstract void initView();
    
    public abstract void initListener();
    
    public abstract void initData();
    
    @androidx.annotation.LayoutRes
    protected abstract int getLayout();
    
    @java.lang.Override
    public void show(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager manager, @org.jetbrains.annotations.Nullable
    java.lang.String tag) {
    }
    
    @java.lang.Override
    public void dismiss() {
    }
    
    @java.lang.Override
    public void onResume() {
    }
}
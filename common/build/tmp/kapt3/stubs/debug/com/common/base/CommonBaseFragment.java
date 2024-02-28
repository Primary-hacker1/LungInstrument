package com.common.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\u001f\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H$\u00a2\u0006\u0002\u0010\u0018J\u0012\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u001aH$J\b\u0010\u001e\u001a\u00020\u001aH$J\b\u0010\u001f\u001a\u00020\u001aH\u0002J\b\u0010 \u001a\u00020\u001aH$J\u0018\u0010!\u001a\u00020\u001a2\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%H\u0014J\"\u0010!\u001a\u00020\u001a2\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0014J\u0012\u0010(\u001a\u00020\u001a2\b\u0010)\u001a\u0004\u0018\u00010\'H\u0016J\u0010\u0010*\u001a\u00020\u001a2\u0006\u0010+\u001a\u00020\u0012H\u0016J&\u0010,\u001a\u0004\u0018\u00010#2\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\b\u0010)\u001a\u0004\u0018\u00010\'H\u0016J\b\u0010-\u001a\u00020\u001aH\u0016J\u0010\u0010.\u001a\u00020\u001a2\u0006\u0010/\u001a\u00020\u000fH\u0016R\u0014\u0010\u0005\u001a\u00020\u0006X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0010\u0010\t\u001a\u00028\u0000X\u0082.\u00a2\u0006\u0004\n\u0002\u0010\nR\u0014\u0010\u000b\u001a\u00028\u00008DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/common/base/CommonBaseFragment;", "VB", "Landroidx/databinding/ViewDataBinding;", "Landroidx/fragment/app/Fragment;", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "_binding", "Landroidx/databinding/ViewDataBinding;", "binding", "getBinding", "()Landroidx/databinding/ViewDataBinding;", "isComplete", "", "isViewCreated", "mContext", "Landroid/content/Context;", "getViewBinding", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "(Landroid/view/LayoutInflater;Landroid/view/ViewGroup;)Landroidx/databinding/ViewDataBinding;", "hideKeyboard", "", "token", "Landroid/os/IBinder;", "initListener", "initView", "lazyLoad", "loadData", "navigate", "view", "Landroid/view/View;", "id", "", "bundle", "Landroid/os/Bundle;", "onActivityCreated", "savedInstanceState", "onAttach", "context", "onCreateView", "onDestroy", "setUserVisibleHint", "isVisibleToUser", "common_debug"})
public abstract class CommonBaseFragment<VB extends androidx.databinding.ViewDataBinding> extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String TAG = null;
    private android.content.Context mContext;
    private VB _binding;
    
    /**
     * 控件是否初始化完成
     */
    private boolean isViewCreated = false;
    
    /**
     * 是否加载过数据
     */
    private boolean isComplete = false;
    private java.util.HashMap _$_findViewCache;
    
    public CommonBaseFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    protected final java.lang.String getTAG() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    protected final VB getBinding() {
        return null;
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
    public void onAttach(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    @java.lang.Override
    public void onActivityCreated(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
    }
    
    protected void navigate(@org.jetbrains.annotations.NotNull
    android.view.View view, int id) {
    }
    
    protected void navigate(@org.jetbrains.annotations.NotNull
    android.view.View view, int id, @org.jetbrains.annotations.Nullable
    android.os.Bundle bundle) {
    }
    
    /**
     * 懒加载
     */
    private final void lazyLoad() {
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
    
    protected abstract void loadData();
    
    protected abstract void initView();
    
    protected abstract void initListener();
    
    @org.jetbrains.annotations.NotNull
    protected abstract VB getViewBinding(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container);
    
    /**
     * @param token - 获取InputMethodManager，隐藏软键盘
     */
    public void hideKeyboard(@org.jetbrains.annotations.Nullable
    android.os.IBinder token) {
    }
}
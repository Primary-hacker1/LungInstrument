package com.common.base;

import java.lang.System;

/**
 * create by 2019/9/28
 *
 * @author yx
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\r\n\u0002\b\u0004\u0018\u00002\u00020\u0001B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\u0002\u0010\tJ \u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0019\u001a\u00020\u0015H\u0016J\u0010\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\u0015H\u0016J\u0012\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0018\u0010\u001e\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u001e\u0010\u001f\u001a\u00020\u00112\u0006\u0010 \u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0003R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0016\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/common/base/BasePageAdapter;", "Landroidx/fragment/app/FragmentPagerAdapter;", "fm", "Landroidx/fragment/app/FragmentManager;", "fragments", "", "Landroidx/fragment/app/Fragment;", "titles", "", "(Landroidx/fragment/app/FragmentManager;Ljava/util/List;Ljava/util/List;)V", "getFm", "()Landroidx/fragment/app/FragmentManager;", "setFm", "(Landroidx/fragment/app/FragmentManager;)V", "fragmentList", "title", "destroyItem", "", "container", "Landroid/view/ViewGroup;", "position", "", "object", "", "finishUpdate", "getCount", "getItem", "i", "getPageTitle", "", "instantiateItem", "setFragment", "manager", "common_debug"})
public final class BasePageAdapter extends androidx.fragment.app.FragmentPagerAdapter {
    private java.util.List<? extends androidx.fragment.app.Fragment> fragmentList;
    private final java.util.List<java.lang.String> title = null;
    @org.jetbrains.annotations.NotNull
    private androidx.fragment.app.FragmentManager fm;
    
    public BasePageAdapter(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager fm, @org.jetbrains.annotations.NotNull
    java.util.List<? extends androidx.fragment.app.Fragment> fragments, @org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> titles) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.fragment.app.FragmentManager getFm() {
        return null;
    }
    
    public final void setFm(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public androidx.fragment.app.Fragment getItem(int i) {
        return null;
    }
    
    @java.lang.Override
    public int getCount() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public java.lang.CharSequence getPageTitle(int position) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.Object instantiateItem(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup container, int position) {
        return null;
    }
    
    @java.lang.Override
    public void destroyItem(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup container, int position, @org.jetbrains.annotations.NotNull
    java.lang.Object object) {
    }
    
    @java.lang.Override
    public void finishUpdate(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup container) {
    }
    
    @android.annotation.SuppressLint(value = {"CommitTransaction"})
    private final void setFragment(androidx.fragment.app.FragmentManager manager, java.util.List<? extends androidx.fragment.app.Fragment> fragments) {
    }
}
package com.just.machine.util;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 \'2\u00020\u0001:\u0001\'B\'\b\u0007\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001c\u001a\u00020\u001bH\u0016J\u0010\u0010\u001d\u001a\u00020\u001b2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0010\u0010 \u001a\u00020\u000f2\u0006\u0010!\u001a\u00020\u001fH\u0016J\u0010\u0010\"\u001a\u00020\u000f2\u0006\u0010!\u001a\u00020\u001fH\u0016J\u0018\u0010#\u001a\u00020\u00072\u0006\u0010$\u001a\u00020\u00072\u0006\u0010%\u001a\u00020\u0007H\u0002J\b\u0010&\u001a\u00020\u001bH\u0002R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/just/machine/util/PatientRV;", "Landroidx/recyclerview/widget/RecyclerView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyle", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "mFirstX", "", "mFirstY", "mFlingView", "Landroid/view/ViewGroup;", "mIsSlide", "", "mLastX", "mMenuViewWidth", "mPosition", "mScroller", "Landroid/widget/Scroller;", "mTouchFrame", "Landroid/graphics/Rect;", "mTouchSlop", "mVelocityTracker", "Landroid/view/VelocityTracker;", "closeMenu", "", "computeScroll", "obtainVelocity", "event", "Landroid/view/MotionEvent;", "onInterceptTouchEvent", "e", "onTouchEvent", "pointToPosition", "x", "y", "releaseVelocity", "Companion", "app_debug"})
public final class PatientRV extends androidx.recyclerview.widget.RecyclerView {
    private android.view.VelocityTracker mVelocityTracker;
    private final int mTouchSlop = 0;
    private android.graphics.Rect mTouchFrame;
    private final android.widget.Scroller mScroller = null;
    private float mLastX = 0.0F;
    private float mFirstX = 0.0F;
    private float mFirstY = 0.0F;
    private boolean mIsSlide = false;
    private android.view.ViewGroup mFlingView;
    private int mPosition = 0;
    private int mMenuViewWidth = 0;
    @org.jetbrains.annotations.NotNull
    public static final com.just.machine.util.PatientRV.Companion Companion = null;
    private static final java.lang.String TAG = "SlideRecyclerView";
    private static final int INVALID_POSITION = -1;
    private static final int INVALID_CHILD_WIDTH = -1;
    private static final int SNAP_VELOCITY = 600;
    private java.util.HashMap _$_findViewCache;
    
    @kotlin.jvm.JvmOverloads
    public PatientRV(@org.jetbrains.annotations.Nullable
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads
    public PatientRV(@org.jetbrains.annotations.Nullable
    android.content.Context context, @org.jetbrains.annotations.Nullable
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads
    public PatientRV(@org.jetbrains.annotations.Nullable
    android.content.Context context, @org.jetbrains.annotations.Nullable
    android.util.AttributeSet attrs, int defStyle) {
        super(null);
    }
    
    @java.lang.Override
    public boolean onInterceptTouchEvent(@org.jetbrains.annotations.NotNull
    android.view.MotionEvent e) {
        return false;
    }
    
    @java.lang.Override
    public boolean onTouchEvent(@org.jetbrains.annotations.NotNull
    android.view.MotionEvent e) {
        return false;
    }
    
    private final void releaseVelocity() {
    }
    
    private final void obtainVelocity(android.view.MotionEvent event) {
    }
    
    private final int pointToPosition(int x, int y) {
        return 0;
    }
    
    @java.lang.Override
    public void computeScroll() {
    }
    
    /**
     * 将显示子菜单的子view关闭
     * 这里本身是要自己来实现的，但是由于不定制item，因此不好监听器点击事件，因此需要调用者手动的关闭
     */
    private final void closeMenu() {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/just/machine/util/PatientRV$Companion;", "", "()V", "INVALID_CHILD_WIDTH", "", "INVALID_POSITION", "SNAP_VELOCITY", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
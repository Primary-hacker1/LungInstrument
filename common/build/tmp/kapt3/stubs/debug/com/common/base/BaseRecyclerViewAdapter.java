package com.common.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u0000*\u0004\b\u0000\u0010\u0001*\b\b\u0001\u0010\u0002*\u00020\u00032\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004B#\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000bJ-\u0010\u0018\u001a\u00020\u00192\u000e\b\u0001\u0010\u001a\u001a\b\u0012\u0004\u0012\u00028\u00010\u00052\u0006\u0010\u001b\u001a\u00020\t2\u0006\u0010\u001c\u001a\u00028\u0000H\u0014\u00a2\u0006\u0002\u0010\u001dJ\b\u0010\u001e\u001a\u00020\tH\u0016J\u0013\u0010\u001f\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00028\u0000\u00a2\u0006\u0002\u0010 J\u001e\u0010!\u001a\u00020\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00028\u00010\u00052\u0006\u0010\"\u001a\u00020\tH\u0016J\u001e\u0010#\u001a\b\u0012\u0004\u0012\u00028\u00010\u00052\u0006\u0010$\u001a\u00020%2\u0006\u0010\"\u001a\u00020\tH\u0016J\u0014\u0010&\u001a\u00020\u00192\f\u0010\'\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007R\u0010\u0010\f\u001a\u00028\u0001X\u0082.\u00a2\u0006\u0004\n\u0002\u0010\rR\u001a\u0010\n\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R \u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000f\"\u0004\b\u0017\u0010\u0011\u00a8\u0006("}, d2 = {"Lcom/common/base/BaseRecyclerViewAdapter;", "T", "Vb", "Landroidx/databinding/ViewDataBinding;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/common/base/BaseDataBingViewHolder;", "itemData", "Landroidx/databinding/ObservableList;", "layoutId", "", "dataId", "(Landroidx/databinding/ObservableList;II)V", "bing", "Landroidx/databinding/ViewDataBinding;", "getDataId", "()I", "setDataId", "(I)V", "getItemData", "()Landroidx/databinding/ObservableList;", "setItemData", "(Landroidx/databinding/ObservableList;)V", "getLayoutId", "setLayoutId", "bindViewHolder", "", "viewHolder", "position", "t", "(Lcom/common/base/BaseDataBingViewHolder;ILjava/lang/Object;)V", "getItemCount", "getItemLayout", "(Ljava/lang/Object;)I", "onBindViewHolder", "i", "onCreateViewHolder", "viewGroup", "Landroid/view/ViewGroup;", "onSetItem", "newItemData", "common_debug"})
public abstract class BaseRecyclerViewAdapter<T extends java.lang.Object, Vb extends androidx.databinding.ViewDataBinding> extends androidx.recyclerview.widget.RecyclerView.Adapter<com.common.base.BaseDataBingViewHolder<Vb>> {
    @org.jetbrains.annotations.NotNull
    private androidx.databinding.ObservableList<T> itemData;
    private int layoutId;
    private int dataId;
    private Vb bing;
    
    public BaseRecyclerViewAdapter(@org.jetbrains.annotations.NotNull
    androidx.databinding.ObservableList<T> itemData, int layoutId, int dataId) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.databinding.ObservableList<T> getItemData() {
        return null;
    }
    
    public final void setItemData(@org.jetbrains.annotations.NotNull
    androidx.databinding.ObservableList<T> p0) {
    }
    
    public final int getLayoutId() {
        return 0;
    }
    
    public final void setLayoutId(int p0) {
    }
    
    public final int getDataId() {
        return 0;
    }
    
    public final void setDataId(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.common.base.BaseDataBingViewHolder<Vb> onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup viewGroup, int i) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.common.base.BaseDataBingViewHolder<Vb> viewHolder, int i) {
    }
    
    protected void bindViewHolder(@org.jetbrains.annotations.NotNull
    @androidx.annotation.NonNull
    com.common.base.BaseDataBingViewHolder<Vb> viewHolder, int position, T t) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    public final int getItemLayout(T itemData) {
        return 0;
    }
    
    public final void onSetItem(@org.jetbrains.annotations.NotNull
    androidx.databinding.ObservableList<T> newItemData) {
    }
}
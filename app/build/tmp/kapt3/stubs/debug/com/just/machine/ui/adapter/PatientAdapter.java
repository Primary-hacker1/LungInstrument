package com.just.machine.ui.adapter;

import java.lang.System;

/**
 * create by 2024/2/27
 * 患者信息adapter
 * @author zt
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0012B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\b\u0010\u000b\u001a\u00020\nH\u0016J\u0018\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\nH\u0017J\u0018\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\nH\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/just/machine/ui/adapter/PatientAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "mDatas", "", "Lcom/just/machine/dao/PatientBean;", "(Ljava/util/List;)V", "deleteItem", "", "position", "", "getItemCount", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ItemHolder", "app_debug"})
public final class PatientAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder> {
    private final java.util.List<com.just.machine.dao.PatientBean> mDatas = null;
    
    public PatientAdapter(@org.jetbrains.annotations.NotNull
    java.util.List<com.just.machine.dao.PatientBean> mDatas) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public androidx.recyclerview.widget.RecyclerView.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
    }
    
    private final void deleteItem(int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\f\u00a8\u0006\u000f"}, d2 = {"Lcom/just/machine/ui/adapter/PatientAdapter$ItemHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "ivDelete", "Landroid/widget/ImageView;", "getIvDelete", "()Landroid/widget/ImageView;", "medicalRecordNumber", "Landroid/widget/TextView;", "getMedicalRecordNumber", "()Landroid/widget/TextView;", "tvContent", "getTvContent", "app_debug"})
    public static final class ItemHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvContent = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView medicalRecordNumber = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.ImageView ivDelete = null;
        
        public ItemHolder(@org.jetbrains.annotations.NotNull
        android.view.View itemView) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvContent() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getMedicalRecordNumber() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.ImageView getIvDelete() {
            return null;
        }
    }
}
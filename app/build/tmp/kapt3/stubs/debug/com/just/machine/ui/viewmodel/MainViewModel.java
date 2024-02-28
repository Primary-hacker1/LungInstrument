package com.just.machine.ui.viewmodel;

import java.lang.System;

/**
 * create by 2020/6/19
 *
 * @author zt
 */
@dagger.hilt.android.lifecycle.HiltViewModel
@kotlin.Suppress(names = {"CAST_NEVER_SUCCEEDS"})
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0013\u001a\u00020\u000fJ\u000e\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011R \u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/just/machine/ui/viewmodel/MainViewModel;", "Lcom/common/viewmodel/BaseViewModel;", "repository", "Lcom/just/machine/api/UserRepository;", "plantDao", "Lcom/just/machine/dao/PlantRepository;", "(Lcom/just/machine/api/UserRepository;Lcom/just/machine/dao/PlantRepository;)V", "itemNews", "Landroidx/databinding/ObservableList;", "Lcom/just/machine/model/Data;", "getItemNews", "()Landroidx/databinding/ObservableList;", "setItemNews", "(Landroidx/databinding/ObservableList;)V", "getDates", "", "type", "", "getNewsLoading", "getPatient", "getRxNews", "app_debug"})
public final class MainViewModel extends com.common.viewmodel.BaseViewModel {
    private com.just.machine.api.UserRepository repository;
    private com.just.machine.dao.PlantRepository plantDao;
    @org.jetbrains.annotations.NotNull
    private androidx.databinding.ObservableList<com.just.machine.model.Data> itemNews;
    
    @javax.inject.Inject
    public MainViewModel(@org.jetbrains.annotations.NotNull
    com.just.machine.api.UserRepository repository, @org.jetbrains.annotations.NotNull
    com.just.machine.dao.PlantRepository plantDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.databinding.ObservableList<com.just.machine.model.Data> getItemNews() {
        return null;
    }
    
    public final void setItemNews(@org.jetbrains.annotations.NotNull
    androidx.databinding.ObservableList<com.just.machine.model.Data> p0) {
    }
    
    /**
     * @param type 协程请求->直接获取结果的
     */
    public final void getDates(@org.jetbrains.annotations.NotNull
    java.lang.String type) {
    }
    
    public final void getPatient() {
    }
    
    /**
     * @param type 协程请求->带loading的
     */
    public final void getNewsLoading(@org.jetbrains.annotations.NotNull
    java.lang.String type) {
    }
    
    /**
     * @param type rxjava请求->直接获取结果的
     */
    public final void getRxNews(@org.jetbrains.annotations.NotNull
    java.lang.String type) {
    }
}
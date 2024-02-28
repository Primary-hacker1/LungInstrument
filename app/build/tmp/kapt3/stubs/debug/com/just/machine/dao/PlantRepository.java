package com.just.machine.dao;

import java.lang.System;

/**
 * 用于处理数据操作的存储库模块。
 *
 * 从 [PlantDao] 中的 Flow 中收集是主要安全的。 Room 支持 Coroutines 并移动
 * 主线程外的查询执行。
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006J\u0012\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006J\u001f\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJ\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0011"}, d2 = {"Lcom/just/machine/dao/PlantRepository;", "", "plantDao", "Lcom/just/machine/dao/PlantDao;", "(Lcom/just/machine/dao/PlantDao;)V", "getPatients", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/just/machine/dao/PatientBean;", "getPlants", "insertAll", "", "plants", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertPatient", "", "patients", "app_debug"})
public final class PlantRepository {
    private final com.just.machine.dao.PlantDao plantDao = null;
    
    @javax.inject.Inject
    public PlantRepository(@org.jetbrains.annotations.NotNull
    com.just.machine.dao.PlantDao plantDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.just.machine.dao.PatientBean>> getPlants() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.just.machine.dao.PatientBean>> getPatients() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object insertAll(@org.jetbrains.annotations.NotNull
    java.util.List<com.just.machine.dao.PatientBean> plants, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    public final long insertPatient(@org.jetbrains.annotations.NotNull
    com.just.machine.dao.PatientBean patients) {
        return 0L;
    }
}
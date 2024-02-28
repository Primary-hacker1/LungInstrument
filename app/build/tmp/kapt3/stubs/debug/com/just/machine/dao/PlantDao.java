package com.just.machine.dao;

import java.lang.System;

/**
 * Plant 类的数据访问对象。
 */
@androidx.room.Dao
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u0014\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u001c\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\b\u001a\u00020\tH\'J\u001f\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005H\'\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0011"}, d2 = {"Lcom/just/machine/dao/PlantDao;", "", "getPatients", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/just/machine/dao/PatientBean;", "getPlants", "getPlantsWithGrowZoneNumber", "age", "", "insertAll", "", "plants", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertPatient", "", "patients", "app_debug"})
public abstract interface PlantDao {
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM patients ORDER BY name")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.just.machine.dao.PatientBean>> getPlants();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM patients WHERE age = :age ORDER BY name")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.just.machine.dao.PatientBean>> getPlantsWithGrowZoneNumber(int age);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM patients")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.just.machine.dao.PatientBean>> getPatients();
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract java.lang.Object insertAll(@org.jetbrains.annotations.NotNull
    java.util.List<com.just.machine.dao.PatientBean> plants, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @androidx.room.Insert
    public abstract long insertPatient(@org.jetbrains.annotations.NotNull
    com.just.machine.dao.PatientBean patients);
}
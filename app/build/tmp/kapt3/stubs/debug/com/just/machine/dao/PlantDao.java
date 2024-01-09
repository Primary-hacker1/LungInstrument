package com.just.machine.dao;

import java.lang.System;

/**
 * Plant 类的数据访问对象。
 */
@androidx.room.Dao
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\b0\u0003H\'J\u001c\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\b0\u00032\u0006\u0010\n\u001a\u00020\u000bH\'J\u001f\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00040\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0010"}, d2 = {"Lcom/just/machine/dao/PlantDao;", "", "getPlant", "Lkotlinx/coroutines/flow/Flow;", "Lcom/just/machine/dao/Plant;", "plantId", "", "getPlants", "", "getPlantsWithGrowZoneNumber", "growZoneNumber", "", "insertAll", "", "plants", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface PlantDao {
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM plants ORDER BY name")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.just.machine.dao.Plant>> getPlants();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM plants WHERE growZoneNumber = :growZoneNumber ORDER BY name")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.just.machine.dao.Plant>> getPlantsWithGrowZoneNumber(int growZoneNumber);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM plants WHERE id = :plantId")
    public abstract kotlinx.coroutines.flow.Flow<com.just.machine.dao.Plant> getPlant(@org.jetbrains.annotations.NotNull
    java.lang.String plantId);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract java.lang.Object insertAll(@org.jetbrains.annotations.NotNull
    java.util.List<com.just.machine.dao.Plant> plants, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
}
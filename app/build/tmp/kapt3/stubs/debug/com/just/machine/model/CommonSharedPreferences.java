package com.just.machine.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J)\u0010\u0005\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u0002H\u0006\u00a2\u0006\u0002\u0010\u000bJ!\u0010\f\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u00062\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u0002H\u0006\u00a2\u0006\u0002\u0010\rJ+\u0010\u000e\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u0002H\u0006H\u0002\u00a2\u0006\u0002\u0010\u0011J!\u0010\u0012\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u00062\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u0002H\u0006\u00a2\u0006\u0002\u0010\u0013J(\u0010\u0005\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006*\u00020\u00142\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u0002H\u0006H\u0086\u0002\u00a2\u0006\u0002\u0010\u0015J%\u0010\u0016\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006*\u00020\u00172\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u0002H\u0006\u00a2\u0006\u0002\u0010\u0018J%\u0010\u0016\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006*\u00020\u00192\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u0002H\u0006\u00a2\u0006\u0002\u0010\u001aJ%\u0010\u001b\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0006*\u00020\u00172\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u0002H\u0006\u00a2\u0006\u0002\u0010\u001cJ%\u0010\u001b\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0006*\u00020\u00192\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u0002H\u0006\u00a2\u0006\u0002\u0010\u001dJ(\u0010\u001e\u001a\u00020\u000f\"\u0004\b\u0000\u0010\u0006*\u00020\u00142\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u0002H\u0006H\u0086\u0002\u00a2\u0006\u0002\u0010\u001fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/just/machine/model/CommonSharedPreferences;", "", "()V", "USER", "", "get", "T", "mShareConfig", "Landroid/content/SharedPreferences;", "key", "defValue", "(Landroid/content/SharedPreferences;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", "getSPValue", "(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", "put", "", "value", "(Landroid/content/SharedPreferences;Ljava/lang/String;Ljava/lang/Object;)V", "setSPValue", "(Ljava/lang/String;Ljava/lang/Object;)V", "Landroid/content/Context;", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", "privateGet", "Landroid/app/Activity;", "(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", "Landroidx/fragment/app/Fragment;", "(Landroidx/fragment/app/Fragment;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", "privateSet", "(Landroid/app/Activity;Ljava/lang/String;Ljava/lang/Object;)V", "(Landroidx/fragment/app/Fragment;Ljava/lang/String;Ljava/lang/Object;)V", "set", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/Object;)V", "app_debug"})
public final class CommonSharedPreferences {
    @org.jetbrains.annotations.NotNull
    public static final com.just.machine.model.CommonSharedPreferences INSTANCE = null;
    
    /**
     * Created by lollipop on 2017/12/10.
     * Update by lollipop on 2020/11/09
     * @author Lollipop
     * 持久化储存的工具类
     */
    private static final java.lang.String USER = "Settings";
    
    private CommonSharedPreferences() {
        super();
    }
    
    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    public final <T extends java.lang.Object>void set(@org.jetbrains.annotations.NotNull
    android.content.Context $this$set, @org.jetbrains.annotations.NotNull
    java.lang.String key, T value) {
    }
    
    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    public final <T extends java.lang.Object>void setSPValue(@org.jetbrains.annotations.NotNull
    java.lang.String key, T value) {
    }
    
    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * 但是它是私有的，只有当前Activity中可以访问
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    public final <T extends java.lang.Object>void privateSet(@org.jetbrains.annotations.NotNull
    android.app.Activity $this$privateSet, @org.jetbrains.annotations.NotNull
    java.lang.String key, T value) {
    }
    
    /**
     * 储存一个受SharedPreferences支持的数据到指定的Key中
     * 但是它是私有的，只有当前Fragment中可以访问
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    public final <T extends java.lang.Object>void privateSet(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.Fragment $this$privateSet, @org.jetbrains.annotations.NotNull
    java.lang.String key, T value) {
    }
    
    /**
     * 储存数据到一个指定的SharedPreferences中
     * @param key 名称
     * @param value 值，目前只支持String，Long，Boolean，Int，Float
     */
    private final <T extends java.lang.Object>void put(android.content.SharedPreferences mShareConfig, java.lang.String key, T value) {
    }
    
    /**
     * 从Context中获取已经储存的值
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    public final <T extends java.lang.Object>T get(@org.jetbrains.annotations.NotNull
    android.content.Context $this$get, @org.jetbrains.annotations.NotNull
    java.lang.String key, T defValue) {
        return null;
    }
    
    /**
     * 从Context中获取已经储存的值
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    public final <T extends java.lang.Object>T getSPValue(@org.jetbrains.annotations.NotNull
    java.lang.String key, T defValue) {
        return null;
    }
    
    /**
     * 从Activity中获取已经储存的值
     * 它是从私有SharedPreferences中获取
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    public final <T extends java.lang.Object>T privateGet(@org.jetbrains.annotations.NotNull
    android.app.Activity $this$privateGet, @org.jetbrains.annotations.NotNull
    java.lang.String key, T defValue) {
        return null;
    }
    
    /**
     * 从Fragment中获取已经储存的值
     * 它是从私有SharedPreferences中获取
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    public final <T extends java.lang.Object>T privateGet(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.Fragment $this$privateGet, @org.jetbrains.annotations.NotNull
    java.lang.String key, T defValue) {
        return null;
    }
    
    /**
     * 从指定的SharedPreferences中获取数据
     * 如果找不到有效的结果，那么将会返回默认值
     * @param key 数据的key
     * @param defValue 默认值，它的类型决定了返回值的类型，
     * 如果是不受支持的数据类型，那么将会直接返回默认值
     * 如果没有找到符合需求的结果，那么也会直接返回默认值
     */
    @kotlin.Suppress(names = {"UNCHECKED_CAST"})
    public final <T extends java.lang.Object>T get(@org.jetbrains.annotations.NotNull
    android.content.SharedPreferences mShareConfig, @org.jetbrains.annotations.NotNull
    java.lang.String key, T defValue) {
        return null;
    }
}
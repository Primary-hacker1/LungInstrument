package com.common.viewmodel;

import java.lang.System;

/**
 * create by 2020/9/16
 * @author zt
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0016\u0018\u0000 \u00142\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002:\u0001\u0014B\u0005\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\n0\t\"\u0004\b\u0000\u0010\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00030\u000eH\u0016J\u000e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0014J\b\u0010\u0013\u001a\u00020\u0003H\u0016R\u001c\u0010\u0005\u001a\u0010\u0012\f\u0012\n \u0007*\u0004\u0018\u00010\u00030\u00030\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/common/viewmodel/BaseLifeViewModel;", "Landroidx/lifecycle/ViewModel;", "Lcom/uber/autodispose/lifecycle/LifecycleScopeProvider;", "Lcom/common/viewmodel/ViewEvent;", "()V", "lifecycleEvents", "Lio/reactivex/subjects/BehaviorSubject;", "kotlin.jvm.PlatformType", "auto", "Lcom/uber/autodispose/AutoDisposeConverter;", "T", "provider", "Lcom/uber/autodispose/ScopeProvider;", "correspondingEvents", "Lcom/uber/autodispose/lifecycle/CorrespondingEventsFunction;", "lifecycle", "Lio/reactivex/Observable;", "onCleared", "", "peekLifecycle", "Companion", "common_debug"})
public class BaseLifeViewModel extends androidx.lifecycle.ViewModel implements com.uber.autodispose.lifecycle.LifecycleScopeProvider<com.common.viewmodel.ViewEvent> {
    private final io.reactivex.subjects.BehaviorSubject<com.common.viewmodel.ViewEvent> lifecycleEvents = null;
    @org.jetbrains.annotations.NotNull
    public static final com.common.viewmodel.BaseLifeViewModel.Companion Companion = null;
    @org.jetbrains.annotations.NotNull
    private static com.uber.autodispose.lifecycle.CorrespondingEventsFunction<com.common.viewmodel.ViewEvent> CORRESPONDING_EVENTS;
    
    public BaseLifeViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public io.reactivex.Observable<com.common.viewmodel.ViewEvent> lifecycle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.uber.autodispose.lifecycle.CorrespondingEventsFunction<com.common.viewmodel.ViewEvent> correspondingEvents() {
        return null;
    }
    
    /**
     * Emit the [ViewModelEvent.CLEARED] event to
     * dispose off any subscriptions in the ViewModel.
     */
    @java.lang.Override
    protected void onCleared() {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.common.viewmodel.ViewEvent peekLifecycle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final <T extends java.lang.Object>com.uber.autodispose.AutoDisposeConverter<T> auto(@org.jetbrains.annotations.NotNull
    com.uber.autodispose.ScopeProvider provider) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R \u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2 = {"Lcom/common/viewmodel/BaseLifeViewModel$Companion;", "", "()V", "CORRESPONDING_EVENTS", "Lcom/uber/autodispose/lifecycle/CorrespondingEventsFunction;", "Lcom/common/viewmodel/ViewEvent;", "getCORRESPONDING_EVENTS", "()Lcom/uber/autodispose/lifecycle/CorrespondingEventsFunction;", "setCORRESPONDING_EVENTS", "(Lcom/uber/autodispose/lifecycle/CorrespondingEventsFunction;)V", "common_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.uber.autodispose.lifecycle.CorrespondingEventsFunction<com.common.viewmodel.ViewEvent> getCORRESPONDING_EVENTS() {
            return null;
        }
        
        public final void setCORRESPONDING_EVENTS(@org.jetbrains.annotations.NotNull
        com.uber.autodispose.lifecycle.CorrespondingEventsFunction<com.common.viewmodel.ViewEvent> p0) {
        }
    }
}
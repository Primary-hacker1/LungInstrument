package com.just.machine.util;

import java.lang.System;

/**
 * create by 2024/1/17
 * ECG心电图
 * @author zt
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0010\u0007\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0003\u001a\u00020\u0004J\u0014\u0010\u0011\u001a\u00020\u00002\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013J\u0006\u0010\u0015\u001a\u00020\u0010R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/just/machine/util/TimerEcg;", "", "()V", "ecgData", "Lcom/just/machine/util/TimerEcg$EcgTimerListener;", "getEcgData", "()Lcom/just/machine/util/TimerEcg$EcgTimerListener;", "setEcgData", "(Lcom/just/machine/util/TimerEcg$EcgTimerListener;)V", "tag", "", "timer1", "Ljava/util/Timer;", "timerTask", "Ljava/util/TimerTask;", "ecgTimerListener", "", "startTimer", "oftenListData", "", "", "stopTimer", "EcgTimerListener", "app_debug"})
public final class TimerEcg {
    private java.lang.String tag;
    private java.util.Timer timer1;
    private java.util.TimerTask timerTask;
    @org.jetbrains.annotations.Nullable
    private com.just.machine.util.TimerEcg.EcgTimerListener ecgData;
    
    public TimerEcg() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.just.machine.util.TimerEcg.EcgTimerListener getEcgData() {
        return null;
    }
    
    public final void setEcgData(@org.jetbrains.annotations.Nullable
    com.just.machine.util.TimerEcg.EcgTimerListener p0) {
    }
    
    public final void ecgTimerListener(@org.jetbrains.annotations.NotNull
    com.just.machine.util.TimerEcg.EcgTimerListener ecgData) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.just.machine.util.TimerEcg startTimer(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Float> oftenListData) {
        return null;
    }
    
    public final void stopTimer() {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/just/machine/util/TimerEcg$EcgTimerListener;", "", "ecgTimerView", "", "cooY", "", "app_debug"})
    public static abstract interface EcgTimerListener {
        
        public abstract void ecgTimerView(float cooY);
    }
}
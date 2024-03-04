package com.just.machine.util;

import android.os.Handler;
import android.os.SystemClock;

/**
 * 修复倒计时出现的跳秒问题
 */
public class FixCountDownTime {

    private int mTimes;
    private int allTimes;
    private final long mCountDownInterval;
    private final Handler mHandler;
    private OnTimerCallBack mCallBack;
    private boolean isStart;
    private long startTime;

    public FixCountDownTime(int times, long countDownInterval) {
        this.mTimes = times;
        this.mCountDownInterval = countDownInterval;
        mHandler = new Handler();
    }

    public synchronized void start(OnTimerCallBack callBack) {
        this.mCallBack = callBack;
        if (isStart || mCountDownInterval <= 0) {
            return;
        }

        isStart = true;
        if (callBack != null) {
            callBack.onStart();
        }
        startTime = SystemClock.elapsedRealtime();

        if (mTimes <= 0) {
            finishCountDown();
            return;
        }
        allTimes = mTimes;

        mHandler.postDelayed(runnable, mCountDownInterval);
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mTimes--;
            if (mTimes > 0) {
                if (mCallBack != null) {
                    mCallBack.onTick(mTimes);
                }

                long nowTime = SystemClock.elapsedRealtime();
                long delay = (nowTime - startTime) - (allTimes - mTimes) * mCountDownInterval;
                // 处理跳秒
                while (delay > mCountDownInterval) {
                    mTimes--;
                    if (mCallBack != null) {
                        mCallBack.onTick(mTimes);
                    }

                    delay -= mCountDownInterval;
                    if (mTimes <= 0) {
                        finishCountDown();
                        return;
                    }
                }

                mHandler.postDelayed(this, 1000 - delay);
            } else {
                finishCountDown();
            }
        }
    };

    private void finishCountDown() {
        if (mCallBack != null) {
            mCallBack.onFinish();
        }
        isStart = false;
    }

    public void cancel() {
        mHandler.removeCallbacksAndMessages(null);
        isStart = false;
    }

    public interface OnTimerCallBack {

        void onStart();

        void onTick(int times);

        void onFinish();

    }

    public int getmTimes() {
        return mTimes;
    }

    public void setmTimes(int mTimes) {
        this.mTimes = mTimes;
    }
}
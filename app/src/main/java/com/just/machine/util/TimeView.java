package com.just.machine.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.just.machine.model.Constants;


/**
 * 跟随服务器时间的view
 */
public class TimeView extends AppCompatTextView {

    private long hours;
    private long minutes;
    private long seconds;
    private long diff;
    private long days;
    private long time = 0;
    private String timeAll;

    private long day = 24 * 60 * 60 * 1000 - 1;//一天的long

    public TimeView(Context context) {
        this(context, null);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate();

    }


    /**
     * 根据 attrs 设置时间开始
     */
    private void onCreate() {
        start();
    }

    //开始计时
    private void start() {

        handler.removeMessages(1);

//        getTime();
        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 1000);
    }


    /**
     * 设置事件
     *
     * @param time
     */
    public void setTime(long time, String timeAll) {
        this.timeAll = timeAll;
        this.diff = time;
        diff = diff + 1000 * 60 * 60 * 8;
//        Log.e("小时小时", "" + (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    }

    final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {         // handle message
            if (msg.what == 1) {//setVisibility(View.VISIBLE);
                diff = diff + 1000;
                getShowTime();
                if (diff > 0) {
                    Message message = handler.obtainMessage(1);
                    handler.sendMessageDelayed(message, 1000);
                }  //setVisibility(View.GONE);


                if (diff > day) {//如果计时器大于一天刷新时间
                    LiveDataBus.get().with(Constants.time).setValue("");
                }
            }
//            Log.d("TAG", "再打印");
            super.handleMessage(msg);
        }
    };

    /**
     * 得到时间
     */
    @SuppressLint("SetTextI18n")
    public String getTime() {
//        Log.d("TAG", "再打印 :getTime");
        days = diff / (1000 * 60 * 60 * 24);
        hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
        if (hours < 10) {
            if (minutes < 10) {
                if (seconds < 10) {
                    return timeAll + " " + "0" + hours + ":" + "0" + minutes + ":" + "0" + seconds;
                } else {
                    return timeAll + " " + "0" + hours + ":" + "0" + minutes + ":" + seconds;
                }

            } else {
                if (seconds < 10) {
                    return timeAll + " " + "0" + hours + ":" + minutes + ":" + "0" + seconds;
                } else {
                    return timeAll + " " + "0" + hours + ":" + minutes + ":" + seconds;
                }
            }
        } else {
            if (minutes < 10) {
                if (seconds < 10) {
                    return timeAll + " " + hours + ":" + "0" + minutes + ":" + "0" + seconds;
                } else {
                    return timeAll + " " + hours + ":" + "0" + minutes + ":" + seconds;
                }

            } else {
                if (seconds < 10) {
                    return timeAll + " " + hours + ":" + minutes + ":" + "0" + seconds;
                } else {
                    return timeAll + " " + hours + ":" + minutes + ":" + seconds;
                }
            }

        }
    }

    /**
     * 获得要显示的时间
     */
    @SuppressLint("SetTextI18n")
    private void getShowTime() {
//        Log.d("TAG", "再打印 :getShowTime");

        days = diff / (1000 * 60 * 60 * 24);
        hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
        if (hours < 10) {
            if (minutes < 10) {
                if (seconds < 10) {
                    setText(timeAll + " " + "0" + hours + ":" + "0" + minutes + ":" + "0" + seconds);
                } else {
                    setText(timeAll + " " + "0" + hours + ":" + "0" + minutes + ":" + seconds);
                }

            } else {
                if (seconds < 10) {
                    setText(timeAll + " " + "0" + hours + ":" + minutes + ":" + "0" + seconds);
                } else {
                    setText(timeAll + " " + "0" + hours + ":" + minutes + ":" + seconds);
                }
            }
        } else {
            if (minutes < 10) {
                if (seconds < 10) {
                    setText(timeAll + " " + hours + ":" + "0" + minutes + ":" + "0" + seconds);
                } else {
                    setText(timeAll + " " + hours + ":" + "0" + minutes + ":" + seconds);
                }

            } else {
                if (seconds < 10) {
                    setText(timeAll + " " + hours + ":" + minutes + ":" + "0" + seconds);
                } else {
                    setText(timeAll + " " + hours + ":" + minutes + ":" + seconds);
                }
            }

        }

    }

    /**
     * 以之前设置的时间重新开始
     */
    public void reStart() {
        this.diff = this.time;
        start();
    }

    /**
     * 设置时间重新开始
     *
     * @param time 重新开始的事件
     */
    public void reStart(long time) {
        if (time > 0) {
            this.diff = time * 1000;
//            Log.d("TAG", "+=========================" + diff);
        }
        start();
    }

}
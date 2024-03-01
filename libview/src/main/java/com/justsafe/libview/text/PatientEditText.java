package com.justsafe.libview.text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.justsafe.libview.R;

import java.lang.reflect.Field;

public class PatientEditText extends AppCompatEditText {
    /*
     * 定义属性变量
     * */
    private Paint mPaint; // 画笔

    private Boolean ed_bg;//是否需要默认颜色

    private int ic_deleteResID; // 删除图标 资源ID
    private Drawable ic_delete; // 删除图标
    private int delete_x, delete_y, delete_width, delete_height; // 删除图标起点(x,y)、删除图标宽、高（px）

    private int ic_left_clickResID, ic_left_unclickResID;    // 左侧图标 资源ID（点击 & 无点击）
    private Drawable ic_left_click, ic_left_unclick; // 左侧图标（点击 & 未点击）
    private int left_x, left_y, left_width, left_height; // 左侧图标起点（x,y）、左侧图标宽、高（px）

    private int cursor; // 光标

    // 分割线变量
    private int lineColor_click, lineColor_unclick;// 点击时 & 未点击颜色
    private int color;
    private int linePosition;


    public PatientEditText(Context context) {
        super(context);

    }

    public PatientEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PatientEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 步骤1：初始化属性
     */

    private void init(Context context, AttributeSet attrs) {

        // 获取控件资源
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoginEditText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setTextCursorDrawable(R.drawable.liner_view);
        }

        // 1. 设置画笔
        mPaint = new Paint();

        // 默认颜色
        ed_bg = typedArray.getBoolean(R.styleable.LoginEditText_ed_backgroundColor, false);

        if (ed_bg) {

            setBackground(ContextCompat.getDrawable(getContext(), R.drawable.patient_edittext_bg));
            // 1. 获取资源ID
            ic_deleteResID = typedArray.getResourceId(R.styleable.LoginEditText_ic_delete, R.drawable.ic_delete);
            // 2. 根据资源ID获取图标资源（转化成Drawable对象）
            ic_delete = ContextCompat.getDrawable(getContext(), ic_deleteResID);
            // 3. 设置图标大小
            // 起点(x，y)、宽= left_width、高 = left_height
            delete_x = typedArray.getInteger(R.styleable.LoginEditText_delete_x, 0);
            delete_y = typedArray.getInteger(R.styleable.LoginEditText_delete_y, 0);
            delete_width = typedArray.getInteger(R.styleable.LoginEditText_delete_width, 40);
            delete_height = typedArray.getInteger(R.styleable.LoginEditText_delete_height, 40);

        }

        ic_delete.setBounds(delete_x, delete_y, delete_width, delete_height);

        setCompoundDrawablePadding(20);

        setPadding(10, 0, 0, 0);

        // 2. 设置分割线颜色（使用十六进制代码，如#333、#8e8e8e）
        int lineColorClick_default = context.getResources().getColor(R.color.font_blue); // 默认 = 蓝色#1296db 输入颜色
        int lineColornClick_default = context.getResources().getColor(R.color.white); // 默认 = 灰色#9b9b9b 下滑线颜色
        lineColor_click = typedArray.getColor(R.styleable.LoginEditText_lineColor_click, lineColorClick_default);
        lineColor_unclick = typedArray.getColor(R.styleable.LoginEditText_lineColor_unclick, lineColornClick_default);

        mPaint.setColor(lineColor_unclick); // 分割线默认颜色 = 灰色
//        setTextColor(color); // 字体默认颜色 = 灰色

    }

    /**
     * 复写EditText本身的方法：onTextChanged（）
     * 调用时刻：当输入框内容变化时
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setDeleteIconVisible(hasFocus() && text.length() > 0, hasFocus());
        // hasFocus()返回是否获得EditTEXT的焦点，即是否选中
        // setDeleteIconVisible（） = 根据传入的是否选中 & 是否有输入来判断是否显示删除图标->>关注1
    }

    /**
     * 复写EditText本身的方法：onFocusChanged（）
     * 调用时刻：焦点发生变化时
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setDeleteIconVisible(focused && length() > 0, focused);
        // focused = 是否获得焦点
        // 同样根据setDeleteIconVisible（）判断是否要显示删除图标->>关注1
    }


    /**
     * 作用：对删除图标区域设置为"点击 即 清空搜索框内容"
     * 原理：当手指抬起的位置在删除图标的区域，即视为点击了删除图标 = 清空搜索框内容
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 原理：当手指抬起的位置在删除图标的区域，即视为点击了删除图标 = 清空搜索框内容
        // 判断动作 = 手指抬起时
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Drawable drawable = ic_delete;

            if (drawable != null && event.getX() <= (getWidth() - getPaddingRight())
                    && event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {

                // 判断条件说明
                // event.getX() ：抬起时的位置坐标
                // getWidth()：控件的宽度
                // getPaddingRight():删除图标图标右边缘至EditText控件右边缘的距离
                // 即：getWidth() - getPaddingRight() = 删除图标的右边缘坐标 = X1
                // getWidth() - getPaddingRight() - drawable.getBounds().width() = 删除图标左边缘的坐标 = X2
                // 所以X1与X2之间的区域 = 删除图标的区域
                // 当手指抬起的位置在删除图标的区域（X2=<event.getX() <=X1），即视为点击了删除图标 = 清空搜索框内容
                setText("");

            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 关注1
     * 作用：判断是否显示删除图标 & 设置分割线颜色
     */
    private void setDeleteIconVisible(boolean deleteVisible, boolean leftVisible) {
        setCompoundDrawables(ic_left_click, null,
                deleteVisible ? ic_delete : null, null);
        color = leftVisible ? lineColor_click : lineColor_unclick;
        setTextColor(color);
        invalidate();
    }

    /**
     * 作用：绘制分割线
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mPaint.setColor(color);
        setTextColor(color);
//        // 绘制分割线
//        // 需要考虑：当输入长度超过输入框时，所画的线需要跟随着延伸
//        // 解决方案：线的长度 = 控件长度 + 延伸后的长度
//        int x = this.getScrollX(); // 获取延伸后的长度
//        int w = this.getMeasuredWidth(); // 获取控件长度
//
//        // 传入参数时，线的长度 = 控件长度 + 延伸后的长度
//        canvas.drawLine(0, this.getMeasuredHeight() - linePosition, w + x,
//                this.getMeasuredHeight() - linePosition, mPaint);

    }
}

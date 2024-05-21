package com.seeker.luckychart.annotation;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Seeker
 */
@IntDef({UIMode.TRANSLATE,UIMode.ERASE})
@Retention(RetentionPolicy.SOURCE)
public @interface UIMode {
    int TRANSLATE = 1;
    int ERASE = 2;
}

package com.just.machine.util

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText

class KeyboardUtil {

    companion object {

        fun setEditTextFilter(edittext: EditText) {
            edittext.filters = arrayOf(object : InputFilter {
                override fun filter(
                    source: CharSequence?,
                    start: Int,
                    end: Int,
                    dest: Spanned?,
                    dstart: Int,
                    dend: Int
                ): CharSequence? {
                    if (source != null && (source.contains("\n"))) {
                        // 如果输入包含换行符，则将换行符替换为空字符串
                        return source.toString().replace("\n", "")
                    } else if (source != null && source.contains(" ")) {
                        // 如果输入包含空格符，则将换行符替换为空字符串
                        return source.toString().replace(" ", "")
                    }
                    return null
                }
            })
        }
    }
}
package org.lwr.edgarapp.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import org.lwr.edgarapp.common.TypefaceUtil


class TextViewBold : TextView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onFinishInflate() {
        super.onFinishInflate()
        typeface = TypefaceUtil.getBold(context)
    }
}
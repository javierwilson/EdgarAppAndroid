package org.lwr.edgarapp.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioButton
import android.widget.TextView
import org.lwr.edgarapp.common.TypefaceUtil


class RadiuButtonRegular : RadioButton {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onFinishInflate() {
        super.onFinishInflate()
        typeface = TypefaceUtil.getRegular(context)
    }
}
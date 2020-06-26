package com.basecamp.android.core.main.info.families.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.basecamp.android.R
import com.basecamp.android.core.common.extensions.convertDpToPixel

class ScrollPoints(context: Context, attributeSet: AttributeSet?=null): LinearLayout(context, attributeSet) {

    private val points = ArrayList<ImageView>()
    private val padding= context.convertDpToPixel(3f).toInt()

    init { LayoutInflater.from(context).inflate(R.layout.view_scroll_points, this, true) }

    fun setPoints(num: Int){
        points.clear()
        removeAllViews()
        if (num <= 1) return
        for (x in 0 until num){
            val point_tmp = ImageView(context)
            point_tmp.setImageResource(R.drawable.swipe_point_light)
            point_tmp.setPadding(padding,0,padding,0)
            points.add(point_tmp)
        addView(points[x])}
        setPosition(0)
    }

    fun setPosition(pos:Int){
        points.map { it.setImageResource(R.drawable.swipe_point_light) }
        points[pos].setImageResource(R.drawable.swipe_point_d)
    }
}
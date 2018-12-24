package test.test.test.kotlintest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View

/**
 * Created by CimZzz on 2018/12/24.<br>
 * Project Name : YIQIMMM<br>
 * Since : YIQIMMM_2.27<br>
 * Description:<br>
 *
 */
class ViewPageIndicator(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    lateinit var viewPage: ViewPager

    lateinit var bitmap: Bitmap

    var needInit: Boolean = false

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        if(needInit) {

        }
    }
}
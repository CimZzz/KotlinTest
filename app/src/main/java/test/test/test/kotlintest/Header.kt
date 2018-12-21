package test.test.test.kotlintest

import android.content.Context
import android.os.SystemClock
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.Scroller

/**
 * Created by CimZzz on 2018/12/20.<br>
 * Project Name : YIQIMMM<br>
 * Since : YIQIMMM_2.27<br>
 * Description:<br>
 *
 */
class Header : CoordinatorLayout {
    private var header: AppBarLayout? = null
    private var content: RecyclerView? = null
    private val touchSlop: Int
    private val scroller: Scroller

    var refreshListener: OnRefreshListener? = null

    private var isRefreshing = false
    private var isRefreshingIntercept = false
    private var refreshEndHeight = 0.0f
    private var curRefreshScrollDistance = 0
    private var isIntercept = false
    private var isAllowCheckIntercept = false
    private var downX : Float = 0.0f
    private var downY : Float = 0.0f

    companion object {
        const val VELOCITY_VALUE: Float = 100 / 100f
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val viewConfig = ViewConfiguration.get(context)
        touchSlop = viewConfig.scaledTouchSlop
        scroller = Scroller(context)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        header = findViewById(R.id.headerList)
        content = findViewById(R.id.contentList)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        scroller.abortAnimation()
        if(isIntercept && ev?.action != MotionEvent.ACTION_DOWN)
            return true

        if(isRefreshingIntercept && ev?.action != MotionEvent.ACTION_DOWN)
            return true

        when (ev?.action) {
            MotionEvent.ACTION_DOWN->{
                isIntercept = false
                isRefreshingIntercept = false
                isAllowCheckIntercept = true
                downX = ev.x
                downY = ev.y
            }
            MotionEvent.ACTION_MOVE->{
                if(isRefreshing && scrollY < 0) {
                    isIntercept = true
                    isRefreshingIntercept = true
                    curRefreshScrollDistance = scrollY
                    downX = ev.x
                    downY = ev.y
                    return true
                }
                else if(checkNeedScroll()) {
                    val distance = ev.y - downY
                    if(distance > touchSlop) {
                        if(isRefreshing) {
                            isIntercept = true
                            isRefreshingIntercept = true
                            curRefreshScrollDistance = scrollY
                            downX = ev.x
                            downY = ev.y
                        }
                        else {
                            isIntercept = true
                            isAllowCheckIntercept = false
                            downX = ev.x
                            downY = ev.y
                        }
                        return true
                    }
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when {
            isRefreshingIntercept -> {
                when (ev?.action) {
                    MotionEvent.ACTION_MOVE -> {
                        var distance = curRefreshScrollDistance + downY - ev.y
                        if(distance > 0)
                            distance = 0f

                        scrollTo(0, distance.toInt())
                    }
                    MotionEvent.ACTION_UP -> {
                        val cancelEvent = MotionEvent.obtain(ev.downTime, SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0f, 0f, 0)
                        super.onTouchEvent(cancelEvent)
                        cancelEvent.recycle()

                        var realDistance = -refreshEndHeight - scrollY
                        when {
                            realDistance > 0 -> {
                                scroller.startScroll(0, scrollY, 0, realDistance.toInt(), (realDistance * VELOCITY_VALUE).toInt())
                                ViewCompat.postInvalidateOnAnimation(this)
                            }

                            realDistance > -refreshEndHeight -> {
                                if(realDistance > -refreshEndHeight / 2) {
                                    scroller.startScroll(0, scrollY, 0, realDistance.toInt(), (-realDistance * VELOCITY_VALUE).toInt())
                                    ViewCompat.postInvalidateOnAnimation(this)
                                }
                                else {
                                    scroller.startScroll(0, scrollY, 0, -scrollY, (-scrollY * VELOCITY_VALUE).toInt())
                                    ViewCompat.postInvalidateOnAnimation(this)
                                }
                            }
                        }

                    }
                }
                return true
            }
            isIntercept -> {
                when (ev?.action) {
                    MotionEvent.ACTION_MOVE -> {
                        var distance = downY - ev.y
                        if(distance > 0)
                            distance = 0f

                        refreshEndHeight = refreshListener?.onRefreshHeader(-distance)?:0.0f

                        scrollTo(0, distance.toInt())
                    }

                    MotionEvent.ACTION_UP -> {
                        var distance = downY - ev.y
                        val realDistance : Float
                        if(refreshEndHeight != 0.0f) {
                            realDistance = -refreshEndHeight - distance
                            isRefreshing = true
                        } else realDistance = -distance

                        scroller.startScroll(0, distance.toInt(), 0, realDistance.toInt(), (Math.abs(realDistance) * VELOCITY_VALUE).toInt())
                        ViewCompat.postInvalidateOnAnimation(this)

                        val cancelEvent = MotionEvent.obtain(ev.downTime, SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0f, 0f, 0)
                        super.onTouchEvent(cancelEvent)
                        cancelEvent.recycle()
                    }

                }

                return true
            }
            isAllowCheckIntercept -> when (ev?.action) {
                MotionEvent.ACTION_DOWN-> {
                    if(isRefreshing && scrollY < 0) {
                        isRefreshingIntercept = true
                        curRefreshScrollDistance = scrollY
                        downX = ev.x
                        downY = ev.y
                        return true
                    }
                }
                MotionEvent.ACTION_MOVE->{
                    val distance = ev.y - downY
                    if(isRefreshing && scrollY < 0) {
                        isRefreshingIntercept = true
                        curRefreshScrollDistance = scrollY
                        downX = ev.x
                        downY = ev.y
                        return true
                    }
                    else if(checkNeedScroll()) {
                        if(distance > touchSlop) {
                            if(isRefreshing) {
                                curRefreshScrollDistance = scrollY
                                isRefreshingIntercept = true
                                downX = ev.x
                                downY = ev.y
                            }
                            else {
                                isIntercept = true
                                downX = ev.x
                                downY = ev.y
                            }
                        }
                    }
                    else if(distance > touchSlop)
                        isAllowCheckIntercept = false
                }
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun computeScroll() {
        if(!scroller.isFinished && scroller.computeScrollOffset()) {
            scrollTo(0, scroller.currY)
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun completed() {
        if(isRefreshing) {
            isRefreshing = false
            if(scrollY != 0) {
                scroller.abortAnimation()
                scroller.startScroll(0, scrollY, 0, -scrollY, ((-scrollY * VELOCITY_VALUE).toInt()))
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }
    }

    private fun checkNeedScroll(): Boolean {
        return checkHeaderTop() && checkContentTop()
    }

    private fun checkHeaderTop() : Boolean = header?.top == 0

    private fun checkContentTop() : Boolean {
        val childCount = content?.childCount ?:return false
        if(childCount == 0)
            return true

        val child = content?.getChildAt(0) ?:return false
        val childIndex = content?.getChildAdapterPosition(child)

        return childIndex == 0 && child.top - (child.layoutParams as MarginLayoutParams).topMargin == 0
    }

    interface OnRefreshListener {
        fun onRefreshHeader(distance: Float): Float
    }
}
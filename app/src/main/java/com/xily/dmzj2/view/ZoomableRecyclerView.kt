package com.xily.dmzj2.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.OverScroller
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Created by Xily on 2020/5/28.
 */
class ZoomableRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr),
    ScaleGestureDetector.OnScaleGestureListener, OnScaleGestureListener,
    OnFlingRunningListener {

    private val mMatrix = Matrix()
    private val MIN_SCALE = 1.0f
    private val MAX_SCALE = 3.0f
    private var currentFlingRunnable: FlingRunnable? = null
    var onSingleTapListener: (() -> Unit)? = null
    private var scaleGestureDetector = ScaleGestureDetector(context, this)
    private var gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            //滑动
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                onScroll(-distanceX, -distanceY)
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

            //松手时惯性滑动
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                onFling(-velocityX, -velocityY)
                return super.onFling(e1, e2, velocityX, velocityY)
            }

            //双击
            override fun onDoubleTap(e: MotionEvent): Boolean {
                onDoubleTap(e.x, e.y)
                return super.onDoubleTap(e)
            }

            //单击
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                if (onSingleTapListener != null) {
                    onSingleTapListener!!.invoke()
                    return true
                }
                return false
            }
        })


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> cancelFling()
        }
        gestureDetector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            super.onTouchEvent(event)
        }
        return true
    }


    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()
        canvas.concat(mMatrix)
        super.dispatchDraw(canvas)
        canvas.restore()
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
        //缩小之后还原效果
        if (calculateScale(mMatrix) < MIN_SCALE) {
            checkBounds()
            val rect = getDisplayRect(mMatrix)
            setScale(MIN_SCALE, rect.centerX(), rect.centerY())
        }
    }

    private fun onScroll(dx: Float, dy: Float) {
        mMatrix.postTranslate(dx, dy)
        checkBounds()
        invalidate()
    }

    private fun onFling(
        velocityX: Float,
        velocityY: Float
    ) {
        checkBounds()
        val rect = getDisplayRect(mMatrix)
        currentFlingRunnable = FlingRunnable(context, this, this).apply {
            fling(rect, width, height, velocityX.toInt(), velocityY.toInt())
        }
        post(currentFlingRunnable)
    }

    private fun cancelFling() {
        if (currentFlingRunnable != null) {
            currentFlingRunnable!!.cancelFling()
            currentFlingRunnable = null
        }
    }

    override fun onFlingRunning(dx: Int, dy: Int) {
        mMatrix.postTranslate(dx.toFloat(), dy.toFloat())
        invalidate()
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val scaleFactor = detector.scaleFactor
        val focusX = detector.focusX
        val focusY = detector.focusY
        onScale(scaleFactor, focusX, focusY)
        return true
    }

    override fun onScale(scaleFactor: Float, focusX: Float, focusY: Float) {
        if (calculateScale(mMatrix) < MAX_SCALE || scaleFactor < 1.0f) {
            mMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
            checkBounds()
            invalidate()
        }
    }

    private fun setScale(scale: Float, focusX: Float, focusY: Float) {
        if (scale in MIN_SCALE..MAX_SCALE) {
            post(AnimatedScaleRunnable(scale, focusX, focusY, this, mMatrix, this))
        }
    }

    //双击
    private fun onDoubleTap(x: Float, y: Float): Boolean {
        val scale: Float = calculateScale(mMatrix)
        //如果放大倍数小于两倍就放大到两倍,否则还原到原本大小
        val scaleFactor = if (scale < 2f) 2f else MIN_SCALE
        setScale(scaleFactor, x, y)
        return true
    }

    //显示区域修正
    private fun checkBounds() {
        val rect: RectF = getDisplayRect(mMatrix)//获取View实际区域(缩放后)
        val viewHeight = height
        val viewWidth = width
        val height = rect.height()
        val width = rect.width()
        var deltaX = 0.0f
        var deltaY = 0.0f
        if (height <= viewHeight) {
            //缩小时的居中效果
            deltaY = (viewHeight - height) / 2 - rect.top
        } else if (rect.top > 0) {
            //上方有空位
            deltaY = -rect.top
        } else if (rect.bottom < viewHeight) {
            //下方有空位
            deltaY = viewHeight - rect.bottom
        }
        if (width <= viewWidth) {
            //缩小时的居中效果
            deltaX = (viewWidth - width) / 2 - rect.left
        } else if (rect.left > 0) {
            //左方有空位
            deltaX = -rect.left
        } else if (rect.right < viewWidth) {
            //右方有空位
            deltaX = viewWidth - rect.right
        }
        mMatrix.postTranslate(deltaX, deltaY)
    }

    private val tempRectF = RectF()
    private val tempRect = Rect()
    private fun getDisplayRect(matrix: Matrix): RectF {
        getLocalVisibleRect(tempRect)
        tempRectF.set(tempRect)
        matrix.mapRect(tempRectF)
        return tempRectF
    }


}

class AnimatedScaleRunnable(
    private val scaleEnd: Float,
    private val focusX: Float,
    private val focusY: Float,
    private val view: View,
    private val matrix: Matrix,
    private val scaleGestureListener: OnScaleGestureListener
) : Runnable {
    private val zoomInterpolator = AccelerateDecelerateInterpolator()
    private val startTime = System.currentTimeMillis()
    private var scaleStart = calculateScale(matrix)
    override fun run() {
        val t = interpolate()
        val scale = scaleStart + t * (scaleEnd - scaleStart)
        val deltaScale = scale / calculateScale(matrix)
        scaleGestureListener.onScale(deltaScale, focusX, focusY)
        if (t < 1f) {
            view.postOnAnimation(this)
        }
    }

    private fun interpolate(): Float {
        var t: Float = 1f * (System.currentTimeMillis() - startTime) / 200
        t = 1f.coerceAtMost(t)
        t = zoomInterpolator.getInterpolation(t)
        return t
    }

}

interface OnScaleGestureListener {
    fun onScale(
        scaleFactor: Float,
        focusX: Float,
        focusY: Float
    )
}

class FlingRunnable(
    context: Context,
    private val flingRunningListener: OnFlingRunningListener,
    private val view: View
) : Runnable {
    private val scroller = OverScroller(context)
    private var currentX = 0
    private var currentY = 0
    fun fling(
        rect: RectF,
        viewWidth: Int,
        viewHeight: Int,
        velocityX: Int,
        velocityY: Int
    ) {
        val minX: Int
        val maxX: Int
        val minY: Int
        val maxY: Int
        //只有实际大小大于显示大小才需要滑动
        val startX = (-rect.left).roundToInt()
        if (viewWidth < rect.width()) {
            minX = 0
            maxX = (rect.width() - viewWidth).roundToInt()
        } else {
            maxX = startX
            minX = maxX
        }
        val startY = (-rect.top).roundToInt()
        if (viewHeight < rect.height()) {
            minY = 0
            maxY = (rect.height() - viewHeight).roundToInt()
        } else {
            maxY = startY
            minY = maxY
        }
        currentX = startX
        currentY = startY
        if (startX != maxX || startY != maxY) {
            scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0)
        }
    }

    override fun run() {
        if (scroller.isFinished) {
            return
        }
        if (scroller.computeScrollOffset()) {
            val newX: Int = scroller.currX
            val newY: Int = scroller.currY
            flingRunningListener.onFlingRunning(currentX - newX, currentY - newY)
            currentX = newX
            currentY = newY
            view.postOnAnimation(this)
        }
    }

    fun cancelFling() {
        scroller.abortAnimation()
    }

}

interface OnFlingRunningListener {
    fun onFlingRunning(dx: Int, dy: Int)
}

fun calculateScale(matrix: Matrix): Float {
    val values = FloatArray(9)
    matrix.getValues(values)
    return sqrt(values[Matrix.MSCALE_X].pow(2.0f) + values[Matrix.MSKEW_Y].pow(2.0f))
}
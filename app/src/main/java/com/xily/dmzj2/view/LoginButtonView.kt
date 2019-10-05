package com.xily.dmzj2.view

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.xily.dmzj2.R

//TODO bug待修,暂时搁置
class LoginButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var animatorSet = AnimatorSet()
    private lateinit var animateRectToCircle: ValueAnimator
    private lateinit var animateRectToAngle: ValueAnimator
    private lateinit var animateCircle: ValueAnimator
    private lateinit var paint: Paint
    private lateinit var textPaint: Paint
    private lateinit var progressPaint: Paint
    private var distance = 0
    private var defaultDistance = 0
    private var mWidth = 0
    private var mHeight = 0
    private var value = 0
    private var isLoading = false
    private var startDrawProgress = false
    private val textRect = RectF()
    private var circleAngle = 0
    private var textColor = 0
    private var textSize = 0f
    private var mBackground = 0
    private var text = ""

    init {
        getAttr(attrs)
        initPaint()
        initTextPaint()
        initProgressPaint()
    }

    private fun getAttr(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoginButtonView)
            textColor = typedArray.getColor(R.styleable.LoginButtonView_textColor, textColor)
            textSize = typedArray.getDimension(R.styleable.LoginButtonView_textSize, textSize)
            mBackground =
                typedArray.getColor(R.styleable.LoginButtonView_backgroundColor, mBackground)
            text = typedArray.getNonResourceString(R.styleable.LoginButtonView_text) ?: ""
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(
            distance.toFloat(),
            0f,
            (mWidth - distance).toFloat(),
            mHeight.toFloat(),
            circleAngle.toFloat(),
            circleAngle.toFloat(),
            paint
        )
        drawText(canvas)
        if (startDrawProgress) {
            drawProgress(canvas)
        }
    }

    private fun drawText(canvas: Canvas) {
        textRect.set(0f, 0f, mWidth.toFloat(), mHeight.toFloat())
        val fontMetrics = textPaint.fontMetricsInt
        val baseline =
            ((textRect.bottom + textRect.top - fontMetrics.bottom.toFloat() - fontMetrics.top.toFloat()) / 2).toInt()
        //文字绘制到整个布局的中心位置
        canvas.drawText(text, textRect.centerX(), baseline.toFloat(), textPaint)
    }

    private fun drawProgress(canvas: Canvas) {
        var start = value - 100
        var angle = (value - start) * 2
        canvas.drawArc(
            distance.toFloat() + 20,
            20f,
            (mWidth - distance).toFloat() - 20,
            mHeight.toFloat() - 20,
            start.toFloat(),
            angle.toFloat(),
            false,
            progressPaint
        )
    }

    fun startAnimate() {
        animatorSet.start()
    }

    fun stopAnimation() {
        isLoading = false
        animatorSet.cancel()
        circleAngle = 0
        distance = 0
        defaultDistance = (mWidth - mHeight) / 2
        textPaint.alpha = 255
        value = 0
        startDrawProgress = false
        invalidate()
    }

    private fun initAnimation() {
        initRectToAngleAnimation()
        initRectToCircleAnimation()
        initCircleAnimation()
        animatorSet.playSequentially(animateRectToAngle, animateRectToCircle, animateCircle)
    }

    private fun initRectToAngleAnimation() {
        animateRectToAngle = ValueAnimator.ofInt(0, mHeight / 2)
        animateRectToAngle.duration = 100
        animateRectToAngle.startDelay = 0
        animateRectToAngle.interpolator = LinearInterpolator()
        animateRectToAngle.addUpdateListener {
            circleAngle = it.animatedValue as Int
            invalidate()
        }
    }

    private fun initRectToCircleAnimation() {
        animateRectToCircle = ValueAnimator.ofInt(0, defaultDistance)
        animateRectToCircle.duration = 100
        animateRectToCircle.startDelay = 0
        animateRectToCircle.interpolator = LinearInterpolator()
        animateRectToCircle.addUpdateListener {
            distance = it.animatedValue as Int
            val alpha = 255 - distance * 255 / defaultDistance
            textPaint.alpha = alpha
            invalidate()
        }
    }

    private fun initCircleAnimation() {
        animateCircle = ValueAnimator.ofInt(0, 359)
        animateCircle.duration = 1000
        animateCircle.repeatCount = 1000
        animateCircle.startDelay = 0
        animateCircle.interpolator = LinearInterpolator()
        animateCircle.addUpdateListener {
            startDrawProgress = true
            value = it.animatedValue as Int
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        defaultDistance = (w - h) / 2
        initAnimation()
    }


    private fun initPaint() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = mBackground
        paint.style = Paint.Style.FILL
    }

    private fun initProgressPaint() {
        progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        progressPaint.color = textColor
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = 5f
        progressPaint.strokeCap = Paint.Cap.ROUND
    }

    private fun initTextPaint() {
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textSize = textSize
        textPaint.color = textColor
        textPaint.textAlign = Paint.Align.CENTER
    }
}
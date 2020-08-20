package com.example.testapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class CircleProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    private var thickness = 10f
    private var progressStartColor = Color.BLACK
    private var progressCenterColor = Color.BLACK
    private var progressEndColor = Color.BLACK
    private var progressBackgroundColor = Color.WHITE
    private var isRoundedEdge = false

    private var progressPaint = Paint()
    private var roundPaint = Paint()
    private var progressRect = RectF()

    private var viewWidth = 0
    private var viewHeight = 0
    private var radius = 0f
    private var startAngle = 270f

    private var progressMaxValue = 10f
    private var progressValue = 10f

    init {
        applyAttributeSet(attrs, defStyleAttr)
        initPaint()
    }

    fun setProgressMaxValue(value: Float) {
        progressMaxValue = value
    }

    fun setProgressValue(value: Float) {
        progressValue = value
    }

    fun updateProgressValue(value: Float) {
        progressValue = value
        invalidate()
    }

    private fun applyAttributeSet(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0)

        thickness = typedArray.getFloat(R.styleable.CircleProgressBar_progressThickness, 10f)
        progressStartColor = typedArray.getColor(R.styleable.CircleProgressBar_progressStartColor, Color.BLACK)
        progressCenterColor = typedArray.getColor(R.styleable.CircleProgressBar_progressCenterColor, Color.BLACK)
        progressEndColor = typedArray.getColor(R.styleable.CircleProgressBar_progressEndColor, Color.BLACK)
        progressBackgroundColor = typedArray.getColor(R.styleable.CircleProgressBar_progressBackgroundColor, Color.WHITE)
        isRoundedEdge = typedArray.getBoolean(R.styleable.CircleProgressBar_roundedEdge, false)

        progressMaxValue = typedArray.getFloat(R.styleable.CircleProgressBar_progressMaxValue, 10f)
        progressValue = typedArray.getFloat(R.styleable.CircleProgressBar_progressValue, 10f)

        typedArray.recycle()
    }

    private fun initViewSize(w: Int, h: Int) {
        viewWidth = w
        viewHeight = h
        radius = min(viewWidth / 2f, viewHeight / 2f)
    }

    private fun initPaint() {
        progressPaint.isAntiAlias = true
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = thickness

        roundPaint.isAntiAlias = true
        roundPaint.style = Paint.Style.FILL
    }

    private fun initShader() {
        progressPaint.shader = SweepGradient(radius, radius, intArrayOf(progressStartColor, progressCenterColor, progressEndColor), null).apply {
            setLocalMatrix(Matrix().apply { postRotate(270f, radius, radius) })
        }
        roundPaint.shader = SweepGradient(radius, radius, intArrayOf(progressStartColor, progressCenterColor, progressEndColor), null).apply {
            setLocalMatrix(Matrix().apply { postRotate(270f, radius, radius) })
        }
    }

    private fun initRect() {
        val half = thickness / 2
        val diameter = min(viewWidth.toFloat() - half, viewHeight.toFloat() - half)
        progressRect.set(half, half, diameter, diameter)
    }

    private fun getEndPointX(degree: Float): Double {
        return progressRect.centerX() + (cos(toRadians(degree)) * (radius - (thickness / 2)))
    }

    private fun getEndPointY(degree: Float): Double {
        return progressRect.centerY() + (sin(toRadians(degree)) * (radius - (thickness / 2)))
    }

    private fun toRadians(degree: Float): Double {
        return degree * (PI / 180)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val degree = (progressValue / progressMaxValue * 360f)
        canvas.apply {
            drawArc(progressRect, startAngle, degree, false, progressPaint)
            if(degree != 0f) {
                drawCircle(getEndPointX(degree + startAngle).toFloat(), getEndPointY(degree + startAngle).toFloat(), thickness / 2, roundPaint)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initViewSize(w, h)
        initShader()
        initRect()
    }
}
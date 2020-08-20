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

    private var startDegree = 270f
    private var thickness = 10f
    private var progressStartColor = Color.BLACK
    private var progressCenterColor = Color.BLACK
    private var progressEndColor = Color.BLACK
    private var progressBackgroundColor = Color.WHITE
    private var isRoundedEdge = false
    private var isRoundedEndPoint = false
    private var progressMaxValue = 10f
    private var progressValue = 10f

    private var progressBackgroundPaint = Paint()
    private var progressPaint = Paint()
    private var roundPaint = Paint()
    private var progressRect = RectF()

    private var gradientColors: IntArray

    private var viewWidth = 0
    private var viewHeight = 0
    private var radius = 0f

    init {
        applyAttributeSet(attrs, defStyleAttr)
        initPaint()
        gradientColors = intArrayOf(progressStartColor, progressCenterColor, progressEndColor)
    }

    fun setProgressStartDegree(degree: Float) {
        startDegree = degree + 270f
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

    fun setProgressColor(colors: IntArray) {
        gradientColors = colors
    }

    private fun applyAttributeSet(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0)

        startDegree = typedArray.getFloat(R.styleable.CircleProgressBar_progressStartDegree, 0f) + 270f
        thickness = typedArray.getFloat(R.styleable.CircleProgressBar_progressThickness, 10f)
        progressStartColor = typedArray.getColor(R.styleable.CircleProgressBar_progressStartColor, Color.BLACK)
        progressCenterColor = typedArray.getColor(R.styleable.CircleProgressBar_progressCenterColor, progressStartColor)
        progressEndColor = typedArray.getColor(R.styleable.CircleProgressBar_progressEndColor, progressStartColor)
        progressBackgroundColor = typedArray.getColor(R.styleable.CircleProgressBar_progressBackgroundColor, Color.WHITE)
        isRoundedEdge = typedArray.getBoolean(R.styleable.CircleProgressBar_roundedEdge, false)
        isRoundedEndPoint = typedArray.getBoolean(R.styleable.CircleProgressBar_roundedEndPoint, false)
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
        progressBackgroundPaint.isAntiAlias = true
        progressBackgroundPaint.style = Paint.Style.STROKE
        progressBackgroundPaint.strokeWidth = thickness
        progressBackgroundPaint.color = progressBackgroundColor

        progressPaint.isAntiAlias = true
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeWidth = thickness
        if(isRoundedEdge) {
            progressPaint.strokeCap = Paint.Cap.ROUND
        }

        roundPaint.isAntiAlias = true
        roundPaint.style = Paint.Style.FILL
    }

    private fun initShader() {
        progressPaint.shader = SweepGradient(radius, radius, gradientColors, null).apply {
            setLocalMatrix(Matrix().apply { postRotate(startDegree, radius, radius) })
        }
        roundPaint.shader = SweepGradient(radius, radius, gradientColors, null).apply {
            setLocalMatrix(Matrix().apply { postRotate(startDegree, radius, radius) })
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
            drawArc(progressRect, startDegree, 360f, false, progressBackgroundPaint)
            drawArc(progressRect, startDegree, degree, false, progressPaint)
            if(isRoundedEndPoint && degree != 0f) {
                drawCircle(getEndPointX(degree + startDegree).toFloat(), getEndPointY(degree + startDegree).toFloat(), thickness / 2, roundPaint)
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

package com.kenny.baselibrary.view.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.kenny.baselibrary.R;


/**
 * An indicator of progress, similar to Android's ProgressBar.
 * Can be used in 'spin mode' or 'increment mode'
 *
 * @author Todd Davies
 *         <p/>
 *         See MIT-LICENSE.txt for licence details
 */
public class ProgressWheel extends View {
    //绘制View用到的各种长、宽带大小
    private int layout_height = 0;
    private int layout_width = 0;
    private int fullRadius = 100;
    private int circleRadius = 80;
    private int barLength = 60;
    private int barWidth = 20;
    private int rimWidth = 20;
    private int textSize = 20;
    private float contourSize = 0;

    //与页边的间距
    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;

    //View要绘制的颜色
    private int barColor = 0xAA000000;
    private int contourColor = 0xAA000000;
    private int circleColor = 0x00000000;
    private int rimColor = 0xAADDDDDD;
    private int textColor = 0xFF000000;

    //绘制要用的画笔
    private Paint barPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint contourPaint = new Paint();

    //绘制要用的矩形
    @SuppressWarnings("unused")
    private RectF rectBounds = new RectF();
    private RectF circleBounds = new RectF();
    private RectF circleOuterContour = new RectF();
    private RectF circleInnerContour = new RectF();

    //动画
    //每次绘制要移动的像素数目
    private int spinSpeed = 2;
    //绘制过程的时间间隔
    private int delayMillis = 0;
    int progress = 0;
    boolean isSpinning = false;

    //其他
    private String text = "";
    private String[] splitText = {};

    /**
     * ProgressWheel的构造方法
     *
     * @param context
     * @param attrs
     */
    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);

        parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.ProgressWheel));
    }

    //----------------------------------
    //初始化一些元素
    //----------------------------------

    /*
     * 调用这个方法时，使View绘制为方形
     * From: http://www.jayway.com/2012/12/12/creating-custom-android-views-part-4-measuring-and-how-to-force-a-view-to-be-square/
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 首先我们要调用超类的onMeasure借口
        // 原因是我们自己去实现一个方法获得长度、宽度太麻烦了
        // 使用超类的的方法非常方便而且让复杂的细节可控
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 在这里我们不能使用getWidth()和getHeight()。
        // 因为这两个方法只能在View的布局完成后才能使用，而一个View的绘制过程是先绘制元素，再绘制Layout
        // 所以我们必须使用getMeasuredWidth()和getMeasuredHeight()
        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        // 最后我们用一些简单的逻辑去计算View的大小并调用setMeasuredDimension()去设置View的大小
        // 在比较View的长宽前我们不考虑间距，但当我们设置View所需要绘制的面积时，我们要考虑它
        // 不考虑间距的View（View内的实际画面）此时就应该是方形的，但是由于间距的存在，最终View所占的面积可能不是方形的
        if (widthWithoutPadding > heigthWithoutPadding) {
            size = heigthWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        // 如果你重写了onMeasure()方法，你必须调用setMeasuredDimension()方法
        // 这是你设置View大小的唯一途径
        // 如果你不调用setMeasuredDimension()方法，父控件会抛出异常，并且程序会崩溃
        // 如果我们使用了超类的onMeasure()方法，我们就不是那么需要setMeasuredDimension()方法
        // 然而，重写onMeasure()方法是为了改变既有的绘制流程，所以我们必须调用setMeasuredDimension()方法以达到我们的目的
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    /**
     * 使用onSizeChanged方法代替onAttachedToWindow获得View的面积
     * 因为这个方法会在测量了MATCH_PARENT和WRAP_CONTENT后马上被调用
     * 使用获得的面积设置View
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Share the dimensions
        layout_width = w;
        layout_height = h;

        setupBounds();
        setupPaints();
        invalidate();
    }

    /**
     * 设置我们想要绘制的progress wheel的颜色
     */
    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);

        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);

        textPaint.setColor(textColor);
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        contourPaint.setColor(contourColor);
        contourPaint.setAntiAlias(true);
        contourPaint.setStyle(Style.STROKE);
        contourPaint.setStrokeWidth(contourSize);
    }

    /**
     * 设置元素的边界
     */
    private void setupBounds() {
        // 为了保持宽度和长度的一致，我们要获得layout_width和layout_height中较小的一个，从而绘制一个圆
        int minValue = Math.min(layout_width, layout_height);

        // 计算在绘制过程中在x,y方向的偏移量
        int xOffset = layout_width - minValue;
        int yOffset = layout_height - minValue;

        // 间距加上偏移量
        paddingTop = this.getPaddingTop() + (yOffset / 2);
        paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        paddingRight = this.getPaddingRight() + (xOffset / 2);

        int width = getWidth(); //this.getLayoutParams().width;
        int height = getHeight(); //this.getLayoutParams().height;

        rectBounds = new RectF(paddingLeft,
                paddingTop,
                width - paddingRight,
                height - paddingBottom);

        circleBounds = new RectF(paddingLeft + barWidth,
                paddingTop + barWidth,
                width - paddingRight - barWidth,
                height - paddingBottom - barWidth);
        circleInnerContour = new RectF(circleBounds.left + (rimWidth / 2.0f) + (contourSize / 2.0f), circleBounds.top + (rimWidth / 2.0f) + (contourSize / 2.0f), circleBounds.right - (rimWidth / 2.0f) - (contourSize / 2.0f), circleBounds.bottom - (rimWidth / 2.0f) - (contourSize / 2.0f));
        circleOuterContour = new RectF(circleBounds.left - (rimWidth / 2.0f) - (contourSize / 2.0f), circleBounds.top - (rimWidth / 2.0f) - (contourSize / 2.0f), circleBounds.right + (rimWidth / 2.0f) + (contourSize / 2.0f), circleBounds.bottom + (rimWidth / 2.0f) + (contourSize / 2.0f));

        fullRadius = (width - paddingRight - barWidth) / 2;
        circleRadius = (fullRadius - barWidth) + 1;
    }

    /**
     * 从XML中解析控件的属性
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        barWidth = (int) a.getDimension(R.styleable.ProgressWheel_barWidth,
                barWidth);

        rimWidth = (int) a.getDimension(R.styleable.ProgressWheel_rimWidth,
                rimWidth);

        spinSpeed = (int) a.getDimension(R.styleable.ProgressWheel_spinSpeed,
                spinSpeed);

        delayMillis = a.getInteger(R.styleable.ProgressWheel_delayMillis,
                delayMillis);
        if (delayMillis < 0) {
            delayMillis = 0;
        }

        barColor = a.getColor(R.styleable.ProgressWheel_barColor, barColor);

        barLength = (int) a.getDimension(R.styleable.ProgressWheel_barLengths,
                barLength);

        textSize = (int) a.getDimension(R.styleable.ProgressWheel_textSize,
                textSize);

        textColor = (int) a.getColor(R.styleable.ProgressWheel_textColor,
                textColor);

        //如果text是空的，就无视它
        if (a.hasValue(R.styleable.ProgressWheel_text)) {
            setText(a.getString(R.styleable.ProgressWheel_text));
        }

        rimColor = (int) a.getColor(R.styleable.ProgressWheel_rimColor,
                rimColor);

        circleColor = (int) a.getColor(R.styleable.ProgressWheel_circleColor,
                circleColor);

        contourColor = a.getColor(R.styleable.ProgressWheel_contourColor, contourColor);
        contourSize = a.getDimension(R.styleable.ProgressWheel_contourSize, contourSize);


        // 使用TypedArray获得控件属性时必须要注意：使用结束后必须回收TypedArray的对象
        a.recycle();
    }

    //----------------------------------
    //动画
    //----------------------------------

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制内圆
        canvas.drawArc(circleBounds, 360, 360, false, circlePaint);
        //绘制边界
        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);
        canvas.drawArc(circleOuterContour, 360, 360, false, contourPaint);
        canvas.drawArc(circleInnerContour, 360, 360, false, contourPaint);
        //绘制条纹
        if (isSpinning) {
            canvas.drawArc(circleBounds, progress - 90, barLength, false,
                    barPaint);
        } else {
            canvas.drawArc(circleBounds, -90, progress, false, barPaint);
        }
        //绘制我们想要设置的文字 (并让它显示在圆水平和垂直方向的中心处)
        float textHeight = textPaint.descent() - textPaint.ascent();
        float verticalTextOffset = (textHeight / 2) - textPaint.descent();

        for (String s : splitText) {
            float horizontalTextOffset = textPaint.measureText(s) / 2;
            canvas.drawText(s, this.getWidth() / 2 - horizontalTextOffset,
                    this.getHeight() / 2 + verticalTextOffset, textPaint);
        }
        if (isSpinning) {
            scheduleRedraw();
        }
    }

    private void scheduleRedraw() {
        progress += spinSpeed;
        if (progress > 360) {
            progress = 0;
        }
        postInvalidateDelayed(delayMillis);
    }

    /**
     *   判断wheel是否在旋转
     */

    public boolean isSpinning() {
        if(isSpinning){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 重设进度条的值
     */
    public void resetCount() {
        progress = 0;
        setText("0%");
        invalidate();
    }

    /**
     * 停止进度条的旋转
     */
    public void stopSpinning() {
        isSpinning = false;
        progress = 0;
        postInvalidate();
    }


    /**
     * 让进度条开启旋转模式
     */
    public void spin() {
        isSpinning = true;
        postInvalidate();
    }

    /**
     * 让进度条每次增加1（最大值为360）
     */
    public void incrementProgress() {
        isSpinning = false;
        progress++;
        if (progress > 360)
            progress = 0;
        setText(Math.round(((float) progress / 360) * 100) + "%");
        postInvalidate();
    }


    /**
     * 设置进度条为一个确切的数值
     */
    public void setProgress(int i) {
        isSpinning = false;
        progress = i;
        setText(Math.round(((float) progress / 360) * 100) + "%");
        postInvalidate();
    }

    //----------------------------------
    //get和set方法
    //----------------------------------

    /**
     * 设置progress bar的文字并不需要刷新View
     *
     * @param text the text to show ('\n' constitutes a new line)
     */
    public void setText(String text) {
        this.text = text;
        splitText = this.text.split("\n");
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getBarLength() {
        return barLength;
    }

    public void setBarLength(int barLength) {
        this.barLength = barLength;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;

        if ( this.barPaint != null ) {
            this.barPaint.setStrokeWidth( this.barWidth );
        }
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;

        if ( this.textPaint != null ) {
            this.textPaint.setTextSize( this.textSize );
        }
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;

        if ( this.barPaint != null ) {
            this.barPaint.setColor( this.barColor );
        }
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;

        if ( this.circlePaint != null ) {
            this.circlePaint.setColor( this.circleColor);
        }
    }

    public int getRimColor() {
        return rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;

        if ( this.rimPaint != null ) {
            this.rimPaint.setColor( this.rimColor );
        }
    }


    public Shader getRimShader() {
        return rimPaint.getShader();
    }

    public void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;

        if ( this.textPaint != null ) {
            this.textPaint.setColor( this.textColor );
        }
    }

    public int getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinSpeed(int spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public int getRimWidth() {
        return rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;

        if ( this.rimPaint != null ) {
            this.rimPaint.setStrokeWidth( this.rimWidth );
        }
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public int getContourColor() {
        return contourColor;
    }

    public void setContourColor(int contourColor) {
        this.contourColor = contourColor;

        if ( contourPaint != null ) {
            this.contourPaint.setColor( this.contourColor );
        }
    }

    public float getContourSize() {
        return this.contourSize;
    }

    public void setContourSize(float contourSize) {
        this.contourSize = contourSize;

        if ( contourPaint != null ) {
            this.contourPaint.setStrokeWidth( this.contourSize );
        }
    }
}
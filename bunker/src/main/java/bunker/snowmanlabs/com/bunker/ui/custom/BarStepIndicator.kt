package bunker.snowmanlabs.com.bunker.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.widget.LinearLayout
import bunker.snowmanlabs.com.bunker.R
import bunker.snowmanlabs.com.bunker.ui.custom.DimensionUtils.Companion.convertDpToPixel
import kotlin.math.max

class BarStepIndicator: LinearLayout {

    var maxSteps = 0

    constructor(context: Context?) : super(context){
        initView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        val ta = context!!.obtainStyledAttributes(attrs, R.styleable.BarStepIndicator, 0, 0)
        try {
            maxSteps = ta.getInteger(R.styleable.BarStepIndicator_maxSteps, 4) - 1
        } finally {
            ta.recycle()
        }

        initView()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initView()
    }

    private fun initView() {
        for (i in 0..maxSteps){
            val linear = LinearLayout(context)
            linear.background = context.getDrawable(R.drawable.ic_step_amarelo)
            val params = LayoutParams(convertDpToPixel(15), convertDpToPixel(2, context))
            params.setMargins(0,convertDpToPixel(5),convertDpToPixel(5),convertDpToPixel(5))
            linear.layoutParams = params

            addView(linear)
        }
    }

    fun setStep(step: Int){
        for (i in 0..maxSteps){
            val linear = getChildAt(i)

            if (i < step){
                linear.background = context.getDrawable(R.drawable.ic_step_completed)
            }else if (i > step){
                linear.background = context.getDrawable(R.drawable.ic_next_step)
            }else{
                linear.background = context.getDrawable(R.drawable.ic_step_amarelo)
            }

            val params = LayoutParams(convertDpToPixel(20), convertDpToPixel(3))
            params.setMargins(0,convertDpToPixel(5),convertDpToPixel(5),convertDpToPixel(5))
            linear.layoutParams = params

            updateViewLayout(linear, params)
        }
    }

    private fun convertDpToPixel(px: Int): Int {
        return DimensionUtils.convertDpToPixel(px, context)
    }
}
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.just.news.R


class CustomMarkerView(context: Context, layoutResource: Int) :
    MarkerView(context, layoutResource) {
    private val tvContent: TextView? = null

    init {
        val tvContent = findViewById<TextView>(R.id.tvLine)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e != null) {
            tvContent?.text = "X: ${e.x}, Y: ${e.y}"
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-width / 2).toFloat(), (-height).toFloat())
    }
}



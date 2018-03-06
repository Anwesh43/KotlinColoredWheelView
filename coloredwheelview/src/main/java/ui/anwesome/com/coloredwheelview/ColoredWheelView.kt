package ui.anwesome.com.coloredwheelview

/**
 * Created by anweshmishra on 07/03/18.
 */
import android.view.*
import android.content.*
import android.graphics.*
val colors : Array<String> = arrayOf("#f44336", "#4CAF50", "#3949AB", "#7B1FA2", "#880E4F", "#64DD17")
class ColoredWheelView(ctx : Context, var n : Int = 6) : View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas : Canvas) {

    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}
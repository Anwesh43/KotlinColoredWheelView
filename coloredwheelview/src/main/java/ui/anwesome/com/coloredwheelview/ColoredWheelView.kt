package ui.anwesome.com.coloredwheelview

/**
 * Created by anweshmishra on 07/03/18.
 */
import android.view.*
import android.content.*
import android.graphics.*
val colors : Array<String> = arrayOf("#f44336", "#4CAF50", "#3949AB", "#7B1FA2", "#880E4F", "#64DD17")
class ColoredWheelView(ctx : Context,var n : Int = 6) : View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer : Renderer = Renderer(this)
    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class State(var prevScale : Float = 0f, var dir : Float = 0f, var jDir : Int = 1, var j : Int = 0) {
        val scales : Array<Float> = arrayOf(0f, 0f)
        fun update(stopcb : (Float) -> Unit) {
            scales[j] += jDir * 0.1f
            if(Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += jDir
                if(j == scales.size || j == -1) {
                    jDir *= -1
                    j += jDir
                    prevScale = scales[j]
                    dir = 0f
                    stopcb(prevScale)
                }
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if(dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }
    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex : Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class ColoredWheel(var i : Int, var n: Int, var state : State = State()) {
        fun draw(canvas : Canvas, paint : Paint) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            val r = Math.min(w, h)/10
            val x = w/2
            if(n > 0) {
                for (i in 0..1) {
                    val mx = r + i * (w - 2 * r)
                    canvas.save()
                    canvas.translate(x + (mx - x) * state.scales[1], h / 2)
                    canvas.rotate(360f * state.scales[0])
                    val deg = 360f / n
                    for (j in 0..n - 1) {
                        canvas.drawAngleArc(0f, 0f, r, deg * j, deg * state.scales[0], Color.parseColor(colors[j%colors.size]), paint)
                    }
                    canvas.restore()
                }
            }
        }
        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Renderer(var view : ColoredWheelView, var time : Int = 0) {
        val wheel : ColoredWheel = ColoredWheel(0, view.n)
        val animator : Animator = Animator(view)
        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            wheel.draw(canvas, paint)
            animator.animate {
                wheel.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            wheel.startUpdating {
                animator.start()
            }
        }
    }
}
fun Canvas.drawAngleArc(x : Float, y : Float, r : Float, start : Float, sweep : Float,  color : Int, paint : Paint) {
    paint.color = color
    val path : Path = Path()
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = r/8
    paint.strokeCap = Paint.Cap.ROUND
    for(i in start.toInt()..(start+sweep).toInt()) {
        val rx = x + r * Math.cos(i * Math.PI/180).toFloat()
        val ry = y + r * Math.sin(i * Math.PI/180).toFloat()
        if(i == start.toInt()) {
            path.moveTo(rx, ry)
        }
        else {
            path.lineTo(rx, ry)
        }
    }
    drawPath(path, paint)
}
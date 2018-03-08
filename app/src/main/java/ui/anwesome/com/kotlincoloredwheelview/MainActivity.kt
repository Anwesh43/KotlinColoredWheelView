package ui.anwesome.com.kotlincoloredwheelview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.coloredwheelview.ColoredWheelView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ColoredWheelView.create(this)
    }
}

package com.medhdj.paonsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.medhdj.paon.containers.DefaultPaonContainer
import com.medhdj.paon.core.PaonContainer
import com.medhdj.paonsample.di.DIHelper
import com.medhdj.paonsample.utils.DummyClass1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dummy1 = DIHelper.ponContainer.inject(DummyClass1::class)

        findViewById<TextView>(R.id.textView).text = dummy1.getText()
    }
}
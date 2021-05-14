package com.medhdj.paonsample

import android.app.Application
import com.medhdj.paonsample.di.DIHelper

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DIHelper.init()
    }
}
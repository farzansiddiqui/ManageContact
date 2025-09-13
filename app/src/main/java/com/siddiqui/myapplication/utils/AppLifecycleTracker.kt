package com.siddiqui.myapplication.utils

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.util.concurrent.atomic.AtomicInteger


class AppLifecycleTracker : ActivityLifecycleCallbacks {
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {}

    override fun onActivityDestroyed(p0: Activity) {}

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityStarted(p0: Activity) {
        if (activityCount.incrementAndGet() == 1) {
            isAppInForeground = true
            // App is now in the foreground
        }
    }

    override fun onActivityStopped(p0: Activity) {
        if (activityCount.decrementAndGet() == 0) {
            isAppInForeground = false
            // App is now in the background
        }
    }

    companion object {
        private val activityCount = AtomicInteger(0)
        var isAppInForeground: Boolean = false
            private set
    }
}
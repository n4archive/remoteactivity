package org.nift4.remoteactivity

import android.app.Activity
import android.app.RemoteActivityProvider
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dalvik.system.PathClassLoader


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).delegate = { a: Activity ->
            val dxl = PathClassLoader(
                "/sdcard/Android/data/org.nift4.remoteactivity/files/classes.dex",
                this.classLoader
            )
            val c = dxl.loadClass("org.nift4.impl.TargetImpl")
            for (l in c.constructors) {
                Log.e("", l.parameterTypes.contentDeepToString())
            }
            c.getConstructor(Activity::class.java).newInstance(a) as RemoteActivityProvider
        }
        startActivity(Intent(this, (application as App).remoteActivity))
        finish()
    }
}
package org.nift4.remoteactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Callable


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).delegate = { a: Activity -> TargetImpl(a, "hi android") }
        startActivity(Intent(this, (application as App).remoteActivity))
        finish()
    }
}


class TargetImpl(thiz: Activity, val s: String): TargetIface(thiz) {
    override fun onCreate(callSuper: Callable<*>, savedInstanceState: Bundle?) {
        callSuper.call()
        val v = TextView(thiz)
        v.text = s
        thiz.setContentView(v)
    }
}
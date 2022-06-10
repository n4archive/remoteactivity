package org.nift4.remoteactivity

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.bytebuddy.ByteBuddy
import net.bytebuddy.android.AndroidClassLoadingStrategy
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.implementation.bind.annotation.*
import net.bytebuddy.matcher.ElementMatchers
import java.lang.RuntimeException
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.concurrent.Callable

class App : Application() {
    lateinit var remoteActivity: Class<out Activity>
    lateinit var superClass: Class<out Activity>
    var delegate: ((Activity) -> TargetIface)? = null
    override fun onCreate() {
        super.onCreate()
        val strategy = AndroidClassLoadingStrategy.Injecting(getDir("generated", Context.MODE_PRIVATE))

        superClass = AppCompatActivity::class.java
        val dynamicType: DynamicType.Unloaded<out Activity> =
            ByteBuddy()
                .subclass(superClass)
                .name("org.nift4.remoteactivity.RemoteActivity")
                .defineField("remoteActivityDelegate", TargetIface::class.java, Modifier.PUBLIC)
                .method(ElementMatchers.not(ElementMatchers.isDeclaredBy<MethodDescription?>(Object::class.java).or(ElementMatchers.isAbstract())))
                .intercept(MethodDelegation.to(TargetActivity::class.java))
                .make()

        remoteActivity = dynamicType.load(
            classLoader, strategy
        ).loaded
    }
}

object TargetActivity {
    @RuntimeType
    @JvmStatic
    fun intercept(@AllArguments args: Array<Any?>, @SuperCall m: Callable<*>, @This thiz: Activity, @Origin method: Method): Any? {
        val app = thiz.application as App?
        val f = thiz.javaClass.getField("remoteActivityDelegate")
        if (app != null) {
            var delegate = f.get(thiz) as TargetIface?
            if (delegate == null) {
                if (app.delegate == null) {
                    throw RuntimeException("app.delegate == null")
                }
                delegate = app.delegate!!(thiz)
                f.set(thiz, delegate)
                app.delegate = null
            }
            return doIntercept(delegate, method, m, args)
        }

        return m.call()
    }

    @JvmStatic
    fun doIntercept(delegate: TargetIface, method: Method, superCall: Callable<*>, args: Array<Any?>): Any? {
        val methodName = method.name
        return if (methodName == "onCreate") {
            delegate.onCreate(superCall, args[0] as Bundle?)
        } else {
            superCall.call()
        }
    }
}

abstract class TargetIface(val thiz: Activity) {
    abstract fun onCreate(callSuper: Callable<*>, savedInstanceState: Bundle?)
}
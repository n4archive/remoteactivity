package org.nift4.remoteactivity

import android.app.*
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import net.bytebuddy.ByteBuddy
import net.bytebuddy.android.AndroidClassLoadingStrategy
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import java.lang.reflect.Modifier

class App : RemoteActivityApplication() {
    override fun onCreate() {
        super.onCreate()
        val strategy = AndroidClassLoadingStrategy.Injecting(getDir("generated", Context.MODE_PRIVATE))

        superClass = AppCompatActivity::class.java
        val dynamicType: DynamicType.Unloaded<out Activity> =
            ByteBuddy()
                .subclass(superClass)
                .name("android.app.RemoteActivity")
                .defineField("remoteActivityDelegate", RemoteActivityProvider::class.java, Modifier.PUBLIC)
                .method(ElementMatchers.not(ElementMatchers.isDeclaredBy<MethodDescription?>(Object::class.java).or(ElementMatchers.isAbstract())))
                .intercept(MethodDelegation.to(RemoteActivityInjector::class.java))
                .make()

        remoteActivity = dynamicType.load(
            classLoader, strategy
        ).loaded
    }
}
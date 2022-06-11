/*
 * (C) 2022 nift4
 *
 * RemoteActivity 1.0
 *
 * File not included in client but in implementation component for server
 */

package android.app;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class RemoteActivityInjector {
    @RuntimeType
    public static Object intercept(@AllArguments Object[] args, @SuperCall Callable<?> m, @This Activity thiz, @Origin Method method) {
        try {
            RemoteActivityApplication app = (RemoteActivityApplication) thiz.getApplication();
            Field f = thiz.getClass().getField("remoteActivityDelegate");
            if (app != null) {
                RemoteActivityProvider delegate = (RemoteActivityProvider) f.get(thiz);
                if (delegate == null) {
                    if (app.delegate == null) {
                        throw new RuntimeException("app.delegate == null");
                    }
                    delegate = app.delegate.invoke(thiz);
                    f.set(thiz, delegate);
                    app.delegate = null;
                }
                return delegate.doIntercept(method, m, args);
            }

            return m.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

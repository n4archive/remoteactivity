/*
 * (C) 2022 nift4
 *
 * RemoteActivity 1.0
 *
 * File included in compileOnly component for client and implementation component for server
 */

package android.app;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public abstract class RemoteActivityProvider {
    protected final Activity thiz;

    public RemoteActivityProvider(Activity thiz) {
        this.thiz = thiz;
    }

    public abstract Object doIntercept(Method method, Callable<?> superCall, Object[] args) throws Exception;
}

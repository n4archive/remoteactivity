/*
 * (C) 2022 nift4
 *
 * RemoteActivity (demo code) 1.0
 *
 * File is included in implementation component for client but not in server
 */

package org.nift4.impl;

import android.app.Activity;
import android.app.RemoteActivityProvider;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class TargetImpl extends RemoteActivityProvider {
    public final String s;

    public TargetImpl(Activity thiz) {
        super(thiz);
        this.s = "hi android.exe";
    }

    @Override
    public Object doIntercept(Method method, Callable<?> superCall, Object[] args) throws Exception {
        if (method.getName().equals("onCreate")) {
            superCall.call();
            TextView v = new TextView(thiz);
            v.setText(s);
            thiz.setContentView(v);
            return null; //void
        }
        return superCall.call();
    }
}
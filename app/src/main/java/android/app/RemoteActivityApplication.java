/*
 * (C) 2022 nift4
 *
 * RemoteActivity 1.0
 *
 * File not included in client but in implementation component for server
 */

package android.app;

public class RemoteActivityApplication extends Application {
    public Class<? extends Activity> remoteActivity;
    public Class<? extends Activity> superClass;
    public RemoteActivityProviderFactory delegate;
}

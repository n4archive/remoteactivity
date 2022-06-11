#!/bin/zsh
mkdir out1 out2
/usr/lib/jvm/java-8-openjdk-amd64/bin/javac -cp ~/Android/Sdk/platforms/android-32/android.jar -d out1 app/src/main/java/android/app/RemoteActivityProvider.java
/usr/lib/jvm/java-8-openjdk-amd64/bin/javac -cp ~/Android/Sdk/platforms/android-32/android.jar:out1 -d out2 demoClient/src/main/java/org/nift4/impl/TargetImpl.java
~/Android/Sdk/build-tools/31.0.0/d8 out2/**/*.class --classpath out1
rm -rf out1 out2
echo Built classes.dex

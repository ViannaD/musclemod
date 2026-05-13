#!/bin/sh
#
# Gradle wrapper script for Unix systems
#

APP_HOME=$(dirname "$0")
APP_HOME=$(cd "$APP_HOME" && pwd)

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Detect Java
if [ -n "$JAVA_HOME" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

exec "$JAVACMD" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"

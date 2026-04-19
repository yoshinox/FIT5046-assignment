#!/bin/sh

#
# Gradle startup script for POSIX generated for the assignment skeleton.
#

APP_HOME=$(cd "${0%/*}" && printf '%s\n' "$PWD")
APP_BASE_NAME=${0##*/}

DEFAULT_JVM_OPTS='-Xmx64m -Xms64m'

warn() {
    printf '%s\n' "$*" >&2
}

die() {
    warn "$*"
    exit 1
}

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

if [ -n "$JAVA_HOME" ] ; then
    JAVACMD=$JAVA_HOME/bin/java
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
    fi
else
    JAVACMD=java
    command -v java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \
    -Dorg.gradle.appname="$APP_BASE_NAME" \
    -classpath "$CLASSPATH" \
    org.gradle.wrapper.GradleWrapperMain "$@"


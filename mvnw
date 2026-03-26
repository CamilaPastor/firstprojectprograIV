#!/bin/sh
# Maven Wrapper script for Unix/Linux/Mac
# Downloads Maven automatically if not present
set -e

if [ -z "$JAVA_HOME" ] ; then
    JAVACMD="java"
else
    JAVACMD="$JAVA_HOME/bin/java"
fi

WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
WRAPPER_JAR=".mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES=".mvn/wrapper/maven-wrapper.properties"

# Determine the project base directory
BASEDIR=$(cd "$(dirname "$0")"; pwd)
cd "$BASEDIR"

# If the wrapper jar doesn't exist, download Maven directly
if [ ! -f "$WRAPPER_JAR" ]; then
    MAVEN_VERSION="3.9.9"
    MAVEN_HOME="$HOME/.m2/wrapper/dists/apache-maven-$MAVEN_VERSION"

    if [ ! -d "$MAVEN_HOME" ]; then
        echo "Downloading Maven $MAVEN_VERSION..."
        mkdir -p "$MAVEN_HOME"
        DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.tar.gz"

        if command -v curl > /dev/null; then
            curl -fsSL "$DOWNLOAD_URL" | tar xz -C "$MAVEN_HOME" --strip-components=1
        elif command -v wget > /dev/null; then
            wget -qO- "$DOWNLOAD_URL" | tar xz -C "$MAVEN_HOME" --strip-components=1
        else
            echo "Error: Neither curl nor wget found. Please install Maven manually."
            exit 1
        fi
    fi

    exec "$MAVEN_HOME/bin/mvn" "$@"
else
    exec "$JAVACMD" -jar "$WRAPPER_JAR" "$@"
fi

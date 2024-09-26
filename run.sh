#!/bin/sh

# Bail on error
set -ex

# Constants
TOMCAT_NAME='tomcat@10'

# This only works on MacOS with tomcat 10 installed via brew
if [ `uname -s` != 'Darwin' ]; then
    echo "Error: this script requires Mac OS" 1>&2
    exit 1
fi
BREW_HOME=`brew --prefix`
if ! [ -d "${BREW_HOME}" ]; then
    echo "Error: hombrew not found" 1>&2
    exit 1
fi
TOMCAT_ROOT=`find -s "${BREW_HOME}"/opt -maxdepth 1 -name "${TOMCAT_NAME}" -print -quit`
if [ -z "${TOMCAT_ROOT}" ]; then
    echo "Error: can't find ${TOMCAT_NAME} under "${BREW_HOME}"/opt" 1>&2
    exit 1
fi

# Build stuff
mvn clean package

# Identify (most recently built) WAR file
WAR=`find target -maxdepth 1 -name 'example-webapp-*.war' | xargs /bin/ls -1t | head -n 1`

# Stick it there
ln -f "${WAR}" "${TOMCAT_ROOT}/libexec/webapps/example.war"

# Show logs
tail -F /tmp/vaadin-flow-issue-16775.log

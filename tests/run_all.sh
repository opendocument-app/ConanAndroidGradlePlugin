#!/bin/sh
set -eu

THIS_FILE=$(readlink -f "$0")
BASEDIR=$(dirname "$THIS_FILE")

cd $BASEDIR/sample_library
./gradlew assemble

cd $BASEDIR/cmake_layout
./gradlew assemble

cd $BASEDIR/no_default_profile_installed
./gradlew assemble

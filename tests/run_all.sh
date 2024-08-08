#!/bin/sh
set -eu

THIS_FILE=$(readlink -f "$0")
BASEDIR=$(dirname "$THIS_FILE")

cd $BASEDIR/sample_library
./gradlew assemble

cd $BASEDIR/cmake_layout
./gradlew assemble
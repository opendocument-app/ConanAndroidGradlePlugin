#!/bin/sh
set -eu

THIS_FILE=$(readlink -f "$0")
BASEDIR=$(dirname "$THIS_FILE")

cd $BASEDIR/0_sample_library
./gradlew assemble

cd $BASEDIR/1_cmake_layout
./gradlew assemble
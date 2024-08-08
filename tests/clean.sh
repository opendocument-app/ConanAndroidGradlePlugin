#!/bin/sh

THIS_FILE=$(readlink -f "$0")
BASEDIR=$(dirname "$THIS_FILE")

rm -r $BASEDIR/0_sample_library/.gradle
rm -r $BASEDIR/0_sample_library/lib/build
rm -r $BASEDIR/0_sample_library/lib/.cxx
rm -r $BASEDIR/0_sample_library/lib/CMakeUserPresets.json

rm -r $BASEDIR/1_cmake_layout/.gradle
rm -r $BASEDIR/1_cmake_layout/lib/build
rm -r $BASEDIR/1_cmake_layout/lib/.cxx
rm -r $BASEDIR/1_cmake_layout/lib/CMakeUserPresets.json

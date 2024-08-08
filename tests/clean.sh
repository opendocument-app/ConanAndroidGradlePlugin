#!/bin/sh

THIS_FILE=$(readlink -f "$0")
BASEDIR=$(dirname "$THIS_FILE")

rm -r $BASEDIR/sample_library/.gradle
rm -r $BASEDIR/sample_library/lib/build
rm -r $BASEDIR/sample_library/lib/.cxx
rm -r $BASEDIR/sample_library/lib/CMakeUserPresets.json

rm -r $BASEDIR/cmake_layout/.gradle
rm -r $BASEDIR/cmake_layout/lib/build
rm -r $BASEDIR/cmake_layout/lib/.cxx
rm -r $BASEDIR/cmake_layout/lib/CMakeUserPresets.json

rm -r $BASEDIR/no_default_profile_installed/.gradle
rm -r $BASEDIR/no_default_profile_installed/lib/build
rm -r $BASEDIR/no_default_profile_installed/lib/.cxx
rm -r $BASEDIR/no_default_profile_installed/lib/CMakeUserPresets.json

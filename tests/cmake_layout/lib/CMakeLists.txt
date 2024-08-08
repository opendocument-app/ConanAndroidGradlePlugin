cmake_minimum_required(VERSION 3.6)
set(CMAKE_BUILD_TYPE Release CACHE STRING "Build configuration (Debug, Release, RelWithDebInfo, MinSizeRel)")

project(samplelib)

find_package(ZLIB REQUIRED)

add_library(zlibuser SHARED zlibuser.cpp)
target_link_libraries(zlibuser ZLIB::ZLIB)

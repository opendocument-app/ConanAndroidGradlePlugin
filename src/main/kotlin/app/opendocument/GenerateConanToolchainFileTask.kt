/*
 * GenerateConanToolchainFileTask.kt
 *
 * ConanAndroidGradlePlugin (https://github.com/opendocument-app/ConanAndroidGradlePlugin)
 *
 * Copyright (c) 2024 ViliusSutkus89.com OpenDocument.app
 *
 * ConanAndroidGradlePlugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation.
 *
 * ConanAndroidGradlePlugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.opendocument

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction


abstract class GenerateConanToolchainFileTask : DefaultTask() {
    @get:OutputFile
    abstract val conanToolchainFile: RegularFileProperty
    init {
        conanToolchainFile.convention(project.layout.buildDirectory.get().dir("conan").file("android_toolchain.cmake"))
    }

    @TaskAction
    fun writeToolchain() {
        val ANDROID_ABI = "\${ANDROID_ABI}"
        val CMAKE_CURRENT_LIST_DIR = "\${CMAKE_CURRENT_LIST_DIR}"
        conanToolchainFile.get().asFile.writeText("""
# During multiple stages of CMake configuration, the toolchain file is processed and command-line
# variables may not be always available. The script exits prematurely if essential variables are absent.

if ( NOT ANDROID_ABI OR NOT CMAKE_BUILD_TYPE )
  return()
endif()
if(${ANDROID_ABI} STREQUAL "x86_64")
  include("${CMAKE_CURRENT_LIST_DIR}/x86_64/conan_toolchain.cmake")
elseif(${ANDROID_ABI} STREQUAL "x86")
  include("${CMAKE_CURRENT_LIST_DIR}/x86/conan_toolchain.cmake")
elseif(${ANDROID_ABI} STREQUAL "arm64-v8a")
  include("${CMAKE_CURRENT_LIST_DIR}/armv8/conan_toolchain.cmake")
elseif(${ANDROID_ABI} STREQUAL "armeabi-v7a")
  include("${CMAKE_CURRENT_LIST_DIR}/armv7/conan_toolchain.cmake")
else()
  message(FATAL "Not supported configuration")
endif()
        """.trimIndent())
    }
}

/*
 * ConanInstallTask.kt
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

import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile


abstract class ConanInstallTask : Exec() {
    @get:Input
    abstract val arch: Property<String>

    @get:Input
    abstract val profile: Property<String>

    @get:Input
    abstract val conanfile: Property<String>

    init {
        profile.convention("default")
        conanfile.convention("conanfile.txt")
    }

    @get:OutputDirectory
    val outputDirectory: Provider<Directory> = arch.map { project.layout.buildDirectory.get().dir("conan/$it") }

    @get:OutputFile
    val conanToolchainFile: Provider<RegularFile> = arch.map { project.layout.buildDirectory.get().file("conan/$it/conan_toolchain.cmake") }

    override fun exec() {
        commandLine(
            "conan",
            "install", conanfile.get(),
            "--output-folder=" + outputDirectory.get(),
            "--build=missing",
            "--profile:host=" + profile.get(),
            "--settings:host", "arch=" + arch.get(),
        )
        super.exec()

        // conan install creates toolchain in one of two places:
        // 1: conan/armv8/conan_toolchain.cmake
        // 2: conan/armv8/build/${CMAKE_BUILD_TYPE}/generators/conan_toolchain.cmake
        conanToolchainFile.get().asFile.let { toolchainFile ->
            if (!toolchainFile.exists()) {
                val CMAKE_CURRENT_LIST_DIR = "\${CMAKE_CURRENT_LIST_DIR}"
                val CMAKE_BUILD_TYPE = "\${CMAKE_BUILD_TYPE}"
                toolchainFile.writeText("include(\"${CMAKE_CURRENT_LIST_DIR}/build/${CMAKE_BUILD_TYPE}/generators/conan_toolchain.cmake\")\n")
            }
        }
    }
}

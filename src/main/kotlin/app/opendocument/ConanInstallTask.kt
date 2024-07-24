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
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory


abstract class ConanInstallTask : Exec() {
    @get:Input
    abstract val arch: Property<String>

    @get:Input
    abstract val profile: Property<String>

    @get:OutputDirectory
    val outputDirectory: Provider<Directory> = arch.map { project.layout.buildDirectory.get().dir("conan/$it") }

    override fun exec() {
        commandLine(
            "conan", "install", ".",
            "--output-folder=" + outputDirectory.get(),
            "--build=missing",
            "--profile:host=" + profile.get(),
            "--settings:host", "arch=" + arch.get(),
        )
        super.exec()
    }
}

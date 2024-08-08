/*
 * ConanAndroidGradlePlugin.kt
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

import org.gradle.api.Plugin
import org.gradle.api.Project


class ConanAndroidGradlePlugin: Plugin<Project> {
    override fun apply(target: Project) {
        val tasks = target.tasks;
        val preBuild = tasks.named("preBuild").get()
        val syncTask = target.parent?.tasks?.named("prepareKotlinBuildScriptModel")?.get()

        val generateToolchainFileTask = tasks.register("generateToolchainFile", GenerateConanToolchainFileTask::class.java)
        preBuild.dependsOn(generateToolchainFileTask)
        syncTask?.dependsOn(generateToolchainFileTask)

        listOf("armv8", "armv7", "x86", "x86_64").forEach { architecture ->
            val conanInstallTask = tasks.register("conanInstall-$architecture", ConanInstallTask::class.java) { conanInstallTask ->
                conanInstallTask.arch.set(architecture)

                // Some issue with conan's local cache.
                // @TODO: isolate and report to conan-client bugtracker
                // Execute at least one conanInstall before allowing the rest to run in parallel.
                if (architecture != "armv8") {
                    conanInstallTask.dependsOn(tasks.named("conanInstall-armv8"))
                }
            }
            preBuild.dependsOn(conanInstallTask)
            syncTask?.dependsOn(conanInstallTask)
            generateToolchainFileTask.get().dependsOn(conanInstallTask)
        }
    }
}

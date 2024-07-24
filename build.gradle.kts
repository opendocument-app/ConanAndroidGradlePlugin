/*
 * build.gradle.kts
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

plugins {
    kotlin("jvm") version "2.0.0"
    id("com.gradle.plugin-publish") version "1.2.1"
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

group = "app.opendocument"
version = "0.9.0"

gradlePlugin {
    website = "https://github.com/opendocument-app/ConanAndroidGradlePlugin"
    vcsUrl = "https://github.com/opendocument-app/ConanAndroidGradlePlugin.git"

    plugins {
        create("conanAndroidGradlePlugin") {
            id = "app.opendocument.conanandroidgradleplugin"
            displayName = "Conan Android Gradle"
            description = "Plugin which plugs Conan to Android in Gradle"
            tags = listOf("conan", "android", "gradle")
            implementationClass = "app.opendocument.ConanAndroidGradlePlugin"
        }
    }
}

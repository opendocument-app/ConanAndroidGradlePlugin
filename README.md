# Conan Android Gradle Plugin

## Why is this needed?

In order to use Conan dependencies in an Android project, `conan install` needs to be executed before CMake tries to find those libraries. This plugin calls `conan install` for all 4 Android's architectures.

## How it works

Conan Android Gradle Plugin creates 4 Gradle tasks: `conanInstall-armv7`, `conanInstall-armv8`, `conanInstall-x86`, `conanInstall-x86_64` - one for each architecture, for which `conan install` will be launched.

`conanInstall` tasks are added as dependencies to module level `preBuild` task and top level `prepareKotlinBuildScriptModel` task, if it's found.

`preBuild` task is executed before building Android project. `prepareKotlinBuildScriptModel` task is executed when doing Android Studio sync.


## How to use

Conan Android Gradle Plugin is distributed through Gradle Plugin Portal ( `pluginManagement { repositories { gradlePluginPortal() } }` in `settings.gradle`).

To use this plugin, include it in the top level `build.gradle`:
```groovy
plugins {
    id 'app.opendocument.conanandroidgradleplugin' version "0.9.0" apply false
}
```

And then apply the plugin in the module's `build.gradle`:
```groovy
plugins {
    id 'app.opendocument.conanandroidgradleplugin'
}
```

Once the plugin is applied, it needs to be configured. Configuration currently consists of setting conan profile file for each architecture:

```groovy
["armv7", "armv8", "x86", "x86_64"].each { arch ->
    tasks.named("conanInstall-" + arch) {
        profile.set("android-21-" + arch)
    }
}
```

Conan profile can be either a relative path or an absolute path, it could also be a regular conan profile, installed and managed by conan (eg. `default`).

Plugin applies `arch` setting when calling `conan install`, thus all architectures can share the same profile, if no differentiation is needed - `{ profile.set("android") }`.

## Further reading

https://docs.conan.io/2/examples/cross_build/android/android_studio.html

#!/usr/bin/env python3
import os
from pathlib import Path

from getVersion import get_version


def replace_version_in_file(file, prefix, version, suffix):
    r = file.open('r')
    lines = r.readlines()
    r.close()
    with open(file, 'w') as w:
        for line in lines:
            if line.startswith(prefix):
                line = prefix + version + suffix + "\n"
            w.write(line)


def main():
    old_version = get_version()
    print("old version:", old_version)

    version = old_version.split(".")
    version[len(version) - 1] = str(int(version[len(version) - 1]) + 1)

    new_version = ".".join(version)
    print("new version:", new_version)

    gh_output = os.environ.get('GITHUB_OUTPUT')
    if gh_output:
        with open(gh_output, 'w') as out:
            print("oldVersion=" + old_version, file=out)
            print("newVersion=" + new_version, file=out)

    root_dir = Path(__file__).resolve().parent.parent
    replace_version_in_file(root_dir / "README.md", "    id 'app.opendocument.conanandroidgradleplugin' version \"", old_version, "\" apply false")
    replace_version_in_file(root_dir / "build.gradle.kts", "version = \"", new_version, "\"")

    replace_version_in_file(root_dir / "tests" / "sample_library" / "build.gradle",
                            prefix="    id 'app.opendocument.conanandroidgradleplugin' version \"",
                            version=new_version,
                            suffix="\" apply false")
    replace_version_in_file(root_dir / "tests" / "cmake_layout" / "build.gradle",
                            prefix="    id 'app.opendocument.conanandroidgradleplugin' version \"",
                            version=new_version,
                            suffix="\" apply false")
    replace_version_in_file(root_dir / "tests" / "no_default_profile_installed" / "build.gradle",
                            prefix="    id 'app.opendocument.conanandroidgradleplugin' version \"",
                            version=new_version,
                            suffix="\" apply false")


if __name__ == "__main__":
    main()

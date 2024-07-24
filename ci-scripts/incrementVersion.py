#!/usr/bin/env python3
import os
from pathlib import Path

from getVersion import get_version


def str_replace_in_file(file, search, replace):
    r = file.open('r')
    updated_file_contents = r.read().replace(search, replace)
    r.close()
    with open(file, 'w') as w:
        w.write(updated_file_contents)


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
    str_replace_in_file(root_dir / "build.gradle.kts",
                        "version = \"{}\"".format(old_version),
                        "version = \"{}\"".format(new_version))
    str_replace_in_file(root_dir / "README.md",
                        "id 'app.opendocument.conanandroidgradleplugin' version \"{}\" apply false".format(old_version),
                        "id 'app.opendocument.conanandroidgradleplugin' version \"{}\" apply false".format(new_version))


if __name__ == "__main__":
    main()

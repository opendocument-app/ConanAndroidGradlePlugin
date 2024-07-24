#!/usr/bin/env python3
import os
from pathlib import Path

from getVersion import get_version


def main():
    oldversion = get_version()
    print("old version:", oldversion)

    version = oldversion.split(".")
    version[len(version)-1] = str(int(version[len(version)-1]) + 1)

    newversion = ".".join(version)
    print("new version:", newversion)

    gh_output = os.environ.get('GITHUB_OUTPUT')
    if gh_output:
        with open(gh_output, 'w') as out:
            print("oldVersion=" + oldversion, file=out)
            print("newVersion=" + newversion, file=out)

    build_gradle_kts = Path(__file__).resolve().parent.parent / "build.gradle.kts"
    r = build_gradle_kts.open('r')
    updated_script = r.read().replace("version = \"{}\"".format(oldversion), "version = \"{}\"".format(newversion))
    r.close()
    with open(build_gradle_kts, 'w') as w:
        w.write(updated_script)


if __name__ == "__main__":
    main()

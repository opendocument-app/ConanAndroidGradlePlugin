#!/usr/bin/env python3
import os
from pathlib import Path


def get_version():
    script_path = Path(__file__).resolve().parent
    with open(script_path.parent / "build.gradle.kts", "r") as f:
        for line in f.readlines():
            prefix = "version = \""
            if line.startswith(prefix):
                return line.strip()[len(prefix):].strip("\"")


def main():
    version = get_version()
    print(version)
    gh_output = os.environ.get('GITHUB_OUTPUT')
    if gh_output:
        with open(gh_output, 'w') as out:
            print("version=" + version, file=out)


if __name__ == "__main__":
    main()

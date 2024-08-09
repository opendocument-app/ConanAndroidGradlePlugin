#!/usr/bin/env python3
import os.path
import shutil
import subprocess
import sys
from pathlib import Path


def main():
    conan_p_dir = Path(f"{os.getenv("HOME")}/.conan2/p")
    if os.path.exists(conan_p_dir):
        shutil.rmtree(conan_p_dir)

    this_directory = Path(__file__).resolve().parent
    for arch in ("x86", "x86_64"):
        if os.path.exists(f"build-{arch}"):
            shutil.rmtree(f"build-{arch}")

    subprocesses = dict()
    for arch in ("x86", "x86_64"):
        subprocesses[arch] = subprocess.Popen(
            ["conan", "install", "conanfile.txt", f"--output-folder=build-{arch}"],
            cwd=this_directory,
        )

    while len(subprocesses) > 0:
        subprocesses2 = dict()
        for p in subprocesses.keys():
            try:
                return_code = subprocesses[p].wait(timeout=1)
                if return_code is None:
                    subprocesses2[p] = subprocesses[p]  
                elif return_code != 0:
                    print("FAILURE!", file=sys.stderr)
                    for pp in subprocesses.keys():
                        if pp != p:
                            subprocesses[pp].kill()
                            sys.exit(1)
            except subprocess.TimeoutExpired:
                pass
        subprocesses = subprocesses


if __name__ == "__main__":
    main()

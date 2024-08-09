#!/usr/bin/env python3
import argparse
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
        if os.path.exists(f"{this_directory}/build-{arch}"):
            shutil.rmtree(f"{this_directory}/build-{arch}")

    parser = argparse.ArgumentParser(description="Test parallel conan install on an empty ~/.conan2/p")
    parser.add_argument("--serial-mode", action="store_true")
    args = parser.parse_args()
    del parser

    subprocesses = dict()
    for arch in ("x86", "x86_64"):
        subprocesses[arch] = subprocess.Popen(
            ["conan", "install", "conanfile.txt", f"--output-folder=build-{arch}", "--build=missing"],
            cwd=this_directory,
        )

        if args.serial_mode:
            subprocesses[arch].wait()
            del subprocesses[arch]

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

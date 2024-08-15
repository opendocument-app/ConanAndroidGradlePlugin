from conan import ConanFile

required_conan_version = ">=2.0.6"


class DotPyConan(ConanFile):
    settings = "os", "compiler", "build_type", "arch"
    requires = "zlib/1.3.1"

    generators = "CMakeToolchain", "CMakeDeps"


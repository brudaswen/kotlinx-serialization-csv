# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]


## [0.2.0] - 2020-05-24
### Changed
- Compatibility with *Kotlin Serialization* `0.20.0`.
- (De-)Serialization of sealed classes no longer reads/writes columns for objects.
- String for `kotlin.Unit` can no longer be provided and the `Unit` object is handled as any other object. 

## [0.1.0] - 2020-02-21
### Added
- Initial release for *Kotlin Serialization* `0.14.0`.

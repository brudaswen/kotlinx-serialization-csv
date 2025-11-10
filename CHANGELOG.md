# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [3.2.0] - 2025-11-10

### Added

- Added `iosX64`, `linuxX64`, `macosX64`, `mingwX64` target support.

## [3.1.0] - 2025-10-21

### Added

- Add `wasmJs` target support.

## [3.0.2] - 2025-10-08

### Added

- Add JVM extensions for `Csv.recordWriter(Appendable)`.

## [3.0.1] - 2025-10-08

### Added

- Add JVM extensions for `Csv.recordReader(Reader)`.
- Add JVM extensions for `Csv.recordReader(InputStream)`.
- Add JVM extensions for `Csv.recordWriter(OutputStream)`.

## [3.0.0] - 2025-10-07

### Added

- Config `shortValueClassHeaderName` for short value class header name.
- Kotlin Multiplatform support.

### Changed

- Some API changes for Kotlin Multiplatform support.

## [2.1.1] - 2025-10-01

### Changed

- Update to Kotlin 2.2.20.
- Update to Kotlinx-Serialization 1.9.0.

## [2.1.0] - 2024-12-13

### Changed

- Update to Kotlin 2.1.0 (🏅 kudos to theyoz).
- Update to Kotlinx-Serialization 1.7.3.
- Make `CsvConfig` public.
- Support for streaming via Reader and Appendable (🏅 kudos to UnknownJoe796).
- Handle Microsoft Excel's insistence on using a byte order marker (🏅 kudos to UnknownJoe796).
- Asynchronous/Streaming support via `CsvRecordReader` and `CsvRecordWriter` (🏅 kudos to
  UnknownJoe796).

### Removed

- Removed `Csv(from: Csv, action: CsvBuilder.() -> Unit)` (use `from.configure {}` instead).

## [2.0.0] - 2020-11-08

### Added

- `Csv {}` builder function to configure Csv instance.

### Changed

- Use Unix newline (`\n`) as default `recordSeparator` (use `Csv { recordSeparator = "\r\n" }` or
  `Csv.Rfc4180` for old behavior).
- Using `QuoteMode.NONE` requires `escapeChar` to be set manually (use
  `Csv { quoteMode = QuoteMode.NONE ; escapeChar = '\\' }` for old behavior).
- Last line in CSV is *always* ignored when empty.
- Throws `SerializationException` instead of `IllegalStateException` in case of error.

### Removed

- Removed `CsvConfiguration` (use `Csv {}` builder function instead).
- Removed `Csv.default` (use `Csv { recordSeparator = "\r\n" }` instead).
- Removed `Csv.rfc4180` (use `Csv.Rfc4180` instead).
- Removed `Csv.excel` (use `Csv.Rfc4180` instead).

## [1.1.0] - 2020-11-08

### Added

- Support `ignoreUnknownColumns`.

## [1.0.2] - 2020-10-11

### Changed

- Update to *Kotlin Serialization* `1.0.0`.

## [1.0.1] - 2020-09-30

### Added

- Source compatibility with Java 8.

### Changed

- Update to *Kotlin Serialization* `1.0.0-RC2`.

## [1.0.0] - 2020-09-24

### Changed

- Compatibility with *Kotlin Serialization* `1.0.0-RC`.

## [0.2.0] - 2020-05-24

### Changed

- Compatibility with *Kotlin Serialization* `0.20.0`.
- (De-)Serialization of sealed classes no longer reads/writes columns for objects.
- String for `kotlin.Unit` can no longer be provided and the `Unit` object is handled as any other
  object.

## [0.1.0] - 2020-02-21

### Added

- Initial release for *Kotlin Serialization* `0.14.0`.

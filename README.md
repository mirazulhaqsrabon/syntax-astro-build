# Astro

A syntax-highlighting extension for [Xed-Editor](https://github.com/Xed-Editor/Xed-Editor).

## Overview

Astro provides syntax recognition and highlighting for `.astro` files, integrating TextMate grammar with Xed-Editor's language system.

## Features

- `.astro` file recognition
- Astro frontmatter highlighting
- HTML and Astro component support
- Embedded JavaScript, TypeScript, and CSS highlighting
- TextMate grammar integration
- Custom file icon
- Language-specific editor configuration

## Installation

1. Download the extension ZIP package.
2. Open Xed-Editor.
3. Navigate to the Extensions section.
4. Import the ZIP package.
5. Open an `.astro` file.

## Reuse

This project can serve as a foundation for developing syntax-highlighting extensions for other programming, scripting, markup, and configuration languages. Its Kotlin implementation, file type registration, TextMate grammar integration, and language configuration can be adapted to support additional languages.

#### Build in Debug Mode:
```bash
./compileDebug
```

#### Build in Release Mode:
```bash
./compileRelease
```

The resulting zip package is outputted to:
```
output/Json LSP.zip
```

This project is licensed under the [MIT License]().

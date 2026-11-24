# Astro

A syntax-highlighting extension for [Xed-Editor](https://github.com/Xed-Editor/Xed-Editor).

## Overview

Astro provides syntax recognition and TextMate highlighting for `.astro` files. The extension code and file-type icon are packaged in the extension; language grammar and configuration files are loaded at runtime from a user-managed directory.

## External assets

Place the following files in:

```text
$HOME/.orange/assets/
├── languages.json
├── astro.tmLanguage.json
└── Astro.config.json
```

The `languages.json` file must reference the other two files using paths relative to this directory. The extension does not download or generate these files. It reads them from the Android process's `HOME` environment variable, falling back to Java's `user.home` property.

**Android note:** the `HOME` seen by Xed-Editor must point to a directory that the Xed-Editor process can read. A terminal app's private home directory is normally isolated from other apps, so a path that exists only inside another terminal app may not be accessible to Xed-Editor.

## Features

- `.astro` file recognition
- Astro frontmatter highlighting
- HTML and Astro component support
- Embedded JavaScript, TypeScript, and CSS highlighting
- Runtime loading of external TextMate grammar and language configuration
- Custom file icon

## Installation

1. Put the three asset files in `$HOME/.orange/assets/`.
2. Build and install the extension ZIP package.
3. Open Xed-Editor and import the ZIP package.
4. Open an `.astro` file.

## Build

Debug:
```bash
./compileDebug
```

Release:
```bash
./compileRelease
```

The resulting ZIP package is written to `output/`.

This project is licensed under the MIT License.

<div align="center">
  <h1>DebugStick</h1>
  <h3>Java Edition Style Debug Stick for PowerNukkitX</h3>

  <p>
    <strong>English</strong> | <a href="README.ja.md">日本語</a>
  </p>

  <p>
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge" alt="License"></a>
    <a href="https://github.com/PowerNukkitX/PowerNukkitX"><img src="https://img.shields.io/badge/Platform-PowerNukkitX-green?style=for-the-badge&logo=minecraft" alt="Platform"></a>
    <a href="https://openjdk.org/"><img src="https://img.shields.io/badge/Language-Java%2021-blue?style=for-the-badge&logo=openjdk" alt="Language"></a>
    <a href="https://github.com/Byakkono/DebugStick/releases"><img src="https://img.shields.io/badge/Version-1.0.0-orange?style=for-the-badge" alt="Version"></a>
  </p>

  <p>
    <strong>Change block states with just a click</strong>
    <br>
    Standalone • Lightweight • English / Japanese
  </p>

  <p>
    <a href="https://github.com/Byakkono/DebugStick/releases">Download</a> •
    <a href="#features">Features</a> •
    <a href="#usage">Usage</a> •
    <a href="#configuration">Configuration</a>
  </p>
</div>

---

<a name="overview"></a>
## Overview

**DebugStick** brings the Java Edition `debug_stick` to **PowerNukkitX**. Change block properties such as stair facing, slab halves and fence connections with just a click, no GUI needed.

<a name="features"></a>
## Features

*   **Property Selection:** Left click to select a property. The selection is stored on the stick and carries over to other blocks.
*   **Value Cycling:** Right click to cycle the value. Sneak to cycle in reverse.
*   **Free Connections:** Blocks are changed without block updates, so fence and wall connections can be set freely.
*   **Keeps Contents:** Chest contents and sign text are kept when changing a block's state.
*   **Access Control:** Choose who can use the stick: operators only (default), anyone in creative mode, or everyone.
*   **Protection Friendly:** Clicks cancelled by other plugins are ignored, so it works alongside land protection plugins.
*   **Standalone:** No dependencies other than PowerNukkitX itself.
*   **English / Japanese:** English by default. Switch to Japanese or each player's game language in the config, or add your own language file.

<a name="installation"></a>
## Installation

1.  Download `DebugStick-1.0.0.jar` from the [releases page](https://github.com/Byakkono/DebugStick/releases).
2.  Place the file into your server's `plugins` folder.
3.  Restart your PowerNukkitX server.

<a name="usage"></a>
## Usage

1.  Run `/debugstick` to get the stick. By default only operators can use it (see [Access](#access)).
2.  Click a block while holding the stick.

| Action | Effect |
| :--- | :--- |
| Left click | Select a property |
| Right click | Cycle the value of the selected property |
| Sneak + click | Cycle in reverse |

> [!NOTE]
> The stick does nothing where another plugin, such as land protection, cancels the click. There is no direct integration with any specific plugin.

<a name="commands"></a>
## Commands & Permissions

| Command | Description | Permission | Default |
| :--- | :--- | :--- | :--- |
| `/debugstick` | Get a debug stick | `debugstick.use` (only when `access: op`) | OP |

<a name="configuration"></a>
## Configuration

<a name="access"></a>
### Access

Choose who can use the debug stick with `access` in `plugins/DebugStick/config.yml`.

```yaml
access: op
```

| Value | Who can use it |
| :--- | :--- |
| `op` | Players with the `debugstick.use` permission. Operators have it by default (default) |
| `creative` | Anyone in creative mode |
| `everyone` | Anyone in any game mode |

In survival, left click selects a property as soon as you start breaking the block, and the block is not broken.

> [!WARNING]
> With `everyone`, survival players can change any block state, including crop growth stages. Use it with care on survival servers.

### Language

Messages are in English by default. Change `language` in `plugins/DebugStick/config.yml` to switch.

```yaml
# en   : English (default)
# ja   : Japanese
# auto : each player's game language (falls back to English)
language: en
```

| Value | Display |
| :--- | :--- |
| `en` | English (default) |
| `ja` | Japanese |
| `auto` | Each player's game language, English if not available |

### Messages

Item name, lore and messages are in `plugins/DebugStick/lang/`. Use `&` for color codes. Copy `lang/en.yml` to a new file such as `lang/fr.yml` to add a language. Missing keys fall back to English.

```yaml
# lang/en.yml
item:
  name: "&bDebug Stick"
  lore:
    - "&7Left click: select property"
    - "&7Right click: change value"
    - "&7Sneak to reverse"
message:
  player-only: "This command can only be used in game."
  creative-only: "&cUse it in creative mode."
  no-permission: "&cYou do not have permission to use the debug stick."
  inventory-full: "&cYour inventory is full."
```

<a name="requirements"></a>
## Requirements

*   PowerNukkitX 3.0.x
*   Java 21

<a name="development"></a>
## Development

Built with **Maven**.

```bash
mvn clean package
```

The jar is created at `target/DebugStick-1.0.0.jar`.

## Contributing

Bug reports and suggestions are welcome. Please open an issue or submit a pull request.

## License

This project is licensed under the **[MIT License](LICENSE)**.

---

<div align="center">
  <p>Made by <strong>Byakkono</strong> for the PowerNukkitX Community.</p>
</div>

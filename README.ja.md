<div align="center">
  <h1>DebugStick</h1>
  <h3>PowerNukkitX 用 Java 版風デバッグ棒</h3>

  <p>
    <a href="README.md">English</a> | <strong>日本語</strong>
  </p>

  <p>
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge" alt="License"></a>
    <a href="https://github.com/PowerNukkitX/PowerNukkitX"><img src="https://img.shields.io/badge/Platform-PowerNukkitX-green?style=for-the-badge&logo=minecraft" alt="Platform"></a>
    <a href="https://openjdk.org/"><img src="https://img.shields.io/badge/Language-Java%2021-blue?style=for-the-badge&logo=openjdk" alt="Language"></a>
    <a href="https://github.com/Byakkono/DebugStick/releases"><img src="https://img.shields.io/badge/Version-1.0.0-orange?style=for-the-badge" alt="Version"></a>
  </p>

  <p>
    <strong>ブロックの状態を、クリックだけで自由に切り替える</strong>
    <br>
    単体で動作 • 軽量 • 英語 / 日本語対応
  </p>

  <p>
    <a href="https://github.com/Byakkono/DebugStick/releases">ダウンロード</a> •
    <a href="#features">特徴</a> •
    <a href="#usage">使い方</a> •
    <a href="#configuration">設定</a>
  </p>
</div>

---

<a name="overview"></a>
## 概要

**DebugStick** は、Java 版の `debug_stick` を **PowerNukkitX** で再現するプラグインです。階段の向き・ハーフブロックの上下・柵の接続など、ブロックが持つ状態(プロパティ)を、GUI を開かずにクリックだけで切り替えられます。

<a name="features"></a>
## 特徴

*   **状態の選択:** 左クリックで、切り替える状態を選びます。選んだ状態は棒に記録され、別のブロックにも引き継がれます。
*   **値の切り替え:** 右クリックで値を切り替えます。スニークしながらだと逆順です。
*   **自由な接続:** 周りのブロックを更新せずに書き換えるので、柵や壁の接続も自由に変えられます。
*   **中身はそのまま:** チェストの向きを変えても、中身や看板の文字は残ります。
*   **使える人を選べる:** OP だけ(標準)・クリエイティブの人・全員、の3つから選べます。
*   **土地保護と共存:** 他のプラグインが取り消したクリックには反応しないので、土地保護プラグインと一緒に使えます。
*   **単体で動作:** PowerNukkitX 本体だけで動きます。他のプラグインは不要です。
*   **英語 / 日本語:** メッセージは英語が標準で、設定で日本語やプレイヤーごとの言語に切り替えられます。言語ファイルを足せば他の言語にも対応できます。

<a name="installation"></a>
## 導入方法

1.  [Releases](https://github.com/Byakkono/DebugStick/releases) から `DebugStick-1.0.0.jar` をダウンロードします。
2.  サーバーの `plugins` フォルダに入れます。
3.  サーバーを再起動します。

<a name="usage"></a>
## 使い方

1.  `/debugstick` を実行すると、デバッグ棒を手に持ちます。標準では OP だけが使えます([使える人](#access)を参照)。手に持っていたアイテムは持ち物の空きに移ります。
2.  デバッグ棒を持ってブロックをクリックします。

| 操作 | 動作 |
| :--- | :--- |
| 左クリック | 切り替える状態を選ぶ |
| 右クリック | 選んだ状態の値を切り替える |
| スニーク + クリック | 逆順に切り替える |

> [!NOTE]
> 土地保護などのプラグインがクリックを取り消した場所では動きません。特定のプラグインとは連携していないため、保護の効き方はそのプラグイン次第です。

<a name="commands"></a>
## コマンドと権限

| コマンド | 説明 | 権限 | 既定 |
| :--- | :--- | :--- | :--- |
| `/debugstick` | デバッグ棒を手に持つ | `debugstick.use`(`access: op` のときだけ) | OP |

<a name="configuration"></a>
## 設定

<a name="access"></a>
### 使える人

`plugins/DebugStick/config.yml` の `access` で、デバッグ棒を使える人を選べます。

```yaml
access: op
```

| 値 | 使える人 |
| :--- | :--- |
| `op` | `debugstick.use` 権限を持つ人。OP は最初から持っています(標準) |
| `creative` | クリエイティブモードの人なら誰でも |
| `everyone` | ゲームモードに関係なく誰でも |

サバイバルでは、ブロックを壊し始めた時点で左クリックの「状態を選ぶ」が働き、ブロックは壊れません。

> [!WARNING]
> `everyone` にすると、サバイバルの人も作物の成長段階などを含むあらゆるブロックの状態を変えられます。サバイバルのサーバーでは注意して使ってください。

### 言語

メッセージは英語が標準です。`plugins/DebugStick/config.yml` の `language` で切り替えられます。

```yaml
# en   : 英語(標準)
# ja   : 日本語
# auto : プレイヤーごとのゲームの言語(対応していない言語は英語)
language: ja
```

| 値 | 表示 |
| :--- | :--- |
| `en` | 英語(標準) |
| `ja` | 日本語 |
| `auto` | プレイヤーごとのゲームの言語。対応していない言語は英語 |

### メッセージ

アイテムの名前・説明とメッセージは `plugins/DebugStick/lang/` のファイルで変えられます。`&` で色を付けられます。`lang/en.yml` をコピーして `lang/fr.yml` のように名前を変えると、言語を追加できます。項目が足りないときは英語が表示されます。

```yaml
# lang/ja.yml
item:
  name: "&bデバッグ棒"
  lore:
    - "&7左クリック: 状態を選ぶ"
    - "&7右クリック: 値を切り替える"
    - "&7スニークで逆順"
message:
  player-only: "このコマンドはゲーム内で実行してください。"
  creative-only: "&cクリエイティブモードで使ってください。"
  no-permission: "&cデバッグ棒を使う権限がありません。"
  inventory-full: "&c持ち物に空きがありません。"
```

<a name="requirements"></a>
## 動作環境

*   PowerNukkitX 3.0.x
*   Java 21

<a name="development"></a>
## ビルド

**Maven** でビルドします。

```bash
mvn clean package
```

`target/DebugStick-1.0.0.jar` が作られます。

## 貢献

不具合の報告や改善の提案は、Issue または Pull Request でお願いします。

## ライセンス

このプロジェクトは **[MIT License](LICENSE)** で公開しています。

---

<div align="center">
  <p>Made by <strong>Byakkono</strong> for the PowerNukkitX Community.</p>
</div>

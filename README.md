# histleap
## api_keyの登録
- 以下のファイルを登録する

`/android/app/src/debug/res/values/google_maps_api.xml`
`/android/app/src/release/res/values/google_maps_api.xml`

- 内容は、下記の通り

```xml
<resources>
    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">MAPS_APIKEY</string>
</resources>
```

## inputデータ
- サンプルは、`sampleInput.json`を参照

### 形式
- json形式
- 以下のGoogleスタイルガイドに準拠する
  - https://google.github.io/styleguide/jsoncstyleguide.xml#Property_Name_Format
- UTF-8

### 内容
#### 地方公共団体の情報
1. localGovernmentCode(必須)
  - 全国地方公共団体コード
    - http://www.soumu.go.jp/denshijiti/code.html
  - 半角数字5桁(チェックディジット無し)
1. spots(必須)
  - スポットの配列

#### スポットの情報
1. spotName(必須)
  - スポット名称
  - 30文字以内
1. spotLatitude(必須)
  - 緯度
    - スポットの位置特定に使用
  - 半角数字
    - 文字列でも数値でも可
1. spotLongitude(必須)
  - 経度
    - スポットの位置特定に使用
  - 半角数字
    - 文字列でも数値でも可
1. spotAddress
  - スポット住所
  - スポットの説明部分に表示される
1. spotOverview
  - スポット概要説明
  - 50文字以内
1. spotURL
  - スポットURL
    - スポットの詳細を知るためのWebサイトURL
1. spotImageURL
  - スポットの画像URL
1. events(必須)
  - イベントの配列

### イベントの情報
1. eventStartYear(必須)
  - 半角数字4文字
    - 文字列でも数値でも可
1. eventEndYear
  - イベントが複数年に及ぶ場合、設定する
  - 半角数字4文字
    - 文字列でも数値でも可
1. eventOverview(必須)
  - イベント概要説明
  - 50文字以内
1. eventURL
  - イベントURL
    - イベントの詳細を知るためのWebサイトURL

# TakoImageEditor
画像編集ソフト<br>
libフォルダのImageUtil.jarは、<a href="https://github.com/2T-T2/ImageUtil/tree/main/out">ココ</a>にあるやつです。<br>
<br>
ダウンロードしてすぐに使いたい場合<br>
 - java実行環境あり → <a href="https://github.com/2T-T2/TakoImageEditor/releases/download/v1.0.0.0/doesnot-have-Java-Environment.zip">ここからダウンロード</a>
 - java実行環境無し → <a href="https://github.com/2T-T2/TakoImageEditor/releases/download/v1.0.0.0/has-Java-Environment.zip">ここからダウンロード</a>

## make.bat

 - 環境変数 JAVA_HOME が設定済み。
 - 環境変数 PATH に JAVA_HOME\\bin が設定済み。
 - 環境変数 PATH に csc.exe のあるフォルダが設定済み。
<br>
であることを前提に作られています
<br>

### make.bat使い方

java実行環境のある人 → 6<br>
java実行環境のない人 → 7<br>
を実行すれば大丈夫です！<br><br>
1～6は、環境変数 PATH に csc.exe のあるフォルダが設定されていなくてもOKです。<br>
7は、環境変数 PATH に csc.exe のあるフォルダが設定してなければいけません。
<br>

1. javaファイルをコンパイルしてclassファイルを作成する。
<code>  
  make compile
</code>
<hr>

2. javaファイルをコンパイルしてclassファイルを作成し、classファイルを実行する。
<code>
  make compile-run
</code>
<hr>

3. classファイルからjarファイルを作成する。
<code>
  make build
</code>
<hr>

4. classファイルからjarファイルを作成し、jarファイルを実行する。
<code>
  make build-run
</code>
<hr>

5. outディレクトリにプログラムに必要なファイルをコピーする。（outディレクトリからプログラムを実行可能にする）
<code>
  make copy-deps-files
</code>
<hr>

6. 1と5と4を実行する。
<code>
  make build-all-run
</code>
<hr>

7. 1と5と3を実行して、java実行環境のない Windowsユーザ向けに exeファイルを作成。
<code>
  make mkdist
</code>

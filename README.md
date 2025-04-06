# チャットボットプロジェクト

## 概要
このプロジェクトは、FastAPIとReactを使用したチャットボットアプリケーションです。AIモデルを使用して自然な会話を実現し、音声入力/出力機能も備えています。

## 機能
- テキストベースのチャット
- 音声入力/出力
- ユーザー認証
- チャット履歴の保存
- 感情分析
- 自動応答機能
  - 状況に応じたテンプレート応答
  - 感情に基づく応答の調整
  - 時間帯に応じた挨拶の追加
  - コンテキストに基づく応答の最適化

## 必要条件
- Python 3.11以上
- MySQL 8.0以上
- pip

## セットアップ手順

### 1. 環境の準備
```bash
# 仮想環境の作成と有効化
python -m venv venv
source venv/bin/activate  # Linuxの場合
.\venv\Scripts\activate   # Windowsの場合

# pipのアップグレード
python -m pip install --upgrade pip

# 必要なパッケージのインストール
pip install -r requirements.txt

# OpenAI APIキーの設定
# Windowsの場合
set OPENAI_API_KEY=your-api-key-here
# Linuxの場合
export OPENAI_API_KEY=your-api-key-here

# 追加パッケージのインストール
pip install numpy==1.26.4
pip install transformers torch numpy
pip install SpeechRecognition==3.10.1
pip install gTTS==2.5.1
pip install openai==1.70.0
```

### 2. MySQL環境の構築（Docker）

#### 2.1 Docker Composeファイルの作成
プロジェクトのルートディレクトリに`docker-compose.yml`を作成します：

```yaml
version: "3.9"
services:
  mysql:
    image: mysql:8.0.28
    platform: linux/amd64
    container_name: mysql-container
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: chatbot
      TZ: "Asia/Tokyo"
    volumes:
      - ./my.cnf:/etc/mysql/conf.d/my.cnf
```

#### 2.2 MySQL設定ファイルの作成
プロジェクトのルートディレクトリに`my.cnf`を作成します：

```ini
[mysqld]
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci

[client]
default-character-set = utf8mb4
```

#### 2.3 Dockerコンテナの起動
```bash
# Dockerコンテナの起動
docker-compose up -d

# コンテナの状態確認
docker-compose ps
```

#### 2.4 データベースの初期化
```bash
# データベースとユーザーを作成
CREATE DATABASE chatbot CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'chatbot_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON chatbot.* TO 'chatbot_user'@'localhost';
FLUSH PRIVILEGES;

# データベースのマイグレーション
python -m alembic upgrade head

# 初期管理者ユーザーの作成
curl -X POST http://localhost:8000/api/setup/admin
```

### 3. 環境変数の設定
`.env`ファイルを作成し、以下の内容を設定：
```env
SECRET_KEY=your-secret-key
JWT_SECRET_KEY=your-jwt-secret-key
DATABASE_URL=mysql://chatbot_user:your_password@localhost/chatbot?charset=utf8mb4
PORT=8000
```

### 4. フロントエンドの設定
フロントエンドの`.env`ファイルを作成し、以下の内容を設定：
```env
VITE_API_BASE_URL=http://localhost:8000
VITE_WS_URL=ws://localhost:8000
```

### 5. アプリケーションの起動
```bash
# 仮想環境がアクティベートされていることを確認
# Windows
.\venv\Scripts\activate
# Linux/Mac
source venv/bin/activate

# アプリケーションの起動
python main.py
```

### 5. フロントエンドの再起動
```bash
# フロントエンドディレクトリに移動
cd frontend

# 既存のプロセスを終了（Windows）
taskkill /F /IM node.exe
# 既存のプロセスを終了（Linux/Mac）
pkill -f "npm run dev"

# 開発サーバーの再起動
npm run dev
```

注意事項：
- バックエンドとフロントエンドの両方を再起動することで、最新の変更が反映されます
- データベースの変更がある場合は、バックエンドの再起動時に自動的に適用されます
- 環境変数の変更がある場合は、必ず再起動が必要です

## デフォルト管理者アカウント
- ユーザー名: admin
- パスワード: admin123
- メールアドレス: admin@example.com

## APIエンドポイント
- `POST /register`: 新規ユーザー登録
- `POST /login`: ユーザーログイン
- `POST /logout`: ログアウト
- `POST /chat`: チャットメッセージの送信
- `GET /chat/history`: チャット履歴の取得
- `GET /health`: ヘルスチェック
- `POST /create-admin`: 管理者アカウントの作成

## 開発時の注意事項
- デバッグモードが有効になっています（`debug=True`）
- デフォルトポートは8000です
- セキュリティ関連の設定は開発用に簡略化されています
- 本番環境では適切なセキュリティ設定が必要です

## エラーハンドリング
### 認証エラー
- トークンの有効期限切れ（401エラー）
  - 自動的にログイン画面にリダイレクト
  - ローカルストレージのトークンが削除される
- 認証失敗（401エラー）
  - ログイン画面にリダイレクト
  - トークンが無効な場合は削除

### サーバーエラー
- 500エラー（Internal Server Error）
  - サーバー側でエラーが発生
  - エラーメッセージが表示される
  - しばらく時間をおいて再度試行

## トラブルシューティング
### データベース接続エラー
- MySQLサーバーが起動していることを確認
- データベースの認証情報が正しいことを確認
- ポート番号が正しいことを確認

### パッケージのインポートエラー
- 仮想環境が有効になっていることを確認
- 必要なパッケージがすべてインストールされていることを確認

## 開発モードの機能
### 認証バイパス
開発時は認証をバイパスし、常に管理者ユーザーとしてログインできます：
- メールアドレス: 任意
- パスワード: 任意
- 自動的に管理者権限が付与されます

### 自動応答機能
チャットボットは以下のパターンで自動応答を行います：

#### 基本的な応答パターン
- 挨拶（「こんにちは」）: 挨拶と支援の申し出
- 感謝（「ありがとう」）: お礼と追加支援の申し出
- 別れ（「さようなら」「バイバイ」）: 別れの挨拶
- 質問（「?」「？」を含む）: 質問内容の確認

#### 高度な応答機能
- 感情分析に基づく応答の調整
- 時間帯に応じた挨拶の追加
- コンテキストを考慮した応答の最適化
- 質問タイプに応じた応答の調整
- 緊急度に基づく優先応答

#### フォールバック応答
APIが利用できない場合や応答生成に失敗した場合、以下のような汎用的な応答を返します：
- 詳細の確認
- 具体的な説明の要求
- 支援の申し出
- フォローアップの質問

これらの応答は、ユーザーとの自然な対話を維持しながら、必要な情報を収集することを目的としています。

## 6. 管理者アカウントの作成

管理者アカウントを作成するには、以下のSQLコマンドをデータベースで実行してください：

```sql
INSERT INTO users (username, email, hashed_password, is_active, is_admin, created_at)
VALUES (
    'admin',
    'admin@example.com',
    '$2b$12$3q1KWxKDK5CGLil0H.SE5Oyf6KOYMMTsjWFEmjNjXmt14XdIt628G',
    1,
    1,
    datetime('now', 'utc')
);
```

このコマンドで作成される管理者アカウントの認証情報：
- ユーザー名: `admin`
- パスワード: `admin123`
- メールアドレス: `admin@example.com`

注意事項：
1. アプリケーションの初回起動時にデータベースが初期化されていることを確認してください
2. 管理者アカウント作成後、セキュリティのためパスワードを変更することを推奨します
3. 本番環境では、より強力なパスワードを使用してください

## シャドウワークスペースの設定

このプロジェクトでは、開発効率を向上させるためにシャドウワークスペースを使用しています。

### 設定方法
1. `.cursor`ディレクトリを作成
```bash
mkdir .cursor
```

2. `.cursor/settings.json`ファイルを作成し、以下の設定を追加
```json
{
  "shadowWorkspace": {
    "enabled": true,
    "path": "shadow",
    "exclude": [
      "node_modules",
      "venv",
      "__pycache__",
      ".git",
      "*.pyc",
      "*.pyo",
      "*.pyd",
      ".env",
      "*.log"
    ]
  }
}
```

3. shadowディレクトリを作成
```bash
mkdir shadow
```

4. プロジェクトファイルをshadowディレクトリにコピー
```bash
# Windowsの場合
xcopy /E /I /Y . shadow\

# Linuxの場合
cp -r . shadow/
```

### 除外ファイル
以下のファイルとディレクトリはシャドウワークスペースから除外されます：
- `node_modules`: フロントエンドの依存関係
- `venv`: Python仮想環境
- `__pycache__`: Pythonのキャッシュファイル
- `.git`: Gitリポジトリ
- `*.pyc`, `*.pyo`, `*.pyd`: Pythonのコンパイル済みファイル
- `.env`: 環境変数ファイル
- `*.log`: ログファイル

### 注意事項
- シャドウワークスペースは開発効率を向上させるための機能です
- 本番環境では使用しないでください
- 除外ファイルは必要に応じて`.cursor/settings.json`で調整可能です

## マイクロサービスアーキテクチャの構築手順

### 1. 環境の準備

#### 1.1 必要なソフトウェア
- Docker Desktop
- Java 17以上
- Maven 3.8以上
- Git

#### 1.2 プロジェクトのクローン
```bash
git clone <repository-url>
cd micro-service
```

### 2. サービスの構成

プロジェクトは以下の主要コンポーネントで構成されています：

- API Gateway (Spring Cloud Gateway)
- サービスディスカバリ (Consul)
- メッセージング (RabbitMQ)
- データベース (PostgreSQL)
- モニタリング (Prometheus + Grafana)
- ログ管理 (ELK Stack)
- サンプルマイクロサービス (User Service)

### 3. サービスの起動

#### 3.1 Docker Composeによる起動
```bash
# すべてのサービスを起動
docker-compose up -d

# サービスの状態確認
docker-compose ps
```

#### 3.2 各サービスのビルドと起動
```bash
# API Gatewayのビルド
cd api-gateway
mvn clean package
docker build -t api-gateway .

# User Serviceのビルド
cd ../services/user-service
mvn clean package
docker build -t user-service .
```

### 4. サービスの確認

起動後、以下のエンドポイントで各サービスにアクセスできます：

- API Gateway: http://localhost:8080
- Consul UI: http://localhost:8500
- RabbitMQ Management: http://localhost:15672
- Grafana: http://localhost:3000
- Kibana: http://localhost:5601

### 5. サンプルAPIの使用

#### 5.1 ユーザーサービスのAPI
```bash
# ユーザー一覧の取得
curl http://localhost:8080/api/users

# ユーザーの作成
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@example.com","password":"password"}'

# 特定のユーザーの取得
curl http://localhost:8080/api/users/1

# ユーザーの更新
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"username":"updated","email":"updated@example.com","password":"newpassword"}'

# ユーザーの削除
curl -X DELETE http://localhost:8080/api/users/1
```

### 6. モニタリングとログ管理

#### 6.1 Prometheus
- メトリクスの収集: http://localhost:9090
- 設定ファイル: monitoring/prometheus/prometheus.yml

#### 6.2 Grafana
- ダッシュボード: http://localhost:3000
- デフォルト認証情報:
  - ユーザー名: admin
  - パスワード: admin

#### 6.3 ELK Stack
- Kibana: http://localhost:5601
- ログ設定: logging/logstash/pipeline/logstash.conf

### 7. 開発ガイドライン

#### 7.1 新しいマイクロサービスの追加
1. `services`ディレクトリに新しいサービスを作成
2. 必要な依存関係を`pom.xml`に追加
3. サービスディスカバリに登録するための設定を追加
4. `docker-compose.yml`に新しいサービスを追加

#### 7.2 環境変数の設定
各サービスは以下の環境変数を使用します：

```env
# データベース設定
DB_HOST=database
DB_PORT=5432
DB_USER=admin
DB_PASSWORD=admin

# メッセージング設定
RABBITMQ_HOST=message-broker
RABBITMQ_PORT=5672
RABBITMQ_USER=admin
RABBITMQ_PASSWORD=admin

# サービスディスカバリ設定
CONSUL_HOST=service-discovery
CONSUL_PORT=8500
```

### 8. トラブルシューティング

#### 8.1 一般的な問題
- サービスが起動しない場合
  - Dockerのログを確認: `docker-compose logs <service-name>`
  - ポートの競合を確認
  - 環境変数の設定を確認

- データベース接続エラー
  - PostgreSQLのログを確認
  - 接続情報を確認
  - ネットワーク設定を確認

#### 8.2 モニタリング関連
- Prometheusのメトリクスが表示されない
  - サービスのヘルスチェックを確認
  - Prometheusの設定を確認
  - ネットワーク接続を確認

- Grafanaのダッシュボードが表示されない
  - データソースの設定を確認
  - Prometheusとの接続を確認

### 9. セキュリティ考慮事項

#### 9.1 本番環境での設定
- 強力なパスワードの使用
- SSL/TLSの設定
- ファイアウォールの設定
- アクセス制御の実装

#### 9.2 推奨される追加設定
- JWT認証の実装
- レート制限の設定
- サーキットブレーカーの実装
- 分散トレーシングの実装

### 10. スケーリング

#### 10.1 水平スケーリング
```bash
# 特定のサービスをスケール
docker-compose up -d --scale user-service=3
```

#### 10.2 負荷分散
- API Gatewayが自動的に負荷分散を処理
- Consulがサービスのヘルスチェックを管理

### 11. バックアップとリストア

#### 11.1 データベースのバックアップ
```bash
# バックアップの作成
docker exec -t database pg_dump -U admin microservices > backup.sql

# リストア
docker exec -i database psql -U admin microservices < backup.sql
```

#### 11.2 ボリュームのバックアップ
```bash
# ボリュームのバックアップ
docker run --rm -v micro-service_postgres-data:/volume -v /tmp:/backup alpine tar -czf /backup/postgres-backup.tar.gz -C /volume ./

# ボリュームのリストア
docker run --rm -v micro-service_postgres-data:/volume -v /tmp:/backup alpine sh -c "rm -rf /volume/* /volume/..?* /volume/.[!.]* ; tar -C /volume/ -xzf /backup/postgres-backup.tar.gz"
``` 
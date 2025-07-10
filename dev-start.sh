#!/bin/bash

# Coffee Journal 開發環境啟動腳本

set -e

echo "🛠️ 啟動 Coffee Journal 開發環境..."

# 檢查 Java 是否安裝
if ! command -v java &> /dev/null; then
    echo "❌ Java 未安裝，請先安裝 Java 17+"
    exit 1
fi

# 檢查 Node.js 是否安裝
if ! command -v node &> /dev/null; then
    echo "❌ Node.js 未安裝，請先安裝 Node.js 18+"
    exit 1
fi

# 檢查 PHP 是否安裝
if ! command -v php &> /dev/null; then
    echo "❌ PHP 未安裝，請先安裝 PHP 8.2+"
    exit 1
fi

# 啟動 Java 後端
echo "🚀 啟動 Java 後端 (端口 8080)..."
cd backend-java
./gradlew bootRun &
JAVA_PID=$!
cd ..

# 等待 Java 後端啟動
echo "⏳ 等待 Java 後端啟動..."
sleep 15

# 啟動 Laravel 前端
echo "🚀 啟動 Laravel 前端 (端口 8000)..."
cd backend-laravel

# 安裝依賴（如果需要）
if [ ! -d "vendor" ]; then
    echo "📦 安裝 PHP 依賴..."
    composer install
fi

if [ ! -d "node_modules" ]; then
    echo "📦 安裝 Node.js 依賴..."
    npm install
fi

# 生成應用程式金鑰（如果需要）
if [ ! -f ".env" ]; then
    echo "🔑 設定環境配置..."
    cp .env.example .env
    php artisan key:generate
fi

# 啟動 Laravel 開發服務器
php artisan serve --port=8000 &
LARAVEL_PID=$!

# 啟動 Vite 開發服務器
npm run dev &
VITE_PID=$!

cd ..

echo "✅ 開發環境啟動完成！"
echo ""
echo "📱 應用程式訪問地址："
echo "   前端應用: http://localhost:8000"
echo "   Java API: http://localhost:8080"
echo "   API 健康檢查: http://localhost:8080/api/health"
echo ""
echo "🔧 開發工具："
echo "   H2 控制台: http://localhost:8080/h2-console"
echo "   Vite 開發服務器: http://localhost:5173"
echo ""
echo "⚠️  按 Ctrl+C 停止所有服務"

# 等待用戶中斷
trap "echo '🛑 停止所有服務...'; kill $JAVA_PID $LARAVEL_PID $VITE_PID 2>/dev/null; exit" INT

# 保持腳本運行
wait

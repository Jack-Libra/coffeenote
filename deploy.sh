#!/bin/bash

# Coffee Journal 部署腳本
# 用於快速部署整個應用程式

set -e

echo "🚀 開始部署 Coffee Journal 應用..."

# Docker 部署已暫時停用
echo "⚠️  Docker 部署功能已暫時停用"
echo ""
echo "請使用以下方式啟動開發環境："
echo ""
echo "🛠️ 本地開發環境："
echo "   make dev          # 啟動完整開發環境"
echo "   make dev-java     # 僅啟動 Java 後端"
echo "   make dev-laravel  # 僅啟動 Laravel 前端"
echo ""
echo "🧪 測試："
echo "   make test         # 執行所有測試"
echo ""
echo "📚 更多指令："
echo "   make help         # 查看所有可用指令"

# # 檢查 Docker 是否安裝
# if ! command -v docker &> /dev/null; then
#     echo "❌ Docker 未安裝，請先安裝 Docker"
#     exit 1
# fi

# if ! command -v docker-compose &> /dev/null; then
#     echo "❌ Docker Compose 未安裝，請先安裝 Docker Compose"
#     exit 1
# fi

# # 進入 docker 目錄
# cd docker

# echo "📦 拉取最新的 Docker 映像..."
# docker-compose pull

# echo "🏗️ 建構應用程式映像..."
# docker-compose build

# echo "🗄️ 啟動資料庫..."
# docker-compose up -d postgres

# echo "⏳ 等待資料庫啟動..."
# sleep 10

# echo "🚀 啟動所有服務..."
# docker-compose up -d

# echo "🔍 檢查服務狀態..."
# docker-compose ps

# echo "✅ 部署完成！"
# echo ""
# echo "📱 應用程式訪問地址："
# echo "   前端應用: http://localhost:8000"
# echo "   Java API: http://localhost:8080"
# echo "   API 文檔: http://localhost:8080/swagger-ui.html"
# echo "   H2 控制台: http://localhost:8080/h2-console"
# echo ""
# echo "🔧 管理命令："
# echo "   查看日誌: docker-compose logs -f"
# echo "   停止服務: docker-compose down"
# echo "   重啟服務: docker-compose restart"
# echo ""
# echo "🎉 Coffee Journal 已成功部署！"

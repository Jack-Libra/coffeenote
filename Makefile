# Coffee Journal - Makefile
# 常用指令集合，簡化開發和部署流程

.PHONY: help build up down restart logs clean test dev prod status health

# 預設目標
.DEFAULT_GOAL := help

# 顏色定義
YELLOW := \033[33m
GREEN := \033[32m
RED := \033[31m
BLUE := \033[34m
RESET := \033[0m

# Docker Compose 文件路徑
DOCKER_COMPOSE_FILE := docker/docker-compose.yml
DOCKER_COMPOSE := docker-compose -f $(DOCKER_COMPOSE_FILE)

## 顯示幫助信息
help:
	@echo "$(BLUE)☕ Coffee Journal - 開發指令$(RESET)"
	@echo ""
	@echo "$(YELLOW)🚀 部署指令:$(RESET)"
	@echo "  make build     - 建構所有 Docker 映像"
	@echo "  make up        - 啟動所有服務"
	@echo "  make down      - 停止所有服務"
	@echo "  make restart   - 重啟所有服務"
	@echo "  make prod      - 生產環境部署"
	@echo ""
	@echo "$(YELLOW)🛠️ 開發指令:$(RESET)"
	@echo "  make dev       - 啟動開發環境"
	@echo "  make dev-java  - 僅啟動 Java 後端"
	@echo "  make dev-laravel - 僅啟動 Laravel 前端"
	@echo "  make install   - 安裝所有依賴"
	@echo ""
	@echo "$(YELLOW)📊 監控指令:$(RESET)"
	@echo "  make logs      - 查看所有服務日誌"
	@echo "  make status    - 查看服務狀態"
	@echo "  make health    - 檢查服務健康狀態"
	@echo ""
	@echo "$(YELLOW)🧪 測試指令:$(RESET)"
	@echo "  make test      - 執行所有測試"
	@echo "  make test-java - 執行 Java 測試"
	@echo "  make test-laravel - 執行 Laravel 測試"
	@echo ""
	@echo "$(YELLOW)🧹 清理指令:$(RESET)"
	@echo "  make clean     - 清理 Docker 資源"
	@echo "  make clean-all - 深度清理（包含 volumes）"

## 建構所有 Docker 映像
build:
	@echo "$(GREEN)🏗️ 建構 Docker 映像...$(RESET)"
	$(DOCKER_COMPOSE) build --no-cache

## 啟動所有服務
up:
	@echo "$(GREEN)🚀 啟動所有服務...$(RESET)"
	$(DOCKER_COMPOSE) up -d
	@echo "$(GREEN)✅ 服務已啟動$(RESET)"
	@echo "$(BLUE)📱 訪問地址:$(RESET)"
	@echo "  前端: http://localhost:8000"
	@echo "  Java API: http://localhost:8080"
	@echo "  健康檢查: http://localhost:8080/api/health"

## 停止所有服務
down:
	@echo "$(YELLOW)🛑 停止所有服務...$(RESET)"
	$(DOCKER_COMPOSE) down

## 重啟所有服務
restart: down up

## 查看服務日誌
logs:
	@echo "$(BLUE)📋 查看服務日誌...$(RESET)"
	$(DOCKER_COMPOSE) logs -f

## 查看特定服務日誌
logs-java:
	$(DOCKER_COMPOSE) logs -f backend-java

logs-laravel:
	$(DOCKER_COMPOSE) logs -f backend-laravel

logs-postgres:
	$(DOCKER_COMPOSE) logs -f postgres

## 查看服務狀態
status:
	@echo "$(BLUE)📊 服務狀態:$(RESET)"
	$(DOCKER_COMPOSE) ps

## 檢查服務健康狀態
health:
	@echo "$(BLUE)🏥 檢查服務健康狀態...$(RESET)"
	@echo "$(YELLOW)Laravel 前端:$(RESET)"
	@curl -s -o /dev/null -w "Status: %{http_code}\n" http://localhost:8000 || echo "❌ 服務未響應"
	@echo "$(YELLOW)Java 後端:$(RESET)"
	@curl -s -o /dev/null -w "Status: %{http_code}\n" http://localhost:8080/api/health || echo "❌ 服務未響應"
	@echo "$(YELLOW)PostgreSQL:$(RESET)"
	@$(DOCKER_COMPOSE) exec postgres pg_isready -U postgres || echo "❌ 資料庫未就緒"

## 開發環境
dev:
	@echo "$(GREEN)🛠️ 啟動開發環境...$(RESET)"
	./dev-start.sh

## 僅啟動 Java 後端開發
dev-java:
	@echo "$(GREEN)☕ 啟動 Java 後端開發環境...$(RESET)"
	cd backend-java && ./gradlew bootRun

## 僅啟動 Laravel 前端開發
dev-laravel:
	@echo "$(GREEN)🎨 啟動 Laravel 前端開發環境...$(RESET)"
	cd backend-laravel && php artisan serve --port=8000 &
	cd backend-laravel && npm run dev

## 安裝所有依賴
install:
	@echo "$(GREEN)📦 安裝依賴...$(RESET)"
	@echo "$(YELLOW)安裝 Laravel 依賴...$(RESET)"
	cd backend-laravel && composer install && npm install
	@echo "$(YELLOW)安裝 Java 依賴...$(RESET)"
	cd backend-java && ./gradlew build -x test

## 生產環境部署
prod:
	@echo "$(GREEN)🚀 生產環境部署...$(RESET)"
	$(DOCKER_COMPOSE) -f docker/docker-compose.prod.yml up -d --build

## 執行所有測試
test:
	@echo "$(GREEN)🧪 執行所有測試...$(RESET)"
	make test-java
	make test-laravel

## 執行 Java 測試
test-java:
	@echo "$(YELLOW)☕ 執行 Java 測試...$(RESET)"
	cd backend-java && ./gradlew test

## 執行 Laravel 測試
test-laravel:
	@echo "$(YELLOW)🎨 執行 Laravel 測試...$(RESET)"
	cd backend-laravel && php artisan test

## 整合測試
test-integration:
	@echo "$(GREEN)🔗 執行整合測試...$(RESET)"
	./test-integration.sh

## 清理 Docker 資源
clean:
	@echo "$(YELLOW)🧹 清理 Docker 資源...$(RESET)"
	$(DOCKER_COMPOSE) down --rmi local --volumes --remove-orphans
	docker system prune -f

## 深度清理
clean-all:
	@echo "$(RED)🗑️ 深度清理（包含 volumes）...$(RESET)"
	$(DOCKER_COMPOSE) down --rmi all --volumes --remove-orphans
	docker system prune -a -f --volumes

## 進入容器 shell
shell-java:
	$(DOCKER_COMPOSE) exec backend-java bash

shell-laravel:
	$(DOCKER_COMPOSE) exec backend-laravel bash

shell-postgres:
	$(DOCKER_COMPOSE) exec postgres psql -U postgres -d coffeenote

## 備份資料庫
backup-db:
	@echo "$(GREEN)💾 備份資料庫...$(RESET)"
	$(DOCKER_COMPOSE) exec postgres pg_dump -U postgres coffeenote > backup_$(shell date +%Y%m%d_%H%M%S).sql

## 還原資料庫
restore-db:
	@echo "$(YELLOW)📥 還原資料庫...$(RESET)"
	@read -p "請輸入備份文件名: " file; \
	$(DOCKER_COMPOSE) exec -T postgres psql -U postgres coffeenote < $$file

## 更新依賴
update:
	@echo "$(GREEN)🔄 更新依賴...$(RESET)"
	cd backend-laravel && composer update && npm update
	cd backend-java && ./gradlew dependencies --refresh-dependencies

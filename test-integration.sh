#!/bin/bash

# Coffee Journal 整合測試腳本
# 測試 Laravel JWT 簽發 → Java 後端驗證的完整流程

set -e

echo "🧪 開始 Coffee Journal 整合測試..."

# 檢查服務是否運行
echo "📡 檢查服務狀態..."

# 檢查 Laravel 前端
if curl -s http://localhost:8000 > /dev/null; then
    echo "✅ Laravel 前端服務正常 (http://localhost:8000)"
else
    echo "❌ Laravel 前端服務未運行，請先啟動"
    exit 1
fi

# 檢查 Java 後端
if curl -s http://localhost:8080/api/health > /dev/null; then
    echo "✅ Java 後端服務正常 (http://localhost:8080)"
else
    echo "❌ Java 後端服務未運行，請先啟動"
    exit 1
fi

echo ""
echo "🔧 測試建議："
echo "1. 在瀏覽器中訪問 http://localhost:8000"
echo "2. 登入或註冊帳號"
echo "3. 前往 /notes 頁面"
echo "4. 點擊 '測試 JWT' 按鈕"
echo "5. 檢查瀏覽器控制台的輸出"
echo ""
echo "📋 預期的測試流程："
echo "   Laravel 認證 → JWT 簽發 → Java 後端驗證 → 筆記 CRUD"
echo ""
echo "🎯 如果一切正常，您應該能夠："
echo "   - 在 Laravel 中登入"
echo "   - 自動獲取 JWT Token"
echo "   - 使用 JWT 調用 Java API"
echo "   - 進行筆記的增刪改查操作"
echo ""
echo "✨ 測試完成！請按照上述步驟進行手動測試。"

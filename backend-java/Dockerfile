# 使用 OpenJDK 17 作為基礎映像
FROM openjdk:17-jdk-slim

# 設定工作目錄
WORKDIR /app

# 安裝必要的工具
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 複製 Gradle 包裝器和建構檔案
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 賦予 gradlew 執行權限
RUN chmod +x ./gradlew

# 複製源碼
COPY src src

# 建構應用程式
RUN ./gradlew build -x test

# 暴露端口
EXPOSE 8080

# 設定健康檢查端點
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# 啟動應用程式
CMD ["java", "-jar", "build/libs/coffeenote-api-0.0.1-SNAPSHOT.jar"]

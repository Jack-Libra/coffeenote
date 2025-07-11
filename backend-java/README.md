# ☕ Java 後端服務

Coffee Journal 的後端 API 服務，負責咖啡筆記的資料管理和業務邏輯處理。

## 🏗️ 技術架構

### 核心技術棧
- **Spring Boot 3**: Java 企業級應用框架
- **Spring Security**: 安全框架，JWT 認證
- **Spring Data JPA**: 資料持久化框架
- **PostgreSQL**: 關聯式資料庫（生產環境）
- **H2 Database**: 記憶體資料庫（開發環境）
- **Gradle**: 建構工具
- **JWT**: 無狀態認證機制

### 主要職責
- 📝 **筆記管理**: 咖啡筆記的 CRUD 操作
- 🔐 **JWT 驗證**: 驗證 Laravel 簽發的 Token
- 👤 **使用者隔離**: 確保資料安全性
- 📊 **統計分析**: 提供筆記統計功能
- 🔍 **搜尋功能**: 支援多條件搜尋

## 📁 專案結構

```
backend-java/
├── src/main/java/com/coffeenote/api/
│   ├── CoffeenoteApiApplication.java    # 主應用程式
│   ├── config/
│   │   └── SecurityConfig.java          # 安全配置
│   ├── controller/
│   │   ├── AuthController.java          # 認證控制器
│   │   ├── HealthController.java        # 健康檢查
│   │   └── NoteController.java          # 筆記 API
│   ├── model/
│   │   └── Note.java                    # 筆記實體類
│   ├── repository/
│   │   └── NoteRepository.java          # 資料存取層
│   ├── security/
│   │   └── JwtAuthenticationFilter.java # JWT 過濾器
│   ├── service/
│   │   └── NoteService.java             # 業務邏輯層
│   └── util/
│       └── JwtUtil.java                 # JWT 工具類
├── src/main/resources/
│   ├── application.properties           # 應用配置
│   └── application-docker.properties    # Docker 環境配置
├── src/test/java/                       # 測試代碼
├── build.gradle                         # Gradle 建構配置
├── pom.xml                             # Maven 配置（備用）
├── Dockerfile                          # Docker 配置
└── docs/
    └── jwt-flow.md                     # JWT 認證流程文檔
```

## 🚀 開發環境設置

### 前置需求
- Java 17+
- Gradle 7.0+ 或使用內建的 Gradle Wrapper

### 快速啟動

1. **克隆專案**
   ```bash
   cd backend-java
   ```

2. **執行應用程式**
   ```bash
   # 使用 Gradle Wrapper（推薦）
   ./gradlew bootRun
   
   # 或使用系統 Gradle
   gradle bootRun
   ```

3. **訪問應用**
   - API 基礎路徑: http://localhost:8080
   - 健康檢查: http://localhost:8080/api/health
   - H2 控制台: http://localhost:8080/h2-console

### 建構和測試

```bash
# 建構專案
./gradlew build

# 執行測試
./gradlew test

# 生成 JAR 文件
./gradlew bootJar

# 清理建構文件
./gradlew clean
```

## ⚙️ 配置說明

### 開發環境配置 (application.properties)
```properties
# 伺服器配置
server.port=8080

# H2 資料庫（開發環境）
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true

# JWT 配置（必須與 Laravel 一致）
jwt.secret=mySecretKey12345678901234567890123456789012345678901234567890
jwt.expiration=86400000

# JPA 配置
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### 生產環境配置 (application-docker.properties)
```properties
# PostgreSQL 資料庫
spring.datasource.url=jdbc:postgresql://postgres:5432/coffeenote
spring.datasource.username=postgres
spring.datasource.password=postgres123

# 生產環境設定
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
logging.level.com.coffeenote.api=INFO
```

## 📡 API 文檔

### 認證端點
| 方法 | 端點 | 說明 | 認證 |
|------|------|------|------|
| POST | `/api/auth/login` | 測試登入 | ❌ |
| GET | `/api/auth/validate` | 驗證 Token | ✅ |
| POST | `/api/auth/refresh` | 刷新 Token | ✅ |

### 筆記管理端點
| 方法 | 端點 | 說明 | 認證 |
|------|------|------|------|
| GET | `/api/notes` | 獲取筆記列表 | ✅ |
| POST | `/api/notes` | 創建筆記 | ✅ |
| GET | `/api/notes/{id}` | 獲取特定筆記 | ✅ |
| PUT | `/api/notes/{id}` | 更新筆記 | ✅ |
| DELETE | `/api/notes/{id}` | 刪除筆記 | ✅ |
| GET | `/api/notes/search` | 搜尋筆記 | ✅ |
| GET | `/api/notes/stats` | 獲取統計資訊 | ✅ |

### 系統端點
| 方法 | 端點 | 說明 | 認證 |
|------|------|------|------|
| GET | `/api/health` | 健康檢查 | ❌ |
| GET | `/api/ping` | 簡單測試 | ❌ |

## 🔐 JWT 認證機制

### Token 驗證流程
1. 前端從 Laravel 獲取 JWT Token
2. 請求時在 Header 中攜帶 Token
3. `JwtAuthenticationFilter` 攔截請求
4. `JwtUtil` 驗證 Token 有效性
5. 設定 Spring Security 認證上下文

### 使用者隔離
- 每個 API 請求都會驗證使用者身份
- 筆記資料按 `userId` 進行隔離
- 確保使用者只能存取自己的資料

## 🗄️ 資料模型

### Note 實體
```java
@Entity
@Table(name = "coffee_notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "bean_name", nullable = false)
    private String beanName;
    
    @Column(name = "origin")
    private String origin;
    
    @Column(name = "roast_level")
    private String roastLevel;
    
    @Column(name = "flavor_notes", columnDefinition = "TEXT")
    private String flavorNotes;
    
    @Column(name = "rating")
    private Integer rating;
    
    @Column(name = "brewing_method")
    private String brewingMethod;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    // 時間戳記由 JPA 自動管理
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

## 🧪 測試

### 單元測試
```bash
# 執行所有測試
./gradlew test

# 執行特定測試類
./gradlew test --tests NoteControllerTest

# 生成測試報告
./gradlew test jacocoTestReport
```

### 整合測試
```bash
# 執行整合測試
./gradlew integrationTest

# 測試特定功能
./gradlew test --tests "*IntegrationTest"
```

## 🐳 Docker 部署

### 建構 Docker 映像
```bash
# 建構映像
docker build -t coffeenote-java-api .

# 執行容器
docker run -p 8080:8080 coffeenote-java-api
```

### 使用 Docker Compose
```bash
# 啟動所有服務
cd ../docker
docker-compose up -d

# 查看 Java 服務日誌
docker-compose logs -f backend-java
```

## 🔧 開發工具

### IDE 設定
推薦使用 IntelliJ IDEA 或 VS Code 進行開發：

**IntelliJ IDEA**:
- 安裝 Spring Boot 插件
- 配置 Gradle 自動導入
- 啟用 Lombok 支援

**VS Code**:
- 安裝 Java Extension Pack
- 安裝 Spring Boot Extension Pack

### 程式碼品質
```bash
# 程式碼格式化
./gradlew spotlessApply

# 程式碼檢查
./gradlew checkstyleMain

# 安全性掃描
./gradlew dependencyCheckAnalyze
```

## 🐛 常見問題

### JWT Token 驗證失敗
- 檢查 `jwt.secret` 是否與 Laravel 一致
- 確認 Token 格式正確（Bearer + 空格 + Token）
- 檢查 Token 是否過期

### 資料庫連接問題
- 確認資料庫服務是否啟動
- 檢查連接字串和認證資訊
- 查看應用程式日誌獲取詳細錯誤

### CORS 問題
- 檢查 `SecurityConfig` 中的 CORS 設定
- 確認前端域名在允許清單中

## 📚 相關文檔

- [JWT 認證流程](docs/jwt-flow.md)
- [Spring Boot 官方文檔](https://spring.io/projects/spring-boot)
- [Spring Security 文檔](https://spring.io/projects/spring-security)
- [Spring Data JPA 文檔](https://spring.io/projects/spring-data-jpa)

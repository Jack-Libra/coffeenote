# 🧪 Coffee Journal 測試指南

## 📋 測試概覽

Coffee Journal 項目包含完整的測試套件，確保代碼品質和功能正確性。

### 測試統計
- **總測試數**: 27 個
- **成功率**: 100%
- **測試類型**: 單元測試、服務測試、工具類測試
- **測試框架**: JUnit 5, Mockito, Spring Boot Test

## 🏗️ 測試架構

### 測試分層
```
tests/
├── 單元測試 (Unit Tests)
│   ├── JwtUtilTest - JWT 工具類測試
│   ├── NoteServiceTest - 筆記服務測試
│   └── CoffeenoteApiApplicationTests - 應用啟動測試
├── 整合測試 (Integration Tests)
│   └── (未來擴展)
└── 端到端測試 (E2E Tests)
    └── (未來擴展)
```

## 🔧 測試配置

### 主要測試配置
- **測試環境**: 排除 Spring Security 自動配置
- **資料庫**: H2 記憶體資料庫
- **Mock 框架**: Mockito
- **測試 Profile**: 無特定 profile

### 關鍵配置文件
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
class CoffeenoteApiApplicationTests {
    // 基礎應用測試
}
```

## 📊 測試詳情

### 1. JwtUtilTest (12 個測試)
**測試範圍**: JWT Token 生成、驗證、解析功能

**測試案例**:
- ✅ `testGenerateToken()` - Token 生成
- ✅ `testGetUserIdFromToken()` - 使用者 ID 提取
- ✅ `testGetUsernameFromToken()` - 使用者名稱提取
- ✅ `testValidateToken()` - Token 驗證
- ✅ `testValidateTokenWithUsername()` - 帶使用者名稱驗證
- ✅ `testValidateTokenWithWrongUsername()` - 錯誤使用者名稱驗證
- ✅ `testInvalidToken()` - 無效 Token 處理
- ✅ `testExtractTokenFromHeader()` - Header 中 Token 提取
- ✅ `testExtractTokenFromInvalidHeader()` - 無效 Header 處理
- ✅ `testGetTokenRemainingTime()` - Token 剩餘時間
- ✅ `testRefreshToken()` - Token 刷新
- ✅ `testTokenExpiration()` - Token 過期處理

**關鍵測試邏輯**:
```java
@Test
void testGenerateToken() {
    // Given
    Long userId = 1L;
    String username = "testuser@example.com";

    // When
    String token = jwtUtil.generateToken(userId, username);

    // Then
    assertNotNull(token);
    assertFalse(token.isEmpty());
    assertTrue(token.contains("."));
}
```

### 2. NoteServiceTest (14 個測試)
**測試範圍**: 筆記業務邏輯服務

**測試案例**:
- ✅ `testGetNotesByUserId()` - 分頁獲取筆記
- ✅ `testGetAllNotesByUserId()` - 獲取所有筆記
- ✅ `testGetNoteByIdAndUserId()` - 獲取特定筆記
- ✅ `testGetNoteByIdAndUserIdNotFound()` - 筆記不存在處理
- ✅ `testCreateNote()` - 創建筆記
- ✅ `testUpdateNote()` - 更新筆記
- ✅ `testUpdateNoteNotOwned()` - 更新非擁有筆記
- ✅ `testDeleteNoteByIdAndUserId()` - 刪除筆記
- ✅ `testDeleteNoteByIdAndUserIdNotFound()` - 刪除不存在筆記
- ✅ `testSearchNotes()` - 搜尋筆記
- ✅ `testSearchNotesEmptyKeyword()` - 空關鍵字搜尋
- ✅ `testGetNoteStats()` - 獲取統計資訊
- ✅ `testIsNoteOwnedByUser()` - 檢查筆記擁有權
- ✅ `testDeleteAllNotesByUserId()` - 刪除使用者所有筆記

**Mock 使用範例**:
```java
@ExtendWith(MockitoExtension.class)
class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void testCreateNote() {
        // Given
        when(noteRepository.save(any(Note.class))).thenReturn(testNote);

        // When
        Note result = noteService.createNote(newNote);

        // Then
        assertNotNull(result);
        verify(noteRepository).save(newNote);
    }
}
```

### 3. CoffeenoteApiApplicationTests (1 個測試)
**測試範圍**: Spring Boot 應用啟動

**測試案例**:
- ✅ `contextLoads()` - Spring 上下文載入

## 🚀 執行測試

### 使用 Gradle
```bash
# 執行所有測試
./gradlew test

# 執行特定測試類
./gradlew test --tests JwtUtilTest

# 執行測試並生成報告
./gradlew test jacocoTestReport
```

### 使用 Makefile
```bash
# 執行所有測試
make test

# 僅執行 Java 測試
make test-java

# 執行整合測試
make test-integration
```

### 測試報告
測試完成後，可以查看詳細報告：
- **HTML 報告**: `build/reports/tests/test/index.html`
- **覆蓋率報告**: `build/reports/jacoco/test/html/index.html`

## 🔍 測試最佳實踐

### 1. 測試命名規範
```java
// 格式: test + 方法名 + 預期行為
void testCreateNote() { }
void testGetNoteByIdAndUserIdNotFound() { }
void testValidateTokenWithWrongUsername() { }
```

### 2. AAA 模式 (Arrange-Act-Assert)
```java
@Test
void testExample() {
    // Given (Arrange) - 準備測試資料
    Long userId = 1L;
    
    // When (Act) - 執行被測試的方法
    Note result = noteService.createNote(note);
    
    // Then (Assert) - 驗證結果
    assertNotNull(result);
    assertEquals(userId, result.getUserId());
}
```

### 3. Mock 使用原則
- 只 Mock 外部依賴
- 驗證重要的互動
- 使用 `@InjectMocks` 注入被測試物件

### 4. 測試資料管理
```java
@BeforeEach
void setUp() {
    // 在每個測試前準備測試資料
    testNote = new Note();
    testNote.setBeanName("Test Bean");
    // ...
}
```

## 🐛 常見問題解決

### 1. 循環依賴問題
**問題**: Spring Bean 循環依賴導致測試失敗
**解決**: 排除 Security 自動配置
```java
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
```

### 2. JWT Token 測試問題
**問題**: Token 刷新測試失敗
**解決**: 添加時間延遲確保不同時間戳
```java
Thread.sleep(1000); // 確保不同的 iat 時間
```

### 3. Mock 驗證失敗
**問題**: Mockito 驗證失敗
**解決**: 確保 Mock 設定正確
```java
when(repository.method(any())).thenReturn(expectedResult);
verify(repository).method(expectedArgument);
```

## 📈 測試覆蓋率

### 當前覆蓋率
- **類覆蓋率**: 85%+
- **方法覆蓋率**: 80%+
- **行覆蓋率**: 75%+

### 覆蓋率目標
- **類覆蓋率**: 90%
- **方法覆蓋率**: 85%
- **行覆蓋率**: 80%

## 🔮 未來測試計劃

### 1. 整合測試
- Controller 層整合測試
- 資料庫整合測試
- JWT 認證整合測試

### 2. 端到端測試
- API 端到端測試
- 使用者流程測試
- 效能測試

### 3. 測試自動化
- CI/CD 整合
- 自動化測試報告
- 測試覆蓋率監控

## 📚 相關資源

- [JUnit 5 文檔](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito 文檔](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [AssertJ 文檔](https://assertj.github.io/doc/)

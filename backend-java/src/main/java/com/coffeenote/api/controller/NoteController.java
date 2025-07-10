package com.coffeenote.api.controller;

import com.coffeenote.api.model.Note;
import com.coffeenote.api.service.NoteService;
import com.coffeenote.api.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 咖啡筆記 REST API 控制器
 * 
 * 這個控制器處理所有與咖啡筆記相關的 HTTP 請求，
 * 包括 CRUD 操作、搜尋、統計等功能。
 * 
 * 所有的 API 端點都需要 JWT 認證，確保使用者只能存取自己的筆記。
 * 
 * @author Coffee Journal Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*") // 允許跨域請求，生產環境應該限制特定域名
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 獲取當前使用者的所有筆記
     * 支援分頁和排序
     * 
     * GET /api/notes?page=0&size=10&sort=createdAt,desc
     * 
     * @param page 頁碼（從 0 開始）
     * @param size 每頁筆記數量
     * @param sort 排序欄位和方向
     * @param authorization JWT Token（從 Header 中獲取）
     * @return 分頁的筆記列表
     */
    @GetMapping
    public ResponseEntity<?> getAllNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestHeader("Authorization") String authorization) {
        
        try {
            // 從 JWT Token 中提取使用者 ID
            Long userId = getUserIdFromToken(authorization);
            
            // 解析排序參數
            String[] sortParams = sort.split(",");
            Sort.Direction direction = sortParams.length > 1 && 
                sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
            
            // 獲取分頁筆記
            Page<Note> notes = noteService.getNotesByUserId(userId, pageable);
            
            return ResponseEntity.ok(notes);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "認證失敗", "message", e.getMessage()));
        }
    }
    
    /**
     * 根據 ID 獲取特定筆記
     * 
     * GET /api/notes/{id}
     * 
     * @param id 筆記 ID
     * @param authorization JWT Token
     * @return 筆記詳細資訊
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getNoteById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization) {
        
        try {
            Long userId = getUserIdFromToken(authorization);
            Note note = noteService.getNoteByIdAndUserId(id, userId);
            
            if (note != null) {
                return ResponseEntity.ok(note);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "認證失敗", "message", e.getMessage()));
        }
    }
    
    /**
     * 創建新的咖啡筆記
     * 
     * POST /api/notes
     * Content-Type: application/json
     * 
     * @param note 筆記資料（JSON 格式）
     * @param authorization JWT Token
     * @return 創建成功的筆記
     */
    @PostMapping
    public ResponseEntity<?> createNote(
            @Valid @RequestBody Note note,
            @RequestHeader("Authorization") String authorization) {
        
        try {
            Long userId = getUserIdFromToken(authorization);
            
            // 設定筆記的使用者 ID
            note.setUserId(userId);
            
            // 創建筆記
            Note createdNote = noteService.createNote(note);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "認證失敗", "message", e.getMessage()));
        }
    }
    
    /**
     * 更新現有的咖啡筆記
     * 
     * PUT /api/notes/{id}
     * Content-Type: application/json
     * 
     * @param id 筆記 ID
     * @param note 更新的筆記資料
     * @param authorization JWT Token
     * @return 更新後的筆記
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody Note note,
            @RequestHeader("Authorization") String authorization) {
        
        try {
            Long userId = getUserIdFromToken(authorization);
            
            // 設定筆記 ID 和使用者 ID
            note.setId(id);
            note.setUserId(userId);
            
            // 更新筆記
            Note updatedNote = noteService.updateNote(note);
            
            if (updatedNote != null) {
                return ResponseEntity.ok(updatedNote);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "認證失敗", "message", e.getMessage()));
        }
    }
    
    /**
     * 刪除咖啡筆記
     * 
     * DELETE /api/notes/{id}
     * 
     * @param id 筆記 ID
     * @param authorization JWT Token
     * @return 刪除結果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization) {
        
        try {
            Long userId = getUserIdFromToken(authorization);
            
            boolean deleted = noteService.deleteNoteByIdAndUserId(id, userId);
            
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "筆記已成功刪除"));
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "認證失敗", "message", e.getMessage()));
        }
    }
    
    /**
     * 搜尋咖啡筆記
     * 
     * GET /api/notes/search?keyword=關鍵字
     * 
     * @param keyword 搜尋關鍵字
     * @param authorization JWT Token
     * @return 符合條件的筆記列表
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchNotes(
            @RequestParam String keyword,
            @RequestHeader("Authorization") String authorization) {
        
        try {
            Long userId = getUserIdFromToken(authorization);
            List<Note> notes = noteService.searchNotes(userId, keyword);
            
            return ResponseEntity.ok(notes);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "認證失敗", "message", e.getMessage()));
        }
    }
    
    /**
     * 獲取使用者的筆記統計資訊
     * 
     * GET /api/notes/stats
     * 
     * @param authorization JWT Token
     * @return 統計資訊（總數、平均評分等）
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getNoteStats(
            @RequestHeader("Authorization") String authorization) {
        
        try {
            Long userId = getUserIdFromToken(authorization);
            Map<String, Object> stats = noteService.getNoteStats(userId);
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "認證失敗", "message", e.getMessage()));
        }
    }
    
    /**
     * 從 JWT Token 中提取使用者 ID
     * 
     * @param authorization Authorization Header 的值
     * @return 使用者 ID
     * @throws Exception 如果 Token 無效或過期
     */
    private Long getUserIdFromToken(String authorization) throws Exception {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new Exception("無效的 Authorization Header");
        }
        
        String token = authorization.substring(7); // 移除 "Bearer " 前綴
        return jwtUtil.getUserIdFromToken(token);
    }
}

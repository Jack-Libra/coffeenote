package com.coffeenote.api.service;

import com.coffeenote.api.model.Note;
import com.coffeenote.api.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 咖啡筆記業務邏輯服務類
 * 
 * 這個服務類包含所有與咖啡筆記相關的業務邏輯，
 * 作為控制器和資料存取層之間的中介層。
 * 
 * 使用 @Transactional 註解確保資料庫操作的一致性。
 * 
 * @author Coffee Journal Team
 * @version 1.0
 */
@Service
@Transactional
public class NoteService {
    
    @Autowired
    private NoteRepository noteRepository;
    
    /**
     * 根據使用者 ID 獲取所有筆記（支援分頁）
     * 
     * @param userId 使用者 ID
     * @param pageable 分頁參數
     * @return 分頁的筆記列表
     */
    @Transactional(readOnly = true)
    public Page<Note> getNotesByUserId(Long userId, Pageable pageable) {
        return noteRepository.findByUserId(userId, pageable);
    }
    
    /**
     * 根據使用者 ID 獲取所有筆記（不分頁）
     * 
     * @param userId 使用者 ID
     * @return 筆記列表
     */
    @Transactional(readOnly = true)
    public List<Note> getAllNotesByUserId(Long userId) {
        return noteRepository.findByUserId(userId);
    }
    
    /**
     * 根據筆記 ID 和使用者 ID 獲取特定筆記
     * 確保使用者只能存取自己的筆記
     * 
     * @param id 筆記 ID
     * @param userId 使用者 ID
     * @return 筆記物件，如果不存在則返回 null
     */
    @Transactional(readOnly = true)
    public Note getNoteByIdAndUserId(Long id, Long userId) {
        Optional<Note> note = noteRepository.findByIdAndUserId(id, userId);
        return note.orElse(null);
    }
    
    /**
     * 創建新的咖啡筆記
     * 
     * @param note 筆記物件
     * @return 創建成功的筆記（包含生成的 ID 和時間戳）
     */
    public Note createNote(Note note) {
        // 確保 ID 為 null，讓資料庫自動生成
        note.setId(null);
        return noteRepository.save(note);
    }
    
    /**
     * 更新現有的咖啡筆記
     * 只有筆記的擁有者才能更新
     * 
     * @param note 包含更新資料的筆記物件
     * @return 更新後的筆記，如果筆記不存在或不屬於該使用者則返回 null
     */
    public Note updateNote(Note note) {
        // 檢查筆記是否存在且屬於該使用者
        if (noteRepository.existsByIdAndUserId(note.getId(), note.getUserId())) {
            return noteRepository.save(note);
        }
        return null;
    }
    
    /**
     * 根據筆記 ID 和使用者 ID 刪除筆記
     * 確保使用者只能刪除自己的筆記
     * 
     * @param id 筆記 ID
     * @param userId 使用者 ID
     * @return 如果成功刪除則返回 true，否則返回 false
     */
    public boolean deleteNoteByIdAndUserId(Long id, Long userId) {
        Optional<Note> note = noteRepository.findByIdAndUserId(id, userId);
        if (note.isPresent()) {
            noteRepository.delete(note.get());
            return true;
        }
        return false;
    }
    
    /**
     * 搜尋咖啡筆記
     * 在咖啡豆名稱、產地、風味描述中搜尋關鍵字
     * 
     * @param userId 使用者 ID
     * @param keyword 搜尋關鍵字
     * @return 符合條件的筆記列表
     */
    @Transactional(readOnly = true)
    public List<Note> searchNotes(Long userId, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return noteRepository.findByUserId(userId);
        }
        return noteRepository.searchNotes(userId, keyword.trim());
    }
    
    /**
     * 根據咖啡豆名稱搜尋筆記
     * 
     * @param userId 使用者 ID
     * @param beanName 咖啡豆名稱（支援部分匹配）
     * @return 符合條件的筆記列表
     */
    @Transactional(readOnly = true)
    public List<Note> findNotesByBeanName(Long userId, String beanName) {
        return noteRepository.findByUserIdAndBeanNameContainingIgnoreCase(userId, beanName);
    }
    
    /**
     * 根據評分搜尋筆記
     * 
     * @param userId 使用者 ID
     * @param rating 評分
     * @return 符合條件的筆記列表
     */
    @Transactional(readOnly = true)
    public List<Note> findNotesByRating(Long userId, Integer rating) {
        return noteRepository.findByUserIdAndRating(userId, rating);
    }
    
    /**
     * 根據烘焙程度搜尋筆記
     * 
     * @param userId 使用者 ID
     * @param roastLevel 烘焙程度
     * @return 符合條件的筆記列表
     */
    @Transactional(readOnly = true)
    public List<Note> findNotesByRoastLevel(Long userId, String roastLevel) {
        return noteRepository.findByUserIdAndRoastLevelContainingIgnoreCase(userId, roastLevel);
    }
    
    /**
     * 獲取使用者的筆記統計資訊
     * 
     * @param userId 使用者 ID
     * @return 包含統計資訊的 Map
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getNoteStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 總筆記數
        long totalNotes = noteRepository.countByUserId(userId);
        stats.put("totalNotes", totalNotes);
        
        // 平均評分
        Double averageRating = noteRepository.findAverageRatingByUserId(userId);
        stats.put("averageRating", averageRating != null ? Math.round(averageRating * 100.0) / 100.0 : null);
        
        // 各評分的筆記數量
        Map<Integer, Long> ratingCounts = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            long count = noteRepository.countByUserIdAndRating(userId, i);
            ratingCounts.put(i, count);
        }
        stats.put("ratingCounts", ratingCounts);
        
        // 最高評分筆記數
        long highRatedNotes = noteRepository.countByUserIdAndRating(userId, 5);
        stats.put("highRatedNotes", highRatedNotes);
        
        // 最低評分筆記數
        long lowRatedNotes = noteRepository.countByUserIdAndRating(userId, 1);
        stats.put("lowRatedNotes", lowRatedNotes);
        
        return stats;
    }
    
    /**
     * 獲取使用者最近的筆記
     * 
     * @param userId 使用者 ID
     * @param limit 限制返回的筆記數量
     * @return 最近的筆記列表
     */
    @Transactional(readOnly = true)
    public List<Note> getRecentNotes(Long userId, int limit) {
        Pageable pageable = Pageable.ofSize(limit);
        return noteRepository.findRecentNotesByUserId(userId, pageable);
    }
    
    /**
     * 檢查筆記是否屬於指定使用者
     * 
     * @param noteId 筆記 ID
     * @param userId 使用者 ID
     * @return 如果筆記屬於該使用者則返回 true，否則返回 false
     */
    @Transactional(readOnly = true)
    public boolean isNoteOwnedByUser(Long noteId, Long userId) {
        return noteRepository.existsByIdAndUserId(noteId, userId);
    }
    
    /**
     * 刪除使用者的所有筆記
     * 通常在刪除使用者帳號時使用
     * 
     * @param userId 使用者 ID
     * @return 被刪除的筆記數量
     */
    public long deleteAllNotesByUserId(Long userId) {
        return noteRepository.deleteByUserId(userId);
    }
}

package com.coffeenote.api.repository;

import com.coffeenote.api.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 咖啡筆記資料存取層
 * 
 * 這個介面繼承自 JpaRepository，提供基本的 CRUD 操作，
 * 同時定義了一些自訂的查詢方法來滿足業務需求。
 * 
 * Spring Data JPA 會自動實現這些方法，無需手動編寫實現類。
 * 
 * @author Coffee Journal Team
 * @version 1.0
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    
    /**
     * 根據使用者 ID 查詢所有筆記
     * 
     * @param userId 使用者 ID
     * @return 該使用者的所有筆記列表
     */
    List<Note> findByUserId(Long userId);
    
    /**
     * 根據使用者 ID 查詢筆記，支援分頁
     * 
     * @param userId 使用者 ID
     * @param pageable 分頁參數
     * @return 分頁的筆記列表
     */
    Page<Note> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根據使用者 ID 和筆記 ID 查詢特定筆記
     * 這個方法確保使用者只能存取自己的筆記
     * 
     * @param id 筆記 ID
     * @param userId 使用者 ID
     * @return 符合條件的筆記，如果不存在則返回 Optional.empty()
     */
    Optional<Note> findByIdAndUserId(Long id, Long userId);
    
    /**
     * 根據使用者 ID 和咖啡豆名稱查詢筆記
     * 支援模糊搜尋（不區分大小寫）
     * 
     * @param userId 使用者 ID
     * @param beanName 咖啡豆名稱（支援部分匹配）
     * @return 符合條件的筆記列表
     */
    List<Note> findByUserIdAndBeanNameContainingIgnoreCase(Long userId, String beanName);
    
    /**
     * 根據使用者 ID 和評分查詢筆記
     * 
     * @param userId 使用者 ID
     * @param rating 評分
     * @return 符合條件的筆記列表
     */
    List<Note> findByUserIdAndRating(Long userId, Integer rating);
    
    /**
     * 根據使用者 ID 查詢評分大於等於指定值的筆記
     * 
     * @param userId 使用者 ID
     * @param minRating 最低評分
     * @return 符合條件的筆記列表
     */
    List<Note> findByUserIdAndRatingGreaterThanEqual(Long userId, Integer minRating);
    
    /**
     * 根據使用者 ID 和烘焙程度查詢筆記
     * 
     * @param userId 使用者 ID
     * @param roastLevel 烘焙程度
     * @return 符合條件的筆記列表
     */
    List<Note> findByUserIdAndRoastLevelContainingIgnoreCase(Long userId, String roastLevel);
    
    /**
     * 根據使用者 ID 查詢最近建立的筆記
     * 使用自訂 JPQL 查詢，按建立時間降序排列
     * 
     * @param userId 使用者 ID
     * @param limit 限制返回的筆記數量
     * @return 最近的筆記列表
     */
    @Query("SELECT n FROM Note n WHERE n.userId = :userId ORDER BY n.createdAt DESC")
    List<Note> findRecentNotesByUserId(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * 根據使用者 ID 和時間範圍查詢筆記
     * 
     * @param userId 使用者 ID
     * @param startDate 開始時間
     * @param endDate 結束時間
     * @return 符合條件的筆記列表
     */
    @Query("SELECT n FROM Note n WHERE n.userId = :userId AND n.createdAt BETWEEN :startDate AND :endDate ORDER BY n.createdAt DESC")
    List<Note> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                       @Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    /**
     * 統計使用者的筆記總數
     * 
     * @param userId 使用者 ID
     * @return 筆記總數
     */
    long countByUserId(Long userId);
    
    /**
     * 根據使用者 ID 和評分統計筆記數量
     * 
     * @param userId 使用者 ID
     * @param rating 評分
     * @return 符合條件的筆記數量
     */
    long countByUserIdAndRating(Long userId, Integer rating);
    
    /**
     * 查詢使用者的平均評分
     * 
     * @param userId 使用者 ID
     * @return 平均評分，如果沒有評分則返回 null
     */
    @Query("SELECT AVG(n.rating) FROM Note n WHERE n.userId = :userId AND n.rating IS NOT NULL")
    Double findAverageRatingByUserId(@Param("userId") Long userId);
    
    /**
     * 根據使用者 ID 刪除所有筆記
     * 這個方法通常在刪除使用者帳號時使用
     * 
     * @param userId 使用者 ID
     * @return 被刪除的筆記數量
     */
    long deleteByUserId(Long userId);
    
    /**
     * 檢查指定的筆記是否屬於指定的使用者
     * 
     * @param id 筆記 ID
     * @param userId 使用者 ID
     * @return 如果筆記存在且屬於該使用者則返回 true，否則返回 false
     */
    boolean existsByIdAndUserId(Long id, Long userId);
    
    /**
     * 全文搜尋筆記
     * 在咖啡豆名稱、產地、風味描述中搜尋關鍵字
     * 
     * @param userId 使用者 ID
     * @param keyword 搜尋關鍵字
     * @return 符合條件的筆記列表
     */
    @Query("SELECT n FROM Note n WHERE n.userId = :userId AND " +
           "(LOWER(n.beanName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.origin) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.flavorNotes) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Note> searchNotes(@Param("userId") Long userId, @Param("keyword") String keyword);
}

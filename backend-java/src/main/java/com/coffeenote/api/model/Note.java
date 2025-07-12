package com.coffeenote.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 咖啡筆記實體類
 * 
 * 這個類代表咖啡筆記的資料模型，包含咖啡豆的基本資訊、
 * 烘焙程度、風味描述、評分等資訊。
 * 
 * 使用 JPA 註解來定義資料庫映射關係。
 * 
 * @author Coffee Journal Team
 * @version 1.0
 */
@Entity
@Table(name = "coffee_notes")
public class Note {
    
    /**
     * 筆記的唯一識別碼
     * 使用自動遞增的主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 咖啡豆名稱
     * 必填欄位，最大長度 255 字元
     */
    @NotBlank(message = "咖啡豆名稱不能為空")
    @Size(max = 255, message = "咖啡豆名稱不能超過 255 字元")
    @Column(name = "bean_name", nullable = false)
    private String beanName;
    
    /**
     * 咖啡豆產地
     * 選填欄位，最大長度 255 字元
     */
    @Size(max = 255, message = "產地不能超過 255 字元")
    @Column(name = "origin")
    private String origin;
    
    /**
     * 烘焙程度
     * 必填欄位，例如：淺烘焙、中烘焙、深烘焙
     */
    @NotBlank(message = "烘焙程度不能為空")
    @Size(max = 255, message = "烘焙程度不能超過 255 字元")
    @Column(name = "roast_level", nullable = false)
    private String roastLevel;
    
    /**
     * 風味描述
     * 選填欄位，用於描述咖啡的風味特色
     */
    @Column(name = "flavor_notes", columnDefinition = "TEXT")
    private String flavorNotes;
    
    /**
     * 評分
     * 選填欄位，範圍 1-5 分
     */
    @Min(value = 1, message = "評分不能小於 1")
    @Max(value = 5, message = "評分不能大於 5")
    @Column(name = "rating")
    private Integer rating;
    
    /**
     * 沖煮方法
     * 選填欄位，例如：手沖、義式、法式壓壺等
     */
    @Size(max = 255, message = "沖煮方法不能超過 255 字元")
    @Column(name = "brewing_method")
    private String brewingMethod;
    
    /**
     * 使用者 ID
     * 關聯到建立此筆記的使用者
     * 注意：此字段由Controller自動設置，不需要客戶端驗證
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * 建立時間
     * 自動設定為當前時間
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新時間
     * 每次更新時自動設定為當前時間
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * 預設建構子
     */
    public Note() {
    }
    
    /**
     * 帶參數的建構子
     * 
     * @param beanName 咖啡豆名稱
     * @param origin 產地
     * @param roastLevel 烘焙程度
     * @param flavorNotes 風味描述
     * @param rating 評分
     * @param brewingMethod 沖煮方法
     * @param userId 使用者 ID
     */
    public Note(String beanName, String origin, String roastLevel, 
                String flavorNotes, Integer rating, String brewingMethod, Long userId) {
        this.beanName = beanName;
        this.origin = origin;
        this.roastLevel = roastLevel;
        this.flavorNotes = flavorNotes;
        this.rating = rating;
        this.brewingMethod = brewingMethod;
        this.userId = userId;
    }
    
    /**
     * JPA 生命週期回調：在持久化之前執行
     * 設定建立時間和更新時間
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
    
    /**
     * JPA 生命週期回調：在更新之前執行
     * 更新修改時間
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getter 和 Setter 方法
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBeanName() {
        return beanName;
    }
    
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    
    public String getRoastLevel() {
        return roastLevel;
    }
    
    public void setRoastLevel(String roastLevel) {
        this.roastLevel = roastLevel;
    }
    
    public String getFlavorNotes() {
        return flavorNotes;
    }
    
    public void setFlavorNotes(String flavorNotes) {
        this.flavorNotes = flavorNotes;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getBrewingMethod() {
        return brewingMethod;
    }
    
    public void setBrewingMethod(String brewingMethod) {
        this.brewingMethod = brewingMethod;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * toString 方法，用於除錯和日誌記錄
     */
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", beanName='" + beanName + '\'' +
                ", origin='" + origin + '\'' +
                ", roastLevel='" + roastLevel + '\'' +
                ", rating=" + rating +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                '}';
    }
}

<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\JwtController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Laravel 負責使用者認證和 JWT Token 簽發
| 咖啡筆記的 CRUD 操作由 Java 後端處理
|
*/

// JWT Token 相關 API（需要認證）
Route::middleware('auth:sanctum')->group(function () {
    // 獲取當前使用者資訊
    Route::get('/user', [JwtController::class, 'getCurrentUser']);

    // JWT Token 管理
    Route::post('/jwt/token', [JwtController::class, 'generateToken']);
    Route::post('/jwt/verify', [JwtController::class, 'verifyToken']);
    Route::post('/jwt/refresh', [JwtController::class, 'refreshToken']);
});

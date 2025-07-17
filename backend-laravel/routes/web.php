<?php

use Illuminate\Support\Facades\Route;

// 基本健康檢查路由
Route::get('/', function () {
    return response()->json([
        'message' => 'Coffee Journal Laravel API',
        'status' => 'running',
        'timestamp' => now()->toISOString()
    ]);
});

// 包含認證路由
require __DIR__.'/auth.php';

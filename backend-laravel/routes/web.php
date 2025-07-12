<?php

use App\Http\Controllers\ProfileController;
use Illuminate\Foundation\Application;
use Illuminate\Support\Facades\Route;
use Illuminate\Http\Request;
use Inertia\Inertia;

Route::get('/', function (Request $request) {
    if ($request->user()) {
        return redirect()->route('notes');
    }

    return Inertia::render('Welcome', [
        'canLogin' => Route::has('login'),
        'canRegister' => Route::has('register'),
        'laravelVersion' => Application::VERSION,
        'phpVersion' => PHP_VERSION,
    ]);
});

Route::get('/dashboard', function () {
    return Inertia::render('Dashboard');
})->middleware(['auth', 'verified'])->name('dashboard');

Route::middleware('auth')->group(function () {
    Route::get('/profile', [ProfileController::class, 'edit'])->name('profile.edit');
    Route::patch('/profile', [ProfileController::class, 'update'])->name('profile.update');
    Route::delete('/profile', [ProfileController::class, 'destroy'])->name('profile.destroy');
});
Route::middleware(['auth', 'verified'])->get('/notes', function () {
    return Inertia::render('Notes');
})->name('notes');

// JWT Token API 路由（使用 web 會話認證，避免 Sanctum 複雜性）
Route::middleware('auth')->group(function () {
    // 獲取當前使用者資訊
    Route::get('/user', [App\Http\Controllers\JwtController::class, 'getCurrentUser']);

    // JWT Token 管理
    Route::post('/jwt/token', [App\Http\Controllers\JwtController::class, 'generateToken']);
    Route::post('/jwt/verify', [App\Http\Controllers\JwtController::class, 'verifyToken']);
    Route::post('/jwt/refresh', [App\Http\Controllers\JwtController::class, 'refreshToken']);
});

require __DIR__.'/auth.php';

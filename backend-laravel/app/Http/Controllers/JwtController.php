<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Firebase\JWT\JWT;
use Firebase\JWT\Key;
use Carbon\Carbon;

/**
 * JWT Token 控制器
 * 
 * 負責為已認證的使用者簽發 JWT Token，
 * 讓前端可以使用這個 Token 來調用 Java 後端 API
 */
class JwtController extends Controller
{
    /**
     * JWT 簽名密鑰
     * 這個密鑰必須與 Java 後端的密鑰一致
     */
    private $jwtSecret;

    public function __construct()
    {
        // 從環境變數獲取 JWT 密鑰，與 Java 後端保持一致
        $this->jwtSecret = env('JWT_SECRET', 'mySecretKey12345678901234567890123456789012345678901234567890');
    }

    /**
     * 為當前認證使用者簽發 JWT Token
     * 
     * POST /api/jwt/token
     * 
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function generateToken(Request $request)
    {
        try {
            // 確保使用者已認證
            if (!Auth::check()) {
                return response()->json([
                    'error' => '使用者未認證',
                    'message' => '請先登入'
                ], 401);
            }

            $user = Auth::user();
            
            // 準備 JWT payload
            $payload = [
                'iss' => config('app.url'), // 簽發者
                'aud' => 'coffeenote-api',   // 接收者
                'iat' => time(),             // 簽發時間
                'exp' => time() + (24 * 60 * 60), // 過期時間（24小時）
                'sub' => $user->email,       // 主題（使用者識別）
                'userId' => $user->id,       // 使用者 ID
                'email' => $user->email,     // 使用者 email
                'name' => $user->name        // 使用者名稱
            ];

            // 生成 JWT Token
            $token = JWT::encode($payload, $this->jwtSecret, 'HS256');

            return response()->json([
                'success' => true,
                'token' => $token,
                'type' => 'Bearer',
                'expires_in' => 86400, // 24 小時（秒）
                'user' => [
                    'id' => $user->id,
                    'name' => $user->name,
                    'email' => $user->email
                ]
            ]);

        } catch (\Exception $e) {
            return response()->json([
                'error' => 'Token 生成失敗',
                'message' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * 驗證 JWT Token
     * 
     * POST /api/jwt/verify
     * 
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function verifyToken(Request $request)
    {
        try {
            $token = $request->input('token');
            
            if (!$token) {
                return response()->json([
                    'valid' => false,
                    'message' => 'Token 不能為空'
                ], 400);
            }

            // 驗證 Token
            $decoded = JWT::decode($token, new Key($this->jwtSecret, 'HS256'));
            
            return response()->json([
                'valid' => true,
                'payload' => $decoded,
                'expires_at' => date('Y-m-d H:i:s', $decoded->exp)
            ]);

        } catch (\Firebase\JWT\ExpiredException $e) {
            return response()->json([
                'valid' => false,
                'message' => 'Token 已過期'
            ], 401);
        } catch (\Exception $e) {
            return response()->json([
                'valid' => false,
                'message' => 'Token 無效'
            ], 401);
        }
    }

    /**
     * 刷新 JWT Token
     * 
     * POST /api/jwt/refresh
     * 
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function refreshToken(Request $request)
    {
        try {
            $token = $request->input('token');
            
            if (!$token) {
                return response()->json([
                    'error' => 'Token 不能為空'
                ], 400);
            }

            // 解碼舊 Token
            $decoded = JWT::decode($token, new Key($this->jwtSecret, 'HS256'));
            
            // 檢查是否即將過期（剩餘時間少於 1 小時才允許刷新）
            $timeLeft = $decoded->exp - time();
            if ($timeLeft > 3600) {
                return response()->json([
                    'error' => 'Token 尚未到刷新時間',
                    'message' => '請在 Token 過期前 1 小時內刷新'
                ], 400);
            }

            // 生成新的 Token
            $payload = [
                'iss' => config('app.url'),
                'aud' => 'coffeenote-api',
                'iat' => time(),
                'exp' => time() + (24 * 60 * 60),
                'sub' => $decoded->sub,
                'userId' => $decoded->userId,
                'email' => $decoded->email,
                'name' => $decoded->name
            ];

            $newToken = JWT::encode($payload, $this->jwtSecret, 'HS256');

            return response()->json([
                'success' => true,
                'token' => $newToken,
                'type' => 'Bearer',
                'expires_in' => 86400
            ]);

        } catch (\Firebase\JWT\ExpiredException $e) {
            return response()->json([
                'error' => 'Token 已過期',
                'message' => '請重新登入'
            ], 401);
        } catch (\Exception $e) {
            return response()->json([
                'error' => 'Token 刷新失敗',
                'message' => $e->getMessage()
            ], 500);
        }
    }

    /**
     * 獲取當前使用者資訊
     * 
     * GET /api/user
     * 
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function getCurrentUser(Request $request)
    {
        try {
            if (!Auth::check()) {
                return response()->json([
                    'error' => '使用者未認證'
                ], 401);
            }

            $user = Auth::user();
            
            return response()->json([
                'success' => true,
                'user' => [
                    'id' => $user->id,
                    'name' => $user->name,
                    'email' => $user->email,
                    'email_verified_at' => $user->email_verified_at,
                    'created_at' => $user->created_at,
                    'updated_at' => $user->updated_at
                ]
            ]);

        } catch (\Exception $e) {
            return response()->json([
                'error' => '獲取使用者資訊失敗',
                'message' => $e->getMessage()
            ], 500);
        }
    }
}

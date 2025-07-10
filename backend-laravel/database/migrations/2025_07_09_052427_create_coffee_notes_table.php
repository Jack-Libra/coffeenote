<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('coffee_notes', function (Blueprint $table) {
            $table->id();
            $table->string('bean_name');               // 咖啡豆名稱
            $table->string('origin')->nullable();      // 產地
            $table->string('roast_level');             // 烘焙程度
            $table->text('flavor_notes')->nullable();  // 風味描述
            $table->integer('rating')->nullable();     // 評分（1~5）
            $table->string('brewing_method')->nullable(); // 沖煮方式
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('coffee_notes');
    }
};

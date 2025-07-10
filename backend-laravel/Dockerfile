# 多階段建構
# 第一階段：建構前端資源
FROM node:18-alpine AS frontend-builder

WORKDIR /app

# 複製 package.json 和 package-lock.json
COPY package*.json ./

# 安裝 Node.js 依賴
RUN npm ci --only=production

# 複製前端源碼
COPY resources resources
COPY public public
COPY vite.config.js .
COPY tailwind.config.js .
COPY postcss.config.js .
COPY jsconfig.json .

# 建構前端資源
RUN npm run build

# 第二階段：建構 Laravel 應用
FROM php:8.2-fpm-alpine

# 安裝系統依賴
RUN apk add --no-cache \
    git \
    curl \
    libpng-dev \
    libxml2-dev \
    zip \
    unzip \
    postgresql-dev \
    nginx \
    supervisor

# 安裝 PHP 擴展
RUN docker-php-ext-install pdo pdo_pgsql gd xml

# 安裝 Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

# 設定工作目錄
WORKDIR /var/www/html

# 複製 Laravel 應用
COPY . .

# 複製建構好的前端資源
COPY --from=frontend-builder /app/public/build public/build

# 安裝 PHP 依賴
RUN composer install --no-dev --optimize-autoloader

# 設定權限
RUN chown -R www-data:www-data /var/www/html \
    && chmod -R 755 /var/www/html/storage \
    && chmod -R 755 /var/www/html/bootstrap/cache

# 複製 Nginx 配置
COPY docker/nginx.conf /etc/nginx/nginx.conf

# 複製 Supervisor 配置
COPY docker/supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# 暴露端口
EXPOSE 8000

# 啟動 Supervisor
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]

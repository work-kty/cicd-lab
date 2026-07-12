FROM nginx:1.27-alpine

# 기본 설정 교체
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 정적 파일 배치
COPY html/ /usr/share/nginx/html/

EXPOSE 80
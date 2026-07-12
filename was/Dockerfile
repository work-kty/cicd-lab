FROM tomcat:10.1-jdk17

# 기본 제공 앱 제거 (경량화 + 보안)
RUN rm -rf /usr/local/tomcat/webapps/*

# 우리 JSP 앱을 /app 컨텍스트로 배포
COPY webapp/ /usr/local/tomcat/webapps/app/

EXPOSE 8080
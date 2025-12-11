#!/bin/bash

# Spring XML에서 Struts2 Action Bean만 제거하고 Service/Manager Bean은 유지
# 작성일: 2025-10-20

echo "=== Struts2 Action Bean 제거 시작 ==="

# 처리할 XML 파일 목록
XML_FILES=(
    "web/WEB-INF/classes/web-config/spring-calendar.xml"
    "web/WEB-INF/classes/web-config/spring-common.xml"
    "web/WEB-INF/classes/web-config/spring-jmobile.xml"
    "web/WEB-INF/classes/web-config/spring-login.xml"
    "web/WEB-INF/classes/web-config/spring-mail.xml"
    "web/WEB-INF/classes/web-config/spring-mobile.xml"
    "web/WEB-INF/classes/web-config/spring-note.xml"
    "web/WEB-INF/classes/web-config/spring-organization.xml"
    "web/WEB-INF/classes/web-config/spring-setting.xml"
    "web/WEB-INF/classes/web-config/spring-webfolder.xml"
)

COUNT=0

for xml_file in "${XML_FILES[@]}"; do
    if [ ! -f "$xml_file" ]; then
        echo "파일 없음: $xml_file"
        continue
    fi
    
    echo "처리 중: $(basename $xml_file)"
    
    # Action으로 끝나는 Bean만 제거 (Service, Manager는 유지)
    # Bean 정의 시작부터 종료까지 한 블록으로 제거
    perl -i -0pe 's/<bean\s+id="[^"]*Action"[^>]*>.*?<\/bean>//gs' "$xml_file"
    
    # 빈 줄 정리
    sed -i '/^$/N;/^\n$/d' "$xml_file"
    
    COUNT=$((COUNT + 1))
    echo "  완료"
done

echo ""
echo "=== 처리 완료: $COUNT개 파일 ==="


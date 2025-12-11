#!/bin/bash

#########################################################
# Struts2 JSP 태그 → Spring/JSTL 태그 변환 스크립트
# 작성일: 2025-10-20
# 설명: Struts2 태그를 JSTL/EL로 변환
#########################################################

# 색상 정의
RED='\033[0:31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 작업 디렉토리
WEB_DIR="/opt/TMS_WEBMAIL_746S_ORIGINAL_BACKUP_20250629_063747/web"
BACKUP_DIR="/opt/TMS_WEBMAIL_746S_ORIGINAL_BACKUP_20250629_063747/backup/jsp-before-struts2-removal-$(date +%Y%m%d_%H%M%S)"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Struts2 JSP 태그 변환 스크립트${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# 백업 생성
echo -e "${YELLOW}[1/5] 백업 생성 중...${NC}"
mkdir -p "$BACKUP_DIR"
find "$WEB_DIR" -name "*.jsp" -exec grep -l "uri=\"/struts-tags\"" {} \; > "$BACKUP_DIR/struts2-jsp-files.txt"
FILE_COUNT=$(wc -l < "$BACKUP_DIR/struts2-jsp-files.txt")
echo -e "${GREEN}✓ Struts2 태그 사용 파일 ${FILE_COUNT}개 발견${NC}"

# 백업 파일 복사
echo -e "${YELLOW}[2/5] 파일 백업 중...${NC}"
while IFS= read -r file; do
    rel_path="${file#$WEB_DIR/}"
    backup_file="$BACKUP_DIR/$rel_path"
    mkdir -p "$(dirname "$backup_file")"
    cp "$file" "$backup_file"
done < "$BACKUP_DIR/struts2-jsp-files.txt"
echo -e "${GREEN}✓ ${FILE_COUNT}개 파일 백업 완료${NC}"

# 변환 작업 시작
echo -e "${YELLOW}[3/5] JSP 태그 변환 중...${NC}"

CONVERTED_COUNT=0

while IFS= read -r file; do
    echo -e "  ${BLUE}변환 중:${NC} ${file#$WEB_DIR/}"
    
    # 1. <s:property value="..."/> → ${}로 변환
    sed -i 's/<s:property\s\+value="\([^"]*\)"\s*\/>/\${\1}/g' "$file"
    
    # 2. <s:property value="..."> → ${}로 변환 (닫는 태그 있는 경우)
    sed -i 's/<s:property\s\+value="\([^"]*\)"\s*><\/s:property>/\${\1}/g' "$file"
    
    # 3. Struts2 taglib 선언 제거
    sed -i '/<%@\s*taglib\s\+prefix="s"\s\+uri="\/struts-tags"%>/d' "$file"
    
    # 4. 공백 라인 정리 (연속된 공백 라인 제거)
    sed -i '/^$/N;/^\n$/D' "$file"
    
    ((CONVERTED_COUNT++))
done < "$BACKUP_DIR/struts2-jsp-files.txt"

echo -e "${GREEN}✓ ${CONVERTED_COUNT}개 파일 변환 완료${NC}"

# 검증
echo -e "${YELLOW}[4/5] 변환 결과 검증 중...${NC}"

# Struts2 태그가 남아있는지 확인
REMAINING_STRUTS_TAGS=$(find "$WEB_DIR" -name "*.jsp" -exec grep -l '<s:' {} \; 2>/dev/null | wc -l)
REMAINING_TAGLIB=$(find "$WEB_DIR" -name "*.jsp" -exec grep -l 'uri="/struts-tags"' {} \; 2>/dev/null | wc -l)

if [ "$REMAINING_STRUTS_TAGS" -eq 0 ] && [ "$REMAINING_TAGLIB" -eq 0 ]; then
    echo -e "${GREEN}✓ 모든 Struts2 태그 제거 완료${NC}"
else
    echo -e "${YELLOW}⚠ Struts2 태그 ${REMAINING_STRUTS_TAGS}개, taglib 선언 ${REMAINING_TAGLIB}개 남음${NC}"
fi

# 보고서 생성
echo -e "${YELLOW}[5/5] 변환 보고서 생성 중...${NC}"

REPORT_FILE="$BACKUP_DIR/conversion-report.txt"

cat > "$REPORT_FILE" << EOF
========================================
Struts2 JSP 태그 변환 보고서
========================================

작업 일시: $(date '+%Y-%m-%d %H:%M:%S')
작업 디렉토리: $WEB_DIR
백업 디렉토리: $BACKUP_DIR

----------------------------------------
변환 통계
----------------------------------------
변환 대상 파일: ${FILE_COUNT}개
변환 완료 파일: ${CONVERTED_COUNT}개
남은 Struts2 태그: ${REMAINING_STRUTS_TAGS}개
남은 taglib 선언: ${REMAINING_TAGLIB}개

----------------------------------------
변환 내용
----------------------------------------
1. <s:property value="..."/> → \${...}
2. <s:property value="...">...</s:property> → \${...}
3. <%@ taglib prefix="s" uri="/struts-tags"%> 제거

----------------------------------------
백업 파일 목록
----------------------------------------
EOF

cat "$BACKUP_DIR/struts2-jsp-files.txt" >> "$REPORT_FILE"

echo -e "${GREEN}✓ 보고서 생성 완료: $REPORT_FILE${NC}"

# 완료
echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}변환 작업 완료!${NC}"
echo -e "${BLUE}========================================${NC}"
echo -e "백업 위치: ${BACKUP_DIR}"
echo -e "보고서: ${REPORT_FILE}"
echo ""
echo -e "${YELLOW}다음 단계:${NC}"
echo -e "  1. 변환된 JSP 파일 검토"
echo -e "  2. 애플리케이션 컴파일 테스트"
echo -e "  3. 화면 동작 검증"
echo ""

exit 0


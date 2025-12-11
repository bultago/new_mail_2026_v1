#!/bin/bash

# DAO 클래스를 MyBatis Mapper 인터페이스로 변환하는 스크립트
# 작성일: 2025-10-20

echo "=== DAO → Mapper 인터페이스 변환 시작 ==="

# 백업 디렉토리 생성
BACKUP_DIR="/opt/TMS_WEBMAIL_746S_ORIGINAL_BACKUP_20250629_063747/backup/dao-before-conversion-$(date +%Y%m%d_%H%M%S)"
mkdir -p "$BACKUP_DIR"

# DAO 파일 찾기 (인터페이스 제외)
DAO_FILES=$(find src/com/terracetech/tims/webmail -name "*Dao.java" ! -name "I*.java" -type f)

COUNT=0
CONVERTED=0

for dao_file in $DAO_FILES; do
    COUNT=$((COUNT + 1))
    
    # 백업
    cp "$dao_file" "$BACKUP_DIR/"
    
    # 파일명과 클래스명 추출
    filename=$(basename "$dao_file")
    classname="${filename%.java}"
    
    echo "[$COUNT] 처리 중: $classname"
    
    # SqlSessionDaoSupport를 확장하는지 확인
    if grep -q "extends SqlSessionDaoSupport" "$dao_file"; then
        # 임시 파일에 Mapper 인터페이스 헤더 작성
        temp_file="${dao_file}.tmp"
        
        # 패키지와 import 부분 복사
        sed -n '1,/^import/p' "$dao_file" > "$temp_file"
        
        # MyBatis import 추가
        echo "" >> "$temp_file"
        echo "import org.apache.ibatis.annotations.Mapper;" >> "$temp_file"
        echo "import org.apache.ibatis.annotations.Param;" >> "$temp_file"
        
        # 기존 import에서 VO 관련 import만 유지
        grep "^import com.terracetech.tims" "$dao_file" | grep -v "SqlSessionDaoSupport" >> "$temp_file"
        
        echo "" >> "$temp_file"
        
        # Javadoc 복사
        sed -n '/\/\*\*/,/\*\//p' "$dao_file" >> "$temp_file"
        
        # 인터페이스 선언 추가
        echo "@Mapper" >> "$temp_file"
        echo "public interface $classname {" >> "$temp_file"
        echo "" >> "$temp_file"
        
        # public 메서드 시그니처 추출 (getSqlSession 호출하는 메서드들)
        # 간단한 패턴 매칭으로 메서드 시그니처만 추출
        grep -A 3 "public " "$dao_file" | grep -v "getSqlSession" | grep -v "^--$" | sed 's/{$/;/' >> "$temp_file"
        
        echo "}" >> "$temp_file"
        
        # 원본 파일 교체
        mv "$temp_file" "$dao_file"
        
        CONVERTED=$((CONVERTED + 1))
        echo "  ✓ 변환 완료: $classname → Mapper 인터페이스"
    else
        echo "  - 건너뜀: SqlSessionDaoSupport 미사용"
    fi
done

echo ""
echo "=== 변환 완료 ==="
echo "총 DAO 파일: $COUNT개"
echo "변환 완료: $CONVERTED개"
echo "백업 위치: $BACKUP_DIR"


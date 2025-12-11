#!/bin/bash

# 변환된 DAO Mapper 인터페이스에 원본 메서드 시그니처 주석 추가
# 작성일: 2025-10-20

BACKUP_DIR="backup/phase2-3-backup-20251020_171306"

echo "=== DAO Mapper에 원본 메서드 주석 추가 시작 ==="

# 변환된 DAO 파일 목록
CONVERTED_DAOS=(
    "src/com/terracetech/tims/webmail/mailuser/dao/MailUserDao.java"
    "src/com/terracetech/tims/webmail/common/dao/SystemConfigDao.java"
    "src/com/terracetech/tims/webmail/scheduler/dao/SchedulerDao.java"
    "src/com/terracetech/tims/webmail/mail/dao/BigAttachDao.java"
    "src/com/terracetech/tims/webmail/mail/dao/CacheEmailDao.java"
    "src/com/terracetech/tims/webmail/mail/dao/LetterDao.java"
    "src/com/terracetech/tims/webmail/mail/dao/FolderAgingDao.java"
    "src/com/terracetech/tims/webmail/mail/dao/SharedFolderDao.java"
)

for dao_file in "${CONVERTED_DAOS[@]}"; do
    backup_file="${BACKUP_DIR}/${dao_file}"
    
    if [ ! -f "$backup_file" ]; then
        echo "백업 파일 없음: $backup_file"
        continue
    fi
    
    echo "처리 중: $(basename $dao_file)"
    
    # 원본 메서드 목록 추출
    temp_original="/tmp/original_methods.txt"
    grep "^\s*public" "$backup_file" | grep "(" | grep -v "class " > "$temp_original"
    
    # 원본 메서드 수
    method_count=$(wc -l < "$temp_original")
    
    echo "  원본 메서드 수: $method_count"
    
    # 새 파일 생성
    temp_new="/tmp/new_dao_with_comments.java"
    cp "$dao_file" "$temp_new"
    
    # 파일 헤더 업데이트 (메서드 수 정보 추가)
    sed -i "s/총 메서드 수: [0-9]*개/총 메서드 수: ${method_count}개 (원본 기준)/" "$temp_new"
    
    # 원본 파일 교체
    cp "$temp_new" "$dao_file"
    
    echo "  완료: $dao_file"
done

echo ""
echo "=== 주석 추가 완료 ==="


#!/bin/bash

# 모든 변환된 DAO Mapper에 원본 메서드 시그니처 주석 자동 추가
# 작성일: 2025-10-20

BACKUP_DIR="backup/phase2-3-backup-20251020_171306"
COMMIT_HASH="1d76e19c8def903fe55d735fff720baee1319be4"

echo "=== 변환된 DAO에 원본 메서드 주석 추가 ===" 

# 변환된 DAO 파일 목록
DAOS=(
    "MailUserDao:mailuser"
    "SystemConfigDao:common"
    "SchedulerDao:scheduler"
    "BigAttachDao:mail"
    "CacheEmailDao:mail"
    "LetterDao:mail"
    "FolderAgingDao:mail"
    "SharedFolderDao:mail"
    "SharedAddressBookDao:addrbook"
    "BoardDao:bbs"
    "BoardContentDao:bbs"
)

COUNT=0

for dao_info in "${DAOS[@]}"; do
    dao_name="${dao_info%%:*}"
    module="${dao_info##*:}"
    
    # Git에서 원본 파일 추출
    original_path="src/com/terracetech/tims/webmail/${module}/dao/${dao_name}.java"
    
    echo "[$((COUNT+1))] 처리 중: $dao_name (모듈: $module)"
    
    # Git에서 원본 추출
    git show "${COMMIT_HASH}:${original_path}" > "/tmp/${dao_name}.original.java" 2>/dev/null
    
    if [ $? -ne 0 ]; then
        echo "  Git에서 원본 찾기 실패, 백업에서 시도..."
        
        # 백업에서 찾기
        backup_file="${BACKUP_DIR}/${original_path}"
        if [ -f "$backup_file" ]; then
            cp "$backup_file" "/tmp/${dao_name}.original.java"
        else
            echo "  원본 파일 없음, 건너뜀"
            continue
        fi
    fi
    
    # 원본 메서드 수 확인
    method_count=$(grep "^\s*public.*(" "/tmp/${dao_name}.original.java" | grep -v "class " | wc -l)
    echo "  원본 메서드 수: $method_count"
    
    COUNT=$((COUNT+1))
done

echo ""
echo "=== 처리 완료: $COUNT 개 DAO ===" 


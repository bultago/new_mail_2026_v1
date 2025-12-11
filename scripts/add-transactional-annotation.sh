#!/bin/bash

# Manager 클래스에 @Service, @Transactional 어노테이션 추가 스크립트
# 작성일: 2025-10-20

echo "=== Manager 클래스에 @Service, @Transactional 어노테이션 추가 시작 ==="

# 우선순위 높은 Manager 클래스들
MANAGERS=(
    "src/com/terracetech/tims/webmail/mail/manager/MailManager.java"
    "src/com/terracetech/tims/webmail/setting/manager/SettingManager.java"
    "src/com/terracetech/tims/webmail/scheduler/manager/SchedulerManager.java"
    "src/com/terracetech/tims/webmail/addrbook/manager/AddressBookManager.java"
    "src/com/terracetech/tims/webmail/bbs/manager/BbsManager.java"
    "src/com/terracetech/tims/webmail/note/manager/NoteManager.java"
    "src/com/terracetech/tims/webmail/webfolder/manager/WebfolderManager.java"
    "src/com/terracetech/tims/webmail/organization/manager/OrganizationManager.java"
    "src/com/terracetech/tims/webmail/common/manager/SystemConfigManager.java"
)

COUNT=0

for file in "${MANAGERS[@]}"; do
    if [ -f "$file" ]; then
        echo "처리 중: $file"
        
        # import 문 추가 (이미 없는 경우에만)
        if ! grep -q "import org.springframework.stereotype.Service" "$file"; then
            sed -i '/import org.slf4j.LoggerFactory;/a import org.springframework.stereotype.Service;\nimport org.springframework.transaction.annotation.Transactional;' "$file"
        fi
        
        # 클래스 선언 앞에 어노테이션 추가
        sed -i '/^public class.*Manager/i @Service\n@Transactional' "$file"
        
        COUNT=$((COUNT + 1))
        echo "완료: $file"
    else
        echo "파일 없음: $file"
    fi
done

echo "=== 완료 ==="
echo "처리된 Manager 클래스: $COUNT개"

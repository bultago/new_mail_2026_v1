#!/bin/bash

# iBATIS → MyBatis SQL 매핑 XML 변환 스크립트
# 작성일: 2025-10-20

echo "=== iBATIS → MyBatis SQL 매핑 XML 변환 시작 ==="

# 변환할 파일들 찾기
SQL_FILES=$(find web/WEB-INF/classes/db-config -name "*.xml" -not -name "sqlMapConfig.xml" | head -20)

for file in $SQL_FILES; do
    echo "변환 중: $file"
    
    # 백업 생성
    cp "$file" "${file}.backup"
    
    # DTD 변경
    sed -i 's|<!DOCTYPE sqlMap PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN"|<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"|g' "$file"
    sed -i 's|"http://ibatis.apache.org/dtd/sql-map-2.dtd">|"http://mybatis.org/dtd/mybatis-3-mapper.dtd">|g' "$file"
    
    # 루트 엘리먼트 변경
    sed -i 's|<sqlMap namespace="\([^"]*\)">|<mapper namespace="com.terracetech.tims.webmail.\1.mapper.\1Mapper">|g' "$file"
    sed -i 's|</sqlMap>|</mapper>|g' "$file"
    
    # 파라미터 바인딩 변경
    sed -i 's|#\([^#]*\)#|#{\1}|g' "$file"
    sed -i 's|\$\([^$]*\)\$|${\1}|g' "$file"
    
    # 속성명 변경
    sed -i 's|resultClass=|resultType=|g' "$file"
    sed -i 's|parameterClass=|parameterType=|g' "$file"
    
    # selectKey 변경
    sed -i 's|resultClass="int"|resultType="int"|g' "$file"
    sed -i 's|keyProperty="\([^"]*\)"|keyProperty="\1"|g' "$file"
    
    echo "완료: $file"
done

echo "=== 변환 완료 ==="
echo "변환된 파일 수: $(echo $SQL_FILES | wc -w)"

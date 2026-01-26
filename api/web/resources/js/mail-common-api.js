/**
 * 메일 공통 기능 REST API JavaScript 래퍼
 * 
 * DWR MailCommonService를 REST API로 전환
 * 
 * @author Phase 3.5 Migration
 * @since 2025-10-21
 */

const MailCommonAPI = {
    
    /**
     * 편지지 목록 조회
     * 
     * DWR: MailCommonService.getLetterList(page)
     * 
     * @param {number} page - 페이지 번호
     * @returns {Promise<Object>} 편지지 목록 및 페이징 정보
     */
    getLetterList: async function(page) {
        return await ApiUtils.get('/mail/common/letter/list', {
            page: page || 1
        });
    },
    
    /**
     * 자동 저장 설정 업데이트
     * 
     * DWR: MailCommonService.updateAutoSaveInfo(mode, term)
     * 
     * @param {string} mode - 모드 (on/off)
     * @param {number} term - 저장 주기 (분)
     * @returns {Promise<Object>} 설정 정보
     */
    updateAutoSaveInfo: async function(mode, term) {
        return await ApiUtils.post('/mail/common/autosave', {
            mode: mode,
            term: term
        });
    },
    
    /**
     * 주소 키워드 검색
     * 
     * DWR: MailCommonService.searchAddressByKeyowrd(keywords, isNotOrgSearch)
     * 
     * @param {Array<string>} keywords - 검색 키워드 배열
     * @param {boolean} isNotOrgSearch - 조직도 검색 제외 여부
     * @returns {Promise<Object>} 검색 결과
     */
    searchAddressByKeyword: async function(keywords, isNotOrgSearch) {
        return await ApiUtils.post('/mail/common/search/address', {
            keywords: Array.isArray(keywords) ? keywords : [keywords],
            isNotOrgSearch: isNotOrgSearch || false
        });
    },
    
    /**
     * 계정 DN 검색
     * 
     * DWR: MailCommonService.searchAccountDN(emails)
     * 
     * @param {Array<string>} emails - 이메일 주소 배열
     * @returns {Promise<Object>} DN 검색 결과
     */
    searchAccountDN: async function(emails) {
        return await ApiUtils.post('/mail/common/search/account', {
            emails: Array.isArray(emails) ? emails : [emails]
        });
    }
};

// DWR 호환 래퍼 함수
window.MailCommonService = {
    getLetterList: function(page, callback) {
        MailCommonAPI.getLetterList(page)
            .then(data => callback && callback(data))
            .catch(error => console.error('getLetterList failed:', error));
    },
    
    updateAutoSaveInfo: function(mode, term, callback) {
        MailCommonAPI.updateAutoSaveInfo(mode, term)
            .then(data => callback && callback(data))
            .catch(error => console.error('updateAutoSaveInfo failed:', error));
    },
    
    searchAddressByKeyowrd: function(keywords, isNotOrgSearch, callback) {
        MailCommonAPI.searchAddressByKeyword(keywords, isNotOrgSearch)
            .then(data => callback && callback(data))
            .catch(error => console.error('searchAddressByKeyword failed:', error));
    },
    
    searchAccountDN: function(emails, callback) {
        MailCommonAPI.searchAccountDN(emails)
            .then(data => callback && callback(data))
            .catch(error => console.error('searchAccountDN failed:', error));
    }
};


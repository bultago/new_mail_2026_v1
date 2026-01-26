/**
 * 메일 검색 폴더 REST API JavaScript 래퍼
 * 
 * DWR MailSearchFolderService를 REST API로 전환
 * 
 * @author Phase 3.5 Migration
 * @since 2025-10-21
 */

const MailSearchFolderAPI = {
    
    /**
     * 검색 폴더 목록 조회
     * 
     * DWR: MailSearchFolderService.getFolderList()
     * 
     * @returns {Promise<Array>} 검색 폴더 목록
     */
    getFolderList: async function() {
        return await ApiUtils.get('/mail/search-folder/list');
    },
    
    /**
     * 검색 폴더 추가
     * 
     * DWR: MailSearchFolderService.addSearchFolder(folderName, query)
     * 
     * @param {string} folderName - 폴더명
     * @param {string} query - 검색 쿼리
     * @returns {Promise<void>}
     */
    addSearchFolder: async function(folderName, query) {
        return await ApiUtils.post('/mail/search-folder', {
            folderName: folderName,
            query: query
        });
    },
    
    /**
     * 검색 폴더 수정
     * 
     * DWR: MailSearchFolderService.modifySearchFolder(oldId, folderName, query)
     * 
     * @param {string} folderId - 폴더 ID
     * @param {string} folderName - 새 폴더명
     * @param {string} query - 새 검색 쿼리
     * @returns {Promise<void>}
     */
    modifySearchFolder: async function(folderId, folderName, query) {
        return await ApiUtils.put(`/mail/search-folder/${folderId}`, {
            folderName: folderName,
            query: query
        });
    },
    
    /**
     * 검색 폴더 삭제
     * 
     * DWR: MailSearchFolderService.deleteSearchFolder(folderIds)
     * 
     * @param {Array<string>} folderIds - 삭제할 폴더 ID 배열
     * @returns {Promise<void>}
     */
    deleteSearchFolder: async function(folderIds) {
        return await ApiUtils.delete('/mail/search-folder', {
            folderIds: Array.isArray(folderIds) ? folderIds : [folderIds]
        });
    }
};

// DWR 호환 래퍼 함수
window.MailSearchFolderService = {
    getFolderList: function(callback) {
        MailSearchFolderAPI.getFolderList()
            .then(data => callback && callback(data))
            .catch(error => console.error('getFolderList failed:', error));
    },
    
    addSearchFolder: function(folderName, query, callback) {
        MailSearchFolderAPI.addSearchFolder(folderName, query)
            .then(data => callback && callback(data))
            .catch(error => console.error('addSearchFolder failed:', error));
    },
    
    modifySearchFolder: function(oldId, folderName, query, callback) {
        MailSearchFolderAPI.modifySearchFolder(oldId, folderName, query)
            .then(data => callback && callback(data))
            .catch(error => console.error('modifySearchFolder failed:', error));
    },
    
    deleteSearchFolder: function(folderIds, callback) {
        MailSearchFolderAPI.deleteSearchFolder(folderIds)
            .then(data => callback && callback(data))
            .catch(error => console.error('deleteSearchFolder failed:', error));
    }
};


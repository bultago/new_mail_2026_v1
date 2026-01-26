/**
 * 메일 폴더 API 클라이언트 래퍼
 * 
 * DWR MailFolderService 대체용
 * 
 * 작성일: 2025-10-21
 * Phase: 3.5 - DWR → REST API 전환
 */

const MailFolderAPI = {
    
    /**
     * 폴더 정보 조회 (폴더 목록 + 메일 개수)
     * 
     * DWR: MailFolderService.getMailFolderInfo()
     */
    getFolderInfo: async function(type = 0) {
        return await ApiUtils.get('/mail/folder/info', { type: type });
    },
    
    /**
     * 폴더 생성
     * 
     * DWR: MailFolderService.addFolder()
     */
    addFolder: async function(folderName) {
        return await ApiUtils.post('/mail/folder', {
            folderName: folderName
        });
    },
    
    /**
     * 폴더 삭제
     * 
     * DWR: MailFolderService.deleteFolder()
     */
    deleteFolder: async function(folderName) {
        return await ApiUtils.delete(`/mail/folder/${encodeURIComponent(folderName)}`);
    },
    
    /**
     * 폴더 이름 변경
     * 
     * DWR: MailFolderService.modifyFolder()
     */
    modifyFolder: async function(previousName, parentName, newName) {
        return await ApiUtils.put(`/mail/folder/${encodeURIComponent(previousName)}`, {
            newName: newName,
            parentName: parentName
        });
    },
    
    /**
     * 폴더 비우기
     * 
     * DWR: MailFolderService.emptyFolder()
     */
    emptyFolder: async function(folderName) {
        return await ApiUtils.delete(`/mail/folder/${encodeURIComponent(folderName)}/empty`);
    },
    
    /**
     * 공유 폴더 목록 조회
     * 
     * DWR: MailFolderService.getSharringFolderList()
     */
    getSharringFolderList: async function() {
        return await ApiUtils.get('/mail/folder/shared');
    },
    
    /**
     * DWR 호환 헬퍼
     */
    dwrCompat: {
        getMailFolderInfo: function(callback, errorCallback) {
            MailFolderAPI.getFolderInfo()
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        addFolder: function(folderName, callback, errorCallback) {
            MailFolderAPI.addFolder(folderName)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        deleteFolder: function(folderName, callback, errorCallback) {
            MailFolderAPI.deleteFolder(folderName)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        emptyFolder: function(folderName, callback, errorCallback) {
            MailFolderAPI.emptyFolder(folderName)
                .then(callback)
                .catch(errorCallback || console.error);
        }
    }
};

// 전역 객체로 export
if (typeof window !== 'undefined') {
    window.MailFolderAPI = MailFolderAPI;
}

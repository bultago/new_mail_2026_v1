/**
 * 메일 태그 API 클라이언트 래퍼
 * 
 * DWR MailTagService 대체용
 * 
 * 작성일: 2025-10-21
 * Phase: 3.5 - DWR → REST API 전환
 */

const MailTagAPI = {
    
    /**
     * 태그 목록 조회
     * 
     * DWR: MailTagService.getTagList()
     */
    getTagList: async function() {
        return await ApiUtils.get('/mail/tag/list');
    },
    
    /**
     * 태그 생성
     * 
     * DWR: MailTagService.addTag()
     */
    addTag: async function(tagName, tagColor) {
        return await ApiUtils.post('/mail/tag', {
            tagName: tagName,
            tagColor: tagColor
        });
    },
    
    /**
     * 태그 수정
     * 
     * DWR: MailTagService.modifyTag()
     */
    modifyTag: async function(tagId, tagName, tagColor) {
        return await ApiUtils.put(`/mail/tag/${tagId}`, {
            tagName: tagName,
            tagColor: tagColor
        });
    },
    
    /**
     * 태그 삭제
     * 
     * DWR: MailTagService.deleteTag()
     */
    deleteTag: async function(tagIds) {
        return await ApiUtils.delete('/mail/tag', {
            tagIds: tagIds
        });
    },
    
    /**
     * 메일에 태그 적용
     * 
     * DWR: MailTagService.taggingMessage()
     */
    taggingMessage: async function(addFlag, mailIds, folders, tagId) {
        return await ApiUtils.post('/mail/tag/apply', {
            addFlag: addFlag,
            mailIds: mailIds,
            folders: folders,
            tagId: tagId
        });
    },
    
    /**
     * DWR 호환 헬퍼
     */
    dwrCompat: {
        getTagList: function(callback, errorCallback) {
            MailTagAPI.getTagList()
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        addTag: function(tagName, tagColor, callback, errorCallback) {
            MailTagAPI.addTag(tagName, tagColor)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        modifyTag: function(tagId, tagName, tagColor, callback, errorCallback) {
            MailTagAPI.modifyTag(tagId, tagName, tagColor)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        deleteTag: function(tagIds, callback, errorCallback) {
            MailTagAPI.deleteTag(tagIds)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        taggingMessage: function(addFlag, mailIds, folders, tagId, callback, errorCallback) {
            MailTagAPI.taggingMessage(addFlag, mailIds, folders, tagId)
                .then(callback)
                .catch(errorCallback || console.error);
        }
    }
};

// 전역 객체로 export
if (typeof window !== 'undefined') {
    window.MailTagAPI = MailTagAPI;
}

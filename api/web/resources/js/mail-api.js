/**
 * 메일 API 클라이언트 래퍼
 * 
 * DWR MailMessageService 대체용 JavaScript 클라이언트
 * ApiUtils를 기반으로 메일 관련 API를 쉽게 호출할 수 있도록 래핑
 * 
 * 작성일: 2025-10-21
 * Phase: 3.5 - DWR → REST API 전환
 * 
 * 사용 예시:
 *   MailAPI.getList('INBOX', 1, 20)
 *     .then(data => console.log(data))
 *     .catch(error => console.error(error));
 */

const MailAPI = {
    
    /**
     * 메일 목록 조회
     * 
     * DWR: MailMessageService.getMessageList()
     * 
     * @param {string} folderName - 폴더명 (기본값: 'INBOX')
     * @param {number} page - 페이지 번호 (기본값: 1)
     * @param {number} pageSize - 페이지 크기 (기본값: 20)
     * @returns {Promise<Object>} 메일 목록 데이터
     */
    getList: async function(folderName = 'INBOX', page = 1, pageSize = 20) {
        return await ApiUtils.get('/mail/list', {
            folderName: folderName,
            page: page,
            pageSize: pageSize
        });
    },
    
    /**
     * 메일 상세 조회
     * 
     * DWR: MailMessageService.getMessageDetail()
     * 
     * @param {string} mailId - 메일 ID
     * @param {string} folderName - 폴더명
     * @returns {Promise<Object>} 메일 상세 데이터
     */
    getDetail: async function(mailId, folderName) {
        return await ApiUtils.get(`/mail/${mailId}`, {
            folderName: folderName
        });
    },
    
    /**
     * 메일 읽음 처리
     * 
     * DWR: MailMessageService.setMessageRead()
     * 
     * @param {string} mailId - 메일 ID
     * @param {string} folderName - 폴더명
     * @param {boolean} read - 읽음 여부 (기본값: true)
     * @returns {Promise<string>} 처리 결과
     */
    markAsRead: async function(mailId, folderName, read = true) {
        return await ApiUtils.patch(`/mail/${mailId}/read`, {
            folderName: folderName,
            read: read
        });
    },
    
    /**
     * 메일 삭제
     * 
     * DWR: MailMessageService.deleteMessages()
     * 
     * @param {Array<string>} mailIds - 메일 ID 목록
     * @param {string} folderName - 폴더명
     * @returns {Promise<string>} 처리 결과
     */
    deleteMessages: async function(mailIds, folderName) {
        return await ApiUtils.delete('/mail', {
            mailIds: mailIds,
            folderName: folderName
        });
    },
    
    /**
     * 메일 이동
     * 
     * DWR: MailMessageService.moveMessages()
     * 
     * @param {Array<string>} mailIds - 메일 ID 목록
     * @param {string} fromFolder - 원본 폴더
     * @param {string} toFolder - 대상 폴더
     * @returns {Promise<string>} 처리 결과
     */
    moveMessages: async function(mailIds, fromFolder, toFolder) {
        return await ApiUtils.patch('/mail/move', {
            mailIds: mailIds,
            fromFolder: fromFolder,
            toFolder: toFolder
        });
    },
    
    /**
     * 메일 플래그 변경
     * 
     * DWR: MailMessageService.switchMessagesFlags()
     * 
     * @param {Array<string>} mailIds - 메일 ID 목록
     * @param {string} folderName - 폴더명
     * @param {string} flagType - 플래그 타입 (SEEN, FLAGGED, etc.)
     * @param {boolean} flagValue - 플래그 값
     * @returns {Promise<string>} 처리 결과
     */
    setFlags: async function(mailIds, folderName, flagType, flagValue) {
        return await ApiUtils.patch('/mail/flags', {
            mailIds: mailIds,
            folderName: folderName,
            flagType: flagType,
            flagValue: flagValue
        });
    },
    
    /**
     * DWR 호환 헬퍼 메서드
     * 기존 DWR 코드를 최소한으로 수정하여 사용할 수 있도록 지원
     * 
     * 사용 예시:
     *   MailAPI.dwrCompat.getList('INBOX', 1, 20, 
     *     function(data) { console.log(data); },
     *     function(error) { console.error(error); }
     *   );
     */
    dwrCompat: {
        getList: function(folderName, page, pageSize, callback, errorCallback) {
            MailAPI.getList(folderName, page, pageSize)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        getDetail: function(mailId, folderName, callback, errorCallback) {
            MailAPI.getDetail(mailId, folderName)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        markAsRead: function(mailId, folderName, read, callback, errorCallback) {
            MailAPI.markAsRead(mailId, folderName, read)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        deleteMessages: function(mailIds, folderName, callback, errorCallback) {
            MailAPI.deleteMessages(mailIds, folderName)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        moveMessages: function(mailIds, fromFolder, toFolder, callback, errorCallback) {
            MailAPI.moveMessages(mailIds, fromFolder, toFolder)
                .then(callback)
                .catch(errorCallback || console.error);
        }
    }
};

// 전역 객체로 export
if (typeof window !== 'undefined') {
    window.MailAPI = MailAPI;
}

// CommonJS/AMD export
if (typeof module !== 'undefined' && module.exports) {
    module.exports = MailAPI;
}
 * 메일 API 클라이언트 래퍼
 * 
 * DWR MailMessageService 대체용 JavaScript 클라이언트
 * ApiUtils를 기반으로 메일 관련 API를 쉽게 호출할 수 있도록 래핑
 * 
 * 작성일: 2025-10-21
 * Phase: 3.5 - DWR → REST API 전환
 * 
 * 사용 예시:
 *   MailAPI.getList('INBOX', 1, 20)
 *     .then(data => console.log(data))
 *     .catch(error => console.error(error));
 */

const MailAPI = {
    
    /**
     * 메일 목록 조회
     * 
     * DWR: MailMessageService.getMessageList()
     * 
     * @param {string} folderName - 폴더명 (기본값: 'INBOX')
     * @param {number} page - 페이지 번호 (기본값: 1)
     * @param {number} pageSize - 페이지 크기 (기본값: 20)
     * @returns {Promise<Object>} 메일 목록 데이터
     */
    getList: async function(folderName = 'INBOX', page = 1, pageSize = 20) {
        return await ApiUtils.get('/mail/list', {
            folderName: folderName,
            page: page,
            pageSize: pageSize
        });
    },
    
    /**
     * 메일 상세 조회
     * 
     * DWR: MailMessageService.getMessageDetail()
     * 
     * @param {string} mailId - 메일 ID
     * @param {string} folderName - 폴더명
     * @returns {Promise<Object>} 메일 상세 데이터
     */
    getDetail: async function(mailId, folderName) {
        return await ApiUtils.get(`/mail/${mailId}`, {
            folderName: folderName
        });
    },
    
    /**
     * 메일 읽음 처리
     * 
     * DWR: MailMessageService.setMessageRead()
     * 
     * @param {string} mailId - 메일 ID
     * @param {string} folderName - 폴더명
     * @param {boolean} read - 읽음 여부 (기본값: true)
     * @returns {Promise<string>} 처리 결과
     */
    markAsRead: async function(mailId, folderName, read = true) {
        return await ApiUtils.patch(`/mail/${mailId}/read`, {
            folderName: folderName,
            read: read
        });
    },
    
    /**
     * 메일 삭제
     * 
     * DWR: MailMessageService.deleteMessages()
     * 
     * @param {Array<string>} mailIds - 메일 ID 목록
     * @param {string} folderName - 폴더명
     * @returns {Promise<string>} 처리 결과
     */
    deleteMessages: async function(mailIds, folderName) {
        return await ApiUtils.delete('/mail', {
            mailIds: mailIds,
            folderName: folderName
        });
    },
    
    /**
     * 메일 이동
     * 
     * DWR: MailMessageService.moveMessages()
     * 
     * @param {Array<string>} mailIds - 메일 ID 목록
     * @param {string} fromFolder - 원본 폴더
     * @param {string} toFolder - 대상 폴더
     * @returns {Promise<string>} 처리 결과
     */
    moveMessages: async function(mailIds, fromFolder, toFolder) {
        return await ApiUtils.patch('/mail/move', {
            mailIds: mailIds,
            fromFolder: fromFolder,
            toFolder: toFolder
        });
    },
    
    /**
     * 메일 플래그 변경
     * 
     * DWR: MailMessageService.switchMessagesFlags()
     * 
     * @param {Array<string>} mailIds - 메일 ID 목록
     * @param {string} folderName - 폴더명
     * @param {string} flagType - 플래그 타입 (SEEN, FLAGGED, etc.)
     * @param {boolean} flagValue - 플래그 값
     * @returns {Promise<string>} 처리 결과
     */
    setFlags: async function(mailIds, folderName, flagType, flagValue) {
        return await ApiUtils.patch('/mail/flags', {
            mailIds: mailIds,
            folderName: folderName,
            flagType: flagType,
            flagValue: flagValue
        });
    },
    
    /**
     * DWR 호환 헬퍼 메서드
     * 기존 DWR 코드를 최소한으로 수정하여 사용할 수 있도록 지원
     * 
     * 사용 예시:
     *   MailAPI.dwrCompat.getList('INBOX', 1, 20, 
     *     function(data) { console.log(data); },
     *     function(error) { console.error(error); }
     *   );
     */
    dwrCompat: {
        getList: function(folderName, page, pageSize, callback, errorCallback) {
            MailAPI.getList(folderName, page, pageSize)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        getDetail: function(mailId, folderName, callback, errorCallback) {
            MailAPI.getDetail(mailId, folderName)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        markAsRead: function(mailId, folderName, read, callback, errorCallback) {
            MailAPI.markAsRead(mailId, folderName, read)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        deleteMessages: function(mailIds, folderName, callback, errorCallback) {
            MailAPI.deleteMessages(mailIds, folderName)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        moveMessages: function(mailIds, fromFolder, toFolder, callback, errorCallback) {
            MailAPI.moveMessages(mailIds, fromFolder, toFolder)
                .then(callback)
                .catch(errorCallback || console.error);
        }
    }
};

// 전역 객체로 export
if (typeof window !== 'undefined') {
    window.MailAPI = MailAPI;
}

// CommonJS/AMD export
if (typeof module !== 'undefined' && module.exports) {
    module.exports = MailAPI;
}
 * 메일 API 클라이언트 래퍼
 * 
 * DWR MailMessageService 대체용 JavaScript 클라이언트
 * ApiUtils를 기반으로 메일 관련 API를 쉽게 호출할 수 있도록 래핑
 * 
 * 작성일: 2025-10-21
 * Phase: 3.5 - DWR → REST API 전환
 * 
 * 사용 예시:
 *   MailAPI.getList('INBOX', 1, 20)
 *     .then(data => console.log(data))
 *     .catch(error => console.error(error));
 */

const MailAPI = {
    
    /**
     * 메일 목록 조회
     * 
     * DWR: MailMessageService.getMessageList()
     * 
     * @param {string} folderName - 폴더명 (기본값: 'INBOX')
     * @param {number} page - 페이지 번호 (기본값: 1)
     * @param {number} pageSize - 페이지 크기 (기본값: 20)
     * @returns {Promise<Object>} 메일 목록 데이터
     */
    getList: async function(folderName = 'INBOX', page = 1, pageSize = 20) {
        return await ApiUtils.get('/mail/list', {
            folderName: folderName,
            page: page,
            pageSize: pageSize
        });
    },
    
    /**
     * 메일 상세 조회
     * 
     * DWR: MailMessageService.getMessageDetail()
     * 
     * @param {string} mailId - 메일 ID
     * @param {string} folderName - 폴더명
     * @returns {Promise<Object>} 메일 상세 데이터
     */
    getDetail: async function(mailId, folderName) {
        return await ApiUtils.get(`/mail/${mailId}`, {
            folderName: folderName
        });
    },
    
    /**
     * 메일 읽음 처리
     * 
     * DWR: MailMessageService.setMessageRead()
     * 
     * @param {string} mailId - 메일 ID
     * @param {string} folderName - 폴더명
     * @param {boolean} read - 읽음 여부 (기본값: true)
     * @returns {Promise<string>} 처리 결과
     */
    markAsRead: async function(mailId, folderName, read = true) {
        return await ApiUtils.patch(`/mail/${mailId}/read`, {
            folderName: folderName,
            read: read
        });
    },
    
    /**
     * 메일 삭제
     * 
     * DWR: MailMessageService.deleteMessages()
     * 
     * @param {Array<string>} mailIds - 메일 ID 목록
     * @param {string} folderName - 폴더명
     * @returns {Promise<string>} 처리 결과
     */
    deleteMessages: async function(mailIds, folderName) {
        return await ApiUtils.delete('/mail', {
            mailIds: mailIds,
            folderName: folderName
        });
    },
    
    /**
     * 메일 이동
     * 
     * DWR: MailMessageService.moveMessages()
     * 
     * @param {Array<string>} mailIds - 메일 ID 목록
     * @param {string} fromFolder - 원본 폴더
     * @param {string} toFolder - 대상 폴더
     * @returns {Promise<string>} 처리 결과
     */
    moveMessages: async function(mailIds, fromFolder, toFolder) {
        return await ApiUtils.patch('/mail/move', {
            mailIds: mailIds,
            fromFolder: fromFolder,
            toFolder: toFolder
        });
    },
    
    /**
     * 메일 플래그 변경
     * 
     * DWR: MailMessageService.switchMessagesFlags()
     * 
     * @param {Array<string>} mailIds - 메일 ID 목록
     * @param {string} folderName - 폴더명
     * @param {string} flagType - 플래그 타입 (SEEN, FLAGGED, etc.)
     * @param {boolean} flagValue - 플래그 값
     * @returns {Promise<string>} 처리 결과
     */
    setFlags: async function(mailIds, folderName, flagType, flagValue) {
        return await ApiUtils.patch('/mail/flags', {
            mailIds: mailIds,
            folderName: folderName,
            flagType: flagType,
            flagValue: flagValue
        });
    },
    
    /**
     * DWR 호환 헬퍼 메서드
     * 기존 DWR 코드를 최소한으로 수정하여 사용할 수 있도록 지원
     * 
     * 사용 예시:
     *   MailAPI.dwrCompat.getList('INBOX', 1, 20, 
     *     function(data) { console.log(data); },
     *     function(error) { console.error(error); }
     *   );
     */
    dwrCompat: {
        getList: function(folderName, page, pageSize, callback, errorCallback) {
            MailAPI.getList(folderName, page, pageSize)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        getDetail: function(mailId, folderName, callback, errorCallback) {
            MailAPI.getDetail(mailId, folderName)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        markAsRead: function(mailId, folderName, read, callback, errorCallback) {
            MailAPI.markAsRead(mailId, folderName, read)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        deleteMessages: function(mailIds, folderName, callback, errorCallback) {
            MailAPI.deleteMessages(mailIds, folderName)
                .then(callback)
                .catch(errorCallback || console.error);
        },
        
        moveMessages: function(mailIds, fromFolder, toFolder, callback, errorCallback) {
            MailAPI.moveMessages(mailIds, fromFolder, toFolder)
                .then(callback)
                .catch(errorCallback || console.error);
        }
    }
};

// 전역 객체로 export
if (typeof window !== 'undefined') {
    window.MailAPI = MailAPI;
}

// CommonJS/AMD export
if (typeof module !== 'undefined' && module.exports) {
    module.exports = MailAPI;
}
    
    /**
     * 첨부파일 제거
     * 
     * DWR: MailMessageService.removeAttachFile()
     * 
     * @param {number} uid - 메일 UID
     * @param {string} folder - 폴더명
     * @param {string} part - 첨부파일 파트
     * @param {string} tmpPath - 임시 경로
     * @returns {Promise<Object>} 새로운 UID
     */
    removeAttachFile: async function(uid, folder, part, tmpPath) {
        return await ApiUtils.delete(`/mail/${uid}/attachment`, {
            folderName: folder,
            part: part,
            tmpPath: tmpPath || '/tmp'
        });
    },
    
    /**
     * 메일 복사
     * 
     * DWR: MailMessageService.copyMessage()
     * 
     * @param {Array<number>} uids - 메일 UID 배열
     * @param {string} fromFolder - 원본 폴더명
     * @param {string} toFolder - 대상 폴더명
     * @returns {Promise<void>}
     */
    copyMessages: async function(uids, fromFolder, toFolder) {
        return await ApiUtils.post('/mail/copy', {
            uids: uids,
            fromFolder: fromFolder,
            toFolder: toFolder
        });
    },
    
    /**
     * 메일 주소 목록 조회
     * 
     * DWR: MailMessageService.getMailAdressList()
     * 
     * @param {boolean} isNotOrgSearch - 조직도 검색 제외 여부
     * @returns {Promise<Object>} 주소 목록
     */
    getMailAddressList: async function(isNotOrgSearch) {
        return await ApiUtils.get('/mail/addresses', {
            isNotOrgSearch: isNotOrgSearch || false
        });
    },
    
    /**
     * 메일 무결성 검사
     * 
     * DWR: MailMessageService.getMessageIntegrity()
     * 
     * @param {string} folder - 폴더명
     * @param {number} uid - 메일 UID
     * @returns {Promise<Object>} 무결성 검사 결과
     */
    getMessageIntegrity: async function(folder, uid) {
        return await ApiUtils.get(`/mail/${uid}/integrity`, {
            folderName: folder
        });
    }
};

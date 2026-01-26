/**
 * REST API 공통 유틸리티
 * 
 * DWR 대체용 JavaScript API 클라이언트
 * 작성일: 2025-10-21
 * Phase: 3.5 - DWR → REST API 전환
 */

const ApiUtils = {
    
    /**
     * 기본 설정
     */
    config: {
        baseUrl: '/api',
        timeout: 30000,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8',
            'X-Requested-With': 'XMLHttpRequest'
        }
    },
    
    /**
     * GET 요청
     */
    get: async function(url, params = {}) {
        try {
            const queryString = new URLSearchParams(params).toString();
            const fullUrl = this.config.baseUrl + url + (queryString ? '?' + queryString : '');
            
            const response = await fetch(fullUrl, {
                method: 'GET',
                headers: this.config.headers,
                credentials: 'same-origin'
            });
            
            return await this.handleResponse(response);
        } catch (error) {
            return this.handleError(error);
        }
    },
    
    /**
     * POST 요청
     */
    post: async function(url, data = {}) {
        try {
            const response = await fetch(this.config.baseUrl + url, {
                method: 'POST',
                headers: this.config.headers,
                body: JSON.stringify(data),
                credentials: 'same-origin'
            });
            
            return await this.handleResponse(response);
        } catch (error) {
            return this.handleError(error);
        }
    },
    
    /**
     * PUT 요청
     */
    put: async function(url, data = {}) {
        try {
            const response = await fetch(this.config.baseUrl + url, {
                method: 'PUT',
                headers: this.config.headers,
                body: JSON.stringify(data),
                credentials: 'same-origin'
            });
            
            return await this.handleResponse(response);
        } catch (error) {
            return this.handleError(error);
        }
    },
    
    /**
     * DELETE 요청
     */
    delete: async function(url, params = {}) {
        try {
            const queryString = new URLSearchParams(params).toString();
            const fullUrl = this.config.baseUrl + url + (queryString ? '?' + queryString : '');
            
            const response = await fetch(fullUrl, {
                method: 'DELETE',
                headers: this.config.headers,
                credentials: 'same-origin'
            });
            
            return await this.handleResponse(response);
        } catch (error) {
            return this.handleError(error);
        }
    },
    
    /**
     * PATCH 요청
     */
    patch: async function(url, data = {}) {
        try {
            const response = await fetch(this.config.baseUrl + url, {
                method: 'PATCH',
                headers: this.config.headers,
                body: JSON.stringify(data),
                credentials: 'same-origin'
            });
            
            return await this.handleResponse(response);
        } catch (error) {
            return this.handleError(error);
        }
    },
    
    /**
     * 응답 처리
     */
    handleResponse: async function(response) {
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || `HTTP ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        
        if (!data.success) {
            throw new Error(data.message || 'API 요청 실패');
        }
        
        return data.data;
    },
    
    /**
     * 에러 처리
     */
    handleError: function(error) {
        console.error('API 요청 실패:', error);
        throw error;
    },
    
    /**
     * 파일 업로드
     */
    upload: async function(url, formData) {
        try {
            const response = await fetch(this.config.baseUrl + url, {
                method: 'POST',
                body: formData,
                credentials: 'same-origin'
                // Content-Type은 브라우저가 자동으로 설정 (multipart/form-data)
            });
            
            return await this.handleResponse(response);
        } catch (error) {
            return this.handleError(error);
        }
    },
    
    /**
     * DWR 호환 래퍼 함수
     * 기존 DWR 코드를 최소한으로 수정하여 사용할 수 있도록 지원
     */
    createDwrCompatibleService: function(serviceName, baseUrl) {
        const self = this;
        return {
            call: function(methodName, params, callback, errorCallback) {
                const url = `${baseUrl}/${methodName}`;
                self.post(url, params)
                    .then(data => {
                        if (callback) callback(data);
                    })
                    .catch(error => {
                        if (errorCallback) errorCallback(error.message);
                        else console.error(`${serviceName}.${methodName} 실패:`, error);
                    });
            }
        };
    }
};

// 전역 객체로 export
if (typeof window !== 'undefined') {
    window.ApiUtils = ApiUtils;
}

// CommonJS/AMD export
if (typeof module !== 'undefined' && module.exports) {
    module.exports = ApiUtils;
}

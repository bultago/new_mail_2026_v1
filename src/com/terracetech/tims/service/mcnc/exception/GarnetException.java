package com.terracetech.tims.service.mcnc.exception;

/**
 * 예외처리
 * @author kevin
 *
 */
public class GarnetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6650232096906530330L;

	private String	exceptionCode		= null;
	private String	exceptionMessage	= null;
	
	/**
	 * NeoBizbox 작업 예외를 처리한다. 
	 * @param exceptionCode 예외처리 코드
	 * @param exceptionMessage 예외처리 코드
	 */
	public GarnetException( String exceptionCode, String exceptionMessage ) {
		this.exceptionCode		= exceptionCode;
		this.exceptionMessage	= exceptionMessage;
	}
	
	/**
	 * 예외 코드를 얻는다.
	 * @return 예외 코드
	 */
	public String getExceptionCode() {
		return exceptionCode;
	}
	
	/**
	 * 예외 메세지를 얻는다.
	 * @return 예외 메세지
	 */
	public String getExceptionMessage() {
		return exceptionMessage;
	}
}


package com.terracetech.tims.service.mcnc.exception;

public class GarnetConnectionException extends GarnetException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -948464048714049014L;

	public GarnetConnectionException( String exceptionCode, String exceptionMessage) {
		super( exceptionCode, exceptionMessage );
	}
}
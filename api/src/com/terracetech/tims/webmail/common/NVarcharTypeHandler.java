package com.terracetech.tims.webmail.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OraclePreparedStatement;

import org.apache.commons.dbcp.DelegatingStatement;

// import com.ibatis.common.jdbc.logging.PreparedStatementLogProxy;
// import com.ibatis.sqlmap.engine.type.StringTypeHandler;

/**
 * @deprecated iBATIS TypeHandler - MyBatis 변환으로 더 이상 사용 안함 (2025-10-23)
 */
@Deprecated
public class NVarcharTypeHandler /* extends StringTypeHandler */ {
	public void setParameter(PreparedStatement ps, int i, Object parameter,
			String jdbcType) throws SQLException {
		Statement delegate = ps;
		while (delegate instanceof DelegatingStatement) {
			delegate = ((DelegatingStatement) delegate).getDelegate();
		}

		if (delegate instanceof Proxy) {
			Proxy p = (Proxy) delegate;
			InvocationHandler handeler = Proxy.getInvocationHandler(p);
			if (handeler instanceof PreparedStatementLogProxy) {
				delegate = ((PreparedStatementLogProxy) handeler).getPreparedStatement();

				if (delegate instanceof OraclePreparedStatement) {
					((OraclePreparedStatement) delegate).setFormOfUse(i,OraclePreparedStatement.FORM_NCHAR);
				}
			}
			
		} else if (delegate instanceof OraclePreparedStatement) {
			((OraclePreparedStatement) delegate).setFormOfUse(i,OraclePreparedStatement.FORM_NCHAR);
		}

		ps.setString(i, ((String) parameter));
	}
}

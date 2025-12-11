package com.terracetech.tims.mail;

import java.util.*;
import jakarta.mail.*;

public class TMailSession {
    public Session session;

    public TMailSession(Properties props) {
        session = Session.getInstance(props, null);
    }

    public TMailStore getStore() throws NoSuchProviderException {
        return new TMailStore(session.getStore());
    }

    public TMailStore getStore(String protocol) throws NoSuchProviderException {
        return new TMailStore(session.getStore(protocol));
    }

    public Transport getTransport() throws NoSuchProviderException {
        return session.getTransport();
    }

	public Session getSession() {
		return session;
	}
}


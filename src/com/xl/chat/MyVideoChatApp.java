package com.xl.chat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.service.IPendingServiceCall;
import org.red5.server.api.service.IPendingServiceCallback;
import org.red5.server.api.service.IServiceCapableConnection;

public class MyVideoChatApp extends ApplicationAdapter implements IPendingServiceCallback {
	
	@Override
	public boolean appConnect(IConnection arg0, Object[] arg1) {

		IScope scope = arg0.getScope();
		String stmName = getClientNo(arg0);
		Set<IConnection> conns = scope.getClientConnections();
		if (conns.size() == 0 || conns.size() == 1) {
			if (conns.size() == 1) {
				IConnection first = conns.iterator().next();
				callClient(first, "_getVideo", new Object[] { stmName });
			}
			return true;
		} else {
			return false;
		}
	}

	private String getClientNo(IConnection conn) {
		return conn.getScope().getName() + conn.getClient().getId();
	}

	private void callClient(IConnection conn, String methodName, Object[] params) {
		if (conn instanceof IServiceCapableConnection) {
			IServiceCapableConnection sc = (IServiceCapableConnection) conn;
			sc.invoke(methodName, params, this);
		}
	}

	public List getStrName() {
		List rst = new ArrayList();
		IConnection conn = Red5.getConnectionLocal();
		IScope scope = conn.getScope();
		String stmName = getClientNo(conn);
		rst.add(stmName);
		Set<IConnection> conns = scope.getClientConnections();
		if (conns.size() == 2) {
			Iterator<IConnection> its = conns.iterator();
			while (its.hasNext()) {
				IConnection first = its.next();
				if (first != conn) {
					String firstName = getClientNo(first);
					rst.add(firstName);
				}
			}
		}
		return rst;
	}

	@Override
	public void resultReceived(IPendingServiceCall ipendingservicecall) {
		// TODO Auto-generated method stub

	}

}

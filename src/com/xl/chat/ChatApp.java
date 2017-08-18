package com.xl.chat;

import java.util.Set;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.scope.IScope;

public class ChatApp extends ApplicationAdapter { 

	/*
	 * ��һ������
	 * 
	 * @Override public boolean appConnect(IConnection arg0, Object[] arg1) {
	 * System.out.println(" ����"); return true; }
	 * 
	 * public String change(String str){ System.out.println(" �ͻ��˵��÷�����"); return
	 * str.toUpperCase(); }
	 */

	/*
	 * ���������ÿͻ��˳��򣬽��տͻ��˵ķ���ֵ��������������ǰ��������
	 * 
	 * @Override public boolean appConnect(IConnection arg0, Object[] arg1) { //
	 *  // callClient(arg0);
	 * this.callEvery(arg0.getScope()); return true; }
	 * 
	 * private void callEvery(IScope scope) { Iterator<IConnection> it =
	 * scope.getClientConnections().iterator(); while (it.hasNext()) {
	 * this.callClient(it.next()); } }
	 * 
	 * private void callClient(IConnection conn) { if (conn instanceof
	 * IServiceCapableConnection) { IServiceCapableConnection sc =
	 * (IServiceCapableConnection) conn; sc.invoke("clientMethod", new Object[]
	 * { conn.getSessionId(), 1 }, this); } }
	 * 
	 * @Override public void resultReceived(IPendingServiceCall arg0) {
	 * System.out.println(" ���Կͻ��˵����أ�" + arg0.getResult());
	 * 
	 * }
	 */

	/*
	 * �������
	 * 
	 * @Override public boolean appConnect(IConnection arg0, Object[] arg1) {
	 * return true; }
	 * 
	 * @Override public boolean appStart(IScope arg0) { System.out.println(
	 * "appStart ��������"); this.createSharedObject(arg0, "point", true);
	 * ISharedObject so = this.getSharedObject(arg0, "point"); if (so != null)
	 * so.addSharedObjectListener(new ShareObjectListener()); else
	 * System.out.println("point ��null"); return true; }
	 * 
	 * @Override public void resultReceived(IPendingServiceCall
	 * ipendingservicecall) { 
	 * 
	 * }
	 */

	@Override
	public boolean appStart(IScope arg0) {
		System.out.println(" ����appStart");
		return true;
	}

	@Override
	public boolean appConnect(IConnection arg2, Object[] arg1) {
		IScope scope = arg2.getScope();
		System.out.println(" ���ӵ�" + scope.getContextPath() + "ID �б� ");
		Set<IClient> i = scope.getClients();
		for (IClient c : i) {
			System.out.println(c.getId());
		}
		return true;
	}

	@Override
	public boolean roomStart(IScope arg0) {
		System.out.println(" ����roomStart");
		return true;
	}

	@Override
	public boolean roomConnect(IConnection arg2, Object[] arg1) {
		IScope arg0 = arg2.getScope();
		System.out.println(" ���ӵ�" + arg0.getContextPath() + "ID �б� ");
		Set<IClient> i = arg0.getClients();
		for (IClient c : i) {
			System.out.println(c.getId());
		}
		return true;
	}

}

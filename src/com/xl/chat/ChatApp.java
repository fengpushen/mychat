package com.xl.chat;

import java.util.Set;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.scope.IScope;

public class ChatApp extends ApplicationAdapter { 

	/*
	 * 第一个程序
	 * 
	 * @Override public boolean appConnect(IConnection arg0, Object[] arg1) {
	 * System.out.println(" 连接"); return true; }
	 * 
	 * public String change(String str){ System.out.println(" 客户端调用服务器"); return
	 * str.toUpperCase(); }
	 */

	/*
	 * 服务器调用客户端程序，接收客户端的返回值，服务器遍历当前所有连接
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
	 * System.out.println(" 来自客户端到返回：" + arg0.getResult());
	 * 
	 * }
	 */

	/*
	 * 共享对象
	 * 
	 * @Override public boolean appConnect(IConnection arg0, Object[] arg1) {
	 * return true; }
	 * 
	 * @Override public boolean appStart(IScope arg0) { System.out.println(
	 * "appStart 程序启动"); this.createSharedObject(arg0, "point", true);
	 * ISharedObject so = this.getSharedObject(arg0, "point"); if (so != null)
	 * so.addSharedObjectListener(new ShareObjectListener()); else
	 * System.out.println("point 是null"); return true; }
	 * 
	 * @Override public void resultReceived(IPendingServiceCall
	 * ipendingservicecall) { 
	 * 
	 * }
	 */

	@Override
	public boolean appStart(IScope arg0) {
		System.out.println(" 启动appStart");
		return true;
	}

	@Override
	public boolean appConnect(IConnection arg2, Object[] arg1) {
		IScope scope = arg2.getScope();
		System.out.println(" 连接到" + scope.getContextPath() + "ID 列表： ");
		Set<IClient> i = scope.getClients();
		for (IClient c : i) {
			System.out.println(c.getId());
		}
		return true;
	}

	@Override
	public boolean roomStart(IScope arg0) {
		System.out.println(" 启动roomStart");
		return true;
	}

	@Override
	public boolean roomConnect(IConnection arg2, Object[] arg1) {
		IScope arg0 = arg2.getScope();
		System.out.println(" 连接到" + arg0.getContextPath() + "ID 列表： ");
		Set<IClient> i = arg0.getClients();
		for (IClient c : i) {
			System.out.println(c.getId());
		}
		return true;
	}

}

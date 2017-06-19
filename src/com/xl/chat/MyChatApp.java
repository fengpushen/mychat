package com.xl.chat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.service.IServiceCapableConnection;
import org.red5.server.api.so.ISharedObject;

public class MyChatApp extends ApplicationAdapter {
	// 属性
	private IScope appScope;

	private String userName;
	// 共享存储在线用户
	private ISharedObject listSO;
	private Map<String, IConnection> onlineList = new HashMap<String, IConnection>();// 在线用户表
	// 程序运行时志向

	public boolean appStart(IScope app) {
		if (!super.appStart(app)) {
			return false;
		}
		appScope = app;
		return true;
	}

	@Override
	public boolean appConnect(IConnection arg0, Object[] arg1) {

		String userId = arg0.getClient().getId();
		if (!super.appConnect(arg0, arg1)) {
			return false;
		}
		if (arg1 != null) {
			userName = (String) arg1[0];
		}
		if (onlineList.get(userName) != null) {
			rejectClient("请不要重复登录");
			return false;
		}
		onlineList.put(userName, arg0);
		listSO = getSharedObject(appScope, "listSO", false);
		listSO.setAttribute(userId, userName);
		System.out.println("The user:" + userName + "," + userName + " logined successfully");
		return true;
	}

	public void getOnloadUser(Object[] params) {
		String clientName = params[0].toString();
		if (null == clientName || "".equals(clientName)) {
			return;
		}
		// 给所有客户端数据
		IScope scope = Red5.getConnectionLocal().getScope();
		Iterator it = scope.getConnections().iterator();
		for (; it.hasNext();) {
			Set connections = (Set) it.next();
			IConnection tempConn = (IConnection) connections.iterator().next();
			if (tempConn instanceof IServiceCapableConnection) {
				IServiceCapableConnection sc = (IServiceCapableConnection) tempConn;
				sc.invoke("result_getOnloadUser", new Object[] { clientName });
			}
		}
	}

	// 聊天
	public void sayToAll(Object[] params) {
		IConnection conn = Red5.getConnectionLocal();
		String user_id = conn.getClient().getId();
		String clientName = (String) listSO.getAttribute(user_id);
		System.out.println("************发言者是：" + clientName);
		String sayToName = params[0] == null ? "" : params[0].toString().trim();
		String sayWhat = params[1] == null ? "" : params[1].toString().trim();
		if ("".equals(sayToName) || "All".equals(sayToName))// 发消息给聊天室的所有人.
		{
			IScope scope = Red5.getConnectionLocal().getScope();
			Iterator it = scope.getConnections().iterator();
			for (; it.hasNext();) {
				Set connections = (Set) it.next();
				IConnection tempConn = (IConnection) connections.iterator().next();
				if (tempConn instanceof IServiceCapableConnection) {
					IServiceCapableConnection sc = (IServiceCapableConnection) tempConn;
					// 调用客户端showMessage方法。
					sc.invoke("showMessage", new Object[] { clientName + " to All:" + sayWhat });
				}
			}
		} else {
			IConnection tempConn = onlineList.get(sayToName);
			if (tempConn instanceof IServiceCapableConnection) {
				IServiceCapableConnection sc = (IServiceCapableConnection) tempConn;
				sc.invoke("showMessage", new Object[] { clientName + " to " + sayToName + ":" + sayWhat });
			}
			IServiceCapableConnection sc = (IServiceCapableConnection) conn;
			sc.invoke("showMessage", new Object[] { clientName + " to " + sayToName + ":" + sayWhat });
		}
	}

	// 用户断开连接的时候触发
	public void appDisconnect(IConnection conn) {
		String dis_user_id = conn.getClient().getId();
		String user = (String) listSO.getAttribute(dis_user_id);
		// 根据ID删除对应在线纪录
		onlineList.remove(user);
		// 删除用户列表共享对象的对应属性
		listSO.removeAttribute(dis_user_id);
		IScope scope = Red5.getConnectionLocal().getScope();
		Iterator it = scope.getConnections().iterator();
		for (; it.hasNext();) {
			Set connections = (Set) it.next();
			IConnection tempConn = (IConnection) connections.iterator().next();
			if (tempConn instanceof IServiceCapableConnection) {
				IServiceCapableConnection sc = (IServiceCapableConnection) tempConn;
				// 服务器端调用客户端flash方法。
				sc.invoke("disconnectMessage", new Object[] { user });
			}
		}
	}

	// 视频邀请
	public void videoInvite(Object[] params) {
		IConnection conn = Red5.getConnectionLocal();
		String user_id = conn.getClient().getId();
		String clientName = (String) listSO.getAttribute(user_id);
		System.out.println("************视频邀请者是：" + clientName);
		String sayToName = params[0] == null ? "" : params[0].toString().trim();
		sayToName = "aaa";
		if ("".equals(sayToName) || "All".equals(sayToName))// 发消息给聊天室的所有人.
		{
			System.out.println("不可以邀请0或者多个人");
		} else {
			IConnection tempConn = onlineList.get(sayToName);
			if (tempConn instanceof IServiceCapableConnection) {
				IServiceCapableConnection sc = (IServiceCapableConnection) tempConn;
				sc.invoke("showInviteMessage", new Object[] { clientName + ";" + sayToName });
			}
		}
	}

	// 同意邀请后调用邀请方方法
	public void agreeVideoInvite(Object[] params) {
		IConnection conn = Red5.getConnectionLocal();
		System.out.println("<<<<<params length" + params.length);
		// 邀请者
		String inviteUserName = params[0] == null ? "" : params[0].toString().trim();
		// 被邀请者
		String otherUserName = params[1] == null ? "" : params[1].toString().trim();
		System.out.println("***邀请者是:" + inviteUserName);
		System.out.println("***被邀请者是:" + otherUserName);
		if ("".equals(inviteUserName))// 发消息给聊天室的所有人.
		{
			System.out.println("出错了");
		} else {
			System.out.println("*********调用成功");
			IConnection tempConn = onlineList.get(inviteUserName);
			if (tempConn instanceof IServiceCapableConnection) {
				IServiceCapableConnection sc = (IServiceCapableConnection) tempConn;
				sc.invoke("showVideo", new Object[] { otherUserName });
				System.out.println("*********调用结束");
			}
		}
	}

}

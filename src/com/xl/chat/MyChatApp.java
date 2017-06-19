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
	// ����
	private IScope appScope;

	private String userName;
	// ����洢�����û�
	private ISharedObject listSO;
	private Map<String, IConnection> onlineList = new HashMap<String, IConnection>();// �����û���
	// ��������ʱ־��

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
			rejectClient("�벻Ҫ�ظ���¼");
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
		// �����пͻ�������
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

	// ����
	public void sayToAll(Object[] params) {
		IConnection conn = Red5.getConnectionLocal();
		String user_id = conn.getClient().getId();
		String clientName = (String) listSO.getAttribute(user_id);
		System.out.println("************�������ǣ�" + clientName);
		String sayToName = params[0] == null ? "" : params[0].toString().trim();
		String sayWhat = params[1] == null ? "" : params[1].toString().trim();
		if ("".equals(sayToName) || "All".equals(sayToName))// ����Ϣ�������ҵ�������.
		{
			IScope scope = Red5.getConnectionLocal().getScope();
			Iterator it = scope.getConnections().iterator();
			for (; it.hasNext();) {
				Set connections = (Set) it.next();
				IConnection tempConn = (IConnection) connections.iterator().next();
				if (tempConn instanceof IServiceCapableConnection) {
					IServiceCapableConnection sc = (IServiceCapableConnection) tempConn;
					// ���ÿͻ���showMessage������
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

	// �û��Ͽ����ӵ�ʱ�򴥷�
	public void appDisconnect(IConnection conn) {
		String dis_user_id = conn.getClient().getId();
		String user = (String) listSO.getAttribute(dis_user_id);
		// ����IDɾ����Ӧ���߼�¼
		onlineList.remove(user);
		// ɾ���û��б������Ķ�Ӧ����
		listSO.removeAttribute(dis_user_id);
		IScope scope = Red5.getConnectionLocal().getScope();
		Iterator it = scope.getConnections().iterator();
		for (; it.hasNext();) {
			Set connections = (Set) it.next();
			IConnection tempConn = (IConnection) connections.iterator().next();
			if (tempConn instanceof IServiceCapableConnection) {
				IServiceCapableConnection sc = (IServiceCapableConnection) tempConn;
				// �������˵��ÿͻ���flash������
				sc.invoke("disconnectMessage", new Object[] { user });
			}
		}
	}

	// ��Ƶ����
	public void videoInvite(Object[] params) {
		IConnection conn = Red5.getConnectionLocal();
		String user_id = conn.getClient().getId();
		String clientName = (String) listSO.getAttribute(user_id);
		System.out.println("************��Ƶ�������ǣ�" + clientName);
		String sayToName = params[0] == null ? "" : params[0].toString().trim();
		sayToName = "aaa";
		if ("".equals(sayToName) || "All".equals(sayToName))// ����Ϣ�������ҵ�������.
		{
			System.out.println("����������0���߶����");
		} else {
			IConnection tempConn = onlineList.get(sayToName);
			if (tempConn instanceof IServiceCapableConnection) {
				IServiceCapableConnection sc = (IServiceCapableConnection) tempConn;
				sc.invoke("showInviteMessage", new Object[] { clientName + ";" + sayToName });
			}
		}
	}

	// ͬ�������������뷽����
	public void agreeVideoInvite(Object[] params) {
		IConnection conn = Red5.getConnectionLocal();
		System.out.println("<<<<<params length" + params.length);
		// ������
		String inviteUserName = params[0] == null ? "" : params[0].toString().trim();
		// ��������
		String otherUserName = params[1] == null ? "" : params[1].toString().trim();
		System.out.println("***��������:" + inviteUserName);
		System.out.println("***����������:" + otherUserName);
		if ("".equals(inviteUserName))// ����Ϣ�������ҵ�������.
		{
			System.out.println("������");
		} else {
			System.out.println("*********���óɹ�");
			IConnection tempConn = onlineList.get(inviteUserName);
			if (tempConn instanceof IServiceCapableConnection) {
				IServiceCapableConnection sc = (IServiceCapableConnection) tempConn;
				sc.invoke("showVideo", new Object[] { otherUserName });
				System.out.println("*********���ý���");
			}
		}
	}

}

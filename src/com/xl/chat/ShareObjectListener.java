package com.xl.chat;

import java.util.List;
import java.util.Map;

import org.red5.server.api.IAttributeStore;
import org.red5.server.api.so.ISharedObjectBase;
import org.red5.server.api.so.ISharedObjectListener;

public class ShareObjectListener implements ISharedObjectListener {

	public void onSharedObjectClear(ISharedObjectBase arg0) {
		System.out.println("onSharedObjectClear");
	}

	public void onSharedObjectConnect(ISharedObjectBase arg0) {
		System.out.println("onSharedObjectConnect");
	}

	public void onSharedObjectDelete(ISharedObjectBase arg0, String arg1) {
		System.out.println("onSharedObjectDelete");
	}

	public void onSharedObjectDisconnect(ISharedObjectBase arg0) {
		System.out.println("onSharedObjectDisconnect");
	}

	public void onSharedObjectSend(ISharedObjectBase arg0, String arg1, List arg2) {
		System.out.println("onSharedObjectSend");
	}

	public void onSharedObjectUpdate(ISharedObjectBase arg0, IAttributeStore arg1) {
		System.out.println("onSharedObjectUpdate");
	}

	public void onSharedObjectUpdate(ISharedObjectBase arg0, Map<String, Object> arg1) {
		System.out.println(" onSharedObjectUpdate");
	}

	public void onSharedObjectUpdate(ISharedObjectBase arg0, String key, Object value) {
		System.out.println(" 更新共享对象的值" + key + ":" + value);
	}

}

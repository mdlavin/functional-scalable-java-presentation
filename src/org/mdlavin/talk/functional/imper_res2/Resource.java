package org.mdlavin.talk.functional.imper_res2;

import org.mdlavin.talk.functional.IResource;

public class Resource implements IResource {

	private final String data;
	private String reservedBy;

	public Resource(String data) {
		this.data = data;
	}
	
	public void reserve(String reserver) {
		synchronized (this) {
			reservedBy = reserver;
		}
	}
	
	public String getData() {
		return data;
	}
	
	public String reservedBy() {
		synchronized (this) {
			return reservedBy;
		}
	}
	
	public void release() {
		synchronized (this) {
			reservedBy = null;
		}
	}
	
}

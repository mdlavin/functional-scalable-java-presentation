package org.mdlavin.talk.functional.imper_res;

import org.mdlavin.talk.functional.IResource;

public class Resource implements IResource {

	private final String data;
	private transient String reservedBy;

	public Resource(String data) {
		this.data = data;
	}
	
	public void reserve(String reserver) {
		reservedBy = reserver;
	}
	
	public String getData() {
		return data;
	}
	
	public String reservedBy() {
		return reservedBy;
	}
	
	public void release() {
		reservedBy = null;
	}
	
}

package org.mdlavin.talk.functional.func_res3;

import org.mdlavin.talk.functional.IResource;

public class Resource implements IResource {

	private final String data;
	private final String reservedBy;
	
	public Resource(String data) {
		this(data, null);
	}

	public Resource(String data, String reservedBy) {
		this.data = data;
		this.reservedBy = reservedBy;
	}
	
	public Resource reserve(String reserver) {
		return new Resource(data, reserver);
	}
	
	public Resource release() {
		return new Resource(data);
	}
	
	public String reservedBy() {
		return reservedBy;
	}
	
	@Override
	public String getData() {
		return data;
	}
	
}

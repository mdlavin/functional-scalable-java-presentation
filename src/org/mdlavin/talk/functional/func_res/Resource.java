package org.mdlavin.talk.functional.func_res;

import org.mdlavin.talk.functional.IResource;

public class Resource implements IResource {

	private final String data;
	private final int index;
	private final String reservedBy;
	
	public Resource(String data, int index) {
		this(data, index,  null);
	}

	public Resource(String data, int index, String reservedBy) {
		this.data = data;
		this.index = index;
		this.reservedBy = reservedBy;
	}
	
	public Resource reserve(String reserver) {
		return new Resource(data, index, reserver);
	}
	
	public Resource release() {
		return new Resource(data, index);
	}
	
	public String reservedBy() {
		return reservedBy;
	}
	
	@Override
	public String getData() {
		return data;
	}
	
	public int getIndex() {
		return index;
	}
	
}

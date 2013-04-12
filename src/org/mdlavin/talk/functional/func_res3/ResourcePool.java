package org.mdlavin.talk.functional.func_res3;

import fj.Equal;
import fj.data.List;


class ResourcePool {

	private final static Equal<Resource> eq = Equal.anyEqual();

	private final List<Resource> free;
	private final List<Resource> acquired;
	private final int perReserverLimit;
	
	public ResourcePool(int size, int perReserverLimit) {
		this.perReserverLimit = perReserverLimit;
		List<Resource> freeTemp = List.nil();
		for (int i=0;i<size;i++) {
			freeTemp = freeTemp.cons(new Resource("Resource " + i));
		}
		free = freeTemp;
		acquired = List.nil();
	}
	
	private ResourcePool(List<Resource> free, List<Resource> acquired, int perReserverLimit) {
		this.free = free;
		this.acquired = acquired;
		this.perReserverLimit = perReserverLimit;
	}
	
	public AcquireResult acquire(String reserverName) {
		if (free.isEmpty())
			return null;

		int reservedCount = 0;
		List<Resource> temp = acquired;
		while (temp.isNotEmpty()) {
			if (reserverName.equals(temp.head().reservedBy())) {
				reservedCount++;
				if (reservedCount >= perReserverLimit) 
					return null;
			}
			temp = temp.tail();
		}
		
		Resource topFree = free.head();
		List<Resource> restFree = free.tail();
		Resource reserved = topFree.reserve(reserverName);
		List<Resource> newAcquired = acquired.cons(reserved);

		return new AcquireResult(new ResourcePool(restFree, newAcquired, perReserverLimit), reserved);
	}
	
	public ResourcePool release(Resource res) {
		List<Resource> newFree = free.cons(res.release());
		List<Resource> newAcquired = acquired.delete(res, eq);
		return new ResourcePool(newFree, newAcquired, perReserverLimit);
	}
	
	public int numAcquired(String reserverName) {
		int count = 0;
		List<Resource> temp = acquired;
		while (temp.isNotEmpty()) {
			if (reserverName.equals(temp.head().reservedBy())) {
				count++;
			}
			temp = temp.tail();
		}

		return count;
	}
	
	class AcquireResult {
		public final ResourcePool pool;
		public final Resource resource;

		public AcquireResult(ResourcePool pool, Resource resource) {
			this.pool = pool;
			this.resource = resource;
		}
	}
	
}

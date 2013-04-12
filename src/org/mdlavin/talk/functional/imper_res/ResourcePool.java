package org.mdlavin.talk.functional.imper_res;

import org.mdlavin.talk.functional.IResourcePool;

public class ResourcePool implements IResourcePool<Resource> {

	private final Resource[] resources;
	private final int perReserverLimit;
	
	public ResourcePool(int size, int perReserverLimit) {
		resources = new Resource[size];
		this.perReserverLimit = perReserverLimit;
		for (int i=0;i<resources.length;i++) {
			resources[i] = new Resource("Resource " + i);
		}
	}
	
	public Resource acquire(String reserverName) {
		synchronized (this) {
			Resource freeResource = null;
			int reservedCount = 0;
			for (Resource res : resources) {
				String reservedBy = res.reservedBy();
				if (reserverName.equals(reservedBy)) {
					reservedCount++;
					if (reservedCount >= perReserverLimit) 
						return null;
				}

				if (null == reservedBy) {
					freeResource = res;
				}
			}
			
			// At this point, the existing reservation limit has not been hit
			// and all resources have been checked for one that is free
			if (freeResource != null) {
				freeResource.reserve(reserverName);
				return freeResource;
			} else {
				return null;
			}
		}
	}
	
	public void release(Resource res) {
		res.release();
	}
	
	@Override
	public int numAcquired(String reserverName) {
		int reservedCount = 0;
		synchronized (this) {
			for (Resource res : resources) {
				String reservedBy = res.reservedBy();
				if (reserverName.equals(reservedBy)) {
					reservedCount++;
				}
			}	
		}
		return reservedCount;
	}
	
}

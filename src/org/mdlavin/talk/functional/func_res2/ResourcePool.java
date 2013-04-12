package org.mdlavin.talk.functional.func_res2;

class ResourcePool {

	private final Resource[] resources;
	private final int perReserverLimit;
	
	public ResourcePool(int size, int perReserverLimit) {
		this.perReserverLimit = perReserverLimit;
		resources = new Resource[size];
		for (int i=0;i<resources.length;i++) {
			resources[i] = new Resource("Resource " + i, i);
		}
	}
	
	private ResourcePool(Resource[] resources, int perReserverLimit) {
		this.resources = resources;
		this.perReserverLimit = perReserverLimit;
	}
	
	public AcquireResult acquire(String reserverName) {
		int freeResource = -1;
		int reservedCount = 0;

		for (int i=0;i<resources.length;i++) {
			Resource res = resources[i];
			String reservedBy = res.reservedBy();
			if (reserverName.equals(reservedBy)) {
				reservedCount++;
				if (reservedCount >= perReserverLimit) 
					return null;
			}

			if (null == res.reservedBy()) {
				freeResource = i;
			}
			
		}
		
		// At this point, the existing reservation limit has not been hit
		// and all resources have been checked for one that is free
		if (freeResource != -1) {
			Resource[] newResources = new Resource[resources.length];
			System.arraycopy(resources, 0, newResources, 0, resources.length);
			Resource reserved = resources[freeResource].reserve(reserverName);
			newResources[freeResource] = reserved;
			return new AcquireResult(new ResourcePool(newResources, perReserverLimit), reserved);
		} else {
			return null;
		}
	}
	
	public ResourcePool release(Resource res) {
		Resource[] newResources = new Resource[resources.length];
		System.arraycopy(resources, 0, newResources, 0, resources.length);
		Resource released = res.release();
		newResources[res.getIndex()] = released;
		return new ResourcePool(newResources, perReserverLimit);
	}
	
	public int numAcquired(String reserverName) {
		int count=0;
		for (int i=0;i<resources.length;i++) {
			Resource res = resources[i];
			String reservedBy = res.reservedBy();
			if (reserverName.equals(reservedBy)) {
				count++;
			}
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

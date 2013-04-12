package org.mdlavin.talk.functional.func_res;

import org.mdlavin.talk.functional.IResourcePool;
import org.mdlavin.talk.functional.func_res.ResourcePool.AcquireResult;

public class ResourcePoolService implements IResourcePool<Resource> {

	private ResourcePool pool;
	
	public ResourcePoolService(int size, int perReserverLimit) {
		pool = new ResourcePool(size, perReserverLimit);
	}
	
	@Override
	public Resource acquire(String reserverName) {
		ResourcePool poolSnapshot;
		synchronized (this) {
			poolSnapshot = pool;
		}
		
		AcquireResult acquire = poolSnapshot.acquire(reserverName);
		if (acquire == null)
			return null;
		
		synchronized (this) {
			if (poolSnapshot == pool) {
				pool = acquire.pool;
				return acquire.resource;
			} else {
				return null;
			}
		}
	}
	
	@Override
	public void release(Resource res) {
		do {
			ResourcePool poolSnapshot;
			synchronized (this) {
				poolSnapshot = pool;
			}

			ResourcePool releasedPool = poolSnapshot.release(res);
			
			synchronized (this) {
				if (poolSnapshot == pool) {
					pool = releasedPool;
					return;
				}
			}
		} while (true);
	}
	
	@Override
	public int numAcquired(String reserverName) {
		ResourcePool poolSnapshot;
		synchronized (this) {
			poolSnapshot = pool;
		}

		return poolSnapshot.numAcquired(reserverName);
	}
	
}

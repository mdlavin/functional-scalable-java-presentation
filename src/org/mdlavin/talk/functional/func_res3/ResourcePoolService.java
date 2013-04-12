package org.mdlavin.talk.functional.func_res3;

import java.util.concurrent.atomic.AtomicReference;

import org.mdlavin.talk.functional.IResourcePool;
import org.mdlavin.talk.functional.func_res3.ResourcePool.AcquireResult;

public class ResourcePoolService implements IResourcePool<Resource> {

	private AtomicReference<ResourcePool> poolRef;
	
	public ResourcePoolService(int size, int perReserverLimit) {
		poolRef = new AtomicReference(new ResourcePool(size, perReserverLimit));
	}
	
	@Override
	public Resource acquire(String reserverName) {
		ResourcePool poolSnapshot = poolRef.get();
		AcquireResult acquire = poolSnapshot.acquire(reserverName);
		if (acquire == null)
			return null;
		
		if (poolRef.compareAndSet(poolSnapshot, acquire.pool)) {
			return acquire.resource;
		} else {
			return null;
		}
	}
	
	@Override
	public void release(Resource res) {
		do {
			ResourcePool poolSnapshot = poolRef.get();
			ResourcePool releasedPool = poolSnapshot.release(res);
			
			if (poolRef.compareAndSet(poolSnapshot, releasedPool)) {
				return;
			}
		} while (true);
	}
	
	@Override
	public int numAcquired(String reserverName) {
		ResourcePool poolSnapshot = poolRef.get();
		return poolSnapshot.numAcquired(reserverName);
	}
	
}

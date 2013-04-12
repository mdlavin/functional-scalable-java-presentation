package org.mdlavin.talk.functional.imper_res2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.mdlavin.talk.functional.IResourcePool;
import org.mdlavin.talk.functional.imper_res.Resource;

public class ResourcePool implements IResourcePool<Resource> {

	private final Set<Resource> free;
	private final Set<Resource> acquired;
	private final int perReserverLimit;
	
	public ResourcePool(int size, int perReserverLimit) {
		acquired = new HashSet<Resource>();
		free = new HashSet<Resource>();
		this.perReserverLimit = perReserverLimit;
		for (int i=0;i<size;i++) {
			free.add(new Resource("Resource " + i));
		}
	}
	
	public Resource acquire(String reserverName) {
		synchronized (this) {
			if (free.isEmpty())
				return null;


			int reservedCount = 0;
			for (Resource res : acquired) {
				String reservedBy = res.reservedBy();
				if (reserverName.equals(reservedBy)) {
					reservedCount++;
					if (reservedCount >= perReserverLimit) 
						return null;
				}
			}
			
			// At this point, the existing reservation limit has not been hit
			// and all resources have been checked for one that is free
			Iterator<Resource> iterator = free.iterator();
			Resource toReserve = iterator.next();
			iterator.remove();
			
			toReserve.reserve(reserverName);
			acquired.add(toReserve);
			return toReserve;
		}
	}
	
	public void release(Resource res) {
		synchronized (this) {
			acquired.remove(res);
			res.release();
			free.add(res);
		}
	}
	
	@Override
	public int numAcquired(String reserverName) {
		int reservedCount = 0;
		synchronized (this) {
			for (Resource res : acquired) {
				String reservedBy = res.reservedBy();
				if (reserverName.equals(reservedBy)) {
					reservedCount++;
				}
			}	
		}
		return reservedCount;
	}
	
}

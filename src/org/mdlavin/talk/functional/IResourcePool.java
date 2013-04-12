package org.mdlavin.talk.functional;


public interface IResourcePool<R extends IResource> {

	public R acquire(String reserverName);
	public void release(R res);
	public int numAcquired(String reserverName);
	
}

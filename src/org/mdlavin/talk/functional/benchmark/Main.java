package org.mdlavin.talk.functional.benchmark;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.mdlavin.talk.functional.IResource;
import org.mdlavin.talk.functional.IResourcePool;
import org.mdlavin.talk.functional.func_res.ResourcePoolService;

public class Main {

	public static void main(String[] args) {
//		// Generate data per read perecentage
//		for (int i=0;i<=95;i+=5) {
//			int reservationLimit = 1;
//			int threads = Runtime.getRuntime().availableProcessors();
//			int uniqueReservers = Runtime.getRuntime().availableProcessors();
//			int poolSize = threads/2;
//			int time = 2000;
//			int readPerecent=i;
//			int funScore = 0;
//			int imperScore = 0;
//			int fun2Score = 0;
//			int fun3Score = 0;
//			int imper2Score = 0;
//			try { 
//				IResourcePool funPool = new ResourcePoolService(poolSize, reservationLimit);
//				funScore = benchmarkPool(threads, uniqueReservers, readPerecent, time, funPool);
//				
//				IResourcePool impPool = new org.mdlavin.talk.functional.imper_res.ResourcePool(poolSize, reservationLimit);
//				imperScore = benchmarkPool(threads, uniqueReservers, readPerecent, time, impPool);
//
//				IResourcePool funPool2 = new org.mdlavin.talk.functional.func_res2.ResourcePoolService(poolSize, reservationLimit);
//				fun2Score = benchmarkPool(threads, uniqueReservers, readPerecent, time, funPool2);
//
//				IResourcePool funPool3 = new org.mdlavin.talk.functional.func_res3.ResourcePoolService(poolSize, reservationLimit);
//				fun3Score = benchmarkPool(threads, uniqueReservers, readPerecent, time, funPool3);
//
//				IResourcePool impPool2 = new org.mdlavin.talk.functional.imper_res2.ResourcePool(poolSize, reservationLimit);
//				imper2Score = benchmarkPool(threads, uniqueReservers, readPerecent, time, impPool2);
//
//				System.out.println(String.format("%d\t%f\t%f\t%f\t%f\t%f", readPerecent, funScore/(double)time, imperScore/(double)time, fun2Score/(double)time, fun3Score/(double)time, imper2Score/(double)time));
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		

		// Generate data for CPU scale at 90% readers
		for (int i=1;i<=Runtime.getRuntime().availableProcessors();i+=1) {
			int reservationLimit = 1;
			int threads = i;
			int uniqueReservers = threads;
			int poolSize = 20;
			int time = 5000;
			int readPerecent=90;
			int funScore = 0;
			int imperScore = 0;
			int fun2Score = 0;
			int fun3Score = 0;
			int imper2Score = 0;
			try { 
				IResourcePool funPool = new ResourcePoolService(poolSize, reservationLimit);
				funScore = benchmarkPool(threads, uniqueReservers, readPerecent, time, funPool);
				
				IResourcePool impPool = new org.mdlavin.talk.functional.imper_res.ResourcePool(poolSize, reservationLimit);
				imperScore = benchmarkPool(threads, uniqueReservers, readPerecent, time, impPool);

				IResourcePool funPool2 = new org.mdlavin.talk.functional.func_res2.ResourcePoolService(poolSize, reservationLimit);
				fun2Score = benchmarkPool(threads, uniqueReservers, readPerecent, time, funPool2);

				IResourcePool funPool3 = new org.mdlavin.talk.functional.func_res3.ResourcePoolService(poolSize, reservationLimit);
				fun3Score = benchmarkPool(threads, uniqueReservers, readPerecent, time, funPool3);

				IResourcePool impPool2 = new org.mdlavin.talk.functional.imper_res2.ResourcePool(poolSize, reservationLimit);
				imper2Score = benchmarkPool(threads, uniqueReservers, readPerecent, time, impPool2);

				System.out.println(String.format("%d\t%f\t%f\t%f\t%f\t%f", threads, funScore/(double)time, imperScore/(double)time, fun2Score/(double)time, fun3Score/(double)time, imper2Score/(double)time));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	private static int benchmarkPool(int threadCount, final int uniqueReservers, final int readPercent, int time, final IResourcePool pool) throws InterruptedException {
		if (readPercent > 100) {
			throw new IllegalArgumentException("The read perecentage must be <= 100, but was " + readPercent);
		}
		
		final Random random = new Random();
		final AtomicInteger count = new AtomicInteger();
		final AtomicBoolean done = new AtomicBoolean(false);
		Thread[] threads = new Thread[threadCount];
		for (int i=0;i<threadCount;i++) {
			final String name = "Reserver " + (i % uniqueReservers);
			threads[i] = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while (done.get() == false) {
						int r = random.nextInt(100);
						if (r < readPercent) {
							pool.numAcquired(name);
							count.incrementAndGet();
						} else {
							IResource res = pool.acquire(name);
							if (res != null) {
								count.incrementAndGet();
								pool.release(res);
							}
						}
					}
				}
			}, name);
		}
		
		for (Thread t : threads) {
			t.start();
		}
		
		Thread.sleep(time);
		
		done.set(true);
		
		for (Thread t : threads) {
			t.join();
		}
		
		return count.get();
	}
	
}

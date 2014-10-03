package com.rahul;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Vm;

public class VmsCreator {

	public List<Vm> createRequiredVms(int numVms, int brokerId) {
		
		
		ArrayList<Vm> vmList= new ArrayList<Vm>();
		
		
		String vmm = "Xen";
		long size = 10000;
		long bw = 1000;
		int ram = 512;
		int numberOfPes = 1;
		double mips = 1000;
		
		for (int i = 0; i < numVms; i++) {
			
			vmList.add(new Vm(i, brokerId, mips, numberOfPes, ram, bw, size, vmm, new CloudletSchedulerSpaceShared()));
		
		}
		return vmList;
	}	
}

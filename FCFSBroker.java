package com.rahul;

import org.cloudbus.cloudsim.DatacenterBroker;

public class FCFSBroker extends DatacenterBroker {

	public FCFSBroker(String name) throws Exception {
		//calling superclass constructor
		super(name);
	}
	
	public void scheduleTaskstoVms(int numTasks, int numVms)
	{
		for (int i=0;i<numTasks;i++) {
			bindCloudletToVm(i, i%numVms);
		}
	}
}

package com.rahul;

import java.util.ArrayList;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

public class CloudletCreator {
	
	public ArrayList<Cloudlet> createRequiredCloulets(int numTasks, int brokerId)
	{
		ArrayList<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
		
		Cloudlet task = null;
		for (int i = 0; i < numTasks; i++) {
			UtilizationModel utilizationModel = new UtilizationModelFull();
			long cloudletOutputSize = 200; 
			int pesNumber = 1;
			long len = 2000;
			long cloudletFileSize = 200;
			if(i == numTasks - 3)
				task = new Cloudlet(i, len *20, pesNumber , cloudletFileSize , cloudletOutputSize , utilizationModel , utilizationModel, utilizationModel);
			else
				task = new Cloudlet(i, len , pesNumber , cloudletFileSize , cloudletOutputSize , utilizationModel , utilizationModel, utilizationModel);
			task.setUserId(brokerId);
			cloudletList.add(task);
		}
		return cloudletList;
		
	}
	

}

package com.rahul;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisioner;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class Main {
	
	//Task allocation on basis of first-come first-serve policy
	
	private static List<Cloudlet> cloudletList;
	private static List<Vm> vmList;
	private static int numTasks = 7;
	private static int numVms = 2;
	
	
	public static void main(String[] args) {
		
		//initialize CloudSim
		int numUser = 1;
		Calendar cal = Calendar.getInstance();
		boolean traceFlag = false;
		CloudSim.init(numUser , cal , traceFlag );
		
		//create one datacenter
		@SuppressWarnings("unused")
		Datacenter dc0 = createDatacenter("Datacenter_0");
		
		/*create Broker - abstraction of User
		 * we will create our own FCFSBroker that extends DatacenterBroker
		 */
		FCFSBroker brk0 = createBroker("Broker_0");
		int brokerId = brk0.getId();
		
		vmList = new VmsCreator().createRequiredVms(numVms, brokerId);
		brk0.submitVmList(vmList);
		
		cloudletList = new CloudletCreator().createRequiredCloulets(numTasks, brokerId);
		brk0.submitCloudletList(cloudletList);
		
		
		//This is the method that implements FcFs
		brk0.scheduleTaskstoVms(numTasks, numVms);
		
		CloudSim.startSimulation();
		CloudSim.stopSimulation();
		
		List<Cloudlet> receivedList = brk0.getCloudletReceivedList();
		//Print result
		printCloudletList(receivedList);

	}
	
	
	private static FCFSBroker createBroker(String name)
	{
		FCFSBroker brk = null;
		
		try {
			brk = new FCFSBroker(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return brk;
	}
	private static Datacenter createDatacenter(String name)
	{
		Datacenter dc = null;

		//Datacenter consist of hosts
		ArrayList<Host> hostList = new ArrayList<Host>();
		
		//We are making a one host data center with a super powerful Pe
		ArrayList<Pe> peList = new ArrayList<Pe>();
		
		double mips = 1000*numVms;
		PeProvisioner peProvisioner = new PeProvisionerSimple(mips);
		peList.add(new Pe(0, peProvisioner));
		
		VmScheduler vmScheduler = new VmSchedulerTimeShared(peList);
		long storage = 1000000;
		long bw = 10000;
		BwProvisioner bwProvisioner = new BwProvisionerSimple(bw);
		int ram = 2048;
		RamProvisioner ramProvisioner = new RamProvisionerSimple(ram);
		int id = 0;
		hostList.add(new Host(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler));
		
		//Creating Datacenter
		
		
		String architecture = "x64";
		String os = "Linux";
		String vmm = "Xen";
		double timeZone = 10;
		double costPerSec = 3;
		double costPerMem = 0.05;
		double costPerStorage = 0.001;
		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(architecture , os , vmm , hostList, timeZone , costPerSec , costPerMem , costPerStorage, costPerStorage);
		
		VmAllocationPolicy vmAllocationPolicy = new VmAllocationPolicySimple(hostList);
		List<Storage> storageList = new ArrayList<Storage>();
		try {
			dc  = new Datacenter(name, characteristics, vmAllocationPolicy , storageList , 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dc;
	}
	
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Data center ID" + indent + "VM ID" + indent + "Time" + indent
				+ "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");

				Log.printLine(indent + indent + cloudlet.getResourceId()
						+ indent + indent + indent + cloudlet.getVmId()
						+ indent + indent
						+ dft.format(cloudlet.getActualCPUTime()) + indent
						+ indent + dft.format(cloudlet.getExecStartTime())
						+ indent + indent
						+ dft.format(cloudlet.getFinishTime()));
			}
		}
	}
	
	

}

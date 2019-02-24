package net.floodlightcontroller.ipblacklist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.*;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.types.SwitchMessagePair;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.restserver.IRestApiService;
import net.floodlightcontroller.util.ConcurrentCircularBuffer;

import org.projectfloodlight.openflow.protocol.OFFlowMod;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.OFBufferId;



public class IPBlacklist implements IOFMessageListener, IFloodlightModule, IPBlacklistService {

    protected IFloodlightProviderService floodlightProvider;  
    protected ArrayList<String> blacklist;
    protected IRestApiService restApi;
    protected ConcurrentCircularBuffer<SwitchMessagePair> buffer;
    protected String newIPaddress;


    @Override  

    public String getName() {  
System.out.println(IPBlacklist.class.getSimpleName());
        return IPBlacklist.class.getSimpleName();  

    }  

    @Override  

    public Collection<Class<? extends IFloodlightService>> getModuleDependencies()    {  

        Collection<Class<? extends IFloodlightService>> l =  

                new ArrayList<Class<? extends IFloodlightService>>();  

            l.add(IFloodlightProviderService.class);  

            l.add(IRestApiService.class);

        return l;  

  

    }  

  
    @Override  

    public boolean isCallbackOrderingPrereq(OFType type, String name) {  

        return false;  

    }  

  

    @Override  

    public boolean isCallbackOrderingPostreq(OFType type, String name) {  

        return false;  

    }  
    
    @Override
    public ConcurrentCircularBuffer<SwitchMessagePair> getBuffer() {
        return buffer;
    }

  

    @Override
    public Collection<Class<? extends IFloodlightService>> getModuleServices() {
        Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
        l.add(IPBlacklistService.class);
        return l;
    }

    @Override
    public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
        Map<Class<? extends IFloodlightService>, IFloodlightService> m = new HashMap<Class<? extends IFloodlightService>, IFloodlightService>();
        m.put(IPBlacklistService.class, this);
        return m;
    }

  


@Override  

    public void init(FloodlightModuleContext context) throws FloodlightModuleException {  

     floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);  
     restApi = context.getServiceImpl(IRestApiService.class);
     buffer = new ConcurrentCircularBuffer<SwitchMessagePair>(SwitchMessagePair.class, 100);  

     blacklist = new ArrayList<String>();

	    try {
OpenBlacklist();
System.out.println("The IP Blacklist: " + Arrays.toString(blacklist.toArray()));

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}}


@Override  

public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {  

    floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);  
    restApi.addRestletRoutable(new IPBlacklistWebRoutable());
System.out.println("in start up class");

   }
    @Override
	public void setIPAddress(String newIP){
		
		System.out.println("IP address fetched from user input = " + newIP);	
		if(newIP != null){
			
			newIPaddress = newIP;
			
			FileWriter file;
			try {
				file = new FileWriter("blacklist.txt",true);
				file.write(newIPaddress + "\n");
				blacklist.add(newIPaddress);
				file.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
    

	

	public void OpenBlacklist() throws IOException {
		File file = new File("blacklist.txt");

		Scanner s = new Scanner(file);

		while (s.hasNext()){

			blacklist.add(s.next());
			System.out.println("Reading the file..");

		}

		s.close();

	}

    protected static final short FLOWMOD_IDLE_TIMEOUT = 3;    
    protected static final short FLOWMOD_HARD_TIMEOUT = 0;
    protected static final short FLOWMOD_PRIORITY = 100;
    
 

    
	private void dropFlowMod(IOFSwitch sw, Match match) {

        OFFlowMod.Builder fmb;
		List<OFAction> actions = new ArrayList<OFAction>(); // set no action to drop

        fmb = sw.getOFFactory().buildFlowAdd();
        fmb.setMatch(match);
        fmb.setIdleTimeout(FLOWMOD_IDLE_TIMEOUT);
        fmb.setHardTimeout(FLOWMOD_HARD_TIMEOUT);
        fmb.setPriority(FLOWMOD_PRIORITY);
    
        fmb.setBufferId(OFBufferId.NO_BUFFER);        
        fmb.setActions(actions);

        
        sw.write(fmb.build());
    }


    @Override  

    public Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx)       {  

System.out.println("Entering receive method!!");

        Ethernet eth =  

                IFloodlightProviderService.bcStore.get(cntx,  

                        IFloodlightProviderService.CONTEXT_PI_PAYLOAD); 
   
        if (eth.getEtherType() == EthType.IPv4) {

          	IPv4 ipv4 = (IPv4) eth.getPayload();

    IPv4Address dstIp = ipv4.getDestinationAddress();
  
 
String dstIPString = dstIp.toString();


int n= blacklist.size();

for(int i=0;i<n;i++){	
	 
	if(blacklist.get(i).equals(dstIPString)){
			System.out.println(blacklist.get(i));
	  System.out.println("Blacklisted IP address: " + dstIPString);
	  OFPacketIn pi = (OFPacketIn) msg;
	  Match m = pi.getMatch();
	   dropFlowMod(sw, m);
		buffer.add(new SwitchMessagePair(sw, msg));
		
  	return Command.STOP;

	  
  }}
        }
  return Command.CONTINUE;

}



	


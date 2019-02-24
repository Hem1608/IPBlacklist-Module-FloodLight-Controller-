package net.floodlightcontroller.ipblacklist;


import java.util.ArrayList;
import java.util.List;
import net.floodlightcontroller.core.types.SwitchMessagePair;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class IPBlacklistResource extends ServerResource{
	
	
	@Get("json")
	public List<SwitchMessagePair> retrieve() {
		System.out.print("2. Entering Resouce class!!");
	
	     IPBlacklistService pihr = (IPBlacklistService)getContext().getAttributes().get(IPBlacklistService.class.getCanonicalName());
	     List<SwitchMessagePair> l = new ArrayList<SwitchMessagePair>();
	     l.addAll(java.util.Arrays.asList(pihr.getBuffer().snapshot()));
	     System.out.println("Done addding !!");
	     return l;    }
		

	
}


package net.floodlightcontroller.ipblacklist;


import org.restlet.resource.ServerResource;



public class IPBlacklistResourceBase extends ServerResource {
    IPBlacklistService getIPBlacklistService() {
	return (IPBlacklistService)getContext().getAttributes().
	        get(IPBlacklistService.class.getCanonicalName());
    }
}

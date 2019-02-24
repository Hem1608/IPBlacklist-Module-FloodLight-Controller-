package net.floodlightcontroller.ipblacklist;

import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import net.floodlightcontroller.restserver.RestletRoutable;

public class IPBlacklistWebRoutable implements RestletRoutable {

	
	@Override
	public Restlet getRestlet(Context context) {
System.out.println("1. Entering REstlet class and getRestlet method!");
        Router router = new Router(context);
        router.attach("/add/ipaddress/json", IPBlacklistAddResource.class);
        router.attach("/history/json", IPBlacklistResource.class);
     
        return router;
    }
	
    @Override
    public String basePath() {
        return "/wm/ipblacklist";

}
    }

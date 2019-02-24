package net.floodlightcontroller.ipblacklist;

import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.types.SwitchMessagePair;
import net.floodlightcontroller.util.ConcurrentCircularBuffer;

public interface IPBlacklistService extends IFloodlightService{

	public ConcurrentCircularBuffer<SwitchMessagePair> getBuffer();
	public String getIPAddress();
	 public void setIPAddress(String newIP);
   
}

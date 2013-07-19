package com.mwr.dz;

import java.math.BigInteger;
import java.security.SecureRandom;

import android.content.Context;

import com.mwr.jdiesel.api.DeviceInfo;
import com.mwr.jdiesel.api.connectors.Endpoint;
import com.mwr.jdiesel.api.links.Client;

public class Agent {
	
	private DeviceInfo device_info;
	private Endpoint endpoint;
	private Client client;
	private String uid;
	
	public Agent(String host, int port) {
		this.device_info = new DeviceInfo(this.getUID(),
				android.os.Build.MANUFACTURER,
				"unknown",
				"unknown");
		this.endpoint = new Endpoint(
				1,
				"drozer Server",
				host,
				port,
				false,
				"",
				"",
				"");
	}
	
	public static void main(String[] args) {
		if(args.length == 2)
			new Agent(args[0], Integer.parseInt(args[1])).run();
		else
			System.out.println("usage: agent.jar host port");
	}
	
	public static Context getContext() {
		return null;
	}
	
	public Context getMercuryContext() {
		return null;
	}

	public String getUID() {
		// we cannot request the ANDROID_ID, because we have no context, so
		// we must generate one at random
		if(this.uid == null)
			this.uid = new BigInteger(64, new SecureRandom()).toString(32);

		return this.uid;
	}
	
	public void run() {
		this.client = new Client(this.endpoint, this.device_info);
		this.client.start();
	}

}

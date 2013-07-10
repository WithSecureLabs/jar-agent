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
	
	public static void main(String[] args) {
		new Agent().run();
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
		this.device_info = new DeviceInfo(this.getUID(),
				android.os.Build.MANUFACTURER,
				"unknown",
				"unknown");
		this.endpoint = new Endpoint(
				1,
				"drozer Server",
				"10.0.2.2",
				31415,
				false,
				"",
				"",
				"");
		this.client = new Client(this.endpoint, this.device_info);
		this.client.start();
	}

}

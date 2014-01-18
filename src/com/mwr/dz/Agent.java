package com.mwr.dz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
		this.device_info = new DeviceInfo(
				this.getUID(),
				android.os.Build.MANUFACTURER,
				android.os.Build.MODEL,
				android.os.Build.VERSION.RELEASE);
		this.endpoint = new Endpoint(
				1,
				"drozer Server",
				host,
				port,
				false,
				"",
				"",
				"",
				true);
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

	private String loadUID(){
		String uid = null;

		File uid_file = new File("uid.txt");
		if(uid_file.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(uid_file)));
				uid = reader.readLine();
				reader.close();
			}
			catch(IOException e) { }
		}

		return uid;
	}
    
	private String createUID(){
		String uid = new BigInteger(64, new SecureRandom()).toString(32);

		File uid_file = new File("uid.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(uid_file)));
			writer.append(uid);
			writer.flush();
			writer.close();
		}
		catch(IOException e) { }

		return uid;
	}

	public String getUID() {
		// we cannot request the ANDROID_ID, because we have no context, so
		// we must generate one at random
		
		// first, check if there is a stored UID that we can load...
        this.uid = this.loadUID();
        // ... if not then create one
		if(this.uid == null)
			this.uid = createUID();

		return this.uid;
	}
	
	public void run() {
		this.client = new Client(this.endpoint, this.device_info);
		this.client.start();
	}

}

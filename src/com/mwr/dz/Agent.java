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
import android.util.Log;

import com.mwr.jdiesel.api.DeviceInfo;
import com.mwr.jdiesel.api.connectors.Endpoint;
import com.mwr.jdiesel.api.links.Client;

public class Agent {
	
	private static Context context;
	
	private DeviceInfo device_info;
	private Endpoint endpoint;
	private Client client;
	private String uid;
	
	public Agent(String host, int port) {
		Agent.context = null;
		
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
	
	public Agent(String host, int port, Context applicationContext)
	{
		Agent.context = applicationContext;
		
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
	
	public Agent(String host, int port, String packageName, Context systemContext)
	{
		try
		{
			Agent.context = systemContext.createPackageContext(packageName, 2);
			
			Log.i("drozer", "Got package context for " + packageName);
		}
		catch (Exception e)
		{
			Log.e("drozer", "Could not get package context for " + packageName);
			Log.d("drozer", e.getMessage());
		}
		
		if (Agent.context == null)
		{
			try
			{
				Agent.context = systemContext.getApplicationContext();
				
				Log.i("drozer", "Got system context");
			}
			catch (Exception e)
			{
				Log.e("drozer", "Could not get system context");
				Log.d("drozer", e.getMessage());
			}
		}
		
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
		return Agent.context;
	}
	
	public Context getMercuryContext() {
		return Agent.context;
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
		if(this.uid == null)
			this.uid = createUID();

		return this.uid;
	}
	
	public void run() {
		this.client = new Client(this.endpoint, this.device_info);
		this.client.start();
	}

}

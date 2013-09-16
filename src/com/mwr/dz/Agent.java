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
import java.net.URISyntaxException;
import java.security.SecureRandom;

import android.content.Context;
import android.util.Log;

import com.mwr.jdiesel.api.DeviceInfo;
import com.mwr.jdiesel.api.connectors.Endpoint;
import com.mwr.jdiesel.api.links.Client;

public class Agent {
	
	private DeviceInfo device_info;
	private Endpoint endpoint;
	private Client client;
	private String uid;
	
	private String jar_path;
	
	public Agent(String host, int port) {
		//this.jar_path = Agent.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		this.jar_path = Agent.getJarPath();
		Log.i("weasel", jar_path);
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
	
	private static String getJarPath(){
		
		return "";
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
	    	BufferedReader reader;
	        if(uid_file.exists()){
	            try{
	                reader = new BufferedReader(new InputStreamReader(new FileInputStream(uid_file)));
	                uid = reader.readLine();
	                reader.close();
	            }catch(IOException e){
	            }
	        }
        
        return uid;
    }
    
    private String createUID(){
    	String uid = new BigInteger(64, new SecureRandom()).toString(32);
    	
    	File uid_file = new File("uid.txt");
    	try{
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(uid_file)));
            writer.append(uid);
            writer.flush();
            writer.close();
        }catch(IOException e){
        }
        
        return uid;
    }

	public String getUID() {
		// we cannot request the ANDROID_ID, because we have no context, so
		// we must generate one at random
        this.uid = this.loadUID();
		if(this.uid == null)
			this.uid = createUID();

		return this.uid;
	}
	
	public void run() {
		this.client = new Client(this.endpoint, this.device_info);
		this.client.start();
	}

}

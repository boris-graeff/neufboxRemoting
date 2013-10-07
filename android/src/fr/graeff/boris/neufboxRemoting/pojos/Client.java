package fr.graeff.boris.neufboxRemoting.pojos;

public class Client
{
	private String ip;
	private String mac;
	
	public Client(String ip, String mac)
	{
		this.ip = ip;
		this.mac = mac;
	}
	
	public String getIp()
	{
		return ip;
	}
	
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	
	public String getMac()
	{
		return mac;
	}
	
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	
}

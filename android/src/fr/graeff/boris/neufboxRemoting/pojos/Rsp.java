package fr.graeff.boris.neufboxRemoting.pojos;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(strict=false)
public class Rsp
{
	@Attribute
	private String stat;
	
	@Attribute
	private String version;
	
	@Element(required=false)
	private Auth auth;
	
	@Element(required=false)
	private Err err;

	public Rsp()
	{
		super();
	}
	
	public Rsp(String stat, String version, Auth auth)
	{
		super();
		this.stat = stat;
		this.version = version;
		this.auth = auth;
	}

	public String getStat()
	{
		return stat;
	}

	public void setStat(String stat)
	{
		this.stat = stat;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public Auth getAuth()
	{
		return auth;
	}

	public void setAuth(Auth auth)
	{
		this.auth = auth;
	}

	public Err getErr()
	{
		return err;
	}

	public void setErr(Err err)
	{
		this.err = err;
	}
	
	
	
}

package fr.graeff.boris.neufboxRemoting.pojos;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict=false)
public class Err
{

	@Attribute
	private int code;
	
	@Attribute
	private String msg;
	
	public Err()
	{
		super();
	}

	public Err(int code, String msg)
	{
		super();
		this.code = code;
		this.msg = msg;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	
	
}
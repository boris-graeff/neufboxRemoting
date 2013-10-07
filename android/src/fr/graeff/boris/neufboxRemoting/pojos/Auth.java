package fr.graeff.boris.neufboxRemoting.pojos;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(strict=false)
public class Auth
{

	@Attribute
	private String token;
	
	@Attribute(required=false)
	private String method;
	
	public Auth()
	{
		super();
	}

	public Auth(String token, String method)
	{
		super();
		this.token = token;
		this.method = method;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}
	
}
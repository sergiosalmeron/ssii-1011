package tads;

public class ParamsConexionBD {

	private String user;
	private String pass;
	private String url;
	
	
	public ParamsConexionBD(String user, String pass, String url) {
		super();
		this.user = user;
		this.pass = pass;
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}

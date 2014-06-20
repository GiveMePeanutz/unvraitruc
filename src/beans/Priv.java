package beans;

import java.io.Serializable;

public class Priv implements Serializable {

	private int privID;
	private String privName;
	private String privDescrition;
	
	public int getPrivID() {
		return privID;
	}
	public void setPrivID(int privID) {
		this.privID = privID;
	}
	public String getPrivName() {
		return privName;
	}
	public void setPrivName(String privName) {
		this.privName = privName;
	}
	public String getPrivDescription() {
		return privDescrition;
	}
	public void setPrivDescrition(String privDescrition) {
		this.privDescrition = privDescrition;
	}
	
	
}

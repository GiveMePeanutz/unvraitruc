package beans;

public class KmeansDataType {
	
	private String username;
	private double[] data;
	private double[] cluster;
	
	public KmeansDataType(String u, double[] d){
		this.username = u;
		this.data = d;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public double[] getData() {
		return data;
	}
	public void setData(double[] data) {
		this.data = data;
	}
	public double[] getCluster() {
		return cluster;
	}
	public void setCluster(double[] cluster) {
		this.cluster = cluster;
	}
	
	
	
	
}
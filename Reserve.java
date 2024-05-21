package movieProject;

public class Reserve {
	private int rno;
	private String rtitle;
	private String rgenre;
	
	public int getRno() {
		return rno;
	}
	public void setRno(int rno) {
		this.rno = rno;
	}
	public String getRtitle() {
		return rtitle;
	}
	public void setRtitle(String rtitle) {
		this.rtitle = rtitle;
	}
	public String getRgenre() {
		return rgenre;
	}
	public void setRgenre(String rgenre) {
		this.rgenre = rgenre;
	}
	
	@Override
	public String toString() {
		return "Reserve [rno=" + rno + ", rtitle=" + rtitle +
				", rgenre=" + rgenre + "]";
	}
}

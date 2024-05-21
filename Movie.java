package movieProject;

public class Movie {
	private int mno;
	private String mtitle;
	private String mgenre;
	
	public int getMno() {
		return mno;
	}
	public void setMno(int mno) {
		this.mno = mno;
	}
	public String getMtitle() {
		return mtitle;
	}
	public void setMtitle(String mtitle) {
		this.mtitle = mtitle;
	}
	public String getMgenre() {
		return mgenre;
	}
	public void setMgenre(String mgenre) {
		this.mgenre = mgenre;
	}

	@Override
	public String toString() {
		return "Moive [mno=" + mno + ", mtitle=" + mtitle + 
				", mgenre=" + mgenre + "]";
	}
}

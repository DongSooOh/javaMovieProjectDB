package movieProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class MovieExam {
	
	private Scanner scan = new Scanner(System.in);
	private Connection conn;
	private final String adminId = "dhehdtn";
	private final String adminPwd = "1q2w3e";	
	
	public MovieExam() {
		try {
			// JDBC Driver ���
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// �����ϱ�
			conn = DriverManager.getConnection(
					// useUnicode=true&characterEncoding=utf8: DB �ѱ� ���� �ذ�
					"jdbc:mysql://localhost:3306/movieproject?useUnicode=true&characterEncoding=utf8",
					"root",
					"1234"
			);
		} catch (Exception e) {
			e.printStackTrace();
			exit();
		}
	}
	
	public void member() {
		System.out.println();
		System.out.println("[ȸ�� �޴�]");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("1.�α��� | 2.ȸ������ | 3.ȸ������ | 4.��ü ȸ������ | 5.�����ϱ�");
		System.out.println("-----------------------------------------------------------------");
		System.out.print("�޴�����:");
		
		String memberNo = scan.nextLine();
		System.out.println();
		
		switch(memberNo) {
			case "1" -> login();
			case "2" -> create();
			case "3" -> read();
			case "4" -> clear();
			case "5" -> exit();
			default -> {
				System.out.println("�߸��� �����Դϴ�. 1 ~ 5�� �� �������ּ���.");
				member();
			}
		}		
	}
	
	public void memlist() {
		System.out.println();
		System.out.println("[ȸ�� ���]");
		System.out.println("-----------------------------------------------------------------");
		System.out.printf("%-6s%-12s%-16s%-40s\n", "���̵�", "�̸�", "email", "��������");
		System.out.println("-----------------------------------------------------------------");
		
		try {
			String sql = "SELECT id, name, email, joinDate FROM t_member order by joinDate desc" ;
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Member member = new Member();
				String id = rs.getString("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				
				member.setId(id);
				member.setName(name);
				member.setEmail(email);
				member.setJoinDate(joinDate);
				System.out.printf("%-6s%-12s%-16s%-40s\n", member.getId(), member.getName(),
									member.getEmail(), member.getJoinDate());
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
		member();
	}
	
	public void login() {
		System.out.print("���̵�: ");
		String id = scan.nextLine();
		System.out.print("��й�ȣ: ");
		String pwd = scan.nextLine();
		
		if (id.equals(adminId) && pwd.equals(adminPwd)) {
			System.out.println("������ ���̵�� �α��εǾ����ϴ�.");
			adminMenu();
		} else {
			try {
				String logSql = "SELECT id, pwd FROM t_member WHERE id=? AND pwd=?";
				PreparedStatement pstmt = conn.prepareStatement(logSql);
				pstmt.setString(1, id);
				pstmt.setString(2, pwd);
				ResultSet rs = pstmt.executeQuery();
				
				if (rs.next()) {
					System.out.println("�Ϲ� ����ڷ� �α��εǾ����ϴ�.");
					menu(id);
				} else {
					System.out.println("���̵� �Ǵ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
					member();
				}
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				exit();
			}
		} 
	}
	
	public void create() {
		Member Member = new Member();
		System.out.println("[ȸ������]");
		
		System.out.print("���̵�: ");
		Member.setId(scan.nextLine());
		
		System.out.print("��й�ȣ: ");
		Member.setPwd(scan.nextLine());
		
		System.out.print("�̸�: ");
		Member.setName(scan.nextLine());
		
		System.out.print("email: ");
		Member.setEmail(scan.nextLine());
		
		System.out.println("-----------------------------------------------------------------");
		System.out.println("�����Ͻðڽ��ϱ�? 1.OK | 2.Cancel");
		System.out.print("�޴�����: ");
		String menuNo = scan.nextLine();
		
		if(menuNo.equals("1")) {
			try {
				String sql = "" + "INSERT INTO t_member(id, pwd, name, email, joinDate) " 
								+ "VALUES(?, ?, ?, ?, now())";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, Member.getId());
				pstmt.setString(2, Member.getPwd());
				pstmt.setString(3, Member.getName());
				pstmt.setString(4, Member.getEmail());
				pstmt.executeUpdate();	// executeUpdate(): DB�� ���� �����͸� �����ϱ� ���� ����  
				pstmt.close();
			} catch (Exception e) {
				System.out.println("�ߺ��� ���̵��Դϴ�.");
				create();
			}
		}
		memlist();
	}
	
	public void read() {
		System.out.println("[ȸ�� ����]");
		System.out.print("���̵�: ");
		String _id = scan.nextLine();
		
		try {
			String sql = "SELECT id, name, email, joinDate FROM t_member WHERE id=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, _id);
			ResultSet rs = pstmt.executeQuery(); // executeQuery(): DB�� ���� �����͸� ��ȸ�ϱ� ���� ���� 
			
			if (rs.next()) {
				Member Member = new Member();
				String id = rs.getString("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				
				Member.setId(id);
				Member.setName(name);
				Member.setEmail(email);
				Member.setJoinDate(joinDate);
				
				System.out.println("###############");
				System.out.println("���̵�: " + Member.getId());
				System.out.println("�̸�: " + Member.getName());
				System.out.println("email: " + Member.getEmail());
				System.out.println("���Գ�¥: " + Member.getJoinDate());
				
				System.out.println("-----------------------------------------------------------------");
				System.out.println("ȸ������: 1.Update | 2.Delete | 3.List");
				System.out.print("�޴�����: ");
				
				String memberNo = scan.nextLine();
				System.out.println();
				
				if(memberNo.equals("1")) {
					update(Member);
				} else if (memberNo.equals("2")) {
					delete(Member);
				}
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			exit();
		}
		memlist();
	}
	
	public void update(Member Member) {
		
		System.out.println("[���� ���� �Է�]");
		System.out.print("�̸�: ");
		Member.setName(scan.nextLine());
		System.out.print("��й�ȣ: ");
		Member.setPwd(scan.nextLine());
		System.out.print("email: ");
		Member.setEmail(scan.nextLine());
		
		System.out.println("-----------------------------------------------------------------");
		System.out.println("�����Ͻðڽ��ϱ�? 1. OK | 2.Cancel");
		System.out.print("�޴�����: ");
		String memberNo = scan.nextLine();
		
		if (memberNo.equals("1")) {
			try {
				String sql = "" + "UPDATE t_member SET pwd=?, name=?, email=? WHERE id=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, Member.getPwd());
				pstmt.setString(2, Member.getName());
				pstmt.setString(3, Member.getEmail());
				pstmt.setString(4, Member.getId());
				pstmt.executeUpdate();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
				exit();
			}
		}
		memlist();
	}
	
	public void delete(Member member) {
		try {
			String sql = "DELETE FROM t_member WHERE id=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e){
			e.printStackTrace();
			exit();
		}
		memlist();
	}
	
	public void clear() {
		System.out.println("[ȸ�� ��ü����]");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("ȸ����ü�� �����Ͻðڽ��ϱ�? 1.OK | 2.Cancel");
		System.out.print("�޴�����: ");
		String memberNo = scan.nextLine();
		
		if (memberNo.equals("1")) {
			try {
				String sql = "TRUNCATE TABLE t_member";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
				exit();
			}
		}
		memlist();
	}
	
	public void adminMenu() {
		System.out.println();
		System.out.println("[������ �޴�]");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("1.��ȭ ��� | 2.��ȭ ���� | 3.�α׾ƿ�");
		System.out.println("-----------------------------------------------------------------");
		System.out.print("�޴�����: ");
		
		String adminMenuNo = scan.nextLine();
		System.out.println();
		
		switch(adminMenuNo) {
			case "1" -> addMovie();
			case "2" -> deleteMovie();
			case "3" -> member();
			default -> {
				System.out.println("�߸��� �����Դϴ�. 1 ~ 3�� �� �������ּ���.");
				adminMenu();
			}
		}
	}
	
	public void movieList() {
		System.out.println("[��ȭ ���]");
		try {
			String sql = "SELECT mno, mtitle, mgenre FROM t_movie";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			System.out.println("-----------------------------------------------------------------");
			System.out.printf("%-6s%-12s%-16s\n", "��ȣ", "����", "�帣");
			System.out.println("-----------------------------------------------------------------");
			while (rs.next()) {
				int mno = rs.getInt("mno");
				String mtitle = rs.getString("mtitle");
				String mgenre = rs.getString("mgenre");
				
				System.out.printf("%-6s%-12s%-16s\n", mno, mtitle, mgenre);
			}
			System.out.println("-----------------------------------------------------------------");
			
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
	}
	
	public void addMovie() {
		System.out.println("[��ȭ ���]");
		System.out.print("��ȭ ����: ");
		String mtitle = scan.nextLine();
		System.out.print("��ȭ �帣: ");
		String mgenre = scan.nextLine();
		
		try {
			String addSql = "INSERT INTO t_movie (mtitle, mgenre) VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(addSql);
			pstmt.setString(1, mtitle);
			pstmt.setString(2, mgenre);
			pstmt.executeUpdate();
			System.out.println("��ȭ ����� �Ϸ�Ǿ����ϴ�.");
			pstmt.close();
			movieList();
		} catch (SQLException e) {
			e.printStackTrace();
			addMovie();
		}
		adminMenu();
	}
	
	public void deleteMovie() {
		System.out.println("[��ȭ ����]");
		movieList();
		System.out.print("������ ��ȭ ��ȣ: ");
		int mno = scan.nextInt();
		scan.nextLine();
		
		try {
			String delSql = "DELETE FROM t_movie WHERE mno=?";
			PreparedStatement pstmt = conn.prepareStatement(delSql);
			pstmt.setInt(1, mno);
			int rows = pstmt.executeUpdate();
			
			if (rows > 0) {
				System.out.println("��ȭ�� �����Ǿ����ϴ�.");
			} else {
				System.out.println("�ش� ��ȣ�� ��ȭ�� �����ϴ�.");
			}
			pstmt.close();
			movieList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		adminMenu();
	}
		
	public void menu(String userId) {
		System.out.println();
		System.out.println("[��ȭ ���� ���α׷�]");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("1.�����ϱ� | 2.����Ȯ�� | 3.������� | 4.�����ϱ�");
		System.out.println("-----------------------------------------------------------------");
		System.out.print("�޴�����: ");
		
		String menuNo = scan.nextLine();
		System.out.println();
		
		switch(menuNo) {
			case "1" -> reserve(userId);
			case "2" -> check(userId);
			case "3" -> cancel(userId);
			case "4" -> exit();
			default -> { 
				System.out.println("�߸��� �����Դϴ�. 1 ~ 4�� �� �������ּ���.");
				menu(userId);
			}
		}
	}
	
	public void reserve(String userId) {
		try {
			String sql = "SELECT mno, mtitle, mgenre FROM t_movie ORDER BY mno";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			System.out.println("��ȭ�� �������ּ���: ");
			
			while(rs.next()) {
				Movie movie = new Movie();
				int mno = rs.getInt("mno");
				String mtitle = rs.getString("mtitle");
				String mgenre = rs.getString("mgenre");
				
				movie.setMno(mno);
				movie.setMtitle(mtitle);
				movie.setMgenre(mgenre);
								
				System.out.printf("%-6s%-12s%-16s\n", movie.getMno(), movie.getMtitle(),
									movie.getMgenre());
			} rs.close();
			pstmt.close();
			
			int selectedMovieNo = scan.nextInt();
			scan.nextLine();
			
			String insertSql = "INSERT INTO t_reserve (rid, rtitle, rgenre) SELECT ?, mtitle, mgenre FROM t_movie WHERE mno=?";
			PreparedStatement insertPstmt = conn.prepareStatement(insertSql);
			insertPstmt.setString(1, userId);
			insertPstmt.setInt(2, selectedMovieNo);
			int insertRows = insertPstmt.executeUpdate();
			
			if (insertRows > 0) {
				System.out.println("��ȭ ���Ű� �Ϸ�Ǿ����ϴ�.");
			} else {
				System.out.println("��ȭ ���ſ� �����߽��ϴ�.");
			}
			insertPstmt.close();
			
			list(userId);
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
	}
	
	public void list(String userId) {
		System.out.println();
		System.out.println("[���ŵ� ��ȭ ���]");
		System.out.println("----------------------------------------------------------------------------");
		System.out.printf("%-6s%-12s%-10s\n", "��ȣ", "����", "�帣");
		System.out.println("----------------------------------------------------------------------------");
		
		try {
			String sql = "SELECT rno, rtitle, rgenre FROM t_reserve WHERE rid=?" ;
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Reserve reserve = new Reserve();
				int rno = rs.getInt("rno");
				String rtitle = rs.getString("rtitle");
				String rgenre = rs.getString("rgenre");
				
				reserve.setRno(rno);
				reserve.setRtitle(rtitle);
				reserve.setRgenre(rgenre);
				
				System.out.printf("%-6s%-12s%-16s\n", reserve.getRno(), reserve.getRtitle(),
									reserve.getRgenre());
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		menu(userId);
	}
	
	public void check(String userId) {
		try {
			System.out.println("[���� Ȯ��]");
			System.out.print("��ȭ ��ȣ�� �Է��ϼ���: ");
			int rno = scan.nextInt();
			
			scan.nextLine();
			
			String sql = "SELECT rno, rtitle, rgenre FROM t_reserve WHERE rno=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rno);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				Reserve reserve = new Reserve();
				int rsRno = rs.getInt("rno");
				String rsRtitle = rs.getString("rtitle");
				String rsRgenre = rs.getString("rgenre");
				
				reserve.setRno(rsRno);
				reserve.setRtitle(rsRtitle);
				reserve.setRgenre(rsRgenre);
				
				System.out.println("[���ŵ� ��ȭ ���]");
				System.out.println("-----------------------------------------------------------------");
				System.out.printf("%-6s%-12s%-10s\n", "��ȣ", "����", "�帣");
				System.out.println("-----------------------------------------------------------------");
				System.out.printf("%-6s%-12s%-10s\n", reserve.getRno(), reserve.getRtitle(), reserve.getRgenre());
			} else {
				System.out.println("�ش� ��ȣ�� ��ȭ ���� ������ �����ϴ�.");
			}
			rs.close();
			pstmt.close();
			menu(userId);
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
	}
	
	
	public void cancel(String userId) {
		try {
			System.out.println("[���� ���]");
			System.out.println("[���ŵ� ��ȭ ���]");
			System.out.println("-----------------------------------------------------------------");
			System.out.printf("%-6s%-12s%-10s\n", "��ȣ", "����", "�帣");
			System.out.println("-----------------------------------------------------------------");
			
			String selectSql = "SELECT rno, rtitle, rgenre FROM t_reserve WHERE rid=?";
			PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
			selectPstmt.setString(1, userId);
			ResultSet rs = selectPstmt.executeQuery();
			
			while (rs.next()) {
				Reserve reserve = new Reserve();
				int rno = rs.getInt("rno");
				String rtitle = rs.getString("rtitle");
				String rgenre = rs.getString("rgenre");
				
				reserve.setRno(rno);
				reserve.setRtitle(rtitle);
				reserve.setRgenre(rgenre);
				
				System.out.printf("%-6s%-12s%-16s\n", reserve.getRno(), reserve.getRtitle(), reserve.getRgenre());
			}
			rs.close();
			selectPstmt.close();
			
			System.out.println("-----------------------------------------------------------------");
			System.out.print("���Ÿ� ����� ��ȭ ��ȣ�� �Է��ϼ���: ");
			int cancelNo = scan.nextInt();
			scan.nextLine();
			
			String cancelSql = "DELETE FROM t_reserve WHERE rno=? AND rid=?";
			PreparedStatement cancelPstmt = conn.prepareStatement(cancelSql);
			cancelPstmt.setInt(1, cancelNo);
			cancelPstmt.setString(2, userId);
			int cancelRows = cancelPstmt.executeUpdate();
			
			if (cancelRows > 0) {
				System.out.println("���Ű� ��ҵǾ����ϴ�.");
			} else {
				System.out.println("���� ��ҿ� �����Ͽ����ϴ�.");
			}
			
			cancelPstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
		menu(userId);
	}
	
	public void exit() {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
			System.out.println("[���� ���α׷� ����]");
			System.exit(0);
		}
	
		public static void main(String[] args) {
			MovieExam movieExam = new MovieExam();
			movieExam.member();
	}
}

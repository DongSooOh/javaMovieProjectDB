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
			// JDBC Driver 등록
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// 연결하기
			conn = DriverManager.getConnection(
					// useUnicode=true&characterEncoding=utf8: DB 한글 깨짐 해결
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
		System.out.println("[회원 메뉴]");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("1.로그인 | 2.회원가입 | 3.회원보기 | 4.전체 회원삭제 | 5.종료하기");
		System.out.println("-----------------------------------------------------------------");
		System.out.print("메뉴선택:");
		
		String memberNo = scan.nextLine();
		System.out.println();
		
		switch(memberNo) {
			case "1" -> login();
			case "2" -> create();
			case "3" -> read();
			case "4" -> clear();
			case "5" -> exit();
			default -> {
				System.out.println("잘못된 선택입니다. 1 ~ 5번 중 선택해주세요.");
				member();
			}
		}		
	}
	
	public void memlist() {
		System.out.println();
		System.out.println("[회원 목록]");
		System.out.println("-----------------------------------------------------------------");
		System.out.printf("%-6s%-12s%-16s%-40s\n", "아이디", "이름", "email", "가입일자");
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
		System.out.print("아이디: ");
		String id = scan.nextLine();
		System.out.print("비밀번호: ");
		String pwd = scan.nextLine();
		
		if (id.equals(adminId) && pwd.equals(adminPwd)) {
			System.out.println("관리자 아이디로 로그인되었습니다.");
			adminMenu();
		} else {
			try {
				String logSql = "SELECT id, pwd FROM t_member WHERE id=? AND pwd=?";
				PreparedStatement pstmt = conn.prepareStatement(logSql);
				pstmt.setString(1, id);
				pstmt.setString(2, pwd);
				ResultSet rs = pstmt.executeQuery();
				
				if (rs.next()) {
					System.out.println("일반 사용자로 로그인되었습니다.");
					menu(id);
				} else {
					System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
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
		System.out.println("[회원가입]");
		
		System.out.print("아이디: ");
		Member.setId(scan.nextLine());
		
		System.out.print("비밀번호: ");
		Member.setPwd(scan.nextLine());
		
		System.out.print("이름: ");
		Member.setName(scan.nextLine());
		
		System.out.print("email: ");
		Member.setEmail(scan.nextLine());
		
		System.out.println("-----------------------------------------------------------------");
		System.out.println("가입하시겠습니까? 1.OK | 2.Cancel");
		System.out.print("메뉴선택: ");
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
				pstmt.executeUpdate();	// executeUpdate(): DB를 상대로 데이터를 변경하기 위해 실행  
				pstmt.close();
			} catch (Exception e) {
				System.out.println("중복된 아이디입니다.");
				create();
			}
		}
		memlist();
	}
	
	public void read() {
		System.out.println("[회원 보기]");
		System.out.print("아이디: ");
		String _id = scan.nextLine();
		
		try {
			String sql = "SELECT id, name, email, joinDate FROM t_member WHERE id=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, _id);
			ResultSet rs = pstmt.executeQuery(); // executeQuery(): DB를 상대로 데이터를 조회하기 위해 실행 
			
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
				System.out.println("아이디: " + Member.getId());
				System.out.println("이름: " + Member.getName());
				System.out.println("email: " + Member.getEmail());
				System.out.println("가입날짜: " + Member.getJoinDate());
				
				System.out.println("-----------------------------------------------------------------");
				System.out.println("회원수정: 1.Update | 2.Delete | 3.List");
				System.out.print("메뉴선택: ");
				
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
		
		System.out.println("[수정 내용 입력]");
		System.out.print("이름: ");
		Member.setName(scan.nextLine());
		System.out.print("비밀번호: ");
		Member.setPwd(scan.nextLine());
		System.out.print("email: ");
		Member.setEmail(scan.nextLine());
		
		System.out.println("-----------------------------------------------------------------");
		System.out.println("수정하시겠습니까? 1. OK | 2.Cancel");
		System.out.print("메뉴선택: ");
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
		System.out.println("[회원 전체삭제]");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("회원전체를 삭제하시겠습니까? 1.OK | 2.Cancel");
		System.out.print("메뉴선택: ");
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
		System.out.println("[관리자 메뉴]");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("1.영화 등록 | 2.영화 삭제 | 3.로그아웃");
		System.out.println("-----------------------------------------------------------------");
		System.out.print("메뉴선택: ");
		
		String adminMenuNo = scan.nextLine();
		System.out.println();
		
		switch(adminMenuNo) {
			case "1" -> addMovie();
			case "2" -> deleteMovie();
			case "3" -> member();
			default -> {
				System.out.println("잘못된 선택입니다. 1 ~ 3번 중 선택해주세요.");
				adminMenu();
			}
		}
	}
	
	public void movieList() {
		System.out.println("[영화 목록]");
		try {
			String sql = "SELECT mno, mtitle, mgenre FROM t_movie";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			System.out.println("-----------------------------------------------------------------");
			System.out.printf("%-6s%-12s%-16s\n", "번호", "제목", "장르");
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
		System.out.println("[영화 등록]");
		System.out.print("영화 제목: ");
		String mtitle = scan.nextLine();
		System.out.print("영화 장르: ");
		String mgenre = scan.nextLine();
		
		try {
			String addSql = "INSERT INTO t_movie (mtitle, mgenre) VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(addSql);
			pstmt.setString(1, mtitle);
			pstmt.setString(2, mgenre);
			pstmt.executeUpdate();
			System.out.println("영화 등록이 완료되었습니다.");
			pstmt.close();
			movieList();
		} catch (SQLException e) {
			e.printStackTrace();
			addMovie();
		}
		adminMenu();
	}
	
	public void deleteMovie() {
		System.out.println("[영화 삭제]");
		movieList();
		System.out.print("삭제할 영화 번호: ");
		int mno = scan.nextInt();
		scan.nextLine();
		
		try {
			String delSql = "DELETE FROM t_movie WHERE mno=?";
			PreparedStatement pstmt = conn.prepareStatement(delSql);
			pstmt.setInt(1, mno);
			int rows = pstmt.executeUpdate();
			
			if (rows > 0) {
				System.out.println("영화가 삭제되었습니다.");
			} else {
				System.out.println("해당 번호의 영화가 없습니다.");
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
		System.out.println("[영화 예매 프로그램]");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("1.예매하기 | 2.예매확인 | 3.예매취소 | 4.종료하기");
		System.out.println("-----------------------------------------------------------------");
		System.out.print("메뉴선택: ");
		
		String menuNo = scan.nextLine();
		System.out.println();
		
		switch(menuNo) {
			case "1" -> reserve(userId);
			case "2" -> check(userId);
			case "3" -> cancel(userId);
			case "4" -> exit();
			default -> { 
				System.out.println("잘못된 선택입니다. 1 ~ 4번 중 선택해주세요.");
				menu(userId);
			}
		}
	}
	
	public void reserve(String userId) {
		try {
			String sql = "SELECT mno, mtitle, mgenre FROM t_movie ORDER BY mno";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			System.out.println("영화를 선택해주세요: ");
			
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
				System.out.println("영화 예매가 완료되었습니다.");
			} else {
				System.out.println("영화 예매에 실패했습니다.");
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
		System.out.println("[예매된 영화 목록]");
		System.out.println("----------------------------------------------------------------------------");
		System.out.printf("%-6s%-12s%-10s\n", "번호", "제목", "장르");
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
			System.out.println("[예매 확인]");
			System.out.print("영화 번호를 입력하세요: ");
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
				
				System.out.println("[예매된 영화 목록]");
				System.out.println("-----------------------------------------------------------------");
				System.out.printf("%-6s%-12s%-10s\n", "번호", "제목", "장르");
				System.out.println("-----------------------------------------------------------------");
				System.out.printf("%-6s%-12s%-10s\n", reserve.getRno(), reserve.getRtitle(), reserve.getRgenre());
			} else {
				System.out.println("해당 번호의 영화 예매 정보가 없습니다.");
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
			System.out.println("[예매 취소]");
			System.out.println("[예매된 영화 목록]");
			System.out.println("-----------------------------------------------------------------");
			System.out.printf("%-6s%-12s%-10s\n", "번호", "제목", "장르");
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
			System.out.print("예매를 취소할 영화 번호를 입력하세요: ");
			int cancelNo = scan.nextInt();
			scan.nextLine();
			
			String cancelSql = "DELETE FROM t_reserve WHERE rno=? AND rid=?";
			PreparedStatement cancelPstmt = conn.prepareStatement(cancelSql);
			cancelPstmt.setInt(1, cancelNo);
			cancelPstmt.setString(2, userId);
			int cancelRows = cancelPstmt.executeUpdate();
			
			if (cancelRows > 0) {
				System.out.println("예매가 취소되었습니다.");
			} else {
				System.out.println("예매 취소에 실패하였습니다.");
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
			System.out.println("[예매 프로그램 종료]");
			System.exit(0);
		}
	
		public static void main(String[] args) {
			MovieExam movieExam = new MovieExam();
			movieExam.member();
	}
}

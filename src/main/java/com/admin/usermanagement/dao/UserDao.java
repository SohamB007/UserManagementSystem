package com.admin.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;

import com.admin.usermanagement.bean.User;
import com.mysql.cj.protocol.Resultset;

public class UserDao {
	private String url = "jdbc:mysql://localhost:3306/portfolio?user=root&password=sql12345" ;
	
	private static final String insertUsers = "INSERT INTO users (name, email, country) VALUES (?, ?, ?);";	
	private static final String SelectUser = "Select * from users where id = ?" ;
	private static final String SelectAllUsers = "Select * from users;" ;
	private static final String DeleteUsers = "Delete from users where id = ?;" ;
	private static final String UpdateUsers = "Update users set name = ? , email = ?, country = ? where id = ?;" ;
	
	
	//
	public UserDao() {
	}
	
	
	//
	protected Connection getConnection() {
	    Connection connection = null;
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection(url);
	        System.out.println("Database connected!");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return connection;
	}

	
	
	//
	public void insertUser(User user) throws SQLException {
		try (Connection connection = getConnection();
			PreparedStatement pstmt = connection.prepareStatement(insertUsers)){
				pstmt.setString(1, user.getName()) ;
				pstmt.setString(2, user.getEmail()) ;
				pstmt.setString(3, user.getCountry()) ;
				
				pstmt.executeUpdate() ;
			} catch (SQLException e) {
				printSQLException(e) ;
		}
	}
	
	
	//
	public User selectUser(int id) {
		User user = null ;
		
		try {
			Connection connection = getConnection() ;
			PreparedStatement pstmt = connection.prepareStatement(SelectUser);
			pstmt.setInt(1, id);
			System.out.println(pstmt);
			
			ResultSet rs = pstmt.executeQuery() ;
			while(rs.next()) {
				String name = rs.getString("name") ;
				String email = rs.getString("email") ;
				String country = rs.getString("country") ;
				user =  new User(id, name, email, country) ;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
		}
		return user;
	}
	
	
	//
	public List<User> selectAllUsers(){
		List<User> users = new ArrayList<User>() ;
		
		
		try (Connection connection = getConnection() ;
			PreparedStatement pstmt = connection.prepareStatement(SelectAllUsers);){
			System.out.println(pstmt);
			
			ResultSet rs = pstmt.executeQuery() ;
			
			while(rs.next()) {
				int id = rs.getInt("id") ;
				String name = rs.getString("name") ;
				String email = rs.getString("email") ;
				String country = rs.getString("country") ;
				users.add(new User(id, name, email, country)) ;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printSQLException(e);
		}
		return users;
	}
	
	
	//
	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try(Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(UpdateUsers);){
			System.out.println("Updated User:" + pstmt);
			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getCountry());
			pstmt.setInt(4, user.getId());
			
			rowUpdated = pstmt.executeUpdate() > 0;		
		}
		return rowUpdated ;
	}
	
	
	//
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try(Connection connection = getConnection();
				PreparedStatement pstmt = connection.prepareStatement(DeleteUsers);){
			System.out.println("Deleted User: "+ pstmt);
			pstmt.setInt(1, id);
			rowDeleted = pstmt.executeUpdate() > 0 ;
		}
		return rowDeleted ;
	}
	
	
	
	//
	private void printSQLException(SQLException ex) {
		// TODO Auto-generated method stub
		for (Throwable e : ex) {
			if(e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + ((SQLException) e).getMessage());
				
				Throwable t = ex.getCause() ;
				while(t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause() ;
				}
			}
		}
		
	}
}

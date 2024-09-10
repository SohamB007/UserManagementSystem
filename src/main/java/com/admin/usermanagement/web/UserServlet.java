package com.admin.usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.admin.usermanagement.bean.User;
import com.admin.usermanagement.dao.UserDao;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserDao userDao ;
    
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		userDao = new UserDao() ;
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getServletPath() ;
		
		switch (action) {
		case "/new": {
			showNewForm(request, response);
			break;
		}
		case "/insert": {
			insertUser(request, response);
			break;
		}
		case "/update": {
			updateUser(request, response);
			break;
		}
		case "/delete": {
			deleteUser(request, response);
			break;
		}
		case "/edit": {
			showEditForm(request, response);
			break;
		}
		default:
			listUser(request, response);
			break;
		}	
	}
	

		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	//New Form 
	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp") ;
		dispatcher.forward(request, response);
	}
	
	//Insert Method
	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String name = request.getParameter("name") ;
		String email = request.getParameter("email") ;
		String country = request.getParameter("country") ;
		User newUser = new User(name,email,country) ;
		try {
			userDao.insertUser(newUser);
		} catch(Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}
	
	//Delete Method
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("id")) ;
		try {
			userDao.deleteUser(id) ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}
	
	//Edit Method
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id")) ;
		User existingUser ;
		
		try {
		existingUser = userDao.selectUser(id) ;
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp") ;
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Update Method
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id")) ;
		
		String name = request.getParameter("name") ;
		String email = request.getParameter("email") ;
		String country = request.getParameter("country") ;
		User user = new User(id, name, email, country) ;
		
			try {
				userDao.updateUser(user);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		response.sendRedirect("list");
	}
	
	//Default Method
	private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try {
			List<User> listUser = userDao.selectAllUsers();
			request.setAttribute("listUser", listUser);
			RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
			dispatcher.forward(request, response);
 	} catch(Exception e) {
 		e.printStackTrace();
 	}
	}
	
}

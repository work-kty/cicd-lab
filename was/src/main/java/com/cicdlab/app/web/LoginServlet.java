package com.cicdlab.app.web;

import com.cicdlab.app.model.User;
import com.cicdlab.app.store.MemoryStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = MemoryStore.get().findUser(username);
        if (user == null || !user.getPassword().equals(password)) {
            req.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }
        HttpSession session = req.getSession(true);
        session.setAttribute("loginUser", user);
        resp.sendRedirect(req.getContextPath() + "/board");
    }
}
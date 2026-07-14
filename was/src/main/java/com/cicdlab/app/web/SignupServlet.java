package com.cicdlab.app.web;

import com.cicdlab.app.model.User;
import com.cicdlab.app.store.MemoryStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String nickname = req.getParameter("nickname");

        if (username == null || username.isBlank()
                || password == null || password.isBlank()
                || nickname == null || nickname.isBlank()) {
            req.setAttribute("error", "모든 항목을 입력하세요.");
            req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
            return;
        }
        boolean ok = MemoryStore.get().addUser(new User(username, password, nickname));
        if (!ok) {
            req.setAttribute("error", "이미 존재하는 아이디입니다.");
            req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/login?joined=1");
    }
}
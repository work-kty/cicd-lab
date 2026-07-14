package com.cicdlab.app.web;

import com.cicdlab.app.model.User;
import com.cicdlab.app.store.DbStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet({"/board", ""})
public class BoardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("posts", DbStore.get().findAllPosts());
        req.getRequestDispatcher("/WEB-INF/views/board.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User loginUser = (session == null) ? null : (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        if (title != null && !title.isBlank()) {
            DbStore.get().addPost(title, content, loginUser.getNickname());
        }
        resp.sendRedirect(req.getContextPath() + "/board");
    }
}
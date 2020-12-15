package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsListServlet
 */
@WebServlet("/reports/list")
public class ReportsListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        int id;
        Employee employee = null;
        try {
            id = Integer.parseInt(request.getParameter("id"));
            employee = em.createNamedQuery("getEmployeeFromId", Employee.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
        }

        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }

        if (employee != null) {
            List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
                    .setParameter("employee", employee)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            long reports_count = (long) em.createNamedQuery("getMyReportsCount", Long.class)
                    .setParameter("employee", employee)
                    .getSingleResult();

            request.setAttribute("reports", reports);
            request.setAttribute("reports_count", reports_count);
            request.setAttribute("page", page);
        }
        request.setAttribute("employee", employee);

        em.close();

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/list.jsp");
        rd.forward(request, response);
    }

}

package controllers.employees;

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
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesIndexServlet
 */
@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        // 開くページ数を取得（デフォルトは1ページ目）
        //request.getParameter() で取得できるのはString型のため、intに変える。→数値でないものを渡すと例外
        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) { }
        // 最大件数と開始位置を指定してメッセージを取得
        /*setFirstResult(15 * (page - 1)) は「何件目からデータを取得するか（配列と同じ0番目から数えていきます）」
         * setMaxResults(15) は「データの最大取得件数（今回は15件で固定）」を設定します。
         *getAllEmployees は複数のデータが結果として戻ってくる可能性があるため getResultList() で問い合わせ結果を取得していますが、
         *getEmployeesCount は全件数という1つの結果のみが戻ってくるので、 getSingleResult() という “1件だけ取得する” という命令を指定 */
        List<Employee> employees = em.createNamedQuery("getAllEmployees", Employee.class).setFirstResult(15 * (page - 1)).setMaxResults(15).getResultList();

        long employees_count = (long)em.createNamedQuery("getEmployeesCount", Long.class).getSingleResult();

        em.close();

        request.setAttribute("employees", employees);
        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);
        // フラッシュメッセージがセッションスコープにセットされていたら
        // リクエストスコープに保存する（セッションスコープからは削除）
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request, response);
    }

}

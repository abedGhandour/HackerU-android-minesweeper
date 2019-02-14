import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;

@javax.servlet.annotation.WebServlet(name = "BestEver")
public class BestEver extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.println("test1");
        String action = request.getParameter("action");
        System.out.println("test2");
        String num1String = request.getParameter("num1");
        System.out.println("test3");
        String num2String = request.getParameter("num2");
        if(isNullOrEmpty(action, num1String, num2String))
            return;
        int num1 = 0;
        int num2 = 0;

        try{
            num1 = Integer.valueOf(num1String);
            num2 = Integer.valueOf(num2String);

        }catch (Exception ex){
            return;
        }

        int result;
        switch (action) {
            case "add":
                result = num1 + num2;
                break;
            case "subtract":
                result = num1 - num2;
                break;
            case "multiply":
                result = num1 * num2;
                break;
            case "divide":
                if (num2 == 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                result = num1 / num2;
                break;
            default:
                return;
        }
        response.getWriter().write(String.valueOf(result));

    }
    private boolean isNullOrEmpty(String... strings){
        if(strings == null || strings.length == 0)
            throw new InvalidParameterException();
        for (int i = 0; i < strings.length; i++) {
            if(strings[i] == null || strings[i].isEmpty())
                return true;
        }
        return false;
    }
}

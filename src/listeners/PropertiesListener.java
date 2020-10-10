package listeners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * リスナーは、Webアプリケーションの動作を監視し、特定の動作が起こったことを感知したら特定の処理を実行
 * Application Lifecycle Listener implementation class PropertiesListener
 *
 */
@WebListener
public class PropertiesListener implements ServletContextListener {

    /**
     * Default constructor.
     */
    public PropertiesListener() {
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  {
        ServletContext context = arg0.getServletContext();

        //パスワードをMySQLに登録しに行くときに、暗号化してから登録
        //application.properties記載の文字列をもとに暗号化 登録するときと照合するときの暗号化の文字列が同じかどうか
        String path = context.getRealPath("/META-INF/application.properties");
        try {
            InputStream is = new FileInputStream(path);
            Properties properties = new Properties();
            properties.load(is);
            is.close();

            Iterator<String> pit = properties.stringPropertyNames().iterator();
            while(pit.hasNext()) {
                String pname = pit.next();
                //読み込んだ値pepperを変数名と値でcontextに格納
                context.setAttribute(pname, properties.getProperty(pname));
            }
        } catch(FileNotFoundException e) {
        } catch(IOException e) {}
    }

}
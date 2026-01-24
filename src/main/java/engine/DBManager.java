package engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBManager {

    private static final String DB_URL =
            "jdbc:sqlite:workflow.db";

    static {
        init();
    }

    private static void init() {

    	String sql =
    		    "CREATE TABLE IF NOT EXISTS steps (" +
    		    "workflow_id TEXT, " +
    		    "step_key TEXT, " +
    		    "status TEXT, " +
    		    "result TEXT, " +
    		    "PRIMARY KEY(workflow_id, step_key)" +
    		    ")";


        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            st.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection()
            throws Exception {

        return DriverManager
                .getConnection(DB_URL);
    }
}

package engine;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Callable;

public class StepExecutor {

    private static final Gson gson =
            new Gson();

    // Thread-safe execution
    @SuppressWarnings("unchecked")
    public static synchronized <T> T step(
            DurableContext ctx,
            String id,
            Callable<T> fn
    ) throws Exception {

        String stepKey =
                ctx.nextStepKey(id);

        // Check cache
        String cached =
                getCached(
                        ctx.getWorkflowId(),
                        stepKey
                );

        if (cached != null) {
            System.out.println(
                    "Skipped (cached): "
                            + stepKey
            );
            return (T) gson.fromJson(
                    cached,
                    Object.class
            );
        }

        System.out.println(
                "Executing: "
                        + stepKey
        );

        try {
            T result = fn.call();

            saveResult(
                    ctx.getWorkflowId(),
                    stepKey,
                    result
            );

            return result;

        } catch (Exception e) {

            markFailed(
                    ctx.getWorkflowId(),
                    stepKey
            );

            throw e;
        }
    }

    // Read DB
    private static String getCached(
            String wid,
            String key
    ) {

        String sql =
                "SELECT result FROM steps " +
                "WHERE workflow_id=? " +
                "AND step_key=? " +
                "AND status='DONE'";

        try (Connection conn =
                     DBManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, wid);
            ps.setString(2, key);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {
                return rs.getString("result");
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }

    // Save DB
    private static void saveResult(
            String wid,
            String key,
            Object result
    ) throws Exception {

    	String sql =
    		    "INSERT OR REPLACE INTO steps VALUES (?, ?, 'DONE', ?)";

        try (Connection conn =
                     DBManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, wid);
            ps.setString(2, key);
            ps.setString(3,
                    gson.toJson(result));

            ps.executeUpdate();
        }
    }

    // Mark failed
    private static void markFailed(
            String wid,
            String key
    ) throws Exception {

    	String sql =
    		    "INSERT OR REPLACE INTO steps VALUES (?, ?, 'FAILED', NULL)";

        try (Connection conn =
                     DBManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, wid);
            ps.setString(2, key);

            ps.executeUpdate();
        }
    }
}

package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import projects.entity.Project;
import projects.exception.DbException;
import provided.util.DaoBase;


@SuppressWarnings("unused")
public class ProjectDao extends DaoBase {
    private static final String CATEGORY_TABLE = "category";
    private static final String MATERIAL_TABLE = "material";
    private static final String PROJECT_TABLE = "project";
    private static final String PROJECT_CATEGORY_TABLE = "project_category";
    private static final String STEP_TABLE = "step";

    // Insert a project into the database
    public Project insertProject(Project project) {
        String sql = "INSERT INTO " + PROJECT_TABLE + " (project_name, estimated_hours, actual_hours, difficulty, notes) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection()) {
            startTransaction(conn); // Start a transaction

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Set the parameter values for the SQL statement
                setParameter(stmt, 1, project.getProjectName(), String.class);
                setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
                setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
                setParameter(stmt, 4, project.getDifficulty(), Integer.class);
                setParameter(stmt, 5, project.getNotes(), String.class);

                stmt.executeUpdate(); // Execute the SQL statement

                // Get the last inserted project's ID from the database
                Integer projectId = getLastInsertId(conn, PROJECT_TABLE);

                commitTransaction(conn); // Commit the transaction

                project.setProjectId(projectId); // Set the project's ID
                return project; // Return the project
            } catch (Exception e) {
                throw new DbException(e); // Throw a custom exception if an error occurs during statement execution
            }
        } catch (SQLException e) {
            throw new DbException(e); // Throw a custom exception if a SQL error occurs
        }
    }
}
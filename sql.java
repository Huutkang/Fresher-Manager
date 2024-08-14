import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class sql {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/freshermanager";
        String user = "Thang";
        String password = "123456";
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            String sql;

            sql = """
                    CREATE TABLE center (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    address VARCHAR(255),
                    manager_name VARCHAR(255)
                );

                    """;

            stmt.executeUpdate(sql);

            sql = """
                    CREATE TABLE fresher (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    email VARCHAR(255) UNIQUE NOT NULL,
                    phone_number VARCHAR(20),
                    programming_language VARCHAR(100),
                    center_id INT,
                    CONSTRAINT fk_center
                        FOREIGN KEY(center_id)
                        REFERENCES center(id)
                        ON DELETE SET NULL
                );

                    """;
            stmt.executeUpdate(sql);

            sql = """
                    CREATE TABLE project (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    center_id INT,
                    manager_name VARCHAR(255),
                    start_date DATE,
                    end_date DATE,
                    language VARCHAR(100),
                    status VARCHAR(50) CHECK (status IN ('not start', 'ongoing', 'canceled', 'closed')),
                    CONSTRAINT fk_center
                        FOREIGN KEY(center_id)
                        REFERENCES center(id)
                        ON DELETE SET NULL
                );

                    """;

            stmt.executeUpdate(sql);
            
            sql = """
                    CREATE TABLE fresher_project (
                    id SERIAL PRIMARY KEY,
                    fresher_id INT,
                    project_id INT,
                    role VARCHAR(100),
                    CONSTRAINT fk_fresher
                        FOREIGN KEY(fresher_id)
                        REFERENCES fresher(id)
                        ON DELETE CASCADE,
                    CONSTRAINT fk_project
                        FOREIGN KEY(project_id)
                        REFERENCES project(id)
                        ON DELETE CASCADE
                );

                    """;

            stmt.executeUpdate(sql);
            
            sql = """
                    CREATE TABLE assignment (
                    id SERIAL PRIMARY KEY,
                    fresher_id INT,
                    project_id INT,
                    assignment_number INT CHECK (assignment_number BETWEEN 1 AND 3),
                    score NUMERIC CHECK (score BETWEEN 0 AND 10),
                    CONSTRAINT fk_fresher
                        FOREIGN KEY(fresher_id)
                        REFERENCES fresher(id)
                        ON DELETE CASCADE,
                    CONSTRAINT fk_project
                        FOREIGN KEY(project_id)
                        REFERENCES project(id)
                        ON DELETE CASCADE
                );

                    """;

            stmt.executeUpdate(sql);
            
            sql = """
                    CREATE TABLE notification (
                    id SERIAL PRIMARY KEY,
                    fresher_id INT,
                    project_id INT,
                    message TEXT,
                    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT fk_fresher
                        FOREIGN KEY(fresher_id)
                        REFERENCES fresher(id)
                        ON DELETE CASCADE,
                    CONSTRAINT fk_project
                        FOREIGN KEY(project_id)
                        REFERENCES project(id)
                        ON DELETE CASCADE
                );

                    """;

            stmt.executeUpdate(sql);
            



            System.out.println("Successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

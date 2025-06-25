package config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtil {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            String resource = "mybatis_config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            System.out.println("MyBatis SqlSessionFactory initialized successfully!");
        } catch (IOException e) {
            System.err.println("Error initializing MyBatis SqlSessionFactory: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize MyBatis SqlSessionFactory.", e);
        }
    }

    /**
     * Get the SqlSessionFactory instance.
     * @return SqlSessionFactory instance
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    /**
     * Get a new SqlSession instance.
     * Always obtain a new SqlSession for each database operation.
     * @return a new SqlSession
     */
    public static SqlSession getSqlSession() {
        // The true parameter means auto-commit is enabled. This is convenient for queries.
        // For update, insert, or delete operations, it is recommended to set auto-commit to false and manually commit or rollback.
        return sqlSessionFactory.openSession();
    }

    /**
     * Get a new SqlSession instance with specified auto-commit behavior.
     * @param autoCommit whether to enable auto-commit
     * @return a new SqlSession
     */
    public static SqlSession getSqlSession(boolean autoCommit) {
        return sqlSessionFactory.openSession(autoCommit);
    }

    /**
     * Close the given SqlSession.
     * @param session the SqlSession to close
     */
    public static void closeSession(SqlSession session) {
        if (session != null) {
            session.close();
        }
    }
}
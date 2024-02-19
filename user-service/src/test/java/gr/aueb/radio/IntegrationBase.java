package gr.aueb.radio;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class IntegrationBase {
    @Inject
    EntityManager em;

    @Transactional
    @BeforeEach
    public void initDb()  {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("import.sql");
        String sql = convertStreamToString(in);
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        em.createNativeQuery(sql).executeUpdate();
    }


    private String convertStreamToString(InputStream in) {
        @SuppressWarnings("resource")
        Scanner s = new Scanner(in,"UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
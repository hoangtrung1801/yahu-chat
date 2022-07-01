package utility;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtils {
    private static final EntityManagerFactory entityManagerFactory;
    private static final EntityManager entityManager;

    private HibernateUtils() {}

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return  entityManagerFactory;
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static void beginTransaction() {
        if(entityManager.getTransaction().isActive()) return;
        entityManager.getTransaction().begin();
    }

    public static void commitTransaction() {
        entityManager.getTransaction().commit();
    }

}

package gr.aueb.radio.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPAUtil {
    private static EntityManagerFactory entityManagerFactory;
    private static final ThreadLocal<EntityManager> currentEntityManager = new ThreadLocal<EntityManager>();

    
    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("radio");
        }
        return entityManagerFactory;
    }
    
    
    public static EntityManager getCurrentEntityManager() {      
        EntityManager entityManager = currentEntityManager.get();         
        if (entityManager  == null || !entityManager.isOpen()) {
            entityManager = getEntityManagerFactory().createEntityManager();
            currentEntityManager.set(entityManager);
        }
        return entityManager;
    }
    
    public static EntityManager createEntityManager() {
    	
    	return getEntityManagerFactory().createEntityManager();
    }

    public static void transactional(Runnable runnable){
    	
    	EntityManager entityManager = getCurrentEntityManager();
    	EntityTransaction entityTransaction = entityManager.getTransaction();
    	entityTransaction.begin();
    	
    	runnable.run();
    	
    	entityTransaction.rollback();
    	entityManager.close();
    }
}

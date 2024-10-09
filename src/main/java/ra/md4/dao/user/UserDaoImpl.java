//package ra.md4.dao.user;
//import org.springframework.stereotype.Repository;
//import ra.md4.models.User;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.TypedQuery;
//
//@Repository
//public class UserDaoImpl implements IUserDao{
//    @PersistenceContext
//    private EntityManager em;
//    @Override
//    public User findByUsername(String username) {
//        TypedQuery<User> query = em.createQuery("from User where username = :username and isDeleted = false", User.class);
//        query.setParameter("username", username);
//        return query.getSingleResult();
//    }
//
//    @Override
//    public void register(User user) {
//        em.persist(user);
//    }
//}
//

package ra.md4.dao.user;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import ra.md4.models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements IUserDao{
    @Autowired
    LocalContainerEntityManagerFactoryBean container;
    @Autowired
    private EntityManager entityManager;
    @Override
    public void register(User user) {
        SessionFactory sessionFactory = (SessionFactory) container.getObject();
        Session session = sessionFactory.openSession();
        Transaction tran = session.beginTransaction();
        try {
            session.saveOrUpdate(user);
            tran.commit();
        }catch (Exception e){
            tran.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }

    }

    @Override
    public User login(String username) {
        TypedQuery<User> query = entityManager.createQuery("from User where username=:username and isDeleted = false", User.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    @Override
    public List<User> getUsers() {
        SessionFactory sessionFactory = (SessionFactory) container.getObject();
        Session session = sessionFactory.openSession();
        TypedQuery<User> typedQuery = session.createQuery("from User ", User.class);
        List<User> users = typedQuery.getResultList();
        session.close();
        return users;
    }

    @Override
    public User findById(Integer id) {
        String query = "select u from User u where id= :id";
        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }

    @Override
    public void changeStatus(Integer id) {
        SessionFactory sessionFactory = (SessionFactory) container.getObject();
        Session session = sessionFactory.openSession();
        Transaction tran = session.beginTransaction();
        try{
            User user = session.get(User.class,id);
            user.setStatus(!user.isStatus());
            session.update(user);
            tran.commit();
            //nếu là admin thì không thể thay đổi được
            if (user.isRole() == true && user.getUsername() == "admin"){
                //thì không được thay đổi
            }
        }catch (Exception e){
            tran.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    @Override
    public void changeUserRole(Integer id) {
        SessionFactory sessionFactory = (SessionFactory) container.getObject();
        Session session = sessionFactory.openSession();
        Transaction tran = session.beginTransaction();
        try{
            User user = session.get(User.class,id);
            user.setRole(!user.isRole());
            session.update(user);
            tran.commit();
        }catch (Exception e){
            tran.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    @Override
    public List<User> searchName(String query) {
        String searchQuery = "%" + query + "%";
        TypedQuery<User> q = entityManager.createQuery(
                "SELECT u FROM User u WHERE (u.username LIKE :query OR u.email LIKE :query) AND u.isDeleted = false",
                User.class
        );
        q.setParameter("query", searchQuery);
        return q.getResultList();
    }
}

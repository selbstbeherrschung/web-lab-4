package dao;

import beansLab.entities.User;
import beansLab.entities.Shot;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import resources.UserManager;
import utils.HibernateSessionFactoryUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class UserDao {

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserDao.class);

    public UserDao() {
        createAllParts();
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            session.clear();
        }
    }

    public User findUserById(int id) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            Transaction tx1 = session.beginTransaction();
            User user = session.get(User.class, id);
            tx1.commit();
            return user;
        }
    }

    public void saveUser(User user) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            Transaction tx1 = session.beginTransaction();
            session.save(user);
            tx1.commit();
        }
    }

    public void updateUser(User user) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            Transaction tx1 = session.beginTransaction();
            session.update(user);
            tx1.commit();
        }
    }

    public void deleteUser(User user) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            Transaction tx1 = session.beginTransaction();
            session.delete(user);
            tx1.commit();
        }
    }

    public void deleteShots(User user) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            Transaction tx1 = session.beginTransaction();
            user.getShots().clear();
            updateUser(user);
            tx1.commit();
        }
    }

    public Shot findShotById(int id) {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            Transaction tx1 = session.beginTransaction();
            Shot shot = session.get(Shot.class, id);
            tx1.commit();
            return shot;
        }
    }

    public CopyOnWriteArrayList<User> findAllUsers() {
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            Transaction tx1 = session.beginTransaction();
            List<User> users = session.createQuery("FROM User", User.class).list();
            tx1.commit();
            return new CopyOnWriteArrayList<User>(users);
        }
    }

//    private void writeAllInfAboutUser(User user){
//        log.info("Login: " + user.getLogin());
//        log.info("Password: " + user.getPassword());
//        log.info("Shots: ");
//        List<Shot> shots = user.getShots();
//        int size = shots.size();
//        for (int i = 0; i < size; i++) {
//            log.info(shots.get(i).toString());
//        }
//    }


//    public void close(){
//        session.close();
//    }


    private static String tableUserCheck = "Select * from user_objects where Object_Name = 'USERS'";
    private static String tableShotsCheck = "Select * from user_objects where Object_Name = 'SHOTS'";
    private static String seqUserCheck = "Select * from user_objects where Object_Name = 'SEQ_USER'";
    private static String seqShotCheck = "Select * from user_objects where Object_Name = 'SEQ_SHOT'";

    private static String tableUsers = "CREATE TABLE users(\n" +
            "\tuser_id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, \n" +
            "\tlogin VARCHAR2( 255 ) NOT NULL,\n" +
            "\tpassword VARCHAR2( 255 ) NOT NULL\n" +
            ")\n";

    private static String tableShots = "CREATE TABLE shots (\n" +
            "\tshot_id NUMBER PRIMARY KEY,\n" +
            "\tx BINARY_FLOAT NOT NULL,\n" +
            "\ty BINARY_FLOAT NOT NULL,\n" +
            "\tr BINARY_FLOAT NOT NULL, \n" +
            "\trg NUMBER NOT NULL,\n" +
            "\tstart_time VARCHAR2( 20 ) NOT NULL,\n" +
            "\tscript_time LONG NOT NULL, \n" +
            "\tuser_id NUMBER NOT NULL,\n" +
            "\tFOREIGN KEY(user_id) REFERENCES users(user_id)\n" +
            "\t)\n";

    private static String seqUser = "create sequence SEQ_USER\n" +
            "minvalue 1\n" +
            "start with 1\n" +
            "increment by 1\n" +
            "nocache\n";

    private static String seqShot = "create sequence SEQ_SHOT\n" +
            "minvalue 1\n" +
            "start with 1\n" +
            "increment by 1\n" +
            "nocache\n";

    private void createAllParts() {
        try {
            createSeqUser();
            createSeqShot();
            createTableUser();
            createTableShot();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void createSeqUser() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Query query = session.createSQLQuery(seqUserCheck);
        List list = query.list();
        if (list.size() == 0) {
            Query queryCreate = session.createSQLQuery(seqUser);
            queryCreate.executeUpdate();
        }
        tx1.commit();
        session.close();
    }

    private void createSeqShot() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Query query = session.createSQLQuery(seqShotCheck);
        List list = query.list();
        if (list.size() == 0) {
            Query queryCreate = session.createSQLQuery(seqShot);
            queryCreate.executeUpdate();
        }
        tx1.commit();
        session.close();
    }

    private void createTableUser() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Query query = session.createSQLQuery(tableUserCheck);
        List list = query.list();
        if (list.size() == 0) {
            Query queryCreate = session.createSQLQuery(tableUsers);
            queryCreate.executeUpdate();
        }
        tx1.commit();
        session.close();
    }

    private void createTableShot() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Query query = session.createSQLQuery(tableShotsCheck);
        List list = query.list();
        if (list.size() == 0) {
            Query queryCreate = session.createSQLQuery(tableShots);
            queryCreate.executeUpdate();
        }
        tx1.commit();
        session.close();
    }
}
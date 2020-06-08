package org.project.services;

import org.hibernate.SessionFactory;
import org.project.domain.Disk;
import org.project.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
@Transactional
public class Repo {

    @Autowired
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;


    //Ищет пользователя по id
    public User findUserById(Long id) {
        return (User) sessionFactory.getCurrentSession().createQuery("from User where id =:id").setParameter("id", id).getSingleResult();
    }

    public User findByUsername(String username) {
        User user = null;
        try {
            user = (User) sessionFactory.getCurrentSession().createQuery("from User where username =: username").setParameter("username", username).getSingleResult();
        } catch (NoResultException ignored) {
        }
        return user;
    }

    public Disk findDiskById(Long id) {
        return (Disk) sessionFactory.getCurrentSession().createQuery("from Disk where id =: id").setParameter("id", id).getSingleResult();
    }

    public List<Disk> getFreeDisks() {
        return sessionFactory.getCurrentSession().createQuery("from Disk where username =:username").setParameter("username", "null").getResultList();
    }

    public User saveUser(User user) {
        sessionFactory.getCurrentSession().saveOrUpdate(user);
        return findUserById(user.getId());
    }

    public void deleteUser(User user) {
        sessionFactory.getCurrentSession().delete(user);
    }

    public Disk saveDisk(Disk disk) {
        sessionFactory.getCurrentSession().save(disk);
        return findDiskById(disk.getId());
    }

    public void updateDisk(Disk disk) {
        sessionFactory.getCurrentSession().update(disk);
    }

    public List<User> getAllUsers() {
        return sessionFactory.getCurrentSession().createQuery("from User").getResultList();
    }
}

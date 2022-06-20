package dao.implement;

import dao.GroupMemberDAO;
import model.GroupMember;
import model.GroupMemberId;
import utilities.HibernateUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class GroupMemberDAOImpl implements GroupMemberDAO {

    private EntityManager entityManager;

    public GroupMemberDAOImpl() {
        entityManager = HibernateUtils.getEntityManager();
    }

    @Override
    public List<GroupMember> read() {
        HibernateUtils.beginTransaction();

        List<GroupMember> groupMembers = entityManager.createQuery("SELECT e FROM GroupMember e", GroupMember.class).getResultList();

        HibernateUtils.commitTransaction();
        return groupMembers;
    }

    @Override
    public GroupMember readById(Integer id) {
        return entityManager.find(GroupMember.class, id);
    }

    @Override
    public GroupMember create(GroupMember entity) {
        HibernateUtils.beginTransaction();

        entityManager.persist(entity);

        HibernateUtils.commitTransaction();
        return entity;
    }

    @Override
    public GroupMember update(GroupMember entity) {
        return null;
    }

    @Override
    public GroupMember delete(GroupMember entity) {
        return null;
    }
}

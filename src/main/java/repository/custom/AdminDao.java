package repository.custom;

import entity.AdminEntity;
import repository.CrudRepository;

public interface AdminDao extends CrudRepository<AdminEntity> {
    AdminEntity findByUsername(String username);
    boolean update(AdminEntity admin);
    AdminEntity findById(String adminId);
}

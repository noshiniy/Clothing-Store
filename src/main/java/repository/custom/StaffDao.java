package repository.custom;

import entity.StaffEntity;
import repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StaffDao extends CrudRepository<StaffEntity> {
    StaffEntity findByUsername(String username);
    boolean add(StaffEntity staff);
    boolean update(StaffEntity staff);
    boolean delete(String staffId);
    StaffEntity findById(String staffId);
    Optional<String> getLastStaffId();
    List<StaffEntity> findAll();
}

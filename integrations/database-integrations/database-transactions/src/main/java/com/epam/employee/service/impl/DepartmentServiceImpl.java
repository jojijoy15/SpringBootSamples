package com.epam.employee.service.impl;

import com.epam.employee.dto.DepartmentDTO;
import com.epam.employee.entity.Department;
import com.epam.employee.mapper.DepartmentMapper;
import com.epam.employee.service.DepartmentService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * DepartmentServiceImpl — BASIC @Transactional USAGE (Spring annotation)
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * KEY POINTS demonstrated here:
 *
 * 1. We use {@code org.springframework.transaction.annotation.Transactional}
 *    (Spring), NOT {@code jakarta.transaction.Transactional} (Jakarta EE).
 *    See the README for a full comparison.
 *
 * 2. {@code readOnly = true} on fetch methods tells Spring to optimise:
 *    - Hibernate skips dirty-checking for managed entities.
 *    - Some JDBC drivers can route to a read-replica.
 *    - Flushing is suppressed — accidental writes will be silently ignored.
 *
 * 3. Default propagation = REQUIRED → joins the caller's transaction, or
 *    creates a new one if none exists.
 *
 * 4. Default isolation = DEFAULT → uses the database default
 *    (READ_COMMITTED in PostgreSQL).
 *
 * 5. By default, Spring only rolls back on unchecked exceptions
 *    (RuntimeException / Error). Use {@code rollbackFor = Exception.class}
 *    if you also want rollback on checked exceptions.
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final EntityManager entityManager;

    @Autowired
    public DepartmentServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * CREATE — default propagation (REQUIRED), default isolation (DEFAULT).
     * A new transaction is started because the controller layer does not
     * begin one.
     */
    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = DepartmentMapper.INSTANCE.mapDepartmentDTOToDepartment(departmentDTO);
        entityManager.persist(department);
        return DepartmentMapper.INSTANCE.mapDepartmentToDepartmentDTO(department);
    }

    /**
     * READ — {@code readOnly = true} enables Hibernate optimisations.
     * <p>
     * The underlying JDBC connection is set with
     * {@code connection.setReadOnly(true)}, and Hibernate disables
     * dirty-checking for all entities loaded in this session.
     */
    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO fetchDepartmentDetails(int deptId) {
        Department department = entityManager.find(Department.class, deptId);
        return DepartmentMapper.INSTANCE.mapDepartmentToDepartmentDTO(department);
    }

    /**
     * UPDATE — merge() copies the detached state onto a managed entity.
     * The transaction commits on normal return; rolls back on
     * RuntimeException.
     */
    @Override
    @Transactional
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO) {
        Department department = DepartmentMapper.INSTANCE.mapDepartmentDTOToDepartment(departmentDTO);
        entityManager.merge(department);
        return DepartmentMapper.INSTANCE.mapDepartmentToDepartmentDTO(department);
    }
}

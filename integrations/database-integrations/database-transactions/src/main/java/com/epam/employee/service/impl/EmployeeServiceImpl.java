package com.epam.employee.service.impl;

import com.epam.employee.dto.EmployeeDTO;
import com.epam.employee.entity.Employee;
import com.epam.employee.mapper.EmployeeMapper;
import com.epam.employee.repo.EmployeeRepo;
import com.epam.employee.service.EmployeeService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * EmployeeServiceImpl — demonstrates Spring @Transactional vs Jakarta
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * IMPORTANT — This class previously used {@code jakarta.transaction.Transactional}.
 * It has been migrated to {@code org.springframework.transaction.annotation.Transactional}.
 *
 * ┌──────────────────────────────────────────────────────────────────────┐
 * │  WHY SPRING @Transactional IS PREFERRED OVER JAKARTA @Transactional │
 * ├──────────────────────────────────────────────────────────────────────┤
 * │                                                                      │
 * │  Spring's annotation supports:                                       │
 * │   • propagation  — 7 propagation types (REQUIRED, REQUIRES_NEW …)   │
 * │   • isolation    — 4 isolation levels + DEFAULT                      │
 * │   • readOnly     — performance optimisation flag                     │
 * │   • timeout      — max seconds before auto-rollback                 │
 * │   • rollbackFor / noRollbackFor — fine-grained exception control    │
 * │   • transactionManager — choose a specific TxManager bean           │
 * │   • label        — descriptive labels for observability (5.3+)      │
 * │                                                                      │
 * │  Jakarta's annotation only supports:                                 │
 * │   • rollbackOn   — equivalent to rollbackFor                        │
 * │   • dontRollbackOn — equivalent to noRollbackFor                    │
 * │   • value (TxType) — only 5 propagation types, no NESTED            │
 * │                                                                      │
 * │  Both annotations work in Spring because Spring recognises Jakarta  │
 * │  @Transactional via JtaTransactionAnnotationParser, but you lose    │
 * │  access to isolation, readOnly, timeout, and the NESTED propagation.│
 * └──────────────────────────────────────────────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final EntityManager entityManager;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepo employeeRepo, EntityManager entityManager) {
        this.employeeRepo = employeeRepo;
        this.entityManager = entityManager;
    }

    /**
     * READ — {@code readOnly = true} tells Spring/Hibernate:
     * <ul>
     *   <li>Skip dirty-checking (no snapshot comparison at flush time)</li>
     *   <li>Set JDBC connection to read-only (driver may route to replica)</li>
     *   <li>Suppress flush — any accidental entity mutation is NOT written</li>
     * </ul>
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO findEmployeeById(int empId) {
        Employee employee = entityManager.find(Employee.class, empId);
        return EmployeeMapper.INSTANCE.mapEmployeeToEmployeeDTO(employee);
    }

    /**
     * CREATE — uses {@code rollbackFor = Exception.class} so that even
     * checked exceptions cause a rollback.
     * <p>
     * <b>Default behaviour:</b> Spring only rolls back on unchecked exceptions.
     * If your method throws a checked exception (e.g., IOException), the
     * transaction would COMMIT by default — usually not what you want.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.INSTANCE.mapEmployeeDTOToEmployee(employeeDTO);
        entityManager.persist(employee);
        return EmployeeMapper.INSTANCE.mapEmployeeToEmployeeDTO(employee);
    }

    /**
     * UPDATE — merge() returns a NEW managed instance; the passed-in
     * entity remains detached. Always use the returned object.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.INSTANCE.mapEmployeeDTOToEmployee(employeeDTO);
        Employee updatedEmployee = entityManager.merge(employee);
        return EmployeeMapper.INSTANCE.mapEmployeeToEmployeeDTO(updatedEmployee);
    }
}

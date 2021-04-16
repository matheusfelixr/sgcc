package com.matheusfelixr.sgcc.repository;

import com.matheusfelixr.sgcc.model.domain.Employee;
import com.matheusfelixr.sgcc.model.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>  {

    Optional<Employee> findByPersonCpf(String cpf);

    Optional<Employee> findByPerson(Person person);

}

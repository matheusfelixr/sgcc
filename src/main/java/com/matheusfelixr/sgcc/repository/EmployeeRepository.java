package com.matheusfelixr.sgcc.repository;

import com.matheusfelixr.sgcc.model.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Person, Long>  {

}

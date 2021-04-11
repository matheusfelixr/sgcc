package com.matheusfelixr.sgcc.repository;

import com.matheusfelixr.sgcc.model.domain.Employee;
import com.matheusfelixr.sgcc.model.domain.PointControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PointControlRepository extends JpaRepository<PointControl, Long>  {

    List<PointControl> findByDateAfterAndEmployee(Date date, Employee employee);

}

package com.matheusfelixr.sgcc.repository;

import com.matheusfelixr.sgcc.model.domain.HistoryAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryAuthenticationRepository extends JpaRepository<HistoryAuthentication, Long>  {

}

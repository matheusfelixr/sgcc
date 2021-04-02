package com.matheusfelixr.sgcc.repository;

import com.matheusfelixr.sgcc.model.domain.HistoryResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryResetPasswordRepository extends JpaRepository<HistoryResetPassword, Long>  {

}

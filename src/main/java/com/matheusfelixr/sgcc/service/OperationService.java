package com.matheusfelixr.sgcc.service;

import com.matheusfelixr.sgcc.model.domain.Employee;
import com.matheusfelixr.sgcc.model.domain.Operation;
import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;

    public Operation create(Operation operation, UserAuthentication currentUser) throws Exception {

        operation.getDataControl().markCreate(currentUser);
        return this.save(operation);
    }

    private Operation save(Operation operation) {
        return this.operationRepository.save(operation);
    }

    public Optional<Operation> findById(Long idOperation) throws Exception {
        return this.operationRepository.findById(idOperation);
    }
}

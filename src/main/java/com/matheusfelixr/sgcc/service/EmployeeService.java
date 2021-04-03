package com.matheusfelixr.sgcc.service;

import com.matheusfelixr.sgcc.model.domain.Employee;
import com.matheusfelixr.sgcc.model.domain.Person;
import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.security.CreateUserRequestDTO;
import com.matheusfelixr.sgcc.repository.EmployeeRepository;
import com.matheusfelixr.sgcc.util.Cpf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee create(CreateUserRequestDTO createUserRequestDTO, UserAuthentication currentUser) throws Exception{

        this.validate(createUserRequestDTO);

        Employee employee = this.createEmployee(createUserRequestDTO, currentUser);

        return this.save(employee);
    }

    private Employee save(Employee employee) {
        return this.employeeRepository.save(employee);
    }

    private Employee createEmployee(CreateUserRequestDTO createUserRequestDTO, UserAuthentication currentUser) {
        Person person = new Person();
        person.setCpf(createUserRequestDTO.getCpf());
        person.setName(createUserRequestDTO.getName());
        person.getDataControl().markCreate(currentUser);

        Employee employee = new Employee();
        employee.setActive(Boolean.TRUE);
        employee.setPerson(person);
        employee.getDataControl().markCreate(currentUser);
        return employee;
    }

    private void validate(CreateUserRequestDTO createUserRequestDTO) throws ValidationException {
        if(!Cpf.validateCpf(createUserRequestDTO.getCpf())){
            throw new ValidationException("CPF inválido. Favor verifique se digitou corretamente.");
        }
        Optional<Employee> employee = findByCpf(createUserRequestDTO);

        if(employee.isPresent()){
            throw new ValidationException("Funcionario já cadastrado.");
        }

        if(createUserRequestDTO.getName() == null || createUserRequestDTO.getName().equals("") ){
            throw new ValidationException("Nome não pode ser vazio.");
        }
    }

    private Optional<Employee> findByCpf(CreateUserRequestDTO createUserRequestDTO) {
        return this.employeeRepository.findByPersonCpf(createUserRequestDTO.getCpf());
    }

    private Optional<Employee> findById(Long idEmployee) {
        return this.employeeRepository.findById(idEmployee);
    }
}

package com.matheusfelixr.sgcc.service;

import com.matheusfelixr.sgcc.model.domain.Employee;
import com.matheusfelixr.sgcc.model.domain.Operation;
import com.matheusfelixr.sgcc.model.domain.Person;
import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.employee.EmployeeDTO;
import com.matheusfelixr.sgcc.model.dto.operation.OperationDTO;
import com.matheusfelixr.sgcc.model.dto.person.PersonDTO;
import com.matheusfelixr.sgcc.model.dto.userAuthentication.AllUserAtributesDTO;
import com.matheusfelixr.sgcc.model.dto.userAuthentication.UserAuthenticationDTO;
import com.matheusfelixr.sgcc.repository.UserAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

@Service
public class UserAuthenticationService {

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private OperationService operationService;

    public UserAuthentication create(UserAuthentication userAuthentication) throws Exception {

        //Realiza validações
        validateNewUser(userAuthentication);

        String password = userAuthentication.getPassword();
        userAuthentication.setPassword(this.passwordEncoder.encode(password));

        UserAuthentication ret = userAuthenticationRepository.save(userAuthentication);
        emailService.newUser(userAuthentication, password);
        return ret;
    }

    public Optional<UserAuthentication> findByUserName(String userName){
        return Optional.ofNullable(userAuthenticationRepository.findByUserNameAndCancellationCancellationDateIsNull(userName));
    }

    public Optional<UserAuthentication> findByEmail(String Email){
        return Optional.ofNullable(userAuthenticationRepository.findByEmailAndCancellationCancellationDateIsNull(Email));
    }

    public UserAuthentication modifyPassword(String userName, String password, Boolean changePassword) throws Exception {
        UserAuthentication userAuthentication = this.validateModifyPassword(userName);
        userAuthentication.setPassword(this.passwordEncoder.encode(password));
        userAuthentication.setChangePassword(changePassword);
        userAuthenticationRepository.save(userAuthentication);
        return userAuthentication;
    }

    private void validateNewUser(UserAuthentication userAuthentication) throws Exception {
        Optional<UserAuthentication> userResName = this.findByUserName(userAuthentication.getUserName());
        if(userResName.isPresent()) {
            throw new ValidationException("Usuário já existente");
        }

        Optional<UserAuthentication> userResEmail = this.findByEmail(userAuthentication.getEmail());
        if(userResEmail.isPresent()) {
            throw new ValidationException("E-mail já cadastrado para outro usuário");
        }
    }

    private UserAuthentication validateModifyPassword(String userName) throws Exception {
        Optional<UserAuthentication> userResName = this.findByUserName(userName);
        if(!userResName.isPresent()) {
            throw new ValidationException("Usuário não encontrado");
        }
        return userResName.get();
    }

    public AllUserAtributesDTO generateAllUserAtributesByPerson(Optional<List<Person>> persons) throws Exception {
        if(persons.get().size() == 1){
            Person person = persons.get().get(0);
            Optional<Employee> employee = this.employeeService.findByPerson(person);
            Optional<UserAuthentication> userAuthentication = this.findByEmployee(employee.get());

            AllUserAtributesDTO allUserAtributesDTO = new AllUserAtributesDTO(UserAuthenticationDTO.convertToDTO(userAuthentication.get()),
                    PersonDTO.convertToDTO(person), EmployeeDTO.convertToDTO(employee.get()));

            return allUserAtributesDTO;
        }else{
            throw new ValidationException("Mais de um resultado.");
        }
    }

    public Optional<UserAuthentication> findByEmployee(Employee employee) {
        return this.userAuthenticationRepository.findByEmployee(employee);
    }
}

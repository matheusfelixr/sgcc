package com.matheusfelixr.sgcc.service;

import com.matheusfelixr.sgcc.model.domain.Employee;
import com.matheusfelixr.sgcc.model.domain.Operation;
import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.MessageDTO;
import com.matheusfelixr.sgcc.model.dto.UserAuthentication.UpdateUserAuthenticationDTO;
import com.matheusfelixr.sgcc.repository.UserAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
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
    private OperationService operationService;

    public UserAuthentication create(UserAuthentication userAuthentication) throws Exception {

        //Realiza validações
        validateNewUser(userAuthentication);

        String password = userAuthentication.getPassword();
        userAuthentication.setPassword(this.passwordEncoder.encode(password));

        UserAuthentication ret = this.save(userAuthentication);
        emailService.newUser(userAuthentication, password);
        return ret;
    }

    private UserAuthentication save(UserAuthentication userAuthentication) {
        return this.userAuthenticationRepository.save(userAuthentication);
    }

    public Optional<UserAuthentication> findByUserName(String userName) {
        return Optional.ofNullable(userAuthenticationRepository.findByUserNameAndCancellationCancellationDateIsNull(userName));
    }

    public Optional<UserAuthentication> findByEmail(String Email) {
        return Optional.ofNullable(userAuthenticationRepository.findByEmailAndCancellationCancellationDateIsNull(Email));
    }

    public Optional<UserAuthentication> findByCpf(String cpf) {
        return Optional.ofNullable(userAuthenticationRepository.findByEmployeePersonCpf(cpf));
    }

    public UserAuthentication modifyPassword(String userName, String password, Boolean changePassword, UserAuthentication currentUser) throws Exception {
        UserAuthentication userAuthentication = this.validateModifyPassword(userName);
        userAuthentication.setPassword(this.passwordEncoder.encode(password));
        userAuthentication.setChangePassword(changePassword);
        if (currentUser != null) {
            userAuthentication.getDataControl().markModified(currentUser);
        }
        userAuthenticationRepository.save(userAuthentication);
        return userAuthentication;
    }

    private void validateNewUser(UserAuthentication userAuthentication) throws Exception {
        Optional<UserAuthentication> userResName = this.findByUserName(userAuthentication.getUserName());
        if (userResName.isPresent()) {
            throw new ValidationException("Usuário já existente");
        }

        Optional<UserAuthentication> userResEmail = this.findByEmail(userAuthentication.getEmail());
        if (userResEmail.isPresent()) {
            throw new ValidationException("E-mail já cadastrado para outro usuário");
        }
    }

    private UserAuthentication validateModifyPassword(String userName) throws Exception {
        Optional<UserAuthentication> userResName = this.findByUserName(userName);
        if (!userResName.isPresent()) {
            throw new ValidationException("Usuário não encontrado");
        }
        return userResName.get();
    }

    public Optional<UserAuthentication> findByEmployee(Employee employee) {

        return this.userAuthenticationRepository.findByEmployee(employee);
    }

    public MessageDTO cancel(Long idUser, String observation, UserAuthentication currentUser) throws Exception {
        Optional<UserAuthentication> userAuthentication = this.findById(idUser);
        userAuthentication.orElseThrow(() -> new ValidationException("Não foi encontrado usuário."));

        userAuthentication.get().getCancellation().markCanceled(observation, currentUser);
        userAuthentication.get().getEmployee().getDataControl().markModified(currentUser);
        userAuthentication.get().getEmployee().getPerson().getDataControl().markModified(currentUser);
        this.save(userAuthentication.get());

        return new MessageDTO("Sucesso ao realizar cancelamento de usuário.");
    }

    public Optional<UserAuthentication> findById(Long idUser) throws Exception{
        return this.userAuthenticationRepository.findById(idUser);
    }

    public MessageDTO removeCancel(Long idUser, UserAuthentication currentUser) throws Exception {
        Optional<UserAuthentication> userAuthentication = this.findById(idUser);
        userAuthentication.orElseThrow(() -> new ValidationException("Não foi encontrado usuário."));

        userAuthentication.get().getCancellation().removeCanceled();
        userAuthentication.get().getEmployee().getDataControl().markModified(currentUser);
        userAuthentication.get().getEmployee().getPerson().getDataControl().markModified(currentUser);
        this.save(userAuthentication.get());

        return new MessageDTO("Cancelamento removido com sucesso.");
    }

    public MessageDTO update(UpdateUserAuthenticationDTO updateUserAuthenticationDTO, UserAuthentication currentUser) throws Exception {
        Optional<UserAuthentication> userAuthentication = this.findById(updateUserAuthenticationDTO.getId());
        userAuthentication.orElseThrow(() -> new ValidationException("Não foi encontrado usuário."));

        Optional<UserAuthentication> byUserName = this.findByUserName(updateUserAuthenticationDTO.getUserName());
        if (byUserName.isPresent() && !byUserName.get().getUserName().equals(userAuthentication.get().getUserName())) {
            new ValidationException("Já existe o nome de usuario para outra pessoa.");
        }

        Optional<UserAuthentication> byCpf = this.findByCpf(updateUserAuthenticationDTO.getCpf());
        if (byCpf.isPresent() && !byCpf.get().getEmployee().getPerson().getCpf().equals(userAuthentication.get().getEmployee().getPerson().getCpf())) {
            new ValidationException("Já existe o CPF cadastrado para outra pessoa.");
        }

        Optional<UserAuthentication> byEmail = this.findByEmail(updateUserAuthenticationDTO.getEmail());
        if (byEmail.isPresent() && !byEmail.get().getEmail().equals(userAuthentication.get().getEmail())) {
            new ValidationException("Já existe o email cadastrado para outra pessoa.");
        }

        Optional<Operation> operation = operationService.findById(updateUserAuthenticationDTO.getIdOperation());
        operation.orElseThrow(() -> new ValidationException("Não foi encontrada operação informada."));


        userAuthentication.get().getDataControl().markModified(currentUser);
        userAuthentication.get().getEmployee().getDataControl().markModified(currentUser);
        userAuthentication.get().getEmployee().getPerson().getDataControl().markModified(currentUser);

        userAuthentication.get().setUserName(updateUserAuthenticationDTO.getUserName());
        userAuthentication.get().getEmployee().getPerson().setName(updateUserAuthenticationDTO.getName());
        userAuthentication.get().getEmployee().getPerson().setCpf(updateUserAuthenticationDTO.getCpf());
        userAuthentication.get().setEmail(updateUserAuthenticationDTO.getEmail());
        userAuthentication.get().setIsAdmin(updateUserAuthenticationDTO.getIsAdmin());
        userAuthentication.get().getEmployee().setOperation(operation.get());
        userAuthentication.get().getEmployee().setIdEmployeeWiseCall(updateUserAuthenticationDTO.getIdEmployeeWiseCall());

        this.save(userAuthentication.get());
        return new MessageDTO("Sucesso ao editar usuario.");
    }
}

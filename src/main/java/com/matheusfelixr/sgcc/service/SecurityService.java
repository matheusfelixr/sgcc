package com.matheusfelixr.sgcc.service;

import com.matheusfelixr.sgcc.model.domain.Employee;
import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.MessageDTO;
import com.matheusfelixr.sgcc.model.dto.security.AuthenticateRequestDTO;
import com.matheusfelixr.sgcc.model.dto.security.AuthenticateResponseDTO;
import com.matheusfelixr.sgcc.model.dto.security.CreateUserRequestDTO;
import com.matheusfelixr.sgcc.model.dto.security.NewPasswordRequestDTO;
import com.matheusfelixr.sgcc.security.JwtTokenUtil;
import com.matheusfelixr.sgcc.util.EmailHelper;
import com.matheusfelixr.sgcc.util.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.util.Locale;
import java.util.Optional;

@Component
public class SecurityService implements UserDetailsService {
	
	@Autowired
	private UserAuthenticationService userAuthenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HistoryAuthenticationService historyAuthenticationService;

    @Autowired
    private HistoryResetPasswordService historyResetPasswordService;

    @Autowired
    private EmployeeService employeeService;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	    	
    	Optional<UserAuthentication> userAuthentication = this.userAuthenticationService.findByUserName(username);
    	
    	if(!userAuthentication.isPresent()) {
            throw new UsernameNotFoundException(username + " nao encontrado");
        }
    	
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                userAuthentication.get().getUserName(),
                userAuthentication.get().getPassword(),
                AuthorityUtils.createAuthorityList(userAuthentication.get().getRoles())
        );
		return userDetails;
    }


    public AuthenticateResponseDTO authenticate(AuthenticateRequestDTO authenticateRequestDTO, HttpServletRequest httpServletRequest ) throws Exception {
        try {
            //valida autenticacao
            this.validateAuthenticate(authenticateRequestDTO);
            //Autentica o usuario
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticateRequestDTO.getUserName(), authenticateRequestDTO.getPassword()));
            //Busca o userDetails para geracao do token
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticateRequestDTO.getUserName());
            //Gera o token
            String token = jwtTokenUtil.generateToken(userDetails);
            //Busca os dados do usuario
            UserAuthentication userAuthentication = this.userAuthenticationService.findByUserName(authenticateRequestDTO.getUserName()).get();
            //Gera historico
            historyAuthenticationService.generateHistorySuccess(userAuthentication, httpServletRequest );
            return new AuthenticateResponseDTO(userAuthentication.getUserName(), token,userAuthentication.getChangePassword(), userAuthentication.getIsAdmin());
        } catch (DisabledException e) {
            historyAuthenticationService.generateHistoryFail(authenticateRequestDTO, httpServletRequest, "Usu??rio desabilitado: " + authenticateRequestDTO.getUserName());
            throw new ValidationException("Usu??rio desabilitado");
        } catch (BadCredentialsException e) {
            historyAuthenticationService.generateHistoryFail(authenticateRequestDTO, httpServletRequest, "Senha invalida para o usu??rio: " + authenticateRequestDTO.getUserName());
            throw new ValidationException("Verifique se digitou corretamente usu??rio e senha.");
        }
    }

    private void validateAuthenticate(AuthenticateRequestDTO authenticateRequestDTO) throws ValidationException {
        if(authenticateRequestDTO.getUserName() == null || authenticateRequestDTO.getUserName().length() == 0 ){
            throw new ValidationException("Usu??rio n??o pode ser vazio");
        }
        if(authenticateRequestDTO.getPassword() == null || authenticateRequestDTO.getUserName().length() == 0 ){
            throw new ValidationException("Senha n??o pode ser vazio");
        }
        authenticateRequestDTO.setUserName(authenticateRequestDTO.getUserName().trim().toLowerCase(Locale.ROOT));
    }

    public MessageDTO resetPassword(String userName, HttpServletRequest httpServletRequest) throws Exception {
        String password = Password.generatePasswordInt(5);
        UserAuthentication userAuthentication = userAuthenticationService.modifyPassword(userName, password , true);
        emailService.resetPassword(userAuthentication, password);
        historyResetPasswordService.generateHistory(userAuthentication, httpServletRequest);
        return new MessageDTO ("Foi enviado uma nova senha para o E-mail: "+EmailHelper.maskEmail(userAuthentication.getEmail()));
    }


    public MessageDTO createUser(CreateUserRequestDTO createUserRequestDTO, UserAuthentication currentUser) throws Exception {
        // Pego senha
        String password = this.getPassword(createUserRequestDTO);

        Employee employee = this.employeeService.create(createUserRequestDTO, currentUser);

        //Cria objeto a ser salvo
        UserAuthentication ret = this.getUserAuthentication(createUserRequestDTO, password, employee);

        //chama metodo para criar usario
        this.userAuthenticationService.create(ret);

        return new MessageDTO ("Usu??rio cadastrado com sucesso! Foi enviada a senha para o E-mail: " + EmailHelper.maskEmail(ret.getEmail()));
    }

    private UserAuthentication getUserAuthentication(CreateUserRequestDTO createUserRequestDTO, String password, Employee employee) {
        UserAuthentication ret = new UserAuthentication();
        ret.setUserName(createUserRequestDTO.getUserName().trim());
        ret.setPassword(password);
        ret.setEmail(createUserRequestDTO.getEmail());
        ret.setChangePassword(true);
        ret.setIsAdmin(createUserRequestDTO.getIsAdmin());
        ret.setEmployee(employee);
        return ret;
    }

    private String getPassword(CreateUserRequestDTO createUserRequestDTO) {
        String password ="";
        if(createUserRequestDTO.getPassword() == null || createUserRequestDTO.getPassword().equals("") ){
            password = Password.generatePasswordInt(5);
        }else{
            password = createUserRequestDTO.getPassword();
        }
        return password;
    }

    public AuthenticateResponseDTO newPassword(NewPasswordRequestDTO newPasswordRequestDTO, HttpServletRequest httpServletRequest) throws Exception {
        UserAuthentication userAuthentication = userAuthenticationService.modifyPassword(newPasswordRequestDTO.getUserName(), newPasswordRequestDTO.getPassword() , false);
        emailService.newPassword(userAuthentication);
        return this.authenticate(new AuthenticateRequestDTO(newPasswordRequestDTO.getUserName(), newPasswordRequestDTO.getPassword()), httpServletRequest);
    }

    public UserAuthentication getCurrentUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Optional<UserAuthentication> ret = userAuthenticationService.findByUserName(principal.getUsername());
        if(!ret.isPresent()){
            throw new ValidationException("Erro ao encontrar usuario. Contate o desenvolvedor.");
        }

        return ret.get();
    }

}
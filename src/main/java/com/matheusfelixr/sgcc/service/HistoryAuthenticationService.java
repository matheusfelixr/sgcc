package com.matheusfelixr.sgcc.service;

import com.matheusfelixr.sgcc.model.domain.HistoryAuthentication;
import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.security.AuthenticateRequestDTO;
import com.matheusfelixr.sgcc.repository.HistoryAuthenticationRepository;
import com.matheusfelixr.sgcc.util.DateNtp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Service
public class HistoryAuthenticationService {

    @Autowired
    private HistoryAuthenticationRepository historyAuthenticationRepository;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    public HistoryAuthentication generateHistorySuccess(UserAuthentication userAuthentication, HttpServletRequest httpServletRequest ){
        HistoryAuthentication historyAuthentication = new HistoryAuthentication();
        historyAuthentication.setUserAuthentication(userAuthentication);
        historyAuthentication.setDate(new Date());
        historyAuthentication.setNtpDate(DateNtp.getDate());
        historyAuthentication.setObservation("Login realizado com sucesso");
        if(httpServletRequest != null){
            historyAuthentication.setIp(httpServletRequest.getRemoteAddr());
        }
        return this.save(historyAuthentication);
    }

    public HistoryAuthentication generateHistoryFail(AuthenticateRequestDTO authenticateRequestDTO, HttpServletRequest httpServletRequest, String observation ){
        HistoryAuthentication historyAuthentication = new HistoryAuthentication();
        historyAuthentication.setUserAuthentication(this.getUser(authenticateRequestDTO));
        historyAuthentication.setDate(new Date());
        historyAuthentication.setNtpDate(DateNtp.getDate());
        historyAuthentication.setObservation(observation);
        if(httpServletRequest != null){
            historyAuthentication.setIp(httpServletRequest.getRemoteAddr());
        }
        return this.save(historyAuthentication);
    }

    private UserAuthentication getUser(AuthenticateRequestDTO authenticateRequestDTO) {
        Optional<UserAuthentication> userAuthentication = userAuthenticationService.findByUserName(authenticateRequestDTO.getUserName());
        if(userAuthentication.isPresent()){
            return userAuthentication.get();
        }
        return null;
    }


    private HistoryAuthentication save(HistoryAuthentication historyAuthentication){
        return historyAuthenticationRepository.save(historyAuthentication);
    }

}

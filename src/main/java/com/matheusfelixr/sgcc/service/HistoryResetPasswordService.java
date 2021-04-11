package com.matheusfelixr.sgcc.service;

import com.matheusfelixr.sgcc.model.domain.HistoryResetPassword;
import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.repository.HistoryResetPasswordRepository;
import com.matheusfelixr.sgcc.util.DateNtp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class HistoryResetPasswordService {

    @Autowired
    private HistoryResetPasswordRepository historyResetPasswordRepository;

    public HistoryResetPassword generateHistory(UserAuthentication userAuthentication, HttpServletRequest httpServletRequest ){
        HistoryResetPassword historyResetPassword = new HistoryResetPassword();
        historyResetPassword.setUserAuthentication(userAuthentication);
        historyResetPassword.setDate(new Date());
        historyResetPassword.setNtpDate(DateNtp.getDate());
        if(httpServletRequest != null){
            historyResetPassword.setIp(httpServletRequest.getRemoteAddr());
        }
        return this.save(historyResetPassword);
    }



    private HistoryResetPassword save(HistoryResetPassword historyResetPassword){
        return historyResetPasswordRepository.save(historyResetPassword);
    }

}

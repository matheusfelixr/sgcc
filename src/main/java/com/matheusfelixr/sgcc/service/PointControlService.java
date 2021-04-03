package com.matheusfelixr.sgcc.service;

import com.matheusfelixr.sgcc.model.domain.PointControl;
import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.MessageDTO;
import com.matheusfelixr.sgcc.model.enums.ControlAccessPointEnum;
import com.matheusfelixr.sgcc.repository.PointControlRepository;
import com.matheusfelixr.sgcc.util.DateNtp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class PointControlService {

    @Autowired
    private PointControlRepository pointControlRepository;

    public MessageDTO registryPoint(UserAuthentication currentUser, HttpServletRequest httpServletRequest) throws Exception {

        if (currentUser.getEmployee() == null || !currentUser.getEmployee().getActive()) {
            throw new ValidationException("Funcionario não ativo ou não encontrado para cadastrar ponto.");
        }

        if (currentUser.getCancellation().isCancelled()) {
            throw new ValidationException("Usuário cancelado! Favor verificar com administrador.");
        }
        PointControl pointControl = new PointControl();

        pointControl.setDate(new Date());
        pointControl.setNtpDate(DateNtp.getDate());
        pointControl.setIp(httpServletRequest.getRemoteAddr());
        pointControl.setEmployee(currentUser.getEmployee());

        if (this.itWasAnEntryLast()) {
            pointControl.setControlAccessPoint(ControlAccessPointEnum.OUTPUT);
        }else{
            pointControl.setControlAccessPoint(ControlAccessPointEnum.INPUT);
        }

        this.save(pointControl);
        return new MessageDTO("Sucesso ao registrar ponto.");
    }

    public PointControl save(PointControl pointControl) {
        return this.pointControlRepository.save(pointControl);
    }

    /**
     * Verifica se o ultimo ponto foi uma entrada se foi retorna true se não retorna false
     *
     * @return
     */
    public Boolean itWasAnEntryLast() {

        List<PointControl> pointControlsToday = this.findByToDay();
        PointControl lastPointControlByDate = new PointControl();
        if (!pointControlsToday.isEmpty()) {
            //adiciona o primeiro item ao lastPointControlByDate
            lastPointControlByDate = pointControlsToday.get(0);
            for (PointControl pointControl : pointControlsToday) {
                if (pointControl.getDate().after(lastPointControlByDate.getDate())) {
                    lastPointControlByDate = pointControl;
                }
            }

            if (lastPointControlByDate.getControlAccessPoint() == ControlAccessPointEnum.OUTPUT) {
                return false;
            } else {
                return true;
            }


        } else {
            return false;
        }
    }

    /**
     * Pega todos os pontos batidos do dia
     *
     * @return
     */
    public List<PointControl> findByToDay() {
        LocalDate today = LocalDate.now();
        Date date = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return this.pointControlRepository.findByDateAfter(date);
    }
}

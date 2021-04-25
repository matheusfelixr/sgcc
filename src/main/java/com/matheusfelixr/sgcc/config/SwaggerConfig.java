package com.matheusfelixr.sgcc.config;

import com.matheusfelixr.sgcc.model.domain.UserAuthentication;
import com.matheusfelixr.sgcc.model.dto.security.AuthenticateRequestDTO;
import com.matheusfelixr.sgcc.security.JwtTokenUtil;
import com.matheusfelixr.sgcc.service.SecurityService;
import com.matheusfelixr.sgcc.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private UserDetailsService apiUserService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserAuthenticationService userAuthenticationService;

    @Autowired
    SecurityService securityService;

    @Bean
    public Docket api() {

        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new ApiKey("Bearer", "Authorization", "header"));

        return new Docket(DocumentationType.SWAGGER_2)
                .produces(Collections.singleton("application/json"))
                .consumes(Collections.singleton("application/json"))
                .ignoredParameterTypes(Authentication.class)
                .globalOperationParameters(
                        Arrays.asList(
                                new ParameterBuilder()
                                        .name("Authorization")
                                        .modelRef(new ModelRef("string"))
                                        .parameterType("header")
                                        .required(true)
                                        .hidden(true)
                                        .defaultValue("Bearer " + security())
                                        .build()
                        ))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    public String security() {
        String token;
        try {
            AuthenticateRequestDTO authenticateRequestDTO = getUser();
            token = securityService.authenticate(authenticateRequestDTO, null ).getToken();
        } catch (Exception e) {
            e.printStackTrace();
            token = "";
        }

        return token;
    }

    private AuthenticateRequestDTO getUser() {
        try {
            Optional<UserAuthentication> user = userAuthenticationService.findByUserName("swagger");
            
            if (!user.isPresent()) {
                throw new ClassCastException("Usuario não encontrado.");
            }

            return new AuthenticateRequestDTO(user.get().getUserName(), "123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

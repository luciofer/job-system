package com.lfdev.gestao_vagas.modules.company.useCases;

import com.auth0.jwt.JWT;
import com.lfdev.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import com.lfdev.gestao_vagas.modules.company.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class AuthCompanyUseCase {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {

        //Verificar se a company existe
        var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("Company not found"));

        //Verificar se as senhas são iguais
        var passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        // Se não for igual -> Erro
        if(!passwordMatches){
            throw new AuthenticationException();
        }

        //Se for igual -> Gerar token
        JWT.create().withIssuer("javagas")
                .withSubject(company.getId().toString());

    }

}

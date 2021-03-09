package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;
    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication){
        Usuario logado = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
        return Jwts.builder()
                .setIssuer("API do Fórum da Alura")         // Quem está gerando o token
                .setSubject(logado.getId().toString())      // Usuario dono do token
                .setIssuedAt(hoje)                          // Data de criação
                .setExpiration(dataExpiracao)               // Data de expiração
                .signWith(SignatureAlgorithm.HS256, secret) // Senha
                .compact();                                 // Compactar e transformar em uma String
    }
}

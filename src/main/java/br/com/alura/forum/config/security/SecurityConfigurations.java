package br.com.alura.forum.config.security;

import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Profile(value = {"prod", "test"})
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // Configurações de autenticação (controle de acesso, login...)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder()); // Hash senha
    }

    // Configurações de autorização (url, quem pode acessar determinada url...)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/topicos").permitAll()
                .antMatchers(HttpMethod.GET, "/topicos/*").permitAll() // * = Qualquer coisa (no caso, id)
                .antMatchers(HttpMethod.POST, "/auth").permitAll() // Liberar o acesso para o endpoint
                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll() // * = Qualquer coisa/ qualquer coisa
                .antMatchers(HttpMethod.DELETE, "/topicos/*").hasRole("MODERADOR") // Só pode executar a ação quem for do perfil MODERADOR
                .anyRequest().authenticated() // Qualquer outro request (url), precisa estar autenticado
                /*.and().formLogin();  // Pro Spring gerar um formulario de autenticação*/
                .and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // Avisa o Spring que quando fizer autenticação, não é pra criar sessão pois usaremos token
                .and().addFilterBefore(new AutenticacaoViaTokenFilter
                (tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
    }

    // Configurações de recursos estáticos (requisições para arquivos js, css, imagens...)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
    }
}

package microservicegroup.folksdevsecurityinmemory.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig
{
    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService()
    {
        UserDetails user1 = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("user"))
                .roles("User")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("Admin")
                .build();
        return new InMemoryUserDetailsManager(user1, admin);
    }
    @Bean
    public SecurityFilterChain securityFilterChain  (HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                .headers(x-> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                /*FormLogini İptal Etme*/
                .formLogin(AbstractHttpConfigurer::disable)
                /*FormLogini Tekrar Default Etme*/
                /*.formLogin(Customizer.withDefaults())*/
                /*PermitAll Gelirse ne gelirse gelsin izin ver demek
                * hasAnyAuthority Authenticate ol yeterli benim için demek
                * hasAuthority şu auth varsa dahil et demek
                * hasAnyrole denirse rolu varsa izin ver demek ,
                * HasRole ise authenticate ol ve şu rollerden birine sahip ol demek  */
                .authorizeHttpRequests(x-> x.requestMatchers("/public/**","/auth/**").permitAll())
                .authorizeHttpRequests(x->x.anyRequest().authenticated())
                .authorizeHttpRequests(x->x.requestMatchers("/Private/user/**").hasRole("User"))
                .authorizeHttpRequests(x->x.requestMatchers("/Private/admin/**").hasRole("Admin"))
                .httpBasic(Customizer.withDefaults());
                return httpSecurity.build();
    }
}

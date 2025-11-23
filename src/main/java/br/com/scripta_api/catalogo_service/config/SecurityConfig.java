package br.com.scripta_api.catalogo_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(CorsConfiguration()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura o Resource Server para validar JWTs
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )

                .authorizeHttpRequests(authz -> authz
                        // (RF-A03, RF-A04) Busca e Detalhes: Público
                        .requestMatchers(HttpMethod.GET, "/livros", "/livros/**", "/livros/buscar").permitAll()

                        // (RF-S05) Gestão de Estoque: Autenticado (qualquer role, pois é chamado por outros serviços)
                        // NOTA: Esta regra deve vir ANTES da regra geral de PUT /livros/** para não ser bloqueada
                        .requestMatchers(HttpMethod.PUT, "/livros/*/estoque/**").authenticated()

                        // (RF-B03, RF-B06) Cadastro e Importação: Bibliotecário
                        .requestMatchers(HttpMethod.POST, "/livros", "/livros/importar/**").hasRole("BIBLIOTECARIO")

                        // (RF-B04) Edição: Bibliotecário
                        .requestMatchers(HttpMethod.PUT, "/livros/**").hasRole("BIBLIOTECARIO")

                        // (RF-B05) Remoção: Bibliotecário
                        .requestMatchers(HttpMethod.DELETE, "/livros/**").hasRole("BIBLIOTECARIO")

                        // Qualquer outra requisição deve estar autenticada
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource CorsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("*"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));

        // Headers permitidos (Authorization é crucial para enviar o Token)
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));

        // Expor headers (caso o front precise ler algum header de resposta específico)
        corsConfig.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    /**
     * Decodifica e valida a assinatura do JWT usando a chave secreta (HMAC).
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        var secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    /**
     * Converte as claims do JWT para as Authorities do Spring Security.
     * Lê a claim "roles" e não adiciona prefixo (pois já vem como ROLE_...).
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedConverter = new JwtGrantedAuthoritiesConverter();
        grantedConverter.setAuthorityPrefix(""); // O prefixo ROLE_ já vem do usuario-service
        grantedConverter.setAuthoritiesClaimName("roles"); // Nome da claim no JWT

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedConverter);
        return jwtConverter;
    }
}
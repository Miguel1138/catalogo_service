package br.com.scripta_api.catalogo_service.config;

/*
TODO: Anotar com @Configuration, @EnableWebSecurity, @RequiredArgsConstructor.

TODO: NÃO injetar AuthenticationProvider (diferente do usuario-service).

TODO: NÃO injetar JwtAuthenticatedFilter (vamos usar o do Spring).

TODO: Injetar @Value("${jwt.secret}") String jwtSecret.

TODO: Criar o @Bean SecurityFilterChain:

TODO: Configurar csrf(disable), sessionManagement(STATELESS).

TODO: [NOVO] Configurar o Resource Server: .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.decoder(jwtDecoder()))).

TODO: Configurar as regras authorizeHttpRequests:

requestMatchers(HttpMethod.GET, "/livros", "/livros/**", "/livros/buscar").permitAll()

requestMatchers(HttpMethod.POST, "/livros", "/livros/importar/**").hasRole("BIBLIOTECARIO")

requestMatchers(HttpMethod.PUT, "/livros/**").hasRole("BIBLIOTECARIO")

requestMatchers(HttpMethod.DELETE, "/livros/**").hasRole("BIBLIOTECARIO")

requestMatchers(HttpMethod.PUT, "/livros/{id}/estoque/**").authenticated()

TODO: Criar o @Bean public JwtDecoder jwtDecoder():

TODO: Criar a chave HMAC a partir do jwtSecret (usando new SecretKeySpec(jwtSecret.getBytes(), "HMACSHA256")).

TODO: Retornar NimbusJwtDecoder.withSecretKey(secretKey).build().

TODO: Criar o @Bean public JwtAuthenticationConverter jwtAuthenticationConverter():

TODO: Este bean diz ao Spring como ler a claim roles (que já vem com ROLE_).

TODO: JwtGrantedAuthoritiesConverter grantedConverter = new JwtGrantedAuthoritiesConverter();

TODO: grantedConverter.setAuthorityPrefix(""); (Pois o prefixo ROLE_ já vem do usuarios-service).

TODO: grantedConverter.setAuthoritiesClaimName("roles"); (O nome da claim no JWT).

TODO: JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

TODO: jwtConverter.setJwtGrantedAuthoritiesConverter(grantedConverter);

TODO: return jwtConverter;
 */
public class SecurityConfig {
}

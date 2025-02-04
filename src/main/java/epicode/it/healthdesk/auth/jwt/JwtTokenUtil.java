package epicode.it.healthdesk.auth.jwt;

import epicode.it.healthdesk.auth.appuser.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}") // Valore segreto utilizzato per firmare i token.
    private String secret;

    @Value("${jwt.expiration}") // Durata del token in millisecondi.
    private long jwtExpirationInMs;

    /**
     * Estrae il nome utente (subject) dal token JWT.
     * @param token Il token JWT.
     * @return Il nome utente contenuto nel token.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Estrae la data di scadenza dal token JWT.
     * @param token Il token JWT.
     * @return La data di scadenza del token.
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Estrae un claim specifico dal token JWT utilizzando una funzione resolver.
     * @param token Il token JWT.
     * @param claimsResolver Funzione per estrarre un claim specifico.
     * @param <T> Tipo del claim restituito.
     * @return Il valore del claim estratto.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Estrae tutti i claims dal token JWT.
     * @param token Il token JWT.
     * @return I claims contenuti nel token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret) // Utilizza la chiave segreta per decodificare il token.
                .parseClaimsJws(token)
                .getBody(); // Restituisce il corpo dei claims.
    }

    /**
     * Verifica se il token JWT è scaduto.
     * @param token Il token JWT.
     * @return True se il token è scaduto, altrimenti False.
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Genera un token JWT per un utente, includendo i ruoli come claim.
     * @param userDetails I dettagli dell'utente per cui generare il token.
     * @return Una stringa rappresentante il token JWT generato.
     */
    public String generateToken(UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority) // Converte i ruoli in stringhe.
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Imposta l'username come subject.
                .claim("roles", roles) // Aggiunge i ruoli come claim.
                .setIssuedAt(new Date(System.currentTimeMillis())) // Imposta la data di creazione.
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs)) // Imposta la data di scadenza.
                .signWith(SignatureAlgorithm.HS256, secret) // Firma il token utilizzando HS256 e la chiave segreta.
                .compact(); // Compatta il token in una stringa.
    }

    public String generateAccessToken(AppUser user) {
        // Costruiamo il token includendo l'email come soggetto e altri claim utili
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("roles", user.getRoles()) // se AppUser ha un campo 'roles'
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Estrae i ruoli dal token JWT.
     * @param token Il token JWT.
     * @return Una lista di ruoli estratti dal token.
     */
    public List<String> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("roles", List.class);
    }

    /**
     * Valida un token JWT confrontando il nome utente e verificando che non sia scaduto.
     * @param token Il token JWT.
     * @param userDetails I dettagli dell'utente.
     * @return True se il token è valido, altrimenti False.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.jwt.SecretKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.ZoneOffset.UTC;

@Component
public class JwtService {

    private static final String SUBJECT = "meetup";

    private static final String ISSUER = "in.sdqali.jwt";

    public static final String LOGIN = "login";

    @Autowired
    private SecretKeyProvider secretKeyProvider;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserDao userDao;

    public User verify(String token) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return userDao.findByLogin(claims.getBody().get(LOGIN).toString());
    }

    public String verifyLogin(String token) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return claims.getBody().get(LOGIN).toString();
    }

    public String tokenFor(User user) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        Date expiration = Date.from(LocalDateTime.now(UTC).plusMinutes(20).toInstant(UTC));
        return Jwts.builder()
                .setSubject(SUBJECT)
                .setExpiration(expiration)
                .setIssuer(ISSUER)
                .claim(LOGIN, user.getLogin())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String tokenForRecoveryPassword(User user) throws IOException, URISyntaxException {
        byte[] secretKey = secretKeyProvider.getKey();
        Date expiration = Date.from(LocalDateTime.now(UTC).plusMinutes(5).toInstant(UTC));
        return Jwts.builder()
                .setSubject(SUBJECT)
                .setExpiration(expiration)
                .setIssuer(ISSUER)
                .claim(LOGIN, user.getLogin())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}

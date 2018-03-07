/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import java.security.Key;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.petstar.configurations.Configuration;
import org.petstar.dao.AutenticacionDAO;


/**
 *
 * @author Tech-Pro
 */
public class ControllerAutenticacion {

    public String createJWT(int id_usuario, String username, String rol) throws UnsupportedEncodingException, Exception {
        Key key = MacProvider.generateKey();
        byte[] keyBytes = key.getEncoded();
        String key_base64 = TextCodec.BASE64.encode(keyBytes);
        AutenticacionDAO dao = new AutenticacionDAO();

        String jwt = Jwts.builder()
                .setSubject(String.valueOf(id_usuario))
                .setExpiration(new Date(System.currentTimeMillis() + 600000000))
                .claim("usuario_acceso", username)
                .claim("perfil", rol)
                .signWith(
                        SignatureAlgorithm.HS256,
                        key_base64
                )
                .compact();
        dao.updateToken_Key(id_usuario, key_base64);
        return jwt;
    }

    /**
     *
     * @param request
     * @return
     */
    public boolean isValidToken(HttpServletRequest request) {
        boolean b;
        String token = request.getHeader(Configuration.HEADER_STRING);
        int id_usuario = Integer.parseInt(request.getParameter("id_usuario"));
        if (token == null) {
            b = false;
            //throw new JwtException(token);
        } else {
            try {
                AutenticacionDAO dao = new AutenticacionDAO();
                String token_key = dao.getToken_Key(id_usuario);
                Claims claims = Jwts.parser().setSigningKey(token_key).parseClaimsJws(token).getBody();
                b = true;
            } catch (SignatureException | ClaimJwtException | MalformedJwtException | UnsupportedJwtException e) {
                b = false;
            } catch (Exception ex) {
                 b = false;
            }
        }
        return b;
    }

}

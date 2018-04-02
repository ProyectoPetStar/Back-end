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
import org.petstar.dto.UserDTO;


/**
 *
 * @author Tech-Pro
 */
public class ControllerAutenticacion {

    public String createJWT(UserDTO usuario) throws UnsupportedEncodingException, Exception {
        Key key = MacProvider.generateKey();
        byte[] keyBytes = key.getEncoded();
        String key_base64 = TextCodec.BASE64.encode(keyBytes);
        AutenticacionDAO dao = new AutenticacionDAO();

        String jwt = Jwts.builder()
                .setSubject(String.valueOf(usuario.getId_usuario()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000000))
                .claim("usuario_acceso", usuario.getUsuario_acceso())
                .claim("perfil", usuario.getPerfil())
                .claim("id_perfil", usuario.getId_perfil())
                .claim("id_grupo",usuario.getId_grupo())
                .claim("id_linea", usuario.getId_linea())
                .signWith(
                        SignatureAlgorithm.HS256,
                        key_base64
                )
                .compact();
        dao.updateToken_Key(usuario.getId_usuario(), key_base64);
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
    
    public String id_usuario_valido(HttpServletRequest request){
        String token = request.getHeader(Configuration.HEADER_STRING);
        String id_usuario = request.getParameter("id_usuario");
        
        if (token == null) {
            id_usuario = "-1";
        } else {
            try {
                AutenticacionDAO dao = new AutenticacionDAO();
                String token_key = dao.getToken_Key(Integer.parseInt(id_usuario));
                Claims claims = Jwts.parser().setSigningKey(token_key).parseClaimsJws(token).getBody();
                
                 id_usuario = claims.getSubject().equals(id_usuario)? id_usuario: "-1";
                 //claims.
                
            } catch (SignatureException | ClaimJwtException | MalformedJwtException | UnsupportedJwtException e) {
               id_usuario = "-1";
            } catch (Exception ex) {
               id_usuario = "-1";
            }
        }
        return id_usuario;
    }
    
    

}

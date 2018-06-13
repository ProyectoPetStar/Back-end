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
 * Controllador de Autenticación
 * @author Tech-Pro
 */
public class ControllerAutenticacion {

    /**
     * Creación de token
     * Metodo que se encarga de la construcción del token y almacenamiento del key
     * @param usuario
     * @return
     * @throws UnsupportedEncodingException
     * @throws Exception 
     */
    public String createJWT(UserDTO usuario) throws UnsupportedEncodingException, Exception {
        Key key = MacProvider.generateKey();
        byte[] keyBytes = key.getEncoded();
        String key_base64 = TextCodec.BASE64.encode(keyBytes);
        AutenticacionDAO dao = new AutenticacionDAO();

        String jwt = Jwts.builder()
                .setSubject(String.valueOf(usuario.getId_acceso()))
                .setExpiration(new Date(System.currentTimeMillis() + 8640000000L))
                .claim("id_acceso", usuario.getId_acceso())
                .claim("nombre", usuario.getNombre())
                .claim("id_grupo",usuario.getId_grupo())
                .claim("id_linea", usuario.getId_linea())
                .claim("perfiles", usuario.getPerfiles())
                .claim("roles_oee", usuario.getRoles_oee())
                .claim("roles_etad", usuario.getRoles_etad())
                .claim("id_grupo_linea", usuario.getId_grupo_linea())
                .claim("roles_ishikawa", usuario.getRoles_ishikawa())
                .claim("roles_generales", usuario.getRoles_generales())
                .signWith(
                        SignatureAlgorithm.HS256,
                        key_base64
                )
                .compact();
        dao.updateToken_Key(usuario.getId_acceso(), key_base64);
        return jwt;
    }

    /**
     * Validar token
     * Metodo que se encarga de la validación del token, que no haya sido alterado
     * @param request
     * @return
     */
    public UserDTO isValidToken(HttpServletRequest request) {
        UserDTO usuario = new UserDTO();
        String token = request.getHeader(Configuration.HEADER_STRING);
        int idAcceso = Integer.parseInt(request.getParameter("id_usuario"));
        if (token != null) {
            try {
                AutenticacionDAO dao = new AutenticacionDAO();
                String token_key = dao.getToken_Key(idAcceso);
                Claims claims = Jwts.parser().setSigningKey(token_key).parseClaimsJws(token).getBody();
                
                usuario.setId_acceso(claims.get("id_acceso", Integer.class));
                usuario.setNombre(claims.get("nombre", String.class));
                usuario.setId_grupo(claims.get("id_grupo", Integer.class));
                usuario.setId_linea(claims.get("id_linea", Integer.class));
                usuario.setPerfiles(claims.get("perfiles", String.class));
                usuario.setRoles_oee(claims.get("roles_oee", String.class));
                usuario.setRoles_etad(claims.get("roles_etad", String.class));
                usuario.setId_grupo_linea(claims.get("id_grupo_linea", Integer.class));
                usuario.setRoles_ishikawa(claims.get("roles_ishikawa", String.class));
                usuario.setRoles_generales(claims.get("roles_generales", String.class));
            } catch (SignatureException | ClaimJwtException | MalformedJwtException | UnsupportedJwtException e) {
                usuario = null;
            } catch (Exception ex) {
                usuario = null;
            }
        } else {
            usuario = null;
        }
        return usuario;
    }
}

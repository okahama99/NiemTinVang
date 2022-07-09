//package com.ntv.ntvcons_backend.configs;
//
//import com.ntv.ntvcons_backend.entities.Role;
//import com.ntv.ntvcons_backend.entities.User;
//import com.ntv.ntvcons_backend.repositories.RoleRepository;
//import com.ntv.ntvcons_backend.repositories.UserRepository;
//import com.ntv.ntvcons_backend.security.UserDetails;
//import com.ntv.ntvcons_backend.services.user.UserService;
//import io.jsonwebtoken.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//@Slf4j
//public class JwtUtil {
//
//    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
//    private final String JWT_SECRET = "NTV2022";
//
//    //Thời gian có hiệu lực của chuỗi jwt
//    private final long JWT_EXPIRATION = 10800000L;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    RoleRepository roleRepository;
//
//    // Tạo ra jwt từ thông tin user
//    public String generateToken(User user) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
//        Long userID = user.getUserId();
//        Role role = roleRepository.getById(user.getRoleId());
//        // Tạo chuỗi json web token từ id của user.
//        return Jwts.builder()
//                .setSubject(Long.toString(userID))
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
//                .claim("role", role.getRoleName())
//                .claim("id", user.getUserId())
//                .compact();
//    }
//
//    public String generateNewToken(Authentication authentication) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
//
//        UserDetails userPrincipal=(UserDetails) authentication.getPrincipal();
//        User user = userRepository.getById(userPrincipal.getUserID());
//        // Tạo chuỗi json web token từ id của user.
//        return Jwts.builder()
//                .setSubject(userPrincipal.getUsername())
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
//                .claim("role", userPrincipal.getAuthorities())
//                .claim("id", userPrincipal.getUserID())
//                .claim("userName", user.getUsername())
//                .claim("email", user.getEmail())
//                .claim("phone", user.getPhone())
//                .compact();
//    }
//    public Long getUserIdFromJWT(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(JWT_SECRET)
//                .parseClaimsJws(token)
//                .getBody();
//
//        return Long.parseLong(claims.getSubject());
//    }
//    public String getUserNameFromJWT(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(JWT_SECRET)
//                .parseClaimsJws(token)
//                .getBody();
//
//        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public boolean validateToken(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
//            return true;
//        } catch (MalformedJwtException ex) {
//            log.error("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            log.error("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            log.error("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            log.error("JWT claims string is empty.");
//        }
//        return false;
//    }
//
//}

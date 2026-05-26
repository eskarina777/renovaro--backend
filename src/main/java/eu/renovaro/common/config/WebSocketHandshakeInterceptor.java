package eu.renovaro.common.config;

import eu.renovaro.common.presence.OnlineUsers;
import eu.renovaro.security.config.JWTTokenProvider;
import eu.renovaro.user.domain.entity.User;
import eu.renovaro.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.security.Principal;
import java.util.Map;
import org.springframework.context.annotation.Lazy;
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JWTTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public WebSocketHandshakeInterceptor(JWTTokenProvider jwtTokenProvider, OnlineUsers onlineUsers,
                                         UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;

    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {
// TODO change token in header as set in frontend
//        if (request instanceof ServletServerHttpRequest servletRequest) {
//            HttpServletRequest httpRequest = servletRequest.getServletRequest();
//            String authHeader = httpRequest.getHeader("Authorization");
//
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                String token = authHeader.substring(7);
//
//                if (jwtTokenProvider.validateToken(token)) {
//                    String email = jwtTokenProvider.getUsername(token);
//                    attributes.put("user", (Principal) () -> email);
//                    System.out.println("Authorization: " + authHeader);
//                    System.out.println("TOKEN valid: " + jwtTokenProvider.validateToken(token));
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            String token = httpRequest.getParameter("token");

            if (token != null && jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getUsername(token);
                User user = userRepository.findByEmail(email)
                        .orElse(null);
                if (user == null) {
                    return false;
                }
                Long userId = user.getUserId();
                attributes.put("user", (Principal) () -> String.valueOf(userId));
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
    }
}

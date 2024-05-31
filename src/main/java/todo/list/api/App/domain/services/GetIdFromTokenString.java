package todo.list.api.App.domain.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import todo.list.api.App.infra.security.TokenService;

@Service
public class GetIdFromTokenString {

    @Autowired
    private TokenService tokenService;

    public Long getId(HttpServletRequest request) {
        return tokenService.getClaim(request.getHeader("Authorization"));
    }
}

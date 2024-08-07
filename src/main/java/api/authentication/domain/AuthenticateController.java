package api.authentication.domain;

import api.authentication.dto.AuthRequest;
import api.authentication.dto.AuthResponse;
import api.authentication.dto.DeveloperCredentialsRequest;
import api.infrastructure.exception.DeveloperAlreadyExistsException;
import api.infrastructure.exception.EntityNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
public record AuthenticateController(AuthenticateService authenticateService) {

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) throws EntityNotFoundException {
        return authenticateService.login(request);
    }

    @PostMapping("/register")
    public void register(@RequestBody DeveloperCredentialsRequest developerCredentialsRequest) throws EntityNotFoundException, DeveloperAlreadyExistsException {
        authenticateService.register(developerCredentialsRequest);
    }

}

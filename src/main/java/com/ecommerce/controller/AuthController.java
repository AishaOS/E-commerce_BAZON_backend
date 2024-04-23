package com.ecommerce.controller;

import com.ecommerce.dto.AuthenticationRequest;
import com.ecommerce.dto.ChangePasswordDto;
import com.ecommerce.dto.SignupRequest;
import com.ecommerce.dto.UserDto;
import com.ecommerce.entity.User;
import com.ecommerce.repo.UserRepository;
import com.ecommerce.service.auth.AuthService;
import com.ecommerce.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final AuthService authService;

    @PostMapping({"/sign-up"})
    public ResponseEntity<?> signupUser(@RequestBody(required = true) SignupRequest signupRequest)
            throws Exception {

        if (authService.hasUserWithEmail(signupRequest.getEmail()))
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);

        UserDto createdUser = authService.createUser(signupRequest);
        if (createdUser == null)
            return new ResponseEntity<>("User not created, come again later", HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException, JSONException, ServletException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password.");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is not activated");
            return;
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            response.getWriter().write(new JSONObject()
                    .put("userId", optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole())
                    .toString()
            );
            response.addHeader("Access-Control-Expose-Headers", "Authorization");
            response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");
            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
        }
    }

    @PostMapping("/api/update")
    public ResponseEntity<UserDto> updateProfile(@ModelAttribute UserDto userDto) throws IOException {
        UserDto updatedUser = authService.updateUser(userDto);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/user/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        UserDto userDto = authService.getUserById(userId);
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        try {
            return authService.updatePasswordById(changePasswordDto);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
    }


}

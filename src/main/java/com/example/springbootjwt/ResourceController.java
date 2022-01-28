package com.example.springbootjwt;


import com.example.springbootjwt.jwt.JwtUtil;
import com.example.springbootjwt.models.AuthenticationRequest;
import com.example.springbootjwt.models.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ResourceController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String helloworld(){
        return "Hello World";
    }
    @RequestMapping(value = "/authenticate",method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
       try{
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
       }
       catch (BadCredentialsException e){
           throw new Exception("Incorrect Username or Password",e);
       }
       final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
       final String jwt = jwtUtil.generateToken(userDetails);
       return ResponseEntity.ok(new AuthenticationResponse(jwt));


    }
}

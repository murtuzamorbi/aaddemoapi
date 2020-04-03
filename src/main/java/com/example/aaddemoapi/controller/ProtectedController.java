package com.example.aaddemoapi.controller;

import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ProtectedController {

    @GetMapping("/group1")
    @ResponseBody
   // @PreAuthorize("hasRole('XpDevOps')")
    public String group1(Principal principal) {

        //principal has private object principal and it holds the jwt context
        System.out.println((PreAuthenticatedAuthenticationToken) principal);

        return "{ \"protected\": 1}";
    }


}

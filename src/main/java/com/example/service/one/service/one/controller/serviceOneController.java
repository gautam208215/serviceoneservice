package com.example.service.one.service.one.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/first")
public class serviceOneController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/one")
    public String serviceone(HttpServletRequest httpServletRequest){
        return "service one response";
    }

    @GetMapping("/callTwo/headers")
    public String callTwo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String accessToken = extractAccessToken(authorizationHeader);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<String> exchange = restTemplate.exchange("http://service-two/second/intercall", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        // Forward the access token to Resource Server B
        return exchange.getBody();
    }

    @GetMapping("/callTwo")
    public String callTwo(){


        String exchange = restTemplate.getForObject("http://service-two/second/intercall", String.class);

        // Forward the access token to Resource Server B
        return exchange;
    }

    private String extractAccessToken(String authorizationHeader) {
        return authorizationHeader.replace("Bearer ", "");
    }


    @GetMapping("/print-all-headers")
    public String getAllheaders(@RequestHeader Map<String,String> headers){
        headers.forEach((key,value) ->{
            System.out.println("Header Name: "+key+" Header Value: "+value);
        });
        return "successfully printed headers";
    }

}

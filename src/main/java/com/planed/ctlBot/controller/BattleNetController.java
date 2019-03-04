package com.planed.ctlBot.controller;

import com.planed.ctlBot.services.BattleNetService;
import com.planed.ctlBot.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class BattleNetController {
    private static final Logger logger = LoggerFactory.getLogger(BattleNetController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private BattleNetService battleNetService;

    @RequestMapping("/")
    public void user(@RequestParam(value = "authCode") final String authCode,
                     Principal principal) {
        final OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
        final Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();

        String battleNetId = userAuthentication.getPrincipal().toString();
        String tokenValue = ((OAuth2AuthenticationDetails) oAuth2Authentication.getDetails()).getTokenValue();

        String discordId = userService.loginUserByAuthCode(authCode, battleNetId, tokenValue);

        battleNetService.retrieveAndSafeUserLadderInformation(battleNetId, tokenValue, discordId);
    }
}
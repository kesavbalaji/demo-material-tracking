package com.example.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthController {

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/welcome",method = RequestMethod.GET)
    public String welcome(){
        return "welcome";
    }

    @RequestMapping(value = "/uploads",method = RequestMethod.GET)
    public String upload(){
        return "upload";
    }

    @GetMapping("/entities")
    public String getEntitiesPage() {
        return "tableEntity";
    }

    @GetMapping("/segmentHistory")
    public String segmentHistoryForm() {
        return "segmentHistory";
    }

    @GetMapping("/segmentConfirmation")
    public String segmentConfirmation() {
        return "segmentConfirmation";
    }

    @GetMapping("/dispatchConfirmation")
    public String dispatchConfirmation() {
        return "dispatchConfirmation";
    }

    @GetMapping("/receiveConfirmation")
    public String receiveConfirmation() {
        return "receiveConfirmation";
    }

    @GetMapping("/erectionConfirmation")
    public String erectionConfirmation() {
        return "segmentErectionConfirmation";
    }

    @GetMapping("/homePage")
    public String homePage() {
        return "homePage";
    }
}

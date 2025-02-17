package com.luckkids.notification.controller;

import com.luckkids.api.ApiResponse;
import com.luckkids.notification.controller.request.ConfirmEmailCheckRequest;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.notification.service.ConfirmEmailService;
import com.luckkids.notification.service.response.ConfirmEmailCheckResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/confirmEmail")
public class ConfirmEmailController {

    private final ConfirmEmailService confirmEmailService;

    @GetMapping
    public ModelAndView confirmEmail(String key){
        ModelAndView mav = new ModelAndView();
        try {
            confirmEmailService.confirmEmail(key);
            mav.setViewName("success");
        }
        catch (LuckKidsException e){
            mav.setViewName("fail");
            mav.addObject("message", e.getMessage());
        }catch (Exception e){
            mav.setViewName("fail");
        }
        return mav;
    }

    @PostMapping("/check")
    @ResponseBody
    public ApiResponse<ConfirmEmailCheckResponse> checkEmail(@Valid @RequestBody ConfirmEmailCheckRequest confirmEmailCheckRequest){
        return ApiResponse.ok(confirmEmailService.checkEmail(confirmEmailCheckRequest.toServiceRequest()));
    }
}

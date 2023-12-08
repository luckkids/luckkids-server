package com.luckkids.api.controller.confirmEmail;

import com.luckkids.api.ApiResponse;
import com.luckkids.api.controller.confirmEmail.request.ConfirmEmailCheckRequest;
import com.luckkids.api.service.confirmEmail.ConfirmEmailService;
import com.luckkids.api.service.confirmEmail.response.ConfirmEmailCheckResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/confirmEmail")
public class ConfirmEmailController {

    private final ConfirmEmailService confirmEmailService;

    @GetMapping("/{email}/{authKey}")
    public String confirmEmail(@PathVariable(name = "email") String email,@PathVariable(name = "authKey") String authKey){
        try {
            confirmEmailService.confirmEmail(email, authKey);
            return "success";
        }
        catch (Exception e){
            return "fail";
        }
    }

    @PostMapping("/check")
    @ResponseBody
    public ApiResponse<ConfirmEmailCheckResponse> checkEmail(@Valid @RequestBody ConfirmEmailCheckRequest confirmEmailCheckRequest){
        return ApiResponse.ok(confirmEmailService.checkEmail(confirmEmailCheckRequest.toServiceRequest()));
    }
}

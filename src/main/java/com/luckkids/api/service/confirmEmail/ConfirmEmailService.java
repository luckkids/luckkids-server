package com.luckkids.api.service.confirmEmail;

import com.luckkids.api.exception.ErrorCode;
import com.luckkids.api.exception.LuckKidsException;
import com.luckkids.api.service.confirmEmail.request.ConfirmEmailCheckServiceRequest;
import com.luckkids.api.service.confirmEmail.request.CreateConfrimEmailServiceRequest;
import com.luckkids.api.service.confirmEmail.response.ConfirmEmailCheckResponse;
import com.luckkids.domain.confirmEmail.ConfirmEmail;
import com.luckkids.domain.confirmEmail.ConfirmEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmEmailService {

    private final ConfirmEmailRepository confirmEmailRepository;
    private final ConfirmEmailReadService confirmEmailReadService;

    public void confirmEmail(String email, String authKey){
        confirmEmailReadService.findByEmailAndAuthKey(email, authKey).confirm();
    }

    public ConfirmEmailCheckResponse checkEmail(ConfirmEmailCheckServiceRequest confirmEmailCheckServiceRequest){
        ConfirmEmail confirmEmail = confirmEmailReadService.findByEmailAndAuthKey(confirmEmailCheckServiceRequest.getEmail(), confirmEmailCheckServiceRequest.getAuthKey());
        confirmEmail.checkEmail();
        return ConfirmEmailCheckResponse.of(confirmEmail.getEmail());
    }

    public ConfirmEmail createConfirmEmail(CreateConfrimEmailServiceRequest createConfrimEmailServiceRequest){
        return confirmEmailRepository.save(createConfrimEmailServiceRequest.toEntity());
    }

    public void removeConfirmEmail(int id){
        ConfirmEmail confirmEmail = confirmEmailRepository.findById(id)
                .orElseThrow(() -> new LuckKidsException(ErrorCode.EMAIL_UNKNOWN));
        confirmEmailRepository.delete(confirmEmail);
    }
}

package com.luckkids.api.service.confirmEmail;

import com.luckkids.api.component.Aes256Component;
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
    private final Aes256Component aes256Component;

    public void confirmEmail(String encryptKey){

        String decryptKey = aes256Component.decrypt(encryptKey);

        String[] value = decryptKey.split("/");

        String email = value[0];
        String authKey = value[1];

        confirmEmailReadService.findByEmailAndAuthKey(email, authKey).confirm();
    }

    public ConfirmEmailCheckResponse checkEmail(ConfirmEmailCheckServiceRequest confirmEmailCheckServiceRequest){
        ConfirmEmail confirmEmail = confirmEmailReadService.findByEmailAndAuthKey(confirmEmailCheckServiceRequest.getEmail(), confirmEmailCheckServiceRequest.getAuthKey());
        confirmEmail.checkEmail();
        return ConfirmEmailCheckResponse.of(confirmEmail.getEmail());
    }

    public void createConfirmEmail(CreateConfrimEmailServiceRequest createConfrimEmailServiceRequest){
        confirmEmailRepository.save(createConfrimEmailServiceRequest.toEntity());
    }

    public void removeConfirmEmail(int id){
        ConfirmEmail confirmEmail = confirmEmailRepository.findById(id)
                .orElseThrow(() -> new LuckKidsException(ErrorCode.EMAIL_UNKNOWN));
        confirmEmailRepository.delete(confirmEmail);
    }
}

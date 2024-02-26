package com.luckkids.api.service.notice;

import com.luckkids.api.service.firebase.FirebaseService;
import com.luckkids.api.service.notice.request.NoticeSaveServiceRequest;
import com.luckkids.api.service.notice.response.NoticeSaveResponse;
import com.luckkids.api.service.push.PushReadService;
import com.luckkids.domain.alertSetting.AlertType;
import com.luckkids.domain.notice.Notice;
import com.luckkids.domain.notice.NoticeRepository;
import com.luckkids.domain.push.Push;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final FirebaseService firebaseService;
    private final PushReadService pushReadService;

    public NoticeSaveResponse saveNotice(NoticeSaveServiceRequest noticeSaveRequest){
        Notice notice = noticeSaveRequest.toEntity();
        Notice savedNotice = noticeRepository.save(notice);

        for(Push push : pushReadService.findAll(AlertType.NOTICE)){
            firebaseService.sendPushNotification(noticeSaveRequest.toSendPushServiceRequest(push));
        }

        return NoticeSaveResponse.of(savedNotice);
    }
}

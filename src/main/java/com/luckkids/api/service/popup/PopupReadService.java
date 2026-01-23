package com.luckkids.api.service.popup;

import com.luckkids.api.service.popup.response.PopupResponse;
import com.luckkids.domain.popup.PopupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PopupReadService {

    private final PopupRepository popupRepository;

    public PopupResponse getLatestPopup() {
        return popupRepository.findFirstByOrderByCreatedDateDesc()
                .map(PopupResponse::createResponse)
                .orElse(null);
    }
}

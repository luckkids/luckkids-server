package com.luckkids.api.service.missionOutcome;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckkids.api.service.missionOutcome.response.MissionOutcomeCountResponse;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeForCalendarResponse;
import com.luckkids.api.service.missionOutcome.response.MissionOutcomeResponse;
import com.luckkids.api.service.security.SecurityService;
import com.luckkids.domain.missionOutcome.MissionOutcome;
import com.luckkids.domain.missionOutcome.MissionOutcomeQueryRepository;
import com.luckkids.domain.missionOutcome.MissionOutcomeRepository;
import com.luckkids.domain.missionOutcome.MissionStatus;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalendarDetailDto;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeCalendarDto;
import com.luckkids.domain.missionOutcome.projection.MissionOutcomeDetailDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionOutcomeReadService {

	private final MissionOutcomeRepository missionOutcomeRepository;
	private final MissionOutcomeQueryRepository missionOutcomeQueryRepository;

	private final SecurityService securityService;

	public MissionOutcome findByOne(Long id) {
		return missionOutcomeRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 미션결과는 없습니다. id = " + id));
	}

	public List<MissionOutcomeResponse> getMissionOutcomeDetailListForStatus(Optional<MissionStatus> missionStatus,
		LocalDate missionDate) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		return missionOutcomeQueryRepository.findMissionDetailsByStatus(missionStatus, userId, missionDate)
			.stream()
			.map(MissionOutcomeDetailDto::toMissionOutcomeResponse)
			.toList();
	}

	public MissionOutcomeForCalendarResponse getMissionOutcomeForCalendar(
		LocalDate referenceDate,
		Function<LocalDate, LocalDate> startCalculator,
		Function<LocalDate, LocalDate> endCalculator) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();

		LocalDate startDate = startCalculator.apply(referenceDate);
		LocalDate endDate = endCalculator.apply(referenceDate);

		List<MissionOutcomeCalendarDto> result = missionOutcomeQueryRepository.findMissionOutcomeByDateRangeAndStatus(
			userId, startDate, endDate);
		return MissionOutcomeForCalendarResponse.of(startDate, endDate, result);
	}

	public List<MissionOutcomeCalendarDetailDto> getMissionOutcomeForCalendarDetail(LocalDate missionDate) {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		return missionOutcomeQueryRepository.findSuccessfulMissionsByDate(userId, missionDate);
	}

	public MissionOutcomeCountResponse getMissionOutcomesCount() {
		int userId = securityService.getCurrentLoginUserInfo().getUserId();
		return MissionOutcomeCountResponse.of(missionOutcomeQueryRepository.findMissionOutcomesCount(userId));
	}

	public MissionStatus getMissionOutcomeStatus(int missionId, LocalDate missionDate) {
		return missionOutcomeRepository.findByMissionIdAndMissionDate(missionId, missionDate).getMissionStatus();
	}
}
package com.luckkids.docs.alertHistory;

import static com.luckkids.domain.alertHistory.AlertHistoryStatus.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import com.luckkids.api.controller.alertHistory.AlertHistoryController;
import com.luckkids.api.controller.alertHistory.request.AlertHistoryDeviceIdRequest;
import com.luckkids.api.page.response.PageCustom;
import com.luckkids.api.page.response.PageableCustom;
import com.luckkids.api.service.alertHistory.AlertHistoryReadService;
import com.luckkids.api.service.alertHistory.AlertHistoryService;
import com.luckkids.api.service.alertHistory.request.AlertHistoryDeviceIdServiceRequest;
import com.luckkids.api.service.alertHistory.response.AlertHistoryResponse;
import com.luckkids.api.service.alertHistory.response.AlertHistoryStatusResponse;
import com.luckkids.docs.RestDocsSupport;
import com.luckkids.domain.alertHistory.AlertHistoryStatus;

public class AlertHistoryControllerDocsTest extends RestDocsSupport {

	private final AlertHistoryService alertHistoryService = mock(AlertHistoryService.class);
	private final AlertHistoryReadService alertHistoryReadService = mock(AlertHistoryReadService.class);

	@Override
	protected Object initController() {
		return new AlertHistoryController(alertHistoryService, alertHistoryReadService);
	}

	@DisplayName("deviceId를 받고 알림 내역을 가져오는 API")
	@Test
	@WithMockUser("USER")
	void getAlertHistory() throws Exception {
		// given
		AlertHistoryDeviceIdRequest request = AlertHistoryDeviceIdRequest.builder()
			.deviceId("dk0WA3lCG0xNqIoFx****")
			.build();

		AlertHistoryResponse response1 = createAlertHistoryResponse(
			1L, "알림 내역1", UNCHECKED,
			LocalDateTime.of(2024, 2, 29, 16, 0, 0)
		);
		AlertHistoryResponse response2 = createAlertHistoryResponse(
			2L, "알림 내역2", CHECKED,
			LocalDateTime.of(2024, 3, 1, 12, 0, 0)
		);

		PageCustom<AlertHistoryResponse> pageResponse = createPageResponse(
			List.of(response1, response2), 1, 1, 2
		);

		given(alertHistoryReadService.getAlertHistory(any(AlertHistoryDeviceIdServiceRequest.class)))
			.willReturn(pageResponse);

		// when // then
		mockMvc.perform(
				get("/api/v1/alertHistories")
					.param("deviceId", request.getDeviceId())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("alertHistory-get",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("deviceId")
						.description("디바이스 ID"),
					parameterWithName("page")
						.description("페이지. 기본값: 1")
						.optional(),
					parameterWithName("size")
						.description("페이지 사이즈. 기본값: 12")
						.optional()
				),
				responseFields(
					fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
						.description("코드"),
					fieldWithPath("httpStatus").type(JsonFieldType.STRING)
						.description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("메세지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터"),
					fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
						.description("데이터 리스트"),
					fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER)
						.description("알림 내역 ID"),
					fieldWithPath("data.content[].alertDescription").type(JsonFieldType.STRING)
						.description("알림 내역 내용"),
					fieldWithPath("data.content[].alertHistoryStatus").type(JsonFieldType.STRING)
						.description("알림 내역 읽음 상태"),
					fieldWithPath("data.content[].createdDate").type(JsonFieldType.STRING)
						.description("알림 내역 등록일"),
					fieldWithPath("data.pageInfo").type(JsonFieldType.OBJECT)
						.description("페이지 정보"),
					fieldWithPath("data.pageInfo.currentPage").type(JsonFieldType.NUMBER)
						.description("현재 페이지"),
					fieldWithPath("data.pageInfo.totalPage").type(JsonFieldType.NUMBER)
						.description("총 페이지"),
					fieldWithPath("data.pageInfo.totalElement").type(JsonFieldType.NUMBER)
						.description("총 개수")
				)
			));
	}

	@DisplayName("알림 내역 ID를 받아 알림 내역 상태를 업데이트하는 API")
	@Test
	@WithMockUser(roles = "USER")
	void updateAlertHistoryStatus() throws Exception {
		// given
		Long id = 1L;

		AlertHistoryStatusResponse response = AlertHistoryStatusResponse.of(CHECKED);

		given(alertHistoryService.updateAlertHistoryStatus(id))
			.willReturn(response);

		// when // then
		mockMvc.perform(
				patch("/api/v1/alertHistories/{id}", id)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("alertHistory-update",
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("id")
						.description("알림 내역 ID")
				),
				responseFields(
					fieldWithPath("statusCode").type(JsonFieldType.NUMBER)
						.description("코드"),
					fieldWithPath("httpStatus").type(JsonFieldType.STRING)
						.description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("메세지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT)
						.description("응답 데이터"),
					fieldWithPath("data.alertHistoryStatus").type(JsonFieldType.STRING)
						.description("알림 내역 읽음 상태")
				)
			));
	}

	private AlertHistoryResponse createAlertHistoryResponse(Long id, String description, AlertHistoryStatus status,
		LocalDateTime createdDate) {
		return AlertHistoryResponse.builder()
			.id(id)
			.alertDescription(description)
			.alertHistoryStatus(status)
			.createdDate(createdDate)
			.build();
	}

	private PageCustom<AlertHistoryResponse> createPageResponse(List<AlertHistoryResponse> content, int currentPage,
		int totalPage, long totalElement) {
		return PageCustom.<AlertHistoryResponse>builder()
			.content(content)
			.pageInfo(PageableCustom.builder()
				.currentPage(currentPage)
				.totalPage(totalPage)
				.totalElement(totalElement)
				.build())
			.build();
	}
}

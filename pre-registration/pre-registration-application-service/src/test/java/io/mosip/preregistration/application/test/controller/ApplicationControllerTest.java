package io.mosip.preregistration.application.test.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.preregistration.application.controller.ApplicationController;
import io.mosip.preregistration.application.dto.ApplicationDetailResponseDTO;
import io.mosip.preregistration.application.dto.ApplicationsListDTO;
import io.mosip.preregistration.application.dto.UIAuditRequest;
import io.mosip.preregistration.application.service.ApplicationServiceIntf;
import io.mosip.preregistration.core.common.dto.MainResponseDTO;
import io.mosip.preregistration.core.common.entity.ApplicationEntity;
import io.mosip.preregistration.core.config.LoggerConfiguration;
import io.mosip.preregistration.core.util.RequestValidator;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ApplicationController.class)
@Import(ApplicationController.class)
@WithMockUser(username = "individual", authorities = { "INDIVIDUAL", "REGISTRATION_OFFICER" })
public class ApplicationControllerTest {

	@MockBean
	private RequestValidator requestValidator;

	@MockBean
	ApplicationServiceIntf applicationService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockmvc;

	@Before
	public void setup() {
		mockmvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	UIAuditRequest auditRequest = new UIAuditRequest();
	private Logger log = LoggerConfiguration.logConfig(ApplicationController.class);

//	@Test
//	public void logAugitTest() throws Exception {
//		MainRequestDTO<UIAuditRequest> auditRequest = new MainRequestDTO<UIAuditRequest>();
//		HttpHeaders header = new HttpHeaders();
//		header.add("User-Agent", "test");
//		MainResponseDTO<String> response = new MainResponseDTO<String>();
//		Mockito.when(applicationService.saveUIEventAudit(auditRequest.getRequest())).thenReturn(response);
//
//		RequestBuilder request = MockMvcRequestBuilders.post("/logAudit").content()
//				.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).headers(header);
//		mockmvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
//	}

	@Test
	public void getApplicationTest() throws Exception {
		MainResponseDTO<ApplicationEntity> response = new MainResponseDTO<ApplicationEntity>();
		String applicationId = "123456";
		Mockito.when(applicationService.getApplicationInfo(applicationId)).thenReturn(response);

		RequestBuilder request = MockMvcRequestBuilders.get("/applications/{applicationId}", applicationId)
				.param("applicationId", applicationId).accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8);
		mockmvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void getApplicationStatusTest() throws Exception {
		MainResponseDTO<String> response = new MainResponseDTO<String>();
		String applicationId = "123456";
		Mockito.when(applicationService.getApplicationStatus(applicationId)).thenReturn(response);

		RequestBuilder request = MockMvcRequestBuilders.get("/applications/status/{applicationId}", applicationId)
				.param("applicationId", applicationId).accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8);
		mockmvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void getBookingsForRegCenterTest() throws Exception {
		MainResponseDTO<List<ApplicationDetailResponseDTO>> response = new MainResponseDTO<List<ApplicationDetailResponseDTO>>();
		String regCenterId = "123456";
		String appointmentDate = DateTime.now().toString();
		Mockito.when(applicationService.getBookingsForRegCenter(regCenterId, appointmentDate)).thenReturn(response);

		RequestBuilder request = MockMvcRequestBuilders.get("/applications/bookings/{regCenterId}", regCenterId)
				.param("regCenterId", regCenterId).param("appointmentDate", appointmentDate)
				.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8);
		mockmvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void getAllApplicationsTest() throws Exception {
		MainResponseDTO<ApplicationsListDTO> response = new MainResponseDTO<ApplicationsListDTO>();
		Mockito.when(applicationService.getAllApplicationsForUser()).thenReturn(response);

		RequestBuilder request = MockMvcRequestBuilders.get("/applications").accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8);
		mockmvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void getAllApplicationsTypeTest() throws Exception {
		MainResponseDTO<ApplicationsListDTO> response = new MainResponseDTO<ApplicationsListDTO>();
		String type = "Booked";
		Mockito.when(applicationService.getAllApplicationsForUserForBookingType(type)).thenReturn(response);

		RequestBuilder request = MockMvcRequestBuilders.get("/applications").param("type", type)
				.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8);
		mockmvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
	}

}

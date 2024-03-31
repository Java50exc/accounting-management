package telran.probes;

import static telran.probes.TestDb.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.probes.api.AccountingValidationErrorMessages.*;
import static telran.probes.api.ServiceExceptionMessages.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.probes.dto.PasswordUpdateData;
import telran.probes.exceptions.*;
import telran.probes.service.AccountingManagementService;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class AccountingManagementControllerTests {
	@Value("${app.accounts.api.path}")
	String ACCOUNTS_PATH;
	@Autowired
	MockMvc mockMvc;
	@MockBean
	AccountingManagementService accountingService;
	@Autowired
	ObjectMapper mapper;

	// correctFlow
	@Test
	void addAccount_correctFlow_success() throws Exception {
		when(accountingService.addAccount(ACCOUNT_DTO)).thenReturn(ACCOUNT_DTO);
		testValidation(ACCOUNT_DTO, mapper.writeValueAsString(ACCOUNT_DTO), post(PATH + ACCOUNTS_PATH), status().isOk());
	}

	@Test
	void removeAccount_correctFlow_success() throws Exception {
		when(accountingService.removeAccount(EMAIL1)).thenReturn(ACCOUNT_DTO);
		testValidation(null, mapper.writeValueAsString(ACCOUNT_DTO), delete(PATH + ACCOUNTS_PATH + "/" + EMAIL1),
				status().isOk());
	}
	
	@Test
	void updatePassword_correctFlow_success() throws Exception {
		doNothing().when(accountingService).updatePassword(anyString(), anyString());
		testValidation(PASSWORD_UPDATE_DTO, mapper.writeValueAsString(PASSWORD_UPDATE_DTO), put(PATH + ACCOUNTS_PATH), status().isOk());
	}

	// service exception handling
	@Test
	void addAccount_exceptionThrown_errorMessage() throws Exception {
		when(accountingService.addAccount(ACCOUNT_DTO)).thenThrow(new AccountIllegalStateException());
		testValidation(ACCOUNT_DTO, ACCOUNT_ALREADY_EXISTS, post(PATH + ACCOUNTS_PATH), status().isBadRequest());
	}

	@Test
	void removeAccount_exceptionNotFoundThrown_errorMessage() throws Exception {
		when(accountingService.removeAccount(EMAIL1)).thenThrow(new AccountNotFoundException());
		testValidation(null, ACCOUNT_NOT_FOUND, delete(PATH + ACCOUNTS_PATH + "/" + EMAIL1), status().isNotFound());
	}
	
	@Test
	void removeAccount_exceptionAccessDeniedThrown_errorMessage() throws Exception {
		when(accountingService.removeAccount(EMAIL1)).thenThrow(new AccessDeniedException(REMOVE_ANOTHER_ACCOUNT_PASSWORD));
		testValidation(null, REMOVE_ANOTHER_ACCOUNT_PASSWORD, delete(PATH + ACCOUNTS_PATH + "/" + EMAIL1), status().isForbidden());
	}
	
	@Test
	void updatePassword_exceptionNotFoundThrown_errorMessage() throws Exception {
		doThrow(new AccountNotFoundException()).when(accountingService).updatePassword(anyString(), anyString());
		testValidation(PASSWORD_UPDATE_DTO, ACCOUNT_NOT_FOUND, put(PATH + ACCOUNTS_PATH), status().isNotFound());
	}
	
	@Test
	void updatePassword_exceptionAccessDeniedThrown_errorMessage() throws Exception {
		doThrow(new AccessDeniedException(UPDATE_ANOTHER_ACCOUNT_PASSWORD)).when(accountingService).updatePassword(anyString(), anyString());
		testValidation(PASSWORD_UPDATE_DTO, UPDATE_ANOTHER_ACCOUNT_PASSWORD, put(PATH + ACCOUNTS_PATH), status().isForbidden());
	}

	// validation tests
	@Test
	void addAccount_wrongEmailFormat_throwsException() throws Exception {
		testValidation(ACCOUNT_WRONG_EMAIL, WRONG_EMAIL_FORMAT, post(PATH + ACCOUNTS_PATH), status().isBadRequest());
	}

	@Test
	void addAccount_nullEmail_throwsException() throws Exception {
		testValidation(ACCOUNT_NULL_EMAIL, EMPTY_EMAIL_MSG, post(PATH + ACCOUNTS_PATH), status().isBadRequest());
	}

	@Test
	void addAccount_emptyEmail_throwsException() throws Exception {
		testValidation(ACCOUNT_EMPTY_EMAIL, EMPTY_EMAIL_MSG, post(PATH + ACCOUNTS_PATH), status().isBadRequest());
	}

	@Test
	void addAccount_shortPassword_throwsException() throws Exception {
		testValidation(ACCOUNT_SHORT_PASSWORD, WRONG_PASSWORD_LENGTH, post(PATH + ACCOUNTS_PATH),
				status().isBadRequest());
	}

	@Test
	void addAccount_whitespacesPassword_throwsException() throws Exception {
		testValidation(ACCOUNT_WHITESPACES_PASSWORD, PASSWORD_WHITESPACES, post(PATH + ACCOUNTS_PATH),
				status().isBadRequest());
	}

	@Test
	void addAccount_emptyPassword_throwsException() throws Exception {
		testValidation(ACCOUNT_EMPTY_PASSWORD, WRONG_PASSWORD_LENGTH, post(PATH + ACCOUNTS_PATH),
				status().isBadRequest());
	}

	@Test
	void addAccount_nullPassword_throwsException() throws Exception {
		testValidation(ACCOUNT_NULL_PASSWORD, EMPTY_PASSWORD, post(PATH + ACCOUNTS_PATH), status().isBadRequest());
	}

	@Test
	void addAccount_rolesEmpty_throwsException() throws Exception {
		testValidation(ACCOUNT_EMPTY_ROLES, EMPTY_ROLES, post(PATH + ACCOUNTS_PATH), status().isBadRequest());
	}

	@Test
	void addAccount_rolesNull_throwsException() throws Exception {
		testValidation(ACCOUNT_NULL_ROLES, EMPTY_ROLES, post(PATH + ACCOUNTS_PATH), status().isBadRequest());
	}

	@Test
	void removeAccount_wrongEmail_throwsException() throws Exception {
		testValidation(null, WRONG_EMAIL_FORMAT, delete(PATH + ACCOUNTS_PATH + "/" + WRONG_EMAIL),
				status().isBadRequest());
	}

	@Test
	void removeAccount_nullEmail_throwsException() throws Exception {
		testValidation(null, WRONG_EMAIL_FORMAT, delete(PATH + ACCOUNTS_PATH + "/" + WRONG_EMAIL),
				status().isBadRequest());
	}

	@Test
	void addAccount_missingAllFields_throwsException() throws Exception {
		Arrays.sort(MISSING_MESSAGES);
		String request = mapper.writeValueAsString(ACCOUNT_ALL_NULL);
		String response = mockMvc
				.perform(post(PATH + ACCOUNTS_PATH).contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		
		String[] messages = response.split(";");
		Arrays.sort(messages);
		assertArrayEquals(MISSING_MESSAGES, messages);
	}

	private void testValidation(Object request, String expectedResponse, MockHttpServletRequestBuilder method,
			ResultMatcher expectedStatus) throws Exception {
		if (request != null) {
			method = method.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request));
		}
		String response = mockMvc.perform(method).andExpect(expectedStatus).andReturn().getResponse()
				.getContentAsString();
		assertEquals(expectedResponse, response);
	}

}

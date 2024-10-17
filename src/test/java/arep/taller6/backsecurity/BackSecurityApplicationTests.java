package arep.taller6.backsecurity;

import arep.taller6.backsecurity.controller.UserController;
import arep.taller6.backsecurity.enums.RoleEntity;
import arep.taller6.backsecurity.model.UserDTO;
import arep.taller6.backsecurity.model.UserEntity;
import arep.taller6.backsecurity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BackSecurityApplicationTests {

	@InjectMocks
	private UserController userController;

	@Mock
	private UserService userService;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	public void testGetUsers_ReturnsListOfUsers() throws Exception {
		// Arrange
		UserEntity user = new UserEntity(1L, "test@example.com", "testuser", "password", RoleEntity.USER);
		List<UserEntity> users = Collections.singletonList(user);
		when(userService.getAll()).thenReturn(users);

		// Act & Assert
		mockMvc.perform(get("/api/user/users"))
				.andExpect(status().isOk());
	}


	@Test
	public void testCreateUser_ReturnsCreatedUser() throws Exception {
		// Arrange
		UserDTO userDTO = new UserDTO("test@example.com", "testuser", "password", "USER");
		UserEntity createdUser = new UserEntity(1L, "test@example.com", "testuser", "encryptedpassword", RoleEntity.USER);
		when(userService.save(any(UserDTO.class))).thenReturn(createdUser);

		// Act & Assert
		mockMvc.perform(post("/api/user/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"test@example.com\",\"username\":\"testuser\",\"password\":\"password\",\"role\":\"USER\"}"))
				.andExpect(status().isCreated());
	}

	@Test
	public void testLogin_ReturnsUser_WhenValidCredentials() throws Exception {
		// Arrange
		UserDTO userDTO = new UserDTO("test@example.com", "testuser", "password", "USER");
		UserEntity user = new UserEntity(1L, "test@example.com", "testuser", "encryptedpassword", RoleEntity.USER);
		when(userService.getUser(any(UserDTO.class))).thenReturn(Collections.singletonList(user));

		// Act & Assert
		mockMvc.perform(post("/api/user/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"test@example.com\",\"password\":\"password\",\"role\":\"USER\"}"))
				.andExpect(status().isOk());
	}

	@Test
	public void testLogin_ReturnsError_WhenInvalidCredentials() throws Exception {
		// Arrange
		UserDTO userDTO = new UserDTO("test@example.com", "testuser", "wrongpassword", "USER");
		when(userService.getUser(any(UserDTO.class))).thenThrow(new RuntimeException("The password is incorrect"));

		// Act & Assert
		mockMvc.perform(post("/api/user/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"test@example.com\",\"password\":\"wrongpassword\"}"))
				.andExpect(status().isInternalServerError());
	}


}

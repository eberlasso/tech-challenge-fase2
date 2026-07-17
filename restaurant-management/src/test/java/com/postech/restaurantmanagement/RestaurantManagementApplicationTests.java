package com.postech.restaurantmanagement;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

class RestaurantManagementApplicationTests {

	@Test
	void applicationClassShouldBeLoadable() {
		assertNotNull(RestaurantManagementApplication.class);
    }

	@Test
	void mainShouldDelegateToSpringApplicationRun() {
		try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
			String[] args = {"--debug"};
			RestaurantManagementApplication.main(args);
			springApplication.verify(() -> SpringApplication.run(eq(RestaurantManagementApplication.class), any(String[].class)));
		}
	}

}

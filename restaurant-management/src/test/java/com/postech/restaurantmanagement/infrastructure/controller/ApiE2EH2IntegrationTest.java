package com.postech.restaurantmanagement.infrastructure.controller;

import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataMenuItemRepository;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataRestaurantRepository;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class ApiE2EH2IntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SpringDataMenuItemRepository menuItemRepository;

    @Autowired
    private SpringDataRestaurantRepository restaurantRepository;

    @Autowired
    private SpringDataUserRepository userRepository;

    @TestConfiguration
    static class NoopAuditGatewayConfig {
        @Bean
        @Primary
        AuditLogGateway testAuditLogGateway() {
            return new AuditLogGateway() {
                @Override
                public void log(String action, String details, String performedBy) {
                }

                @Override
                public java.util.List<com.postech.restaurantmanagement.domain.entity.AuditLog> findAll() {
                    return java.util.List.of();
                }

                @Override
                public java.util.List<com.postech.restaurantmanagement.domain.entity.AuditLog> findByAction(String action) {
                    return java.util.List.of();
                }
            };
        }
    }

    @BeforeEach
    void cleanup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUserRestaurantAndMenuItemEndToEnd() throws Exception {
        Long ownerId = createUser("owner1@email.com");
        Long restaurantId = createRestaurant("Bistro Paris", "Main St 10", ownerId);

        String menuPayload = "{" +
                "\"name\":\"Risotto\"," +
                "\"description\":\"Creamy rice\"," +
                "\"price\":59.90," +
                "\"availableOnlyInRestaurant\":true," +
                "\"imagePath\":\"/img/risotto.jpg\"," +
                "\"restaurantId\":" + restaurantId +
                "}";

        mockMvc.perform(post("/api/v1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Risotto"))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId));
    }

    @Test
    void shouldRejectDuplicateRestaurantByNameAndAddress() throws Exception {
        Long ownerId = createUser("owner2@email.com");

        createRestaurant("Cantina", "Rua A, 100", ownerId);

        String duplicateRestaurant = "{" +
                "\"name\":\"cantina\"," +
                "\"address\":\"rua a, 100\"," +
                "\"cuisineType\":\"Italian\"," +
                "\"operatingHours\":\"10:00-22:00\"," +
                "\"ownerId\":" + ownerId +
                "}";

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateRestaurant))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldRejectDuplicateMenuInSameRestaurantButAllowInAnotherRestaurant() throws Exception {
        Long owner1 = createUser("owner3@email.com");
        Long owner2 = createUser("owner4@email.com");

        Long restaurant1 = createRestaurant("Rest One", "Address 1", owner1);
        Long restaurant2 = createRestaurant("Rest Two", "Address 2", owner2);

        String menuR1 = "{" +
                "\"name\":\"Burger\"," +
                "\"description\":\"Beef burger\"," +
                "\"price\":29.90," +
                "\"availableOnlyInRestaurant\":false," +
                "\"imagePath\":\"/img/burger.jpg\"," +
                "\"restaurantId\":" + restaurant1 +
                "}";

        mockMvc.perform(post("/api/v1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuR1))
                .andExpect(status().isCreated());

        String duplicateInSameRestaurant = "{" +
                "\"name\":\"burger\"," +
                "\"description\":\"Other desc\"," +
                "\"price\":31.90," +
                "\"availableOnlyInRestaurant\":true," +
                "\"imagePath\":\"/img/burger2.jpg\"," +
                "\"restaurantId\":" + restaurant1 +
                "}";

        mockMvc.perform(post("/api/v1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateInSameRestaurant))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());

        String sameNameDifferentRestaurant = "{" +
                "\"name\":\"Burger\"," +
                "\"description\":\"Allowed in other restaurant\"," +
                "\"price\":35.90," +
                "\"availableOnlyInRestaurant\":true," +
                "\"imagePath\":\"/img/burger3.jpg\"," +
                "\"restaurantId\":" + restaurant2 +
                "}";

        mockMvc.perform(post("/api/v1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sameNameDifferentRestaurant))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.restaurantId").value(restaurant2));
    }

    private Long createUser(String email) throws Exception {
        String payload = "{" +
                "\"name\":\"Owner\"," +
                "\"email\":\"" + email + "\"," +
                "\"password\":\"StrongPass@123\"," +
                "\"phoneNumber\":\"11999999999\"," +
                "\"roles\":[\"RESTAURANT_OWNER\"]" +
                "}";

        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn();

        return extractId(result.getResponse().getContentAsString());
    }

    private Long createRestaurant(String name, String address, Long ownerId) throws Exception {
        String payload = "{" +
                "\"name\":\"" + name + "\"," +
                "\"address\":\"" + address + "\"," +
                "\"cuisineType\":\"Italian\"," +
                "\"operatingHours\":\"10:00-22:00\"," +
                "\"ownerId\":" + ownerId +
                "}";

        MvcResult result = mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn();

        return extractId(result.getResponse().getContentAsString());
    }

    private Long extractId(String json) {
        Matcher matcher = Pattern.compile("\"id\"\\s*:\\s*(\\d+)").matcher(json);
        if (!matcher.find()) {
            throw new IllegalStateException("Could not extract id from response: " + json);
        }
        return Long.parseLong(matcher.group(1));
    }
}


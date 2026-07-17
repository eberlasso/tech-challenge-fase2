package com.postech.restaurantmanagement.infrastructure.controller;

import com.postech.restaurantmanagement.domain.entity.AuditLog;
import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.usecase.*;
import com.postech.restaurantmanagement.infrastructure.controller.dto.*;
import com.postech.restaurantmanagement.infrastructure.controller.mapper.AuditLogMapper;
import com.postech.restaurantmanagement.infrastructure.controller.mapper.MenuItemMapper;
import com.postech.restaurantmanagement.infrastructure.controller.mapper.RestaurantMapper;
import com.postech.restaurantmanagement.infrastructure.controller.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ControllersIntegrationTest {

    @Mock private CreateUserUseCase createUserUseCase;
    @Mock private ListUsersUseCase listUsersUseCase;
    @Mock private GetUserByIdUseCase getUserByIdUseCase;
    @Mock private UpdateUserUseCase updateUserUseCase;
    @Mock private DeleteUserUseCase deleteUserUseCase;
    @Mock private UserMapper userMapper;

    @Mock private CreateRestaurantUseCase createRestaurantUseCase;
    @Mock private ListRestaurantsUseCase listRestaurantsUseCase;
    @Mock private GetRestaurantByIdUseCase getRestaurantByIdUseCase;
    @Mock private UpdateRestaurantUseCase updateRestaurantUseCase;
    @Mock private DeleteRestaurantUseCase deleteRestaurantUseCase;
    @Mock private RestaurantMapper restaurantMapper;

    @Mock private CreateMenuItemUseCase createMenuItemUseCase;
    @Mock private ListMenuItemsUseCase listMenuItemsUseCase;
    @Mock private GetMenuItemByIdUseCase getMenuItemByIdUseCase;
    @Mock private UpdateMenuItemUseCase updateMenuItemUseCase;
    @Mock private DeleteMenuItemUseCase deleteMenuItemUseCase;
    @Mock private MenuItemMapper menuItemMapper;

    @Mock private GetAuditLogsUseCase getAuditLogsUseCase;
    @Mock private AuditLogMapper auditLogMapper;

    @Test
    void shouldCreateAndListAndUpdateAndDeleteUserViaHttp() throws Exception {
        MockMvc mvc = MockMvcBuilders
                .standaloneSetup(new UserController(
                        createUserUseCase,
                        listUsersUseCase,
                        getUserByIdUseCase,
                        updateUserUseCase,
                        deleteUserUseCase,
                        userMapper))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        CreateUserRequest request = new CreateUserRequest("John", "john@email.com", "Secret123", "1199", Set.of("CLIENT"));
        User domainInput = User.builder().name("John").email("john@email.com").password("Secret123").roles(Set.of()).build();
        User created = User.builder().id(1L).name("John").email("john@email.com").password("Secret123").phoneNumber("1199").roles(Set.of()).build();
        UserResponse response = new UserResponse(1L, "John", "john@email.com", "1199", Set.of("CLIENT"));

        when(userMapper.toDomain(request)).thenReturn(domainInput);
        when(createUserUseCase.execute(domainInput)).thenReturn(created);
        when(updateUserUseCase.execute(org.mockito.ArgumentMatchers.any(User.class))).thenReturn(created);
        when(getUserByIdUseCase.execute(1L)).thenReturn(created);
        when(listUsersUseCase.execute()).thenReturn(List.of(created));
        when(userMapper.toResponse(created)).thenReturn(response);

        String body = "{" +
                "\"name\":\"John\"," +
                "\"email\":\"john@email.com\"," +
                "\"password\":\"Secret123\"," +
                "\"phoneNumber\":\"1199\"," +
                "\"roles\":[\"CLIENT\"]}";

        mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        mvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        mvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        mvc.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        mvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldCreateAndListAndUpdateAndDeleteRestaurantViaHttp() throws Exception {
        MockMvc mvc = MockMvcBuilders
                .standaloneSetup(new RestaurantController(
                        createRestaurantUseCase,
                        listRestaurantsUseCase,
                        getRestaurantByIdUseCase,
                        updateRestaurantUseCase,
                        deleteRestaurantUseCase,
                        restaurantMapper))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        CreateRestaurantRequest request = new CreateRestaurantRequest("Bistro", "Main", "French", "10-22", 7L);
        Restaurant domain = Restaurant.builder().name("Bistro").address("Main").cuisineType("French").operatingHours("10-22").owner(User.builder().id(7L).build()).build();
        Restaurant created = Restaurant.builder().id(9L).name("Bistro").address("Main").cuisineType("French").operatingHours("10-22").owner(User.builder().id(7L).build()).build();
        RestaurantResponse response = new RestaurantResponse(9L, "Bistro", "Main", "French", "10-22", 7L);

        when(restaurantMapper.toDomain(request)).thenReturn(domain);
        when(createRestaurantUseCase.execute(domain)).thenReturn(created);
        when(updateRestaurantUseCase.execute(org.mockito.ArgumentMatchers.any(Restaurant.class))).thenReturn(created);
        when(getRestaurantByIdUseCase.execute(9L)).thenReturn(created);
        when(listRestaurantsUseCase.execute()).thenReturn(List.of(created));
        when(restaurantMapper.toResponse(created)).thenReturn(response);

        String body = "{\"name\":\"Bistro\",\"address\":\"Main\",\"cuisineType\":\"French\",\"operatingHours\":\"10-22\",\"ownerId\":7}";

        mvc.perform(post("/api/v1/restaurants").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ownerId").value(7L));

        mvc.perform(get("/api/v1/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(9L));

        mvc.perform(get("/api/v1/restaurants/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9L));

        mvc.perform(put("/api/v1/restaurants/9").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9L));

        mvc.perform(delete("/api/v1/restaurants/9"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldCreateAndListAndUpdateAndDeleteMenuItemViaHttp() throws Exception {
        MockMvc mvc = MockMvcBuilders
                .standaloneSetup(new MenuItemController(
                        createMenuItemUseCase,
                        listMenuItemsUseCase,
                        getMenuItemByIdUseCase,
                        updateMenuItemUseCase,
                        deleteMenuItemUseCase,
                        menuItemMapper))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        CreateMenuItemRequest request = new CreateMenuItemRequest("Pizza", "Cheese", new BigDecimal("10.00"), true, "/img", 3L);
        MenuItem domain = MenuItem.builder().name("Pizza").description("Cheese").price(new BigDecimal("10.00")).availableOnlyInRestaurant(true).imagePath("/img").restaurant(Restaurant.builder().id(3L).build()).build();
        MenuItem created = MenuItem.builder().id(4L).name("Pizza").description("Cheese").price(new BigDecimal("10.00")).availableOnlyInRestaurant(true).imagePath("/img").restaurant(Restaurant.builder().id(3L).build()).build();
        MenuItemResponse response = new MenuItemResponse(4L, "Pizza", "Cheese", new BigDecimal("10.00"), true, "/img", 3L);

        when(menuItemMapper.toDomain(request)).thenReturn(domain);
        when(createMenuItemUseCase.execute(domain)).thenReturn(created);
        when(updateMenuItemUseCase.execute(org.mockito.ArgumentMatchers.any(MenuItem.class))).thenReturn(created);
        when(getMenuItemByIdUseCase.execute(4L)).thenReturn(created);
        when(listMenuItemsUseCase.execute(null)).thenReturn(List.of(created));
        when(listMenuItemsUseCase.execute(3L)).thenReturn(List.of(created));
        when(menuItemMapper.toResponse(created)).thenReturn(response);

        String body = "{\"name\":\"Pizza\",\"description\":\"Cheese\",\"price\":10.00,\"availableOnlyInRestaurant\":true,\"imagePath\":\"/img\",\"restaurantId\":3}";

        mvc.perform(post("/api/v1/menu-items").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.restaurantId").value(3L));

        mvc.perform(get("/api/v1/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(4L));

        mvc.perform(get("/api/v1/menu-items").param("restaurantId", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].restaurantId").value(3L));

        mvc.perform(get("/api/v1/menu-items/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4L));

        mvc.perform(put("/api/v1/menu-items/4").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4L));

        mvc.perform(delete("/api/v1/menu-items/4"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetAuditLogsViaHttp() throws Exception {
        MockMvc mvc = MockMvcBuilders
                .standaloneSetup(new AuditLogController(getAuditLogsUseCase, auditLogMapper))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        AuditLog log = new AuditLog("id", "CREATE", "details", LocalDateTime.now(), "SYSTEM");
        AuditLogResponse response = new AuditLogResponse("id", "CREATE", "details", log.getTimestamp(), "SYSTEM");

        when(getAuditLogsUseCase.executeByAction("CREATE")).thenReturn(List.of(log));
        when(auditLogMapper.toResponse(log)).thenReturn(response);

        mvc.perform(get("/api/v1/audit-logs").param("action", "CREATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("id"))
                .andExpect(jsonPath("$[0].action").value("CREATE"));
    }
}

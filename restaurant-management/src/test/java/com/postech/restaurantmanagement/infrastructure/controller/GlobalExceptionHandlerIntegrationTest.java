package com.postech.restaurantmanagement.infrastructure.controller;

import com.postech.restaurantmanagement.domain.exception.EntityValidationException;
import com.postech.restaurantmanagement.domain.exception.BusinessException;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerIntegrationTest {

    @RestController
    static class ThrowingController {

        static class CustomBusinessException extends BusinessException {
            CustomBusinessException(String message) {
                super(message);
            }
        }

        @GetMapping("/conflict")
        public String conflict() {
            throw new ResourceAlreadyExistsException("already exists");
        }

        @GetMapping("/bad-request")
        public String badRequest() {
            throw new EntityValidationException("invalid payload");
        }

        @GetMapping("/not-found")
        public String notFound() {
            throw new ResourceNotFoundException("not found");
        }

        @GetMapping("/integrity")
        public String integrity() {
            throw new DataIntegrityViolationException("integrity", new RuntimeException("duplicate key"));
        }

        @GetMapping("/business")
        public String business() {
            throw new CustomBusinessException("business error");
        }

        @GetMapping("/unexpected")
        public String unexpected() {
            throw new RuntimeException("boom");
        }
    }

    @Test
    void shouldTranslateExceptionsToExpectedHttpStatus() throws Exception {
        MockMvc mvc = MockMvcBuilders
                .standaloneSetup(new ThrowingController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mvc.perform(get("/conflict").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("already exists"));

        mvc.perform(get("/bad-request").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("invalid payload"));

        mvc.perform(get("/not-found").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("not found"));

        mvc.perform(get("/integrity").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());

        mvc.perform(get("/unexpected").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").exists());

        mvc.perform(get("/business").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("business error"));
    }
}

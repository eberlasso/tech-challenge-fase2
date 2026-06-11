package com.postech.restaurantmanagement.domain.entity;

import java.util.regex.Pattern;

public class Client {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;

    // Expressão regular simples para validação estrutural de e-mail
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // --- REGRAS DE NEGÓCIO CENTRAIS ---

    /**
     * Validates if the client business constraints are fully satisfied before registration.
     *
     * @return true if the structure, name, and email format are correct, false otherwise.
     */
    public boolean isValid() {
        return name != null && !name.isBlank() &&
               email != null && !email.isBlank() && EMAIL_PATTERN.matcher(email).matches() &&
               phoneNumber != null && !phoneNumber.isBlank();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static Pattern getEmailPattern() {
        return EMAIL_PATTERN;
    }

    

}

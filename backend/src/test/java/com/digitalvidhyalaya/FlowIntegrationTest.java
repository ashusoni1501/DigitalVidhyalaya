package com.digitalvidhyalaya;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class FlowIntegrationTest {

    private static final String PLATFORM_ADMIN_KEY = "test-admin-key";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateSchoolConfigureSettingsCreateUserAndLogin() throws Exception {
        MvcResult schoolResult = mockMvc.perform(post("/api/v1/tenants/schools")
                        .header("X-Platform-Admin-Key", PLATFORM_ADMIN_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Springfield Public School",
                                  "code": "SPS",
                                  "addressLine1": "Main Road",
                                  "addressLine2": "Near Market",
                                  "country": "India",
                                  "state": "Delhi",
                                  "district": "New Delhi",
                                  "city": "Delhi",
                                  "pincode": "110001",
                                  "primaryPhone": "9876543210",
                                  "primaryEmail": "info@sps.edu.in"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.schoolId").exists())
                .andReturn();

        JsonNode schoolJson = objectMapper.readTree(schoolResult.getResponse().getContentAsString());
        String schoolId = schoolJson.path("data").path("schoolId").asText();

        mockMvc.perform(put("/api/v1/tenants/schools/{schoolId}/settings", schoolId)
                        .header("X-Platform-Admin-Key", PLATFORM_ADMIN_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "requireAdmissionApproval": true,
                                  "requireStudentEditApproval": true,
                                  "allowCashPayment": true,
                                  "allowUpiPayment": true,
                                  "receiptPrefix": "RCPT",
                                  "admissionPrefix": "ADM",
                                  "defaultCountry": "India"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.schoolId").value(schoolId))
                .andExpect(jsonPath("$.data.receiptPrefix").value("RCPT"));

        MvcResult userResult = mockMvc.perform(post("/api/v1/users")
                        .header("X-Platform-Admin-Key", PLATFORM_ADMIN_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "schoolId": "%s",
                                  "username": "schooladmin",
                                  "password": "admin123",
                                  "fullName": "School Admin",
                                  "email": "admin@sps.edu.in",
                                  "phone": "9999999999",
                                  "roles": ["ADMIN"]
                                }
                                """.formatted(schoolId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("schooladmin"))
                .andExpect(jsonPath("$.data.roles[0]").value("ADMIN"))
                .andReturn();

        JsonNode userJson = objectMapper.readTree(userResult.getResponse().getContentAsString());
        long userId = userJson.path("data").path("id").asLong();

        mockMvc.perform(get("/api/v1/users")
                        .header("X-Platform-Admin-Key", PLATFORM_ADMIN_KEY)
                        .param("schoolId", schoolId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(userId))
                .andExpect(jsonPath("$.data[0].username").value("schooladmin"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "schoolId": "%s",
                                  "username": "schooladmin",
                                  "password": "admin123"
                                }
                                """.formatted(schoolId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("schooladmin"))
                .andExpect(jsonPath("$.data.readOnlyAccess").value(false))
                .andExpect(jsonPath("$.data.roles[0]").value("ADMIN"));
    }
}

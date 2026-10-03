package com.banking.api.controller;

import com.banking.api.config.SecurityConfig;
import com.banking.api.config.WebConfig;
import com.banking.api.service.AccountFinancialService;
import com.banking.api.service.AccountService;
import com.banking.api.service.AccountStatsService;
import com.banking.api.service.CustomerService;
import com.banking.api.service.DeService;
import com.banking.api.service.RtellerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = {
        AccountController.class,
        CustomerController.class,
        DeController.class,
        RtellerController.class,
        AccountStatsController.class,
        AccountFinancialController.class
})
@Import({
        SecurityConfig.class,
        WebConfig.class,
        AccountService.class,
        CustomerService.class,
        DeService.class,
        RtellerService.class,
        AccountStatsService.class,
        AccountFinancialService.class
})
@EnabledIfSystemProperty(
        named = "runRealClientIntegration",
        matches = "true",
        disabledReason = "Calls configured downstream services, including financial write operations"
)
class EndpointRequestIntegrationTest {

    private static final List<EndpointExpectation> ENDPOINTS = List.of(
            new EndpointExpectation("/customer-account-details", "ROLE_VIEW_CUSTOMER_ACCOUNT_DETAILS",
                    "custDetailsFull", "custDetailsIO"),
            new EndpointExpectation("/query-amount-block", "ROLE_QUERY_AMOUNT_BLOCK",
                    "amountBlocksFull", "amountBlocksIO"),
            new EndpointExpectation("/query-customer", "ROLE_QUERY_CUSTOMER",
                    "customerFull", "customerIO"),
            new EndpointExpectation("/create-customer", "ROLE_CREATE_CUSTOMER", "customerFull"),
            new EndpointExpectation("/create-corporate-customer", "ROLE_CREATE_CORPORATE_CUSTOMER",
                    "customerFull"),
            new EndpointExpectation("/amount-block", "ROLE_CREATE_AMOUNT_BLOCK",
                    "amountBlocksFull", "amountBlocksIO"),
            new EndpointExpectation("/cheque-book-request", "ROLE_CHECKOUT_ACCOUNT", "chqBkDetailsFull"),
            new EndpointExpectation("/summary-balance", "ROLE_VIEW_SUMMARY_BALANCE",
                    "stvwAccountSumaryFull", "stvwAccountSumaryIO"),
            new EndpointExpectation("/statement", "ROLE_VIEW_ACCOUNT_STATEMENT", "mainFull", "mainIO"),
            new EndpointExpectation("/account-details", "ROLE_VIEW_ACCOUNT_DETAILS",
                    "custDetailsFull", "custDetailsIO"),
            new EndpointExpectation("/create-account", "ROLE_CREATE_ACCOUNT", "custAccountFull"),
            new EndpointExpectation("/full-account-balance", "ROLE_VIEW_FULL_ACCOUNT_BALANCE",
                    "custAccountFull"),
            new EndpointExpectation("/de-single-debit-credit-journal", "ROLE_CREATE_DE_JOURNAL",
                    "detbsJrnlTxnMasterFull"),
            new EndpointExpectation("/query-journal", "ROLE_QUERY_JOURNAL",
                    "detbsJrnlTxnMasterIO", "detbsJrnlTxnMasterFull"),
            new EndpointExpectation("/de-reversal", "ROLE_CREATE_DE_TEMPLATE", "acvwsAllAcEntriesFull"),
            new EndpointExpectation("/multi-de-bulk-journal", "ROLE_CREATE_DE_JOURNAL",
                    "detbsJrnlTxnMasterFull"),
            new EndpointExpectation("/authorize", "ROLE_AUTHORIZE_DE_TRANSACTION",
                    "detbsJrnlTxnMasterIO", "detbsJrnlTxnMasterFull"),
            new EndpointExpectation("/query-transaction", "ROLE_QUERY_TRANSACTION",
                    "transactionDetailsFull"),
            new EndpointExpectation("/query-product", "ROLE_QUERY_PRODUCT", "rtProductFull", "rtProductIO"),
            new EndpointExpectation("/pass-entry", "ROLE_PASS_ENTRY", "transactionDetails"),
            new EndpointExpectation("/authorize-transaction", "ROLE_AUTHORIZE_TRANSACTION",
                    "transactionDetails"),
            new EndpointExpectation("/reverse-transaction", "ROLE_REVERSE_TRANSACTION", "transactionDetails"),
            new EndpointExpectation("/customer-stats", "ROLE_VIEW_CUSTOMER_STATS", "cumulativeIO", "cumulativeFull"),
            new EndpointExpectation("/audit-trail", "ROLE_VIEW_AUDIT_TRAIL",
                    "acvwAcdaudtrFull", "acvwAcdaudtrIO"),
            new EndpointExpectation("/account-transactions", "ROLE_VIEW_ACCOUNT_TRANSACTIONS",
                    "accDetailsFull", "accDetailsIO"),
            new EndpointExpectation("/customer-statement", "ROLE_VIEW_CUSTOMER_STATEMENT",
                    "custAccStmtAdhocRequest")
    );

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void invokesRealDownstreamClientsAndParsesEveryRequestFixture() throws Exception {
        assertFixtures("Testdata/customer-account-requests.json");
        assertFixtures("Testdata/account-requests.json");
        assertFixtures("Testdata/de-endpoint-requests.json");
        assertFixtures("Testdata/rteller-requests.json");
        assertFixtures("Testdata/account-stats-requests.json");
        assertFixtures("Testdata/customer-statement-requests.json");
    }

    private void assertFixtures(String fixturePath) throws Exception {
        JsonNode fixtures = readFixture(fixturePath);
        if (fixtures.isArray()) {
            for (JsonNode fixture : fixtures) {
                String endpoint = fixture.path("endpoint").asText();
                assertRequest(endpoint, fixture.path("request"));
            }
            return;
        }

        var entries = fixtures.fields();
        while (entries.hasNext()) {
            var entry = entries.next();
            assertRequest(entry.getKey(), entry.getValue());
        }
    }

    private void assertRequest(String endpoint, JsonNode request) throws Exception {
        assertFalse(request.isMissingNode(), "Missing request fixture for " + endpoint);
        EndpointExpectation expectation = ENDPOINTS.stream()
                .filter(candidate -> candidate.path().equals(endpoint))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No expected response mapping for " + endpoint));

        MvcResult result = mockMvc.perform(post("/api/service" + endpoint)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_CLIENT"),
                                new SimpleGrantedAuthority(expectation.authority())
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request.toString()))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertNotEquals(500, status, endpoint + " returned HTTP 500: " + result.getResponse().getContentAsString());
        assertEquals(200, status, endpoint + " returned an unexpected status");

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.path("fcubsheader").isObject(),
                endpoint + " response did not map the FCUBS header");
        String messageStatus = response.path("fcubsheader").path("msgstat").asText();
        assertTrue(List.of("SUCCESS", "FAILURE").contains(messageStatus),
                endpoint + " returned an invalid FCUBS message status: " + messageStatus);

        JsonNode body = response.path("fcubsbody");
        assertTrue(body.isObject(), endpoint + " response did not map the FCUBS body");
        boolean mappedOperationBody = false;
        for (String field : expectation.responseFields()) {
            if (body.hasNonNull(field)) {
                mappedOperationBody = true;
                break;
            }
        }
        JsonNode errors = body.path("fcubserrorresp");
        boolean mappedErrors = errors.isArray() && !errors.isEmpty();
        assertTrue(mappedOperationBody || mappedErrors,
                endpoint + " response contains neither its expected FCUBS body field "
                        + "nor mapped FCUBS errors: " + result.getResponse().getContentAsString());
    }

    private JsonNode readFixture(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        try (var inputStream = resource.getInputStream()) {
            return objectMapper.readTree(inputStream);
        }
    }

    private record EndpointExpectation(String path, String authority, String... responseFields) {
    }

}

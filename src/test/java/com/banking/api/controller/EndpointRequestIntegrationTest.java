package com.banking.api.controller;

import com.banking.api.config.SecurityConfig;
import com.banking.api.dto.AccountResponse;
import com.banking.api.dto.AccountStatsResponse;
import com.banking.api.dto.AccountStatementResponse;
import com.banking.api.dto.CreateAccountResponse;
import com.banking.api.dto.CustomerResponse;
import com.banking.api.dto.DeResponse;
import com.banking.api.dto.FullAccountBalanceResponse;
import com.banking.api.dto.RtellerResponse;
import com.banking.api.dto.StatementResponse;
import com.banking.api.dto.SummaryBalanceResponse;
import com.banking.api.service.AccountService;
import com.banking.api.service.AccountFinancialService;
import com.banking.api.service.AccountStatsService;
import com.banking.api.service.CustomerService;
import com.banking.api.service.DeService;
import com.banking.api.service.RtellerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        AccountController.class,
        CustomerController.class,
        DeController.class,
        RtellerController.class,
        AccountStatsController.class,
        AccountFinancialController.class
})
@Import(SecurityConfig.class)
class EndpointRequestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private DeService deService;

    @MockitoBean
    private RtellerService rtellerService;

    @MockitoBean
    private AccountStatsService accountStatsService;

    @MockitoBean
    private AccountFinancialService accountFinancialService;

    @BeforeEach
    void stubMappedDownstreamResponses() throws IOException {
        when(accountService.summaryBalance(any())).thenReturn(response(
                SummaryBalanceResponse.class,
                "\"stvwAccountSumaryIO\":{\"CUSTNO\":\"1030046420801014\"}"
        ));
        when(accountService.statement(any())).thenReturn(response(
                StatementResponse.class,
                "\"mainIO\":{\"CUSNO\":\"001633\",\"STMTID\":\"001633\"}"
        ));
        when(accountService.accountDetails(any())).thenReturn(response(
                AccountResponse.class,
                "\"custDetailsFull\":{\"CUSTNO\":\"001633\"}"
        ));
        when(accountService.createAccount(any())).thenReturn(response(
                CreateAccountResponse.class,
                "\"custAccountFull\":{\"CUSTNO\":\"054872\",\"ACC\":\"1010548720000000\"}"
        ));
        when(accountService.fullAccountBalance(any())).thenReturn(response(
                FullAccountBalanceResponse.class,
                "\"custAccountFull\":{\"ACC\":\"1010016330301010\"}"
        ));
        when(accountService.requestChequeBook(any())).thenReturn(response(
                AccountResponse.class,
                "\"chqBkDetailsFull\":{\"ACCOUNT\":\"1040258210101010\"}"
        ));

        when(customerService.accountDetails(any())).thenReturn(response(
                CustomerResponse.class,
                "\"custDetailsFull\":{\"CUSTNO\":\"001633\"}"
        ));
        when(customerService.queryAmountBlock(any())).thenReturn(response(
                CustomerResponse.class,
                "\"amountBlocksIO\":{\"amtblkno\":\"1234\"}"
        ));
        when(customerService.queryCustomer(any())).thenReturn(response(
                CustomerResponse.class,
                "\"customerIO\":{\"custno\":\"001633\"}"
        ));
        when(customerService.createCustomer(any())).thenReturn(response(
                CustomerResponse.class,
                "\"customerFull\":{\"custno\":\"054871\"}"
        ));
        when(customerService.createCorporateCustomer(any())).thenReturn(response(
                CustomerResponse.class,
                "\"customerFull\":{\"custno\":\"054872\"}"
        ));
        when(customerService.amountBlock(any())).thenReturn(response(
                CustomerResponse.class,
                "\"amountBlocksFull\":{\"amtblkno\":\"A123456\"}"
        ));

        when(deService.multiJournal2(any())).thenReturn(response(
                DeResponse.class,
                "\"detbsJrnlTxnMasterFull\":{\"REFERENCENO\":\"101qlql262870001\"}"
        ));
        when(deService.queryJournal(any())).thenReturn(response(
                DeResponse.class,
                "\"detbsJrnlTxnMasterIO\":{\"REFERENCENO\":\"101qlql262870001\"}"
        ));
        when(deService.reverseJournal(any())).thenReturn(response(
                DeResponse.class,
                "\"acvwsAllAcEntriesFull\":{\"TRNREFNO\":\"101qlql262870001\"}"
        ));
        when(deService.multiDeJournal(any())).thenReturn(response(
                DeResponse.class,
                "\"detbsJrnlTxnMasterFull\":{\"REFERENCENO\":\"101lqlq262870001\"}"
        ));
        when(deService.authorize(any())).thenReturn(response(
                DeResponse.class,
                "\"detbsJrnlTxnMasterIO\":{\"REFERENCENO\":\"101lqlq262870001\"}"
        ));

        when(rtellerService.queryTransaction(any())).thenReturn(response(
                RtellerResponse.class,
                "\"transactionDetailsFull\":{\"FCCREF\":\"101CHWL262870005\"}"
        ));
        when(rtellerService.queryProduct(any())).thenReturn(response(
                RtellerResponse.class,
                "\"RTProductFull\":{\"PRDCD\":\"CHWL\"}"
        ));
        when(rtellerService.passAccountEntry(any())).thenReturn(response(
                RtellerResponse.class,
                "\"transactionDetails\":{\"FCCREF\":\"101CHWL262870006\"}"
        ));
        when(rtellerService.authorizeTransaction(any())).thenReturn(response(
                RtellerResponse.class,
                "\"transactionDetails\":{\"FCCREF\":\"101CHWL262870006\"}"
        ));
        when(rtellerService.reverseTransaction(any())).thenReturn(response(
                RtellerResponse.class,
                "\"transactionDetails\":{\"FCCREF\":\"101CHWL262870014\"}"
        ));

        when(accountStatsService.queryCustomerStats(any())).thenReturn(response(
                AccountStatsResponse.class,
                "\"cumulativeIO\":{\"CUSTOMERNO\":\"008997\",\"CUSTOMERACCNO\":\"1010089970301010\",\"BRANCHCODE\":\"101\"}"
        ));
        when(accountStatsService.queryAuditTrail(any())).thenReturn(response(
                AccountStatsResponse.class,
                "\"acvwAcdaudtrIO\":{\"BRANCHCODE\":\"101\",\"CUSTACNO\":\"1010089970301010\",\"TRNFROMDT\":\"2026-10-01\",\"TRNTODT\":\"2026-10-14\"}"
        ));
        when(accountStatsService.queryAccountTransaction(any())).thenReturn(response(
                AccountStatsResponse.class,
                "\"accDetailsFull\":{\"ACCNO\":\"1010089970301010\",\"ACCBRN\":\"101\",\"NUMOFTRN\":50}"
        ));

        when(accountFinancialService.queryCustomerStatement(any())).thenReturn(response(
                AccountStatementResponse.class,
                "\"custAccStmtAdhocRequest\":{\"XREF\":\"1234567\",\"DCN\":\"101MSOG26287000A\"}"
        ));
    }

    @Test
    void servesAllCustomerAndAccountRequestFixturesWithMappedResponses() throws Exception {
        JsonNode customerRequests = readFixture("Testdata/customer-account-requests.json");
        assertEndpoint(customerRequests, "/customer-account-details",
                "ROLE_VIEW_CUSTOMER_ACCOUNT_DETAILS", "custDetailsFull.CUSTNO", "001633");
        assertEndpoint(customerRequests, "/query-amount-block",
                "ROLE_QUERY_AMOUNT_BLOCK", "amountBlocksIO.amtblkno", "1234");
        assertEndpoint(customerRequests, "/query-customer",
                "ROLE_QUERY_CUSTOMER", "customerIO.custno", "001633");
        assertEndpoint(customerRequests, "/create-customer",
                "ROLE_CREATE_CUSTOMER", "customerFull.custno", "054871");
        assertEndpoint(customerRequests, "/create-corporate-customer",
                "ROLE_CREATE_CORPORATE_CUSTOMER", "customerFull.custno", "054872");
        assertEndpoint(customerRequests, "/amount-block",
                "ROLE_CREATE_AMOUNT_BLOCK", "amountBlocksFull.amtblkno", "A123456");
        assertEndpoint(customerRequests, "/cheque-book-request",
                "ROLE_CHECKOUT_ACCOUNT", "chqBkDetailsFull.ACCOUNT", "1040258210101010");

        JsonNode accountRequests = readFixture("Testdata/account-requests.json");
        for (JsonNode fixture : accountRequests) {
            String endpoint = fixture.path("endpoint").asText();
            String authority;
            String responseField;
            String expectedValue;
            switch (endpoint) {
                case "/summary-balance" -> {
                    authority = "ROLE_VIEW_SUMMARY_BALANCE";
                    responseField = "stvwAccountSumaryIO.CUSTNO";
                    expectedValue = "1030046420801014";
                }
                case "/statement" -> {
                    authority = "ROLE_VIEW_ACCOUNT_STATEMENT";
                    responseField = "mainIO.CUSNO";
                    expectedValue = "001633";
                }
                case "/account-details" -> {
                    authority = "ROLE_VIEW_ACCOUNT_DETAILS";
                    responseField = "custDetailsFull.CUSTNO";
                    expectedValue = "001633";
                }
                case "/create-account" -> {
                    authority = "ROLE_CREATE_ACCOUNT";
                    responseField = "custAccountFull.ACC";
                    expectedValue = "1010548720000000";
                }
                case "/full-account-balance" -> {
                    authority = "ROLE_VIEW_FULL_ACCOUNT_BALANCE";
                    responseField = "custAccountFull.ACC";
                    expectedValue = "1010016330301010";
                }
                case "/cheque-book-request" -> {
                    authority = "ROLE_CHECKOUT_ACCOUNT";
                    responseField = "chqBkDetailsFull.ACCOUNT";
                    expectedValue = "1040258210101010";
                }
                default -> throw new AssertionError("Unexpected account fixture endpoint: " + endpoint);
            }
            assertEndpoint(endpoint, fixture.path("request"), authority, responseField, expectedValue);
        }
    }

    @Test
    void servesAllDebitCreditRequestFixturesWithMappedResponses() throws Exception {
        JsonNode requests = readFixture("Testdata/de-endpoint-requests.json");
        assertEndpoint(requests, "/de-single-debit-credit-journal",
                "ROLE_CREATE_DE_JOURNAL", "detbsJrnlTxnMasterFull.referenceno", "101qlql262870001");
        assertEndpoint(requests, "/query-journal",
                "ROLE_QUERY_JOURNAL", "detbsJrnlTxnMasterIO.REFERENCENO", "101qlql262870001");
        assertEndpoint(requests, "/de-reversal",
                "ROLE_CREATE_DE_TEMPLATE", "acvwsAllAcEntriesFull.TRNREFNO", "101qlql262870001");
        assertEndpoint(requests, "/multi-de-bulk-journal",
                "ROLE_CREATE_DE_JOURNAL", "detbsJrnlTxnMasterFull.referenceno", "101lqlq262870001");
        assertEndpoint(requests, "/authorize",
                "ROLE_AUTHORIZE_DE_TRANSACTION", "detbsJrnlTxnMasterIO.REFERENCENO", "101lqlq262870001");
    }

    @Test
    void servesAllRetailTellerRequestFixturesWithMappedResponses() throws Exception {
        JsonNode requests = readFixture("Testdata/rteller-requests.json");
        assertEndpoint(requests, "/query-transaction",
                "ROLE_QUERY_TRANSACTION", "transactionDetailsFull.FCCREF", "101CHWL262870005");
        assertEndpoint(requests, "/query-product",
                "ROLE_QUERY_PRODUCT", "rtProductFull.PRDCD", "CHWL");
        assertEndpoint(requests, "/pass-entry",
                "ROLE_PASS_ENTRY", "transactionDetails.FCCREF", "101CHWL262870006");
        assertEndpoint(requests, "/authorize-transaction",
                "ROLE_AUTHORIZE_TRANSACTION", "transactionDetails.FCCREF", "101CHWL262870006");
        assertEndpoint(requests, "/reverse-transaction",
                "ROLE_REVERSE_TRANSACTION", "transactionDetails.FCCREF", "101CHWL262870014");
    }

    @Test
    void servesAllAccountStatsRequestFixturesWithMappedResponses() throws Exception {
        JsonNode requests = readFixture("Testdata/account-stats-requests.json");
        assertEndpoint(requests, "/customer-stats",
                "ROLE_VIEW_CUSTOMER_STATS", "cumulativeIO.customeraccno", "1010089970301010");
        assertEndpoint(requests, "/audit-trail",
                "ROLE_VIEW_AUDIT_TRAIL", "acvwAcdaudtrIO.CUSTACNO", "1010089970301010");
        assertEndpoint(requests, "/account-transactions",
                "ROLE_VIEW_ACCOUNT_TRANSACTIONS", "accDetailsFull.ACCNO", "1010089970301010");
    }

    @Test
    void servesCustomerStatementRequestFixtureWithMappedResponse() throws Exception {
        JsonNode requests = readFixture("Testdata/customer-statement-requests.json");
        assertEndpoint(requests, "/customer-statement",
                "ROLE_VIEW_CUSTOMER_STATEMENT",
                "custAccStmtAdhocRequest.dcn", "101MSOG26287000A");
    }

    private void assertEndpoint(
            JsonNode fixtures,
            String endpoint,
            String authority,
            String responseField,
            String expectedValue
    ) throws Exception {
        JsonNode request = fixtures.path(endpoint);
        assertFalse(request.isMissingNode(), "No request fixture found for " + endpoint);
        assertEndpoint(endpoint, request, authority, responseField, expectedValue);
    }

    private void assertEndpoint(
            String endpoint,
            JsonNode request,
            String authority,
            String responseField,
            String expectedValue
    ) throws Exception {
        mockMvc.perform(post("/api/service" + endpoint)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_CLIENT"),
                                new SimpleGrantedAuthority(authority)
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fcubsheader.msgstat").value("SUCCESS"))
                .andExpect(jsonPath("$.fcubsbody." + responseField).value(expectedValue));
    }

    private JsonNode readFixture(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        try (var inputStream = resource.getInputStream()) {
            return objectMapper.readTree(inputStream);
        }
    }

    private <T> T response(Class<T> responseType, String bodyFields) throws IOException {
        String rawResponse = """
                {
                  "fcubsheader": {"msgstat": "SUCCESS"},
                  "fcubsbody": {%s}
                }
                """.formatted(bodyFields);
        return objectMapper.readValue(rawResponse, responseType);
    }
}

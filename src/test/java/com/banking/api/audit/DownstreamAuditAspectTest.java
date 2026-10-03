package com.banking.api.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DownstreamAuditAspectTest {

    private final DownstreamAuditRepository repository = mock(DownstreamAuditRepository.class);
    private final DownstreamAuditAspect aspect =
            new DownstreamAuditAspect(repository, new ObjectMapper());

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void persistsServiceMethodNameAsOperation() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ProceedingJoinPoint controllerJoinPoint = mock(ProceedingJoinPoint.class);
        Signature controllerSignature = mock(Signature.class);
        when(controllerJoinPoint.getSignature()).thenReturn(controllerSignature);
        when(controllerSignature.getName()).thenReturn("queryCustomer");
        when(controllerSignature.getDeclaringType()).thenReturn(CustomerControllerMarker.class);
        when(controllerJoinPoint.proceed()).thenAnswer(invocation -> {
            JoinPoint serviceJoinPoint = mock(JoinPoint.class);
            Signature serviceSignature = mock(Signature.class);
            when(serviceJoinPoint.getSignature()).thenReturn(serviceSignature);
            when(serviceSignature.getName()).thenReturn("accountDetails");
            aspect.captureServiceOperation(serviceJoinPoint);
            return ResponseEntity.ok("response");
        });

        aspect.audit(controllerJoinPoint);

        var savedAudit = forClass(DownstreamAudit.class);
        verify(repository).save(savedAudit.capture());
        assertEquals("accountDetails", savedAudit.getValue().getOperation());
    }

    private static final class CustomerControllerMarker {
    }
}

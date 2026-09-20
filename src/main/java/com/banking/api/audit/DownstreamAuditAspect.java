package com.banking.api.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DownstreamAuditAspect {

    private final DownstreamAuditRepository repository;
    private final ObjectMapper objectMapper;

    @Around("within(@org.springframework.web.bind.annotation.RestController *) && " +
            "execution(org.springframework.http.ResponseEntity *(..))")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime start = LocalDateTime.now();
        String request = serialize(joinPoint.getArgs());
        String userId = currentUserId();
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String serviceName = requestAttribute(requestAttributes, "downstream.serviceName",
                joinPoint.getSignature().getDeclaringType().getSimpleName());
        String operation = requestAttribute(requestAttributes, "downstream.operation", "unknown");
        int status = 200;
        String response = null;

        try {
            Object result = joinPoint.proceed();
            response = serialize(result);
            if (result instanceof ResponseEntity<?> responseEntity) {
                status = responseEntity.getStatusCode().value();
            }
            return result;
        } catch (Throwable exception) {
            status = 500;
            response = serialize(new AuditError(exception.getClass().getSimpleName(), exception.getMessage()));
            throw exception;
        } finally {
            try {
                DownstreamAudit audit = new DownstreamAudit();
                audit.setUserId(userId);
                audit.setServiceName(serviceName);
                audit.setOperation(operation);
                audit.setRequest(request);
                audit.setResponse(response);
                audit.setHttpStatus(status);
                audit.setStartTime(start);
                audit.setEndTime(LocalDateTime.now());
                repository.save(audit);
            } catch (Exception exception) {
                log.error("Unable to persist downstream audit record", exception);
            }

        }
    }

    private String requestAttribute(ServletRequestAttributes attributes, String name, String fallback) {
        if (attributes == null) {
            return fallback;
        }
        Object value = attributes.getRequest().getAttribute(name);
        return value == null ? fallback : value.toString();
    }

    private String currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : authentication.getName();
    }

    private String serialize(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            return "{\"serializationError\":\"" + exception.getClass().getSimpleName() + "\"}";
        }
    }

    private record AuditError(String type, String message) {
    }
}

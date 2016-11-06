package com.ymdwiseguy.col.crosscutting.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.ymdwiseguy.col.crosscutting.logging.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 This Controller handles and 'defuses' Exceptions that come bubbling up in our controller calls, i.e. if a JDBC Connection can't be obtained
 */
@ControllerAdvice
public class GlobalExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonNode> handleAllException(Exception ex) {
        JsonNode jNode = JsonNodeFactory.instance.textNode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        LogUtil.error(ex, () -> LOGGER.error("Exception caught by global exception handler: ", ex));
        return new ResponseEntity<>(jNode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoAccessException.class)
    public ResponseEntity<JsonNode> handleNoAccessException(NoAccessException ex) {
        JsonNode jNode = JsonNodeFactory.instance.textNode(HttpStatus.FORBIDDEN.getReasonPhrase());
        LOGGER.warn("NoAccessException: " + ex);
        return new ResponseEntity<>(jNode, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<JsonNode> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        JsonNode jNode = JsonNodeFactory.instance.textNode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        LogUtil.error(ex, () -> LOGGER.error("HttpMessageNotReadableException: " + ex));
        return new ResponseEntity<>(jNode, HttpStatus.BAD_REQUEST);
    }

}

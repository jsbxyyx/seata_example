package org.xxz.test.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.seata.core.context.RootContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author jsbxyyx
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity globalException(Exception e) {

        e.printStackTrace();

        String xid = RootContext.getXID();
        System.out.println("xid=" + xid);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("code", new JsonPrimitive(500));
        jsonObject.add("message", new JsonPrimitive(e.getMessage() + ", 出错了, xid=" + xid));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject.toString());
    }

}


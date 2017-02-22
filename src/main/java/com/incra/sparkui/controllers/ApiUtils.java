package com.incra.sparkui.controllers;

import com.beust.jcommander.internal.Maps;
import com.incra.sparkui.utils.JsonSerde;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

/**
 * @author rbanikaz
 * @since 2016-03-16
 */
public class ApiUtils {
    private final static Logger logger = LoggerFactory.getLogger(ApiUtils.class);

    public enum ApiStatus {
        OK,
        WARN,
        ERROR
    }

    public static String toErrorResponse(String message) {
        return toErrorResponse(Optional.empty(), message);
    }

    public static String toErrorResponse(String title, String message) {
        return toErrorResponse(Optional.of(title), message);
    }

    public static String toErrorResponse(Optional<String> title, String message) {
        Map error = Maps.newHashMap();

        if (title.isPresent()) {
            error.put("title", title.get());
        }

        error.put("message", message);

        return toApiResponse(ApiStatus.ERROR, "error", error);
    }

    public static String toWarnResponse(String message) {
        return toApiResponse(ApiStatus.WARN, message);
    }


    public static String toApiResponse(String message) {
        return toApiResponse(ApiStatus.OK, message);
    }

    public static String toApiResponse(Map response) {
        return toApiResponse(ApiStatus.OK, response);
    }

    public static String toApiResponse(ApiStatus status, Map response) {
        return toApiResponse(status, "response", response);
    }

    public static String toApiResponse(ApiStatus status, String response) {
        return toApiResponse(status, "response", response);
    }

    public static String toApiResponse(ApiStatus status, String responseKey, Map response) {
        return JsonSerde.serializeToString(Maps.newHashMap("status", status.toString(), responseKey, response));
    }

    public static String toApiResponse(ApiStatus status, String responseKey, String response) {
        return JsonSerde.serializeToString(Maps.newHashMap("status", status.toString(), responseKey, response));
    }
}

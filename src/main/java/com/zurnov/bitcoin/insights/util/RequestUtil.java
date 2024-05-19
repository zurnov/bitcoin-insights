package com.zurnov.bitcoin.insights.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class RequestUtil {

    public static String generateRequestId() {
        return UUID.randomUUID().toString().replace("-","");
    }
}

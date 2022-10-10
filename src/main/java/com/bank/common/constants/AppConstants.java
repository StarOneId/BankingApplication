package com.bank.common.constants;

public class AppConstants {
    public static final String JWT_SECRET = "bank-secret-key-minimum-32-characters-for-256-bit";
    public static final long JWT_EXPIRATION = 86400000; // 24 hours
    public static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 7 days

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";

    public static final String ACCOUNT_STATUS_ACTIVE = "ACTIVE";
    public static final String ACCOUNT_STATUS_INACTIVE = "INACTIVE";
    public static final String ACCOUNT_STATUS_BLOCKED = "BLOCKED";

    public static final String OPERATION_TYPE_CREDIT = "CREDIT";
    public static final String OPERATION_TYPE_DEBIT = "DEBIT";
    public static final String OPERATION_TYPE_TRANSFER = "TRANSFER";

    public static final String DEFAULT_CURRENCY = "USD";
}

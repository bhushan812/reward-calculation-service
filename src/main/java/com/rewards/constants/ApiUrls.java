package com.rewards.constants;

public final class ApiUrls {

    private ApiUrls() {}

    // Base path
    public static final String BASE = "/rewards";

    // Customer-related paths
    public static final String CUSTOMERS = "/customers";
    public static final String CUSTOMER_BY_ID = "/customers/{id}";
    public static final String CUSTOMER_BY_CUSTOMERID = "/customers/{customerId}";
    
    // Base
    public static final String TRANSACTION_BASE = "/transaction";

    // Endpoints
    public static final String ADD_TRANSACTION = "/transactions";
    public static final String UPDATE_TRANSACTION = "/transactions/{id}";
    public static final String DELETE_TRANSACTION = "/transactions/{transactionId}";
    public static final String LIST_TRANSACTIONS = "/transactionsList";
    
}

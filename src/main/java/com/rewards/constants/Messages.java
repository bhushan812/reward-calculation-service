package com.rewards.constants;

public final class Messages {

    private Messages() {}

    // Success
    public static final String CUSTOMER_ADD_SUCCESS = "Customer data added successfully.";
    public static final String CUSTOMER_UPDATE_SUCCESS = "Customer data updated successfully.";
    public static final String CUSTOMER_DELETE_SUCCESS = "Customer deleted successfully.";

    // Not Found
    public static final String NO_CUSTOMERS_FOUND = "No customers found.";
    public static final String CUSTOMER_NOT_FOUND_OR_NO_REWARDS = "Customer not found or no rewards available.";

    // Error prefixes
    public static final String ERROR_ADD = "Error adding customer: ";
    public static final String ERROR_UPDATE = "Error updating customer: ";
    public static final String ERROR_DELETE = "Error deleting customer: ";
    public static final String ERROR_FETCH = "Error fetching customers: ";
    public static final String ERROR_FETCH_REWARDS = "Error fetching customer rewards: ";
    
    
 // Transaction messages
    public static final String TRANSACTION_ADD_SUCCESS = "Transaction added successfully.";
    public static final String TRANSACTION_UPDATE_SUCCESS = "Transaction updated successfully.";
    public static final String TRANSACTION_DELETE_SUCCESS = "Transaction deleted successfully.";
    public static final String TRANSACTION_LIST_SUCCESS = "Transactions fetched successfully.";

    public static final String ERROR_ADD_TRANSACTION = "Error adding transaction: ";
 // Transaction Messages
    public static final String TRANSACTION_NOT_FOUND = "Transaction not found";
 // Global Error Constants
    public static final String INTERNAL_SERVER_EXCEPTION = "INTERNAL_SERVER_EXCEPTION";
    public static final String NULL_POINTER_EXCEPTION = "NULL_POINTER_EXCEPTION";

   
}

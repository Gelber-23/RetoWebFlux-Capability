package com.pragma.capabilities.domain.util;

public class ExceptionConstans {


    public static final String FIRST_PART_MESSAGE_EXCEPTION = "Message" ;
    public static final String CAPABILITY_TECHNOLOGY_NOT_COUNT_MIN_MAX = "Tech count must be " + ValueConstants.MIN_COUNT_TECHNOLOGY + " - " + ValueConstants.MAX_COUNT_TECHNOLOGY ;
    public static final String CAPABILITY_TECHNOLOGY_DUPLICATE = "Duplicate technology IDs" ;
    public static final String CAPABILITY_TECHNOLOGY_NOT_FOUND = "Some technologies not found" ;

    public static final String CAPABILITY_NAME_REQUIRED= "Name must not be empty";
    public static final String CAPABILITY_DESCRIPTION_REQUIRED = "Description must not be empty";
    public static final String CAPABILITY_NAME_EXCEEDS_LIMIT = "Name exceeds max length of " + ValueConstants.MAX_LENGTH_NAME_CAPABILITY;
    public static final String CAPABILITY_DESCRIPTION_EXCEEDS_LIMIT = "Description exceeds max length of " + ValueConstants.MAX_LENGTH_DESCRIPTION_CAPABILITY;

    public static final String VALUE_NOT_IN_ENUM ="%s Invalid parameter '%s': '%s'. Allowed values: [%s]";
}

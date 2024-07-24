package com.gyt.corepackage.utils.exceptions.problemDetails;

public class BusinessProblemDetails extends com.gyt.corepackage.utils.exceptions.problemDetails.ProblemDetails {
    public BusinessProblemDetails(){
        setTitle("Business Rule Violation");
        setType("http://mydomain.com/exceptions/business");
        setStatus("400");
    }
}

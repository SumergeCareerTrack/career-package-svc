package com.sumerge.careertrack.career_package_svc.exceptions;

public class AlreadyExistException extends RuntimeException{

    public static final String CAREER_PACKAGE = "Career Package \"%s\" already exists";
    public static final String Title = "Career Package for Title \"%s\" already exists";
    public static final String Employee = "Title \"%s\" already exists";


    public AlreadyExistException(){}
    public AlreadyExistException(String message){super(message);}
    public AlreadyExistException(String message, Object... args){super(String.format(message, args));}
}

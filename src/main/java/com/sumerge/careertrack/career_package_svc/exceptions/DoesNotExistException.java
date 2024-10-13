package com.sumerge.careertrack.career_package_svc.exceptions;

public class DoesNotExistException extends RuntimeException {

    public static final String CAREER_PACKAGE = "Career Package \"%s\" does not exist";
    public static final String Title = "Title \"%s\" does not exist";
    public static final String Employee = "Title \"%s\" does not exist";


    public DoesNotExistException() {super();}
    public DoesNotExistException(String message) {super(message);}
    public DoesNotExistException(String message, Object... args) {
        super(String.format(message, args));
    }

}

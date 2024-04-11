package org.quantcast.exceptions;

public class CommonExceptions {

    public static class MissingInputException extends Exception{
        public MissingInputException(String message){
            super(message);
        }
    }

    public static class InvalidDateException extends Exception{
        public InvalidDateException(String message){
            super(message);
        }
    }

}

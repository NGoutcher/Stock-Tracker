package Exceptions;

/**
 * Exception to be thrown when there are no portfolios open.
 * */
public class NoPortfoliosException extends Exception {
    public NoPortfoliosException(String errorMessage){
        super(errorMessage);
    }
}

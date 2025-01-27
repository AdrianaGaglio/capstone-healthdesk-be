package epicode.it.healthdesk.exceptions;

public class AddressMismatchingException extends RuntimeException{
    public AddressMismatchingException() {
        super();
    }

    public AddressMismatchingException(String message) {
        super(message);
    }
}

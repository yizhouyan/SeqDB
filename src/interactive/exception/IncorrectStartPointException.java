package interactive.exception;

/**
 * Created by yizhouyan on 4/24/18.
 */
public class IncorrectStartPointException extends RuntimeException{
    public IncorrectStartPointException(){
        super("Incorrect Start Point for Pattern");
    }

    public IncorrectStartPointException(String message){
        super(message);
    }
}

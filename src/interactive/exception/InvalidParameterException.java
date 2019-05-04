package interactive.exception;

/**
 * Created by yizhouyan on 4/24/18.
 */
public class InvalidParameterException extends RuntimeException{
    public InvalidParameterException(){
        super();
    }

    public InvalidParameterException(String message){
        super(message);
    }
}

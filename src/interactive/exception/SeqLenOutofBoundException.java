package interactive.exception;

/**
 * Created by yizhouyan on 4/24/18.
 */
public class SeqLenOutofBoundException extends RuntimeException{
    public SeqLenOutofBoundException(){
        super("sequence length out of bound");
    }

    public SeqLenOutofBoundException(String message){
        super(message);
    }
}

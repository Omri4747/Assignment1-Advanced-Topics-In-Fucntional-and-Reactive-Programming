package bgu.atd.a1;

public class ResultDetails {
    private final boolean succeeded;
    private final String message;

    public ResultDetails(boolean success, String msg){
        succeeded = success;
        message = msg;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

}

package chosun.keyboard_project.exception;

public class LoginFailException extends  RuntimeException{
    public LoginFailException(String message){
        super(message);
    }
}

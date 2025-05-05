package chosun.keyboard_project.exception;

//  사용자 정의 예외 클래스
public class DuplicateUsernameException extends RuntimeException{
    public DuplicateUsernameException(String message){

//      부모 클래스인 RuntimeException의 생성자에 message를 넘겨줌
//      이 메시지는 나중에 .getMessage()로 꺼낼 수 있고,
//      @ExceptionHandler에서도 e.getMessage()로 출력되도록 함
        super(message);

    }
}

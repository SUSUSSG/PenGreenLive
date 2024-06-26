package susussg.pengreenlive.chat.handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class MyHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    //웹소켓 연결 시 발생하는 상태에 해당하는 메소드를 정의한다.
    //최초 연결 시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        final String sessionId = session.getId();
        final String enteredMessage = String.format("{\"message\":\"%s님이 입장하셨습니다.\", \"writer\":\"System\"}", sessionId);

        sessions.put(sessionId, session);
        //    sendMessage(sessionId, new TextMessage(enteredMessage));
    }


    //양방향 데이터 통신할 떄 해당 메서드가 call 된다.
    //연결된 웹소켓들이 메시지를 주고받을 때 call되는 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final String sessionId = session.getId();
        final String payload = message.getPayload();

        String msgWithWriter = String.format("{\"message\":\"%s\", \"writer\":\"%s\"}", payload, sessionId);
        sendMessage(sessionId, new TextMessage(msgWithWriter));
    }


    //웹소켓 종료
    //session이 끊기게 되면 map에 저장되어있는 객체를 remove 하고,
    // 다른 접속자들에게 leave message를 보냄
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        final String sessionId = session.getId();
        final String leaveMessage = String.format("{\"message\":\"%s님이 퇴장하셨습니다.\", \"writer\":\"System\"}", sessionId);
        sessions.remove(sessionId);
//        sendMessage(sessionId, new TextMessage(leaveMessage));
    }


    //통신 에러 발생 시
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {}

    private void sendMessage(String sessionId, WebSocketMessage<?> message) {
        sessions.values().forEach(s -> {
            if(!s.getId().equals(sessionId) && s.isOpen()) {
                try {
                    s.sendMessage(message);
                } catch (IOException e) {}
            }
        });
    }
}
package pl.mzuchnik.springsecurityhomework3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.mzuchnik.springsecurityhomework3.entity.VerifyTokenType;

import java.util.UUID;

@Component
public class TokenGenerator {

    private static String serverName;
    private static String serverPort;

    public static String getRandomToken(){
        return UUID.randomUUID().toString();
    }

    public static String getGeneratedPathWithToken(String token, VerifyTokenType type)
    {
        return "http://www."+
                serverName+
                ":"+
                serverPort+
                "/verify-token?token="
                +token+
                "&type="+
                type;
    }

    @Value("${server.name}")
    public void setServerAddress(String serverName) {
        TokenGenerator.serverName = serverName;
    }

    @Value("${server.port}")
    public void setServerPort(String serverPort) {
        TokenGenerator.serverPort = serverPort;
    }
}

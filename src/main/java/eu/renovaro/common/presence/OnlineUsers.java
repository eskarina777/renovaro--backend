package eu.renovaro.common.presence;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUsers{
    public final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();
}

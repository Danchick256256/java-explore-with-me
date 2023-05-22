package ru.yandex.practicum.explore.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.practicum.explore.client.BaseClient;
import ru.yandex.practicum.explore.event.util.Hit;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class EventStatClient extends BaseClient implements EventStat {

    private static final String API_PREFIX = "";

    @Autowired
    public EventStatClient(@Value("${explore-stat.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    @Override
    public ResponseEntity<Object> addHitToStatistic(HttpServletRequest request) {
        Hit hit = Hit.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .timestamp(LocalDateTime.now())
                .build();
        return post(hit);
    }


}

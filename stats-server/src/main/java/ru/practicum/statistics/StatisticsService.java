package ru.practicum.statistics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statistics.dao.StatisticRepository;
import ru.practicum.statistics.dto.HitPayload;
import ru.practicum.statistics.dto.StatisticMessage;
import ru.practicum.statistics.model.Hit;
import ru.practicum.statistics.utility.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.parse;
import static ru.practicum.statistics.mapper.EndPointHitMapper.toHit;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticRepository repository;

    public List<StatisticMessage> getStatistic(String start, String end, List<String> uris, Boolean unique) {
        List<Hit> hits;
        if (uris.size() == 0) hits = repository.findAllByTimestampBetween(parse(start, Constants.TIME_FORMATTER), parse(end, Constants.TIME_FORMATTER));
        else hits = repository.findAllByTimestampBetweenAndUriIn(parse(start, Constants.TIME_FORMATTER), parse(end, Constants.TIME_FORMATTER), uris);
        List<StatisticMessage> statisticMessages = hits.stream()
                .collect(Collectors.groupingBy(Hit::getUri))
                .values()
                .stream()
                .map(list -> {
                    Hit hit = list.get(0);
                    Integer hitCount = unique ? repository.getCountOfUniqueIpByUri(hit.getUri()) : repository.getCountIpByUri(hit.getUri());
                    return new StatisticMessage(hit.getApp(), hit.getUri(), hitCount);
                })
                .collect(Collectors.toList());

        return statisticMessages;
        /*return hits.stream().map(hit -> {
            Integer hitCount = unique ? repository.getCountOfUniqueIpByUri(hit.getUri()) : repository.getCountIpByUri(hit.getUri());
            return new StatisticMessage(hit.getApp(), hit.getUri(), hitCount);
        }).collect(Collectors.toList());*/
    }

    public Hit addHit(HitPayload endpointHitPayload) {
        return repository.save(toHit(endpointHitPayload));
    }
}

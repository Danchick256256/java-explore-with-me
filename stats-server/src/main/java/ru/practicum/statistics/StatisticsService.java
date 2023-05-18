package ru.practicum.statistics;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statistics.dao.StatisticRepository;
import ru.practicum.statistics.dto.HitPayload;
import ru.practicum.statistics.dto.StatisticMessage;
import ru.practicum.statistics.model.Hit;
import ru.practicum.statistics.utility.Constants;

import javax.xml.bind.ValidationException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.parse;
import static ru.practicum.statistics.mapper.EndPointHitMapper.toHit;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticRepository repository;

    @SneakyThrows
    public List<StatisticMessage> getStatistic(String start, String end, List<String> uris, Boolean unique) {
        if (parse(start, Constants.TIME_FORMATTER).isAfter(parse(end, Constants.TIME_FORMATTER))) throw new ValidationException("Start is after end");
        List<Hit> hits;
        if (uris.isEmpty())
            hits = repository.findAllByTimestampBetween(parse(start, Constants.TIME_FORMATTER), parse(end, Constants.TIME_FORMATTER));
        else
            hits = repository.findAllByTimestampBetweenAndUriIn(parse(start, Constants.TIME_FORMATTER), parse(end, Constants.TIME_FORMATTER), uris);
        return hits.stream()
                .collect(Collectors.groupingBy(Hit::getUri))
                .values()
                .stream()
                .map(list -> {
                    Hit hit = list.get(0);
                    Integer hitCount = unique ? repository.getCountOfUniqueIpByUri(hit.getUri()) : repository.getCountIpByUri(hit.getUri());
                    return new StatisticMessage(hit.getApp(), hit.getUri(), hitCount);
                })
                .sorted(Comparator.comparing(StatisticMessage::getHits, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public Hit addHit(HitPayload endpointHitPayload) {
        return repository.save(toHit(endpointHitPayload));
    }
}

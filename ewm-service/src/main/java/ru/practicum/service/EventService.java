package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.*;
import ru.practicum.enums.StateAction;
import ru.practicum.enums.StateSort;
import ru.practicum.enums.StatusParticipation;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ValidationIdException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final LocationRepository locationRepository;
    private final StatsClient statsClient;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Event findEventByCategoryId(Long catId) {
        return eventRepository.findTopByCategoryId(catId);
    }

    public List<EventShortDto> getEventForUser(Long userId, Integer from, Integer size) {
        List<Event> eventList = eventRepository.findByInitiatorId(userId, PageRequest.of(from, size));
        return eventList.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public EventFullDto createEvent(Long userId, EventCreateDto eventDto) {
        LocalDateTime created = LocalDateTime.now();
        long hoursDifference = ChronoUnit.HOURS.between(created, eventDto.getEventDate());

        if (hoursDifference < 2) {
            throw new BadRequestException("Должно содержать дату, которая еще не наступила");
        }

        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new ValidationIdException("Категория с id=" + eventDto.getCategory() + ", не найдена"));

        User initiator = userService.checkUser(userId);
        Location location = locationRepository.save(eventDto.getLocation());

        Event event = EventMapper.toEvent(eventDto, category, location, initiator, created);
        Event newEvent = eventRepository.save(event);

        return EventMapper.toEventFullDto(newEvent);

    }

    public EventFullDto getEventByUserAndEventId(Long userId, Long eventId) {
        Event event = checkEvent(eventId, userId);
        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto updateEvent(Long userId, Long eventId, EventUpdateUserDto eventUserRequest) {
        LocalDateTime currentTime = LocalDateTime.now();

        if (eventUserRequest.getEventDate() != null) {
            long hoursDifference = ChronoUnit.HOURS.between(currentTime, eventUserRequest.getEventDate());

            if (hoursDifference < 2) {
                throw new BadRequestException("Должно содержать дату, которая еще не наступила");
            }
        }

        Event event = checkEvent(eventId, userId);

        if (event.getState() == StatusParticipation.PUBLISHED) {
            throw new ConflictException("Можно изменить только отложенные или отмененные события");
        }


        if (eventUserRequest.getStateAction() == StateAction.CANCEL_REVIEW) {
            event.setState(StatusParticipation.CANCELED);
        } else {
            event.setState(StatusParticipation.PENDING);
        }


        BeanUtils.copyProperties(eventUserRequest, event, getNullPropertyNames(eventUserRequest));
        Event updateEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(updateEvent);
    }

    public List<EventFullDto> getEventsAdmin(List<Long> users, StatusParticipation status, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from, size);

        Specification<Event> spec = (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (users != null && !users.isEmpty())
                predicates.add(root.get("initiator").in(users));
            if (status != null)
                predicates.add(cb.equal(root.get("state"), status));
            if (categories != null && !categories.isEmpty())
                predicates.add(root.join("category", JoinType.INNER).get("id").in(categories));
            if (rangeStart != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            if (rangeEnd != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        List<Event> eventList = eventRepository.findAll(spec, pageable).getContent();
        return eventList.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    public EventFullDto updateEventAndStatus(Long eventId, EventUpdateAdminDto adminRequest) {
        LocalDateTime currentTime = LocalDateTime.now();

        if (adminRequest.getEventDate() != null) {
            long hoursDifference = ChronoUnit.HOURS.between(currentTime, adminRequest.getEventDate());
            if (adminRequest.getEventDate().isBefore(currentTime)) {
                throw new BadRequestException("Дата начала изменяемого события должна быть в будущем");
            }

            if (hoursDifference < 1) {
                throw new ConflictException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            }
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ValidationIdException("Event with id=" + eventId + " was not found"));

        if (event.getState() != StatusParticipation.PENDING) {
            throw new ConflictException("Событие можно публиковать, только если оно в состоянии ожидания публикации, текущее состояние: " + event.getState());
        }

        BeanUtils.copyProperties(adminRequest, event, getNullPropertyNames(adminRequest));

        if (adminRequest.getStateAction() == StateAction.PUBLISH_EVENT) {
            event.setState(StatusParticipation.PUBLISHED);
        } else {
            event.setState(StatusParticipation.CANCELED);
        }

        Event updateEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(updateEvent);
    }

    private static String[] getNullPropertyNames(Object source) {

        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(source.getClass());

        return Stream.of(propertyDescriptors)
                .map(PropertyDescriptor::getName)
                .filter(propertyName -> {
                    Method getter = ReflectionUtils.findMethod(source.getClass(), "get" + StringUtils.capitalize(propertyName));
                    return getter != null && ReflectionUtils.invokeMethod(getter, source) == null;
                })
                .toArray(String[]::new);
    }

    public Event checkEvent(Long eventId, Long userId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event == null) {
            throw new ValidationIdException("Event with id=" + eventId + " was not found");
        }
        return event;
    }

    public EventFullDto getEventsById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, StatusParticipation.PUBLISHED);
        if (event == null) {
            throw new ValidationIdException("Event with id=" + eventId + " was not found");
        }

        Integer oldCountHit = getCountUniqueViews(request);

        statsClient.createInfo(EndpointHit.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .build());

        Integer newCountHit = getCountUniqueViews(request);

        if (newCountHit > oldCountHit) {
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
        }


        return EventMapper.toEventFullDto(event);
    }

    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, StateSort sort, Integer from, Integer size, HttpServletRequest request) {

        Pageable pageable = PageRequest.of(from, size, sort.toSort(Sort.Direction.DESC));

        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = rangeStart.plusYears(1000);
        }

        if (rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Дата начала сортировки должна быть ранне конца сортировки");
        }

        LocalDateTime finalRangeStart = rangeStart;
        LocalDateTime finalRangeEnd = rangeEnd;

        Specification<Event> spec = (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("state"), StatusParticipation.PUBLISHED));

            if (text != null)
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("description")), "%" + text.toLowerCase() + "%")
                ));
            if (categories != null) {
                predicates.add(root.join("category", JoinType.INNER).get("id").in(categories));
            }
            if (paid != null)
                predicates.add(cb.equal(root.get("paid"), paid));
            if (finalRangeStart != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), finalRangeStart));
            if (finalRangeEnd != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), finalRangeEnd));
            if (onlyAvailable != null && onlyAvailable)
                predicates.add(cb.greaterThan(root.get("participantLimit"), root.get("confirmedRequests")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));

        };

        Page<Event> events = eventRepository.findAll(spec, pageable);

        statsClient.createInfo(EndpointHit.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .build());

        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    private Integer getCountUniqueViews(HttpServletRequest request) {
        String[] uris = new String[]{request.getRequestURI()};

        List<ViewStats> response = statsClient.getStats(
                LocalDateTime.now().minusYears(100).format(formatter),
                LocalDateTime.now().plusHours(1).format(formatter),
                true,
                uris);
        return response.size();
    }
}


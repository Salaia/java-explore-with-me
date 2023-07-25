package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.EventRequestStatusUpdateResult;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.enums.StatusEventRequest;
import ru.practicum.enums.StatusParticipation;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ValidationIdException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserService userService;

    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ValidationIdException("Event with id = " + eventId + " was not found."));

        if (event.getState() != StatusParticipation.PUBLISHED) {
            throw new ConflictException("Participation is only allowed in events at state PUBLISHED. Current state: " + event.getState());
        }

        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("Event initiator cannot be sign in for participation.");
        }

        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ConflictException("Event has reached it's participation limit: " + event.getParticipantLimit());
        }

        Request request = RequestMapper.toRequest(userId, eventId);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(StatusEventRequest.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(StatusEventRequest.PENDING);

        }

        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    public List<ParticipationRequestDto> getRequest(Long userId) {
        userService.checkUser(userId);
        List<Request> requestList = requestRepository.findAllByRequester(userId);
        return requestList.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    public ParticipationRequestDto updateRequest(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequester(requestId, userId);

        if (request == null) {
            throw new ValidationIdException("Request with id = " + requestId + " was not found.");
        }
        request.setStatus(StatusEventRequest.CANCELED);
        Request updateRequest = requestRepository.save(request);

        return RequestMapper.toParticipationRequestDto(updateRequest);
    }

    public List<ParticipationRequestDto> getRequestEventByUser(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);

        if (event == null) {
            throw new ValidationIdException("Event with id = " + eventId + " was not found.");
        }

        List<Request> requestList = requestRepository.findAllByEvent(eventId);
        return requestList.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest statusUpdateRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);

        if (event == null) {
            throw new ValidationIdException("Event with id = " + eventId + " was not found.");
        }

        Set<Long> requestIds = statusUpdateRequest.getRequestIds();

        List<Request> requestList = requestRepository.findAllByEventAndIdIn(eventId, requestIds);

        EventRequestStatusUpdateResult requestStatusUpdateResult = new EventRequestStatusUpdateResult();

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            requestStatusUpdateResult.setConfirmedRequests(requestList.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList()));
            return requestStatusUpdateResult;
        }

        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Event has reached it's participation limit: " + event.getParticipantLimit());
        }


        for (Request request : requestList) {
            Integer limitRequest = event.getParticipantLimit();
            Integer confirmedRequests = event.getConfirmedRequests();

            if (confirmedRequests >= limitRequest) {
                request.setStatus(StatusEventRequest.REJECTED);
                requestStatusUpdateResult.getRejectedRequests().add(RequestMapper.toParticipationRequestDto(request));
                requestRepository.save(request);
            }

            if (!request.getStatus().equals(StatusEventRequest.PENDING)) {
                throw new ConflictException("Only requests at PENDING status may have their status changed. Current state: " + request.getStatus());
            }

            if (statusUpdateRequest.getStatus() == StatusEventRequest.CONFIRMED) {
                request.setStatus(StatusEventRequest.CONFIRMED);
                requestRepository.save(request);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                event = eventRepository.save(event);
                requestStatusUpdateResult.getConfirmedRequests().add(RequestMapper.toParticipationRequestDto(request));

            } else {
                request.setStatus(StatusEventRequest.REJECTED);
                requestStatusUpdateResult.getRejectedRequests().add(RequestMapper.toParticipationRequestDto(request));
                requestRepository.save(request);
            }
        }

        return requestStatusUpdateResult;
    }
}

package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.enums.StateAction;
import ru.practicum.enums.StatusParticipation;
import ru.practicum.service.CategoryService;
import ru.practicum.service.CompilationService;
import ru.practicum.service.EventService;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CategoryCreateDto category) {
        log.debug("Requested endpoint: admin/categories createCategory: " + category);
        return categoryService.createCategory(category);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive Long catId) {
        log.debug("Requested endpoint: admin/categories deleteCategory: " + catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable @Positive Long catId,
                                      @RequestBody @Valid CategoryCreateDto categoryDto) {
        log.debug("Requested endpoint: admin/categories/{catId} updateCategory, id = " + catId +
                ", new category dto: " + categoryDto);
        return categoryService.updateCategory(categoryDto, catId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEventsAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) StatusParticipation states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("Requested endpoint: admin/events getEvents" +
                ", params: users: " + users + ", status: " + states + ", categories: " + categories +
                ", rangeStart: " + rangeStart + ", rangeEnd: " + rangeEnd + ", from: " + from + ", size: " + size);
        return eventService.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEventAndStatus(@PathVariable @Positive Long eventId,
                                             @RequestBody @Validated EventUpdateAdminDto adminRequest) {
        log.debug("Requested endpoint: admin/events updateEventAndStatus, с id= " + eventId +
                ", dto for update: " + adminRequest);
        return eventService.updateEventAndStatus(eventId, adminRequest);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("Requested endpoint: admin/users getUsers, users' ids: " + ids + ", from: " + from + ", size: " + size);
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid UserCreateDto userRequest) {
        log.debug("Requested endpoint: admin/users createUser, dto: " + userRequest);
        return userService.createUser(userRequest);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive Long userId) {
        log.debug("Requested endpoint: admin/users deleteUser by id = " + userId);
        userService.deleteUser(userId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid CompilationCreateDto compilationCreateDto) {
        log.debug("Requested endpoint: admin/compilations createCompilation, dto: " + compilationCreateDto);
        return compilationService.create(compilationCreateDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Long compId) {
        log.debug("Requested endpoint: admin/compilations/{compId} deleteCompilation by id = " + compId);
        compilationService.delete(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable @Positive Long compId,
                                            @RequestBody @Valid CompilationUpdateDto compilationUpdateDto) {
        log.debug("Requested endpoint: admin/compilations/{compId} updateCompilation c compId = " + compId +
                ", dto: " + compilationUpdateDto);
        return compilationService.update(compId, compilationUpdateDto);
    }

    @GetMapping("/moderation")
    public List<EventShortDto> getEventWaitModeration() {
        log.debug("Requested endpoint: admin/moderation getEventWaitModeration");
        return eventService.getWaitModeration();
    }

    @GetMapping("/moderation/{modId}")
    public ModerationDto getModerationById(@PathVariable @Positive Long modId) {
        log.debug("Requested endpoint: admin/moderation/{modId} getModerationById, modId = " + modId);
        return eventService.getModerationById(modId);
    }

    @PostMapping("/{eventId}/moderation")
    public EventFullDto addModeration(@PathVariable Long eventId,
                                      @RequestBody @Valid ModerationDto moderationDto,
                                      @RequestParam StateAction state) {
        log.debug("Requested endpoint: admin/{eventId}/moderation getModerationById, eventId = " + eventId +
                ", dto: " + moderationDto + ", state: " + state);
        return eventService.addAnswerModeration(eventId, moderationDto, state);
    }
}

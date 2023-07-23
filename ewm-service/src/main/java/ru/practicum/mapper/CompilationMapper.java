package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.List;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> eventList) {
        return Compilation.builder()
                .events(eventList)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilationSave, List<EventShortDto> eventShortDtos) {
        return CompilationDto.builder()
                .events(eventShortDtos)
                .id(compilationSave.getId())
                .pinned(compilationSave.getPinned())
                .title(compilationSave.getTitle())
                .build();
    }
}

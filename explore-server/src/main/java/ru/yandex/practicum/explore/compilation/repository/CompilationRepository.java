package ru.yandex.practicum.explore.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explore.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}

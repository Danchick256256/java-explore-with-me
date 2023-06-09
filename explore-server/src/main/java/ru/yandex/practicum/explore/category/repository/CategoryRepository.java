package ru.yandex.practicum.explore.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.explore.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

package com.seiai.server.repositories;

import com.seiai.server.domain.Widget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetRepository extends CrudRepository<Widget, String> {
}

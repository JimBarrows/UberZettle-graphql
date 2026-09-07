package com.nsfwllc.uberzettlegraphql.idea;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface IdeaRepository extends
								JpaRepository<Idea, UUID> {
	List<Idea> findByOrderByIdAsc(Pageable pageable);

	List<Idea> findByIdGreaterThanOrderByIdAsc(UUID id, Pageable pageable);

}

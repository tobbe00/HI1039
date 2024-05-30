package com.kth.cprtraining.mapper;

/**
 * Mapper interface for converting between entity and DTO objects.
 * Provides methods to map from entity to DTO and vice versa.
 *
 * @param <A> The type of the entity.
 * @param <B> The type of the DTO.
 */
public interface Mapper<A, B>  {
    /**
     * Maps an entity to a DTO.
     * @param a The entity to map.
     * @return The mapped DTO.
     */
    B mapToDTO(A a);

    /**
     * Maps a DTO to an entity.
     * @param b The DTO to map.
     * @return The mapped entity.
     */
    A mapToEntity(B b);
}

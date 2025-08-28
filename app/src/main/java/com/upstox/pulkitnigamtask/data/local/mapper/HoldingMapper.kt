package com.upstox.pulkitnigamtask.data.local.mapper

import com.upstox.pulkitnigamtask.data.local.entity.HoldingEntity
import com.upstox.pulkitnigamtask.domain.model.Holding

/**
 * Mapper class for converting between Holding domain model and HoldingEntity.
 * Follows the mapping pattern for clean architecture.
 */
object HoldingMapper {
    
    /**
     * Convert HoldingEntity to Holding domain model.
     */
    fun toDomain(entity: HoldingEntity): Holding {
        return Holding(
            symbol = entity.symbol,
            quantity = entity.quantity,
            ltp = entity.ltp,
            avgPrice = entity.avgPrice,
            close = entity.close
        )
    }
    
    /**
     * Convert Holding domain model to HoldingEntity.
     */
    fun toEntity(domain: Holding): HoldingEntity {
        return HoldingEntity(
            symbol = domain.symbol,
            quantity = domain.quantity,
            ltp = domain.ltp,
            avgPrice = domain.avgPrice,
            close = domain.close
        )
    }
    
    /**
     * Convert list of HoldingEntity to list of Holding domain models.
     */
    fun toDomainList(entities: List<HoldingEntity>): List<Holding> {
        return entities.map { toDomain(it) }
    }
    
    /**
     * Convert list of Holding domain models to list of HoldingEntity.
     */
    fun toEntityList(domains: List<Holding>): List<HoldingEntity> {
        return domains.map { toEntity(it) }
    }
}

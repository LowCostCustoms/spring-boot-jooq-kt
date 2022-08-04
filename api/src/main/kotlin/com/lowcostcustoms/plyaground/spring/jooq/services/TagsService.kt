package com.lowcostcustoms.plyaground.spring.jooq.services

import com.lowcostcustoms.plyaground.spring.jooq.db.tables.records.TagsRecord
import com.lowcostcustoms.plyaground.spring.jooq.exceptions.BadRequestException
import com.lowcostcustoms.plyaground.spring.jooq.exceptions.NotFoundException
import com.lowcostcustoms.plyaground.spring.jooq.models.OffsetLimit
import com.lowcostcustoms.plyaground.spring.jooq.models.Page
import com.lowcostcustoms.plyaground.spring.jooq.models.TagRequest
import com.lowcostcustoms.plyaground.spring.jooq.models.TagResponse
import com.lowcostcustoms.plyaground.spring.jooq.repositories.TagsRepository
import com.lowcostcustoms.plyaground.spring.jooq.repositories.TagsRepository.Companion.UNIQUE_NAME_CONSTRAINT
import com.lowcostcustoms.plyaground.spring.jooq.utils.db.isConstraintViolation
import java.util.UUID
import org.jooq.exception.DataAccessException
import org.springframework.stereotype.Service

@Service
class TagsService(private val tagsRepository: TagsRepository) {
    suspend fun listTags(params: OffsetLimit): Page<TagResponse> {
        return Page(
            count = tagsRepository.count(),
            results = tagsRepository
                .findAll(offset = params.offset, limit = params.limit)
                .map { TagResponse.fromTagsRecord(it) },
        )
    }

    suspend fun getTag(id: UUID): TagResponse {
        val tag = tagsRepository.findById(id) ?: throw NotFoundException("Tag not found")

        return TagResponse.fromTagsRecord(tag)
    }

    suspend fun createTag(request: TagRequest): TagResponse {
        return handleUpdateCollisions {
            val tag = TagsRecord(UUID.randomUUID(), request.name)
            tagsRepository.create(tag)

            TagResponse.fromTagsRecord(tag)
        }
    }

    suspend fun replaceTag(id: UUID, request: TagRequest): TagResponse {
        return handleUpdateCollisions {
            val tag = TagsRecord(id, request.name)
            if (tagsRepository.update(tag) == 0) {
                throw NotFoundException("Tag not found")
            }

            TagResponse.fromTagsRecord(tag)
        }
    }

    private suspend fun <T> handleUpdateCollisions(block: suspend () -> T): T {
        return try {
            block()
        } catch (ex: DataAccessException) {
            if (ex.isConstraintViolation(UNIQUE_NAME_CONSTRAINT)) {
                throw BadRequestException("Tag with the same name already exists")
            }

            throw ex
        }
    }
}

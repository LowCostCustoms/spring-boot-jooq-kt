package com.lowcostcustoms.plyaground.spring.jooq.services

import com.lowcostcustoms.plyaground.spring.jooq.db.tables.records.PetTagsRecord
import com.lowcostcustoms.plyaground.spring.jooq.db.tables.records.PetsRecord
import com.lowcostcustoms.plyaground.spring.jooq.db.tables.records.TagsRecord
import com.lowcostcustoms.plyaground.spring.jooq.exceptions.BadRequestException
import com.lowcostcustoms.plyaground.spring.jooq.exceptions.NotFoundException
import com.lowcostcustoms.plyaground.spring.jooq.models.OffsetLimit
import com.lowcostcustoms.plyaground.spring.jooq.models.Page
import com.lowcostcustoms.plyaground.spring.jooq.models.PetRequest
import com.lowcostcustoms.plyaground.spring.jooq.models.PetResponse
import com.lowcostcustoms.plyaground.spring.jooq.models.PetStatus
import com.lowcostcustoms.plyaground.spring.jooq.models.TagResponse
import com.lowcostcustoms.plyaground.spring.jooq.repositories.PetsRepository
import com.lowcostcustoms.plyaground.spring.jooq.repositories.TagsRepository
import com.lowcostcustoms.plyaground.spring.jooq.utils.db.TransactionManager
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class PetService(
    private val petsRepository: PetsRepository,
    private val tagsRepository: TagsRepository,
    private val transactionManager: TransactionManager,
) {
    suspend fun listPets(params: OffsetLimit): Page<PetResponse> {
        val pets = petsRepository.findAll(offset = params.offset, limit = params.limit)
        val petTags = tagsRepository.findByPetIds(pets.map { it.id })

        return Page(
            count = petsRepository.count(),
            results = pets.map { PetResponse.fromPetsRecord(record = it, tags = petTags) },
        )
    }

    suspend fun getPet(id: UUID): PetResponse {
        val pet = petsRepository.findById(id) ?: throw NotFoundException("Pet not found")
        val petTags = tagsRepository.findByPetIds(listOf(id))

        return PetResponse.fromPetsRecord(record = pet, tags = petTags)
    }

    suspend fun createPet(request: PetRequest): PetResponse {
        return transactionManager.withTransaction {
            val tags = findTags(request.tags)
            val pet = PetsRecord(UUID.randomUUID(), request.name, request.status.value)
            petsRepository.create(pet)
            tagsRepository.createPetTags(tags.map { PetTagsRecord(pet.id, it.id) })

            PetResponse.fromPetsRecord(record = pet, tags = tags.map { TagResponse.fromTagsRecord(it) })
        }
    }

    suspend fun replacePet(id: UUID, request: PetRequest): PetResponse {
        return transactionManager.withTransaction {
            val tags = findTags(request.tags)
            val record = PetsRecord(id, request.name, request.status.value)
            if (petsRepository.update(record) == 0) {
                throw NotFoundException("Pet not found")
            }

            tagsRepository.deletePetTagsByPetId(id)
            tagsRepository.createPetTags(tags.map { PetTagsRecord(id, it.id) })

            PetResponse.fromPetsRecord(record = record, tags = tags.map { TagResponse.fromTagsRecord(it) })
        }
    }

    suspend fun deletePet(id: UUID) {
        if (petsRepository.deleteById(id) == 0) {
            throw NotFoundException("Pet not found")
        }
    }

    private suspend fun findTags(names: List<String>): List<TagsRecord> {
        val tags = tagsRepository.findByNames(names)
        if (tags.size != names.size) {
            throw BadRequestException("One or more tags could not be found")
        }

        return tags
    }
}

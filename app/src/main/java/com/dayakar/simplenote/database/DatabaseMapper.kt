package com.dayakar.simplenote.database

import com.dayakar.simplenote.model.Note
import com.dayakar.simplenote.util.EntityMapper
import javax.inject.Inject

class DatabaseMapper @Inject constructor(): EntityMapper<NoteDatabaseEntity, Note> {

    override fun mapFromEntity(entity: NoteDatabaseEntity): Note {
        return Note(
            id=entity.id,
            userId = entity.userId,
            title = entity.title,
            note = entity.note,
            updatetime = entity.updatetime,
            isSynced = entity.isSynced
        )
    }

    override fun mapToEntity(domainModel: Note): NoteDatabaseEntity {
        return NoteDatabaseEntity(
            id=domainModel.id,
            userId = domainModel.userId,
            title = domainModel.title,
            note = domainModel.note,
            updatetime = domainModel.updatetime,
            isSynced = domainModel.isSynced
        )
    }

    fun mapFromEntityList(entities:List<NoteDatabaseEntity>):List<Note>{
        return entities.map { mapFromEntity(it) }
    }
    fun mapToDataBaseModel(models:List<Note>):List<NoteDatabaseEntity>{
        return models.map { mapToEntity(it) }
    }
}
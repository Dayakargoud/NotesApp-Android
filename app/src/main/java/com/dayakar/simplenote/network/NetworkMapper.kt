package com.dayakar.simplenote.network

import com.dayakar.simplenote.model.Note
import com.dayakar.simplenote.util.EntityMapper
import javax.inject.Inject

class NetworkMapper @Inject constructor():EntityMapper<NoteNetworkEntity,Note> {
    override fun mapFromEntity(entity: NoteNetworkEntity): Note {

        return Note(
            id=entity.id,
            userId = entity.userId,
            title = entity.title,
            note = entity.note,
            updatetime = entity.updatetime,
            isSynced = entity.isSynced
        )
    }

    override fun mapToEntity(domainModel: Note): NoteNetworkEntity {
        return NoteNetworkEntity(
            id=domainModel.id,
            userId = domainModel.userId,
            title = domainModel.title,
            note = domainModel.note,
            updatetime = domainModel.updatetime,
            isSynced = domainModel.isSynced
        )
    }

    fun mapFromEntityList(entities:List<NoteNetworkEntity>):List<Note>{

        return entities.map {
            mapFromEntity(it)
        }
    }

    fun mapToNetworkModelList(modelList:List<Note>):List<NoteNetworkEntity>{
        return modelList.map {
            mapToEntity(it)
        }
    }
}
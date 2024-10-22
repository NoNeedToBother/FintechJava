package ru.kpfu.itis.paramonov.mapper

import ru.kpfu.itis.paramonov.dto.Place
import ru.kpfu.itis.paramonov.dto.PlaceResponseDto

class PlaceMapper {

    fun mapResponseToDto(responseDto: PlaceResponseDto): Place {
        return Place(
            id = responseDto.id,
            title = responseDto.title,
            address = responseDto.address,
            phoneNumber = responseDto.phoneNumber
        )
    }
}
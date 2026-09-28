package com.mountouris.sportsapp.remote

import com.mountouris.sportsapp.domain.model.Sport
import com.mountouris.sportsapp.remote.dto.SportDto
import com.mountouris.sportsapp.remote.mapper.toDomain
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val SPORTS_URL = "https://thodorismount.github.io/SportsApp/sports.json"

class SportsApiSourceImpl(private val client: HttpClient) : SportsApiSource {
    override suspend fun fetchSports(): List<Sport> =
        client.get(SPORTS_URL).body<List<SportDto>>().map { it.toDomain() }
}

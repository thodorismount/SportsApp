package com.kaizen.sportsapp.remote

import com.kaizen.sportsapp.domain.model.Sport
import com.kaizen.sportsapp.remote.dto.SportDto
import com.kaizen.sportsapp.remote.mapper.toDomain
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val SPORTS_URL = "https://ios-kaizen.github.io/MockSports/sports.json"

class SportsApiSourceImpl(private val client: HttpClient) : SportsApiSource {
    override suspend fun fetchSports(): List<Sport> =
        client.get(SPORTS_URL).body<List<SportDto>>().map { it.toDomain() }
}

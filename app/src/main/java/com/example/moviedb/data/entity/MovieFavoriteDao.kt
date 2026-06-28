package com.example.moviedb.data.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieFavoriteDao {
    @Upsert
    suspend fun upsert(movie: MovieFavorite)
    @Query("SELECT * FROM movie_favorites WHERE id = :id")
    suspend fun getFavorite(id: Long): MovieFavorite?
    @Query("SELECT * FROM movie_favorites")
    suspend fun getAllFavorite(): List<MovieFavorite>
    @Query("DELETE FROM movie_favorites WHERE id = :id")
    suspend fun deleteFavorite(id: Long)


}
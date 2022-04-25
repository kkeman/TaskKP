/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.service.codingtest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.service.codingtest.model.response.ImageRemoteKeyEntity

@Dao
interface ImageRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: ImageRemoteKeyEntity)

    @Query("SELECT * FROM remote_keys WHERE searchWord = :searchWord")
    suspend fun remoteKeyBySearchWord(searchWord: String): ImageRemoteKeyEntity

    @Query("DELETE FROM remote_keys WHERE searchWord = :searchWord")
    suspend fun deleteBySearchWord(searchWord: String)
}
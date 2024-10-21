package com.adedom.data.datasource.local

import com.adedom.data.database.table.JittaSqliteTable
import com.adedom.data.models.entities.JittaEntity
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.joda.time.LocalDate

interface JittaLocalDataSource {
    suspend fun selectAll(): List<JittaEntity>
    suspend fun selectAllWhereToday(): List<JittaEntity>
    suspend fun insert(symbol: String, isUnderJittaLine: Boolean)
}

class JittaLocalDataSourceImpl : JittaLocalDataSource {
    override suspend fun selectAll(): List<JittaEntity> {
        return newSuspendedTransaction {
            JittaSqliteTable.selectAll()
                .map { row ->
                    JittaEntity(
                        id = row[JittaSqliteTable.id].value,
                        symbol = row[JittaSqliteTable.symbol],
                        isUnderJittaLine = row[JittaSqliteTable.underJittaLine] == 1,
                        createdAt = row[JittaSqliteTable.createdAt].toLocalDateTime().toString(),
                    )
                }
        }
    }

    override suspend fun selectAllWhereToday(): List<JittaEntity> {
        val today = LocalDate.now()
        return newSuspendedTransaction {
            JittaSqliteTable.selectAll()
                .where {
                    JittaSqliteTable.createdAt.greaterEq(today.toDateTimeAtStartOfDay()) and
                            JittaSqliteTable.createdAt.lessEq(today.toDateTimeAtStartOfDay().plusDays(1).minusMillis(1))
                }
                .map { row ->
                    JittaEntity(
                        id = row[JittaSqliteTable.id].value,
                        symbol = row[JittaSqliteTable.symbol],
                        isUnderJittaLine = row[JittaSqliteTable.underJittaLine] == 1,
                        createdAt = row[JittaSqliteTable.createdAt].toLocalDateTime().toString(),
                    )
                }
        }
    }

    override suspend fun insert(symbol: String, isUnderJittaLine: Boolean) {
        return newSuspendedTransaction {
            JittaSqliteTable.insert {
                it[this.symbol] = symbol
                it[this.underJittaLine] = if (isUnderJittaLine) 1 else 0
            }
        }
    }
}

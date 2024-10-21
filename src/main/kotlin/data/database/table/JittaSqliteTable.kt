package com.adedom.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object JittaSqliteTable : IntIdTable("jitta") {
    val symbol: Column<String> = text("symbol")
    val underJittaLine: Column<Int> = integer("under_jitta_line")
    val createdAt: Column<DateTime> = datetime("created_at").default(DateTime.now())
}

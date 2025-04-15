package com.satnamsinghmaggo.crudmate

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "crudmate_users.db"
        const val DATABASE_VERSION = 1
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_AGE = "age"
        const val COLUMN_CITY = "city"
        const val COLUMN_STATE = "state"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_ADDRESS = "address"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_AGE INTEGER,
                $COLUMN_CITY TEXT,
                $COLUMN_STATE TEXT,
                $COLUMN_PHONE TEXT,
                $COLUMN_ADDRESS TEXT
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(user: UserData): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_AGE, user.age)
            put(COLUMN_CITY, user.city)
            put(COLUMN_STATE, user.state)
            put(COLUMN_PHONE, user.phone)
            put(COLUMN_ADDRESS, user.address)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun createUser(user: UserData): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_AGE, user.age)
            put(COLUMN_CITY, user.city)
            put(COLUMN_STATE, user.state)
            put(COLUMN_PHONE, user.phone)
            put(COLUMN_ADDRESS, user.address)
        }
        return db.insert(TABLE_USERS, null, values).toInt()
    }

    fun getAllUsers(): List<UserData> {
        val users = mutableListOf<UserData>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS", null)

        if (cursor.moveToFirst()) {
            do {
                val user = UserData(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                    city = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)),
                    state = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATE)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }

    fun getUserById(id: Int): UserData? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_ID=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        val user = if (cursor.moveToFirst()) {
            UserData(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                city = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)),
                state = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATE)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
            )
        } else {
            null
        }
        cursor.close()
        return user
    }

    fun updateUser(user: UserData): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_AGE, user.age)
            put(COLUMN_CITY, user.city)
            put(COLUMN_STATE, user.state)
            put(COLUMN_PHONE, user.phone)
            put(COLUMN_ADDRESS, user.address)
        }
        return db.update(TABLE_USERS, values, "$COLUMN_ID=?", arrayOf(user.id.toString()))
    }

    fun deleteUser(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_USERS, "$COLUMN_ID=?", arrayOf(id.toString()))
    }
}

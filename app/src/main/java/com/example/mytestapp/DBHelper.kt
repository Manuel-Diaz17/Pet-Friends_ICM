package com.example.mytestapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class DBHelper(context: Context):SQLiteOpenHelper(context,dbname,factory,version) {
    companion object{
        internal val dbname = "SqlDB"
        internal val factory = null
        internal val version = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE users(email nvarchar(50) primary key," +
                         "password nvarchar(50)," +
                         "name nvarchar(50)," +
                         "lastname nvarchar(50)," +
                         "address nvarchar(50)," +
                         "phone char(10))")


        db?.execSQL("CREATE TABLE userLoggedIn(email nvarchar(50) primary key," +
                        "password nvarchar(50))")

        db?.execSQL("CREATE TABLE petSitterActive(name nvarchar(50) primary key," +
                        "address nvarchar(50)," +
                        "rating REAL," +
                        "image INTEGER," +
                        "age INTEGER," +
                        "typeOfPets nvarchar(50)," +
                        "review1 nvarchar(50)," +
                        "review2 nvarchar(50)," +
                        "review3 nvarchar(50)," +
                        "email nvarchar(50)," +
                        "FOREIGN KEY(email) REFERENCES userLoggedIn(email))")

        db?.execSQL("CREATE TABLE pets(pet blob," +
                        "id nvarchar(50)," +
                        "email nvarchar(50) references users(email))")
        Log.d("ded", "db")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
    fun insertDB(email:String,password:String,name:String,lastname:String,address:String,phone:String){
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()

        values.put("email",email)
        values.put("password",password)
        values.put("name",name)
        values.put("lastname",lastname)
        values.put("address",address)
        values.put("phone",phone)

        db.insert("users",null,values)
        db.close()
    }

    fun insertDBuserLoggedIn(email: String, password: String){
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()

        values.put("email",email)
        values.put("password",password)

        db.insert("userLoggedIn", null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun selectPetSitterActive(email: String): PetSitter?{
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT name, address, rating, image, age, typeOfPets, review1, review2, review3 FROM petSitterActive where email ='${email}'"
        val petSitterActive: PetSitter

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val address = cursor.getString(cursor.getColumnIndex("address"))
            val rating = cursor.getFloat(cursor.getColumnIndex("rating"))
            val image = cursor.getInt(cursor.getColumnIndex("image"))
            val age = cursor.getInt(cursor.getColumnIndex("age"))
            val typeOfPets = cursor.getString(cursor.getColumnIndex("typeOfPets"))
            val review1 = cursor.getString(cursor.getColumnIndex("review1"))
            val review2 = cursor.getString(cursor.getColumnIndex("review2"))
            val review3 = cursor.getString(cursor.getColumnIndex("review3"))
            cursor.close()
            petSitterActive = PetSitter(name, address, rating, image, age, typeOfPets, review1, review2, review3)
            return petSitterActive
        } else {
            cursor.close()
            return null
        }
    }

    fun insertDBpetSitterActive(name:String,address:String,rating:Float,image:Int,age:Int,typeOfPets:String, review1:String, review2:String, review3:String, email: String){
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()

        values.put("name",name)
        values.put("address",address)
        values.put("rating",rating)
        values.put("image",image)
        values.put("age",age)
        values.put("typeOfPets",typeOfPets)
        values.put("review1",review1)
        values.put("review2",review2)
        values.put("review3",review3)
        values.put("email", email)

        db.insert("petSitterActive",null,values)
        db.close()
    }

    fun getPetSitterActiveCount(email: String): Int {
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT COUNT(*) FROM petSitterActive where email ='${email}'"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

    fun deletePetSitterActiveByEmail(email: String) {
        val db: SQLiteDatabase = writableDatabase
        db.delete("petSitterActive", "email='${email}'", null)
        db.close()
    }

    fun getUserLoggedInCount(): Int {
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT COUNT(*) FROM userLoggedIn"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

    fun deleteAllUserLoggedIn() {
        val db: SQLiteDatabase = writableDatabase
        db.delete("userLoggedIn", null, null)
        db.close()
    }

    @SuppressLint("Range")
    fun selectUserLoggedIn(): UserCredentials? {
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT email, password FROM userLoggedIn"

        val userCredentials: UserCredentials

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            cursor.close()
            userCredentials = UserCredentials(email, password)
            return userCredentials
        } else {
            cursor.close()
            return null
        }
    }

    fun selectDB(email:String,password:String):Boolean{
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT email, password FROM users where email ='${email}' and password = '${password}'"
        val cursor:Cursor = db.rawQuery(query,null)

        if(cursor.count <= 0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    fun selectLoginCredentials(email: String, password: String): UserCredentials {
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT email, password FROM users where email ='${email}' and password = '${password}'"
        return UserCredentials(email, password)
    }

    @SuppressLint("Range")
    fun selectUserData(email: String, password: String): User? {
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT email, name, lastname, address, phone FROM users where email ='${email}' and password = '${password}'"
        val user1: User

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val lastname = cursor.getString(cursor.getColumnIndex("lastname"))
            val address = cursor.getString(cursor.getColumnIndex("address"))
            val phone = cursor.getString(cursor.getColumnIndex("phone"))
            cursor.close()
            user1 = User(email, name, lastname, address, phone)
            return user1
        } else {
            cursor.close()
            return null
        }

    }

    fun updateUserData(email: String, name: String, lastname: String, address: String, phone: String){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("lastname", lastname)
        contentValues.put("address", address)
        contentValues.put("phone", phone)
        Log.d("name", name)

        db.update("users", contentValues, "email = '${email}'", null)

    }

    fun insertPetData(email: String, pet: Pet) {
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()

        // Serialize the pet object into a byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(pet)
        objectOutputStream.flush()
        val serializedPet = byteArrayOutputStream.toByteArray()

        // Add the email and serialized pet to the ContentValues object
        values.put("email", email)
        values.put("pet", serializedPet)
        values.put("id",pet.id)

        // Insert the row into the database
        db.insert("pets", null, values)

        // Close the database connection
        db.close()
    }

    fun deletePet(pet: Pet): Boolean {
        val db: SQLiteDatabase = writableDatabase

        if(db.delete("pets","id='${pet.id}'",null) == 0) {
            Log.d("DELETE_PET", "Failed to delete pet")
            return false
        }
        Log.d("DELETE_PET", "Pet deleted successfully")
        return true
    }

    fun retrievePetData(email: String): ArrayList<Pet>{
        val pets = ArrayList<Pet>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM pets WHERE email='$email'", null)

        if (cursor.moveToFirst()) {
            do {
                val idx = cursor.getColumnIndex("pet")
                val serializedPet = cursor.getBlob(idx)
                val pet = ObjectInputStream(ByteArrayInputStream(serializedPet)).use { it.readObject() } as Pet
                pets.add(pet)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return pets
    }

    private fun serializePet(pet: Pet): ByteArray {
        val serializedPet = ByteArrayOutputStream().also { stream ->
            ObjectOutputStream(stream).use { it.writeObject(pet) }
        }.toByteArray()
        return serializedPet
    }

    private fun deserializePet(serializedPet: ByteArray): Pet {
        val inputStream = ByteArrayInputStream(serializedPet)
        val objectInputStream = ObjectInputStream(inputStream)
        val pet = objectInputStream.readObject() as Pet
        objectInputStream.close()
        inputStream.close()
        return pet
    }


}
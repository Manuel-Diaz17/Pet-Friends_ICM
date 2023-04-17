package com.example.mytestapp

class User(
    val email:String,
    val name:String,
    val lastname:String,
    val address:String,
    val phone:String,
    var petList: List<Pet> = emptyList<Pet>()
): java.io.Serializable {
    override fun toString(): String {
        return "${email}, ${name}, ${lastname}, ${address}, ${phone}"
    }
}
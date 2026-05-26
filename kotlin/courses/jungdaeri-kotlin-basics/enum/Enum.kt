enum class Pet(val info: String) {
    CAT("고양이"),
    DOG("강아지"),
    BIRD("앵무새")
}

fun main() {

    val pet = Pet.DOG

    when (pet) {
        Pet.CAT -> println(pet.info)
        Pet.DOG -> println(pet.info)
        Pet.BIRD -> println(pet.info)
    }
}

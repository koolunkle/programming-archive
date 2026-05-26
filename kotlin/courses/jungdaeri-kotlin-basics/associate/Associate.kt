data class Bread(val name: String, val price: Int) {}

fun main() {

    val breads = mutableListOf<Bread>()

    breads.add(Bread("단팥빵", 1000))
    breads.add(Bread("단팥빵1", 1100))
    breads.add(Bread("단팥빵2", 1200))
    breads.add(Bread("단팥빵3", 1300))

    println("breads : $breads")

    // associateWith = Key: Element, Value: Selector result
    val breadsAssociateWith: Map<Bread, Int> = breads.associateWith({ it.price })

    println(breadsAssociateWith)

    // associateBy = Key: Selector result, Value: Element
    val breadsAssociateBy: Map<String, Bread> = breads.associateBy({ it.name })

    println(breadsAssociateBy)

    // associate = Key/Value: Custom mapping
    // val breadsAssociate: Map<Bread, Bread> = breads.associate({ it to it })
    val breadsAssociate: Map<String, Bread> = breads.associate({ it.name to it })

    println(breadsAssociate)
}

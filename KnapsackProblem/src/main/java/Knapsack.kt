import java.util.*

@DslMarker
annotation class KnapsackDsl

@KnapsackDsl
class ItemBuilder {
    var name = ""
    var value = 0
    var weight = 0

    fun build(): Item = Item(name, value, weight)
}

@KnapsackDsl
class KnapsackBuilder {
    var capacity = 0
    private val items = mutableListOf<Item>()

    fun item(block: ItemBuilder.() -> Unit) = items.add(ItemBuilder().apply(block).build())

    fun build(): Knapsack = Knapsack(items, capacity)
}

class Solution(private val items: List<Item>, private val value: Int) {
    fun display() {
        if (items.isNotEmpty()) {
            println("Knapsack solution")
            System.out.println("Values: $value")
            println("Items :")
            items.forEach(::print)
            println()
        }
    }

}

class Knapsack(private val items: List<Item>, private val capacity: Int) {
    fun solve(): Solution {
        val len = items.size
        val matrix = Array(len + 1) { IntArray(capacity + 1) }

        for (i in 0..capacity)
            matrix[0][i] = 0

        for (i in 1..len) {
            for (j in 0..capacity) {
                if (items[i - 1].weight > j)
                    matrix[i][j] = matrix[i - 1][j]
                else
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i - 1][j - items[i - 1].weight] + items[i - 1].value)
            }
        }

        var res = matrix[len][capacity]
        var w = capacity
        val itemsSolution = ArrayList<Item>()

        var i = len
        while (i > 0 && res > 0) {
            if (res != matrix[i - 1][w]) {
                itemsSolution.add(items[i - 1])
                // we remove items value and weight
                res -= items[i - 1].value
                w -= items[i - 1].weight
            }
            i--
        }

        return Solution(itemsSolution, matrix[len][capacity])
    }

    fun display() {
        if (items.isNotEmpty()) {
            println("Knapsack problem")
            System.out.println("Capacity : $capacity")
            println("Items :")
            items.forEach(::print)
            println()
        }
    }
}

data class Item(val name: String, val value: Int, val weight: Int)

fun knapsack(block: KnapsackBuilder.() -> Unit): Knapsack = KnapsackBuilder().apply(block).build()

fun main(args: Array<String>) {
    val knapsack = knapsack {
        item {
            name = "No1"
            value = 4
            weight = 12
        }
        item {
            name = "No2"
            value = 2
            weight = 1
        }
        item {
            name = "No3"
            value = 2
            weight = 2
        }
        item {
            name = "No4"
            value = 1
            weight = 1
        }
        item {
            name = "No5"
            value = 10
            weight = 4
        }
        capacity = 5
    }

    knapsack.display()
    knapsack.solve().display()
}
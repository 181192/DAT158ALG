@DslMarker
annotation class KnapsackDsl

@KnapsackDsl
class KnapsackBuilder {
    var capacity = 0
    var items = mutableListOf<Item>()

    fun build(): Knapsack = Knapsack(items, capacity)
}

fun knapsack(block: KnapsackBuilder.() -> Unit): Knapsack = KnapsackBuilder().apply(block).build()

data class Item(val name: String, val value: Int, val weight: Int)

class Knapsack(private val items: List<Item>, private val capacity: Int) {
    fun solve() {
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
        itemsSolution.forEach(::println)
    }
}

fun main(args: Array<String>) {
    val items = ArrayList<Item>()
    items.add(Item("No1", 3, 5))
    items.add(Item("No2", 2, 3))
    items.add(Item("No3", 2, 2))
    items.add(Item("No4", 4, 5))
    items.add(Item("No5", 3, 4))
    items.add(Item("No6", 1, 2))

    val knapsack = knapsack {
        this.items = items
        this.capacity = 6
    }

    knapsack.solve()
}
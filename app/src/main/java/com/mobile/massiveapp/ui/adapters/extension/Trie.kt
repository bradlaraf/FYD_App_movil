package com.mobile.massiveapp.ui.adapters.extension


class TrieNode<Key>(var key: Key?, var parent: TrieNode<Key>?) {
    val children: HashMap<Key, TrieNode<Key>> = HashMap()
    var isTerminating = false
}
class Trie<Key> {

    val root = TrieNode<Key>(key = null, parent = null)

    // Insert a list of keys into the trie
    fun insert(list: List<Key>) {
        var current = root

        list.forEach { element ->
            if (current.children[element] == null) {
                current.children[element] = TrieNode(element, current)
            }
            current = current.children[element]!!
        }

        current.isTerminating = true
    }

    // Check if a list of keys exists in the trie
    fun contains(list: List<Key>): Boolean {
        var current = root

        list.forEach { element ->
            val child = current.children[element] ?: return false
            current = child
        }

        return current.isTerminating
    }

    // Remove a list of keys from the trie
    fun remove(collection: List<Key>) {
        var current = root

        collection.forEach {
            val child = current.children[it] ?: return
            current = child
        }

        if (!current.isTerminating) return

        current.isTerminating = false

        var parent = current.parent
        while (current.children.isEmpty() && !current.isTerminating) {
            parent?.let {
                it.children.remove(current.key)
                current = it
                parent = current.parent
            }
        }
    }

    // Find all collections with a given prefix
    fun collections(prefix: List<Key>): List<List<Key>> {
        var current = root

        prefix.forEach { element ->
            val child = current.children[element] ?: return emptyList()
            current = child
        }

        return collections(prefix, current)
    }

    private fun collections(prefix: List<Key>, node: TrieNode<Key>?): List<List<Key>> {
        val results = mutableListOf<List<Key>>()

        if (node?.isTerminating == true) {
            results.add(prefix)
        }

        node?.children?.forEach { (key, node) ->
            results.addAll(collections(prefix + key, node))
        }

        return results
    }
}

fun Trie<Char>.insert(string: String) {
    insert(string.toList())
}

fun Trie<Char>.contains(string: String): Boolean {
    return contains(string.toList())
}

fun Trie<Char>.remove(string: String) {
    remove(string.toList())
}

fun Trie<Char>.collections(prefix: String): List<String> {
    return collections(prefix.toList()).map { it.joinToString(separator = "") }
}


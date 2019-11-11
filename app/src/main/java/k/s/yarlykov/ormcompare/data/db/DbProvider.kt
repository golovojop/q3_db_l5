package k.s.yarlykov.ormcompare.data.db

interface DbProvider<in U, out R> {
    fun insert(u : U)
    fun update(u : U)
    fun delete(u : U)
    fun select() : R
    fun clear()
}
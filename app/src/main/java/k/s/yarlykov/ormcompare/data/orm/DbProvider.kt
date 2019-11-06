package k.s.yarlykov.ormcompare.data.orm

interface DbProvider<in T, out R> {
    fun insert(t: T)
    fun update(t : T)
    fun delete(t : T)
    fun select() : R
}
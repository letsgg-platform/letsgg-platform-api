package net.letsgg.platform.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import java.time.Instant
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractJpaPersistable<T : Serializable> {

    companion object {
        private const val serialVersionUID = 1L
    }

    @Id
    @GeneratedValue
    var id: T? = null

    @CreationTimestamp
    val creationTimestamp: Instant? = null

    @UpdateTimestamp
    var updateTimestamp: Instant? = null

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as AbstractJpaPersistable<*>

        return if (this.id == null) false else this.id == other.id
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun toString(): String {
        return "${this::class.simpleName}(id=$id, creationTimestamp=$creationTimestamp, updateTimestamp=$updateTimestamp"
    }
}

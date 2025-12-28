package me.spadium.kassette.media.spotify.model

data class Artist(
    val externalUrls: Map<String, String>,
    val followers: FollowersObject,
    val genres: Array<String>,
    val images: Array<Image>,
    val name: String,
    val popularity: Int,
    val uri: String
) {
    companion object {
        fun fetch(id: String): Artist {
            TODO()
        }
    }

    data class FollowersObject(
        val href: String, val total: Int
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Artist

        if (popularity != other.popularity) return false
        if (externalUrls != other.externalUrls) return false
        if (followers != other.followers) return false
        if (!genres.contentEquals(other.genres)) return false
        if (!images.contentEquals(other.images)) return false
        if (name != other.name) return false
        if (uri != other.uri) return false

        return true
    }

    override fun hashCode(): Int {
        var result = popularity
        result = 31 * result + externalUrls.hashCode()
        result = 31 * result + followers.hashCode()
        result = 31 * result + genres.contentHashCode()
        result = 31 * result + images.contentHashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + uri.hashCode()
        return result
    }
}
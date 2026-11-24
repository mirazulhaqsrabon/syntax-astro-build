package syn.astro.build

import io.github.rosemoe.sora.langs.textmate.registry.provider.FileResolver
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * Resolves TextMate files from the user-managed $HOME/.orange/assets directory.
 * Paths in languages.json remain relative to that directory.
 */
class ExternalAssetsFileResolver(directory: File) : FileResolver {
    private val root = directory.canonicalFile
    private val rootPrefix = root.path + File.separator

    override fun resolveStreamByPath(path: String): InputStream? {
        return try {
            val candidate = File(root, path).canonicalFile

            // Keep all resolved files inside the configured assets directory.
            if (!candidate.path.startsWith(rootPrefix)) {
                return null
            }
            if (!candidate.isFile || !candidate.canRead()) {
                return null
            }

            FileInputStream(candidate)
        } catch (_: IOException) {
            null
        } catch (_: SecurityException) {
            null
        }
    }
}

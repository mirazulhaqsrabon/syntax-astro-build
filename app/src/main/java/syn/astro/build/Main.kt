package syn.astro.build

import android.util.Log
import androidx.annotation.Keep
import com.rk.extension.ExtensionAPI
import com.rk.extension.ExtensionContext
import com.rk.file.FileTypeManager
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry
import java.io.File

@Keep
@Suppress("unused")
class Main(context: ExtensionContext) : ExtensionAPI(context) {
    private var fileResolver: ExternalAssetsFileResolver? = null
    private var astroFileType: AstroFileType? = null

    override fun onLoad() {
        val assetsDirectory = resolveExternalAssetsDirectory()

        if (assetsDirectory == null || !assetsDirectory.isDirectory || !assetsDirectory.canRead()) {
            Log.e(TAG, "External Astro assets directory is missing or unreadable: ${assetsDirectory?.absolutePath ?: "HOME is not set"}")
        } else {
            val requiredFiles = listOf(
                "languages.json",
                "astro.tmLanguage.json",
                "Astro.config.json"
            )
            val missingFiles = requiredFiles.filterNot { File(assetsDirectory, it).isFile }

            if (missingFiles.isNotEmpty()) {
                Log.e(TAG, "Missing external Astro asset files: ${missingFiles.joinToString()}")
            } else {
                loadExternalGrammars(assetsDirectory)
            }
        }

        val fileType = AstroFileType(context.resources)
        astroFileType = fileType
        FileTypeManager.register(fileType)
    }

    /**
     * FileProviderRegistry synchronizes provider mutations but its
     * tryGetInputStream() iteration is not synchronized. Keep registration and
     * grammar loading under the registry monitor so another extension cannot
     * mutate the provider ArrayList while GrammarRegistry is reading it.
     */
    private fun loadExternalGrammars(assetsDirectory: File) {
        val registry = FileProviderRegistry.getInstance()
        val resolver = ExternalAssetsFileResolver(assetsDirectory)

        synchronized(registry) {
            try {
                registry.addFileProvider(resolver)

                val grammars = GrammarRegistry.getInstance().loadGrammars("languages.json")
                check(grammars.isNotEmpty()) { "languages.json did not define any grammars" }

                fileResolver = resolver
                Log.i(TAG, "Loaded Astro grammar assets from ${assetsDirectory.absolutePath}")
            } catch (error: Exception) {
                Log.e(TAG, "Could not load Astro grammar assets from ${assetsDirectory.absolutePath}", error)
                registry.removeFileProvider(resolver)
                resolver.dispose()
            }
        }
    }

    override fun onDispose() {
        val registry = FileProviderRegistry.getInstance()
        fileResolver?.let { resolver ->
            synchronized(registry) {
                registry.removeFileProvider(resolver)
                resolver.dispose()
            }
        }
        fileResolver = null

        astroFileType?.let { fileType ->
            FileTypeManager.unregister(fileType)
        }
        astroFileType = null
    }

    private fun resolveExternalAssetsDirectory(): File? {
        val homePath = System.getenv("HOME")
            ?.takeIf { it.isNotBlank() }
            ?: System.getProperty("user.home")?.takeIf { it.isNotBlank() }
            ?: return null

        return File(homePath, ".orange/assets")
    }

    private companion object {
        const val TAG = "AstroExtension"
    }
}

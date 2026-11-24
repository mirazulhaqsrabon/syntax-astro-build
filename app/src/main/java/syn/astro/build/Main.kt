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
            val resolver = ExternalAssetsFileResolver(assetsDirectory)
            fileResolver = resolver
            FileProviderRegistry.getInstance().addFileProvider(resolver)

            try {
                GrammarRegistry.getInstance().loadGrammars("languages.json")
                Log.i(TAG, "Loaded Astro grammar assets from ${assetsDirectory.absolutePath}")
            } catch (error: Exception) {
                Log.e(TAG, "Could not load Astro grammar assets from ${assetsDirectory.absolutePath}", error)
                FileProviderRegistry.getInstance().removeFileProvider(resolver)
                fileResolver = null
            }
        }

        val fileType = AstroFileType(context.resources)
        astroFileType = fileType
        FileTypeManager.register(fileType)
    }

    override fun onDispose() {
        fileResolver?.let { resolver ->
            FileProviderRegistry.getInstance().removeFileProvider(resolver)
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

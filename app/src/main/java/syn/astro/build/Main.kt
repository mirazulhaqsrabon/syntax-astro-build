package syn.astro.build

import androidx.annotation.Keep
import com.rk.extension.ExtensionAPI
import com.rk.extension.ExtensionContext
import com.rk.file.FileTypeManager
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver

@Keep
@Suppress("unused")
class Main(context: ExtensionContext) : ExtensionAPI(context) {
    private var fileResolver: AssetsFileResolver? = null
    private var astroFileType: AstroFileType? = null

    override fun onLoad() {
        val resolver = AssetsFileResolver(context.assets)
        fileResolver = resolver
        FileProviderRegistry.getInstance().addFileProvider(resolver)

        GrammarRegistry.getInstance().loadGrammars("languages.json")

        val fileType = AstroFileType(context.resources)
        astroFileType = fileType
        FileTypeManager.register(fileType)
    }

    override fun onDispose() {
        fileResolver?.let { resolver ->
            FileProviderRegistry.getInstance().removeFileProvider(resolver)
        }
        astroFileType?.let { fileType ->
            FileTypeManager.unregister(fileType)
        }
    }
}
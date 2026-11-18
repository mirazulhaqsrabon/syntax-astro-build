package syn.astro.build

import android.content.res.Resources
import syn.astro.build.R
import com.rk.file.FileType
import com.rk.icons.Icon

class AstroFileType(resources: Resources) : FileType {
    override val extensions = listOf("astro")
    override val textmateScope = "source.astro"
    override val name = "astro"
    override val title = "Astro"
    override val icon = Icon.ExternalResourceIcon(R.drawable.astro, resources)
}
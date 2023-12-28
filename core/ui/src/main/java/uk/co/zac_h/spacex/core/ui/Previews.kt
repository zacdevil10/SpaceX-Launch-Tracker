package uk.co.zac_h.spacex.core.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "foldable", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
annotation class DevicePreviews

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class ComponentPreviews
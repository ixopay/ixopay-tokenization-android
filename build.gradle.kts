import com.github.jk1.license.filter.LicenseBundleNormalizer
import com.github.jk1.license.render.InventoryHtmlReportRenderer
import java.util.Properties
import kotlin.apply

allprojects {
	version = "3.1.0"
	group = "com.ixopay.ixopay-tokenization"
}

plugins {
	id("com.android.application") version "8.7.3" apply false
	id("com.android.library") version "8.7.3" apply false
	id("org.jetbrains.kotlin.android") version "2.0.20" apply false
	id("androidx.navigation.safeargs.kotlin") version "2.8.6" apply false
	id("com.github.jk1.dependency-license-report") version "2.9"
}

tasks {
	register("publishToMavenLocal") {
		dependsOn("ixopay-tokenization-api:publishToMavenLocal")
	}
	register<Delete>("clean") {
		delete(rootProject.layout.buildDirectory)
	}
}

licenseReport {
	renderers = arrayOf(
		InventoryHtmlReportRenderer()
	)

	filters = arrayOf(
		LicenseBundleNormalizer()
	)
}

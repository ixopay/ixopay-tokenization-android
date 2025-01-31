import com.ixopay.util.IxopayBuildProperties

plugins {
	`java-library`
	`maven-publish`
}

group = "com.ixopay.ixopay-tokenization-android"
version = "3.1.0"

java {
	toolchain {
		languageVersion = if (JavaLanguageVersion.current().asInt() > 17) JavaLanguageVersion.current() else JavaLanguageVersion.of(17)
	}

	withSourcesJar()
	withJavadocJar()
}

dependencies {
	api("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("org.json:json:20250107")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
}

tasks {
	test {
		useJUnitPlatform()

		val buildProperties = IxopayBuildProperties(rootProject)
		systemProperty("com.ixopay.gateway_host", buildProperties.gatewayHost)
		systemProperty("com.ixopay.tokenization_host", buildProperties.tokenizationHost)
		systemProperty("com.ixopay.integration_key", buildProperties.integrationKey)
	}
}

publishing {
	publications {
		register<MavenPublication>("maven") {
			pom {
				from(components["java"])
				name.set("IXOPAY")
			}
		}
	}
}

plugins {
	`kotlin-dsl`
}

repositories {
	mavenCentral()
}

java {
	toolchain {
		languageVersion = if (JavaLanguageVersion.current().asInt() > 17) JavaLanguageVersion.current() else JavaLanguageVersion.of(17)
	}
}

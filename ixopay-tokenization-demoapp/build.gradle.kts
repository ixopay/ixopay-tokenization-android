import com.ixopay.util.IxopayBuildProperties

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("androidx.navigation.safeargs.kotlin")
	kotlin("kapt")
}

group = "com.ixopay.ixopay-tokenization-android"

android {
	namespace = "com.ixopay.tokenizationdemo"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.ixopay.tokenizationdemo"
		minSdk = 21
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

		val buildProperties = IxopayBuildProperties(rootProject)
		buildConfigField("String", "IXOPAY_GATEWAY_HOST", "\"${buildProperties.gatewayHost}\"")
		buildConfigField("String", "IXOPAY_TOKENIZATION_HOST", "\"${buildProperties.tokenizationHost}\"")
		buildConfigField("String", "IXOPAY_INTEGRATION_KEY", "\"${buildProperties.integrationKey}\"")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	dataBinding {
		enable = true
	}
	viewBinding {
		enable = true
	}
	buildFeatures {
		viewBinding = true
		dataBinding = true
		buildConfig = true
	}
}

dependencies {
	implementation(fileTree("libs") { include("*.jar") })
	implementation(project(":ixopay-tokenization-api"))

	implementation("com.android.support:appcompat-v7:28.0.0")
	implementation("com.google.android.material:material:1.12.0")
	implementation("androidx.appcompat:appcompat:1.7.0")
	implementation("androidx.constraintlayout:constraintlayout:2.2.0")
	implementation("androidx.navigation:navigation-fragment-ktx:2.8.6")
	implementation("androidx.navigation:navigation-ui-ktx:2.8.6")

	testImplementation("junit:junit:4.13.2")

	androidTestImplementation("com.android.support.test:runner:1.0.2")
	androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}
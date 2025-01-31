package com.ixopay.util

import java.util.Properties
import org.gradle.api.Project

class IxopayBuildProperties(rootProject: Project) {
	private val localProperties = Properties().apply {
		val file = rootProject.file("local.properties")
		if (file.exists()) {
			load(file.inputStream())
		}
	}
	private val gradleProperties = rootProject.properties

	private fun getProperty(key: String, default: String = ""): String {
		val envKey = key.replace("com.ixopay.", "IXOPAY_").uppercase()
		return localProperties.getProperty(key) ?: gradleProperties[key]?.toString() ?: System.getenv(envKey) ?: default
	}

	val gatewayHost: String by lazy {
		getProperty("com.ixopay.gateway_host", "https://gateway.ixopay.com")
	}

	val tokenizationHost: String by lazy {
		getProperty("com.ixopay.tokenization_host", "https://secure.ixopay.com")
	}

	val integrationKey: String by lazy {
		getProperty("com.ixopay.integration_key", "")
	}
}

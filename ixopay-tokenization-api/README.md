
# IXOPAY Android tokenization API

<!-- shields -->
[![Release][release-shield]][release]
![JDK 11+][jdk-shield]
[![License][license-shield]][license]

This SDK enables you to tokenize card data natively from your Android app to IXOPAY's PCI-certified servers.

<details>
    <summary>Table of Contents</summary>

<!-- TOC -->
- [IXOPAY Android tokenization API](#ixopay-android-tokenization-api)
  - [Installation](#installation)
    - [Requirements](#requirements)
    - [Gradle](#gradle)
    - [Maven](#maven)
  - [Usage](#usage)
<!-- TOC -->

</details>

## Installation

### Requirements

- JDK 8 or newer
- M2 repository compatible dependency manager
  - Gradle
  - Maven

### Gradle

<details>
<summary>build.gradle</summary>

Add the [jitpack.io](https://jitpack.io) repository:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency, replace `VERSION` with the [GitHub release name][github-releases]:

```gradle
dependencies {
    ...
    implementation 'com.ixopay:ixopay-tokenization-android:VERSION'
    ...
}
```

</details>

<details open>
<summary>build.gradle.kts</summary>

Add the [jitpack.io](https://jitpack.io) repository:

```kotlin
repositories {
    // ...
    maven("https://jitpack.io")
}
```

Add the dependency, replace `VERSION` with the [GitHub release name][github-releases]:

```kotlin
dependencies {
    // ...
    implementation("com.ixopay:ixopay-tokenization-android:VERSION")
    // ...
}
```

</details>

### Maven

<details open>
<summary>pom.xml</summary>

Add the [jitpack.io](https://jitpack.io) repository:

```maven
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add the dependency, replace `VERSION` with the [GitHub release name][github-releases]:

 ```maven
<dependency>
    <groupId>com.ixopay</groupId>
    <artifactId>ixopay-tokenization-android</artifactId>
    <version>VERSION</version>
</dependency>
```

</details>

## Usage

See [README][parent-readme].

<!-- references -->
[release-shield]: https://jitpack.io/v/com.ixopay/ixopay-tokenization-android.svg
[release]: https://jitpack.io/#com.ixopay/ixopay-tokenization-android
[jdk-shield]: https://img.shields.io/badge/jdk-8%2B-green
[license-shield]: https://img.shields.io/github/license/ixopay/ixopay-tokenization-android
[license]: ../LICENSE.md
[github-releases]: https://github.com/ixopay/ixopay-tokenization-android/releases
[parent-readme]:  ../README.md

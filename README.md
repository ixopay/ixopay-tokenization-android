
# IXOPAY Android tokenization

<!-- shields -->
[![Release][release-shield]][release]
[![JavaDoc][javadoc-shield]][javadoc]
![JDK 11+][jdk-shield]
[![License][license-shield]][license]

This SDK enables you to tokenize card data natively from your Android app to IXOPAY's PCI-certified servers.

<details>
    <summary>Table of Contents</summary>

<!-- TOC -->
- [IXOPAY Android tokenization](#ixopay-android-tokenization)
  - [Installation](#installation)
    - [Requirements](#requirements)
    - [Gradle](#gradle)
    - [Maven](#maven)
  - [Usage](#usage)
    - [Requirements](#requirements-1)
    - [Tokenization API](#tokenization-api)
    - [Running the demo app](#running-the-demo-app)
  - [Development](#development)
  - [License](#license)
  - [Changelog](#changelog)
<!-- TOC -->

</details>

## Installation

### Requirements

- JDK 8 or newer, for the demo app JDK 17 or newer
- M2 repository compatible dependency manager
  - Gradle
  - Maven

### Gradle

<details>
<summary>build.gradle</summary>

Add the [jitpack.io][jitpack] repository:

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

Add the [jitpack.io][jitpack] repository:

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

Add the [jitpack.io][jitpack] repository:

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

### Requirements

- [IXOPAY][ixopay] account
- API User
  - username
  - password
- Connector - consisting of
  - API key, and
  - optional: shared secret
  - public integration key

### Tokenization API

Initialize `TokenizationApi` by at least providing a public integration key.
Then call the created `TokenizationApi` instance's `tokenizeCardData` method.

```java
class App {
    public void tokenizeCard()  {
        TokenizationApi api = com.ixopay.api.tokenization.TokenizationApi.builder()
            .integrationKey("YOUR_INTEGRATION_KEY")
            .build();

        Token result = api.tokenizeCardData(new CardData("4111 1111 1111 1111", "111", "Some cardholder", 2, 2023));
    }
}
```

- For an Android example see: invocation of [TokenizationApi.java][example-tokenization-api]
  in [MainActivity.java][example-main-activity].
- For a minimal example program see: [TestMain.java][example-minimal].

### Running the demo app

To run the demo app, set appropriate credentials in `local.properties` (ignored by Git) or `gradle.properties` (in Git) in the project root directory.
Then you can start the demo app in your Android simulator.

```properties
sdk.dir=ANDROID_SDK_DIR
# default: com.ixopay.gateway_host=https://gateway.ixopay.com
# default: com.ixopay.tokenization_host=https://secure.ixopay.com
com.ixopay.integration_key=IXOPAY_INTEGRATION_KEY
```

## Development

To run the tests, you have to inject environment variables that specify the endpoints:

```shell
./gradlew test -Dgateway=$IXOPAY_GATEWAY_HOST -Dtokenization=$IXOPAY_TOKENIZATION_HOST -Dkey=$IXOPAY_INTEGRATION_KEY
```

## Support

If you have suggestions for new features, spotted a bug, or encountered a
technical problem, [create an issue here][repo-new-issue].
Also, you can always contact IXOPAY's Support Team as defined in your contract.

## License

This repository is available under the [MIT License][license].

## Changelog

[CHANGELOG][changelog]

<!-- references -->
[release-shield]: https://jitpack.io/v/com.ixopay/ixopay-tokenization-android.svg
[release]: https://jitpack.io/#com.ixopay/ixopay-tokenization-android
[jdk-shield]: https://img.shields.io/badge/jdk-8%2B-green
[javadoc-shield]: https://img.shields.io/badge/javadoc-browse-lightgrey
[javadoc]: https://jitpack.io/com/ixopay/ixopay-tokenization-android/latest/javadoc/
[license-shield]: https://img.shields.io/github/license/ixopay/ixopay-tokenization-android
[license]: LICENSE.md
[jitpack]: https://jitpack.io
[github-releases]: https://github.com/ixopay/ixopay-tokenization-android/releases
[changelog]: CHANGELOG.md
[example-tokenization-api]: ixopay-tokenization-api/src/main/java/com/ixopay/api/tokenization/TokenizationApi.java
[example-main-activity]: ixopay-tokenization-demoapp/src/main/java/com/ixopay/tokenizationdemo/MainActivity.java
[example-minimal]: ixopay-tokenization-api/src/test/java/com/ixopay/api/tokenization/TestMain.java
[example-secrets]: ixopay-tokenization-demoapp/src/main/res/values/secrets.xml
[ixopay]: https://gateway.ixopay.com
[repo-new-issue]: https://github.com/ixopay/ixopay-tokenization-android/issues/new/choose

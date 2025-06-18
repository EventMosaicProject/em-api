plugins {
	java
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
}

group = "com.neighbor.eventmosaic"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.spring.boot.starter.actuator)
	implementation(libs.spring.boot.starter.data.elasticsearch)
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.springdoc.openapi.starter.webmvc.ui)
	developmentOnly(libs.spring.boot.docker.compose)
//	developmentOnly(libs.spring.boot.devtools)

	implementation(libs.spring.cloud.starter.netflix.eureka)

	implementation(libs.micrometer.prometheus)
	implementation(libs.logstash.logback.encoder)

	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)

	// MapStruct
	implementation(libs.mapstruct)
	annotationProcessor(libs.mapstructProcessor)



	testImplementation(libs.spring.boot.starter.test)
	testRuntimeOnly(libs.junit.platform.launcher)
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

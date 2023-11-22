plugins {
    id("java")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

}

dependencies {
    implementation("com.alibaba:fastjson:2.0.28")
    implementation("org.apache.logging.log4j:log4j-api:2.22.0")
    implementation("org.apache.logging.log4j:log4j-core:2.22.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.create("MyFatJar", Jar::class) {
    group = "my tasks" // OR, for example, "build"
    description = "Creates a self-contained fat JAR of the application that can be run."
    manifest.attributes["Main-Class"] = "Main"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    val dependencies = configurations
            .runtimeClasspath
            .get()
            .map(::zipTree)
    from(dependencies)
    with(tasks.jar.get())
}

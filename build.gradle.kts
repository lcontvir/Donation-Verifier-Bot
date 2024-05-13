plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

application.mainClass = "com.lcontvir.bot.modelo.Bot" //
group = "com.lcontvir"
version = "1.0"

val jdaVersion = "JDA_VERSION_HERE" //

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.19")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("org.json:json:20210307")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true
    sourceCompatibility = "1.8"
}
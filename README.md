# Ayumi Starter — Kotlin + Jetpack Compose in VS Code

A minimal working Android project: Gradle + Kotlin + Jetpack Compose, no Android Studio required.

**Versions (verified Oct 2026):** AGP 9.4.1 · Kotlin 2.4.20 (via buildscript classpath — AGP 9+ has built-in Kotlin, no kotlin-android plugin) · Compose BOM 2026.09.00 · compileSdk 37 · minSdk 26

## What's inside

- `MainActivity.kt` — a mini Ayumi "Today" screen: sample routine slots, tap the circle to toggle done, live "X of Y done" counter
- `ui/Theme.kt` — Ayumi brand colors (washi, ink, persimmon, matcha, navy)
- Full Gradle setup (`settings.gradle.kts`, `build.gradle.kts`, versioned, ready to build)

## Get it running

See **[SETUP.md](../SETUP.md)** for the full step-by-step (JDK 17 → SDK → VS Code → build → install on your phone).

Short version once set up:

```bash
# Windows: gradlew.bat ; macOS/Linux: ./gradlew
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

Then open `MainActivity.kt` and start shaping it into the real Ayumi from your spec.

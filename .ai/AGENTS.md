# AGENTS.MD - AI Coding Agents Guide

## Project Overview

This is an Android Kotlin application built with Gradle. The app is a calendar day widget that displays different colors based on calendar events.

Specifically, this is an Android widget application that displays a colored square on the home screen based on whether specific all-day calendar events exist for the current day. The app checks for two configurable event titles:
- Event A ("MAMMA") -> Pink color (#FF4FA3)
- Event B ("PAPI") -> Blue color (#4FC3FF)
- No events -> Transparent
- No calendar permission -> Dark gray (#2E3440)

### Key Technologies
- Kotlin
- Android SDK
- Gradle build system
- Firebase (for analytics)

## Build Commands

### Building the Application
```bash
# Assemble debug APK (local development)
./gradlew assembleDebug

# Assemble release APK
./gradlew assembleRelease

# Build all variants
./gradlew build

# Build and install debug APK to connected device
./gradlew installDebug
```

Using Android Studio:
- Open project in Android Studio
- Select "Build > Make Project" or use Run button

### Testing Commands
```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew testDebugUnitTest --tests "com.giulio.calendardaywidget.MyTestClass"

# Run specific test method
./gradlew testDebugUnitTest --tests "com.giulio.calendardaywidget.MyTestClass.myTestMethod"

# Run instrumentation tests on connected device
./gradlew connectedDebugAndroidTest
```

Note: Currently, there are no test files in this project. To add tests:
- Unit tests would go in src/test/
- Instrumentation tests would go in src/androidTest/

### Linting Commands
```bash
# Run Android lint checks
./gradlew lint

# Run lint for debug variant
./gradlew lintDebug

# Run lint for release variant
./gradlew lintRelease
```

### Clean Commands
```bash
# Clean build artifacts
./gradlew clean

# Clean and rebuild everything
./gradlew clean build
```

## Code Style Guidelines

### Imports
1. Use wildcard imports for more than 5 imports from the same package
2. Order imports by:
   - Android package imports
   - Third-party imports (alphabetical order)
   - Kotlin standard library imports
   - This project imports
3. Separate each group with a blank line
4. No unused imports

### Formatting
1. Line length limit: 120 characters
2. Indentation: 4 spaces (no tabs)
3. Braces:
   ```kotlin
   // Opening braces at end of line
   if (condition) {
       // code
   }

   // For classes and functions, opening brace on same line
   class MyClass {
       fun myMethod() {
           // code
       }
   }
   ```

### Naming Conventions
1. Classes: PascalCase (e.g., `CalendarStatusWidgetProvider`)
2. Functions: camelCase (e.g., `updateWidget`)
3. Variables: camelCase (e.g., `widgetId`)
4. Constants: UPPER_SNAKE_CASE (e.g., `EVENT_A_TITLE`)
5. Private members: Prefix with underscore if needed for clarity
6. Companion objects: Put constants and utility functions here when appropriate

### Types
1. Prefer immutable collections (`List`, `Map`) over mutable ones when possible
2. Use Kotlin data classes for simple data containers
3. Use sealed classes for representing restricted class hierarchies
4. Nullability:
   - Prefer non-null types
   - Use `lateinit` for properties initialized in lifecycle methods
   - Use safe call operator `?.` and Elvis operator `?:` appropriately

### Error Handling
1. Use Kotlin's null safety features instead of try/catch when possible
2. For expected errors, use sealed classes to represent success/failure states
3. Log meaningful error messages for debugging
4. Handle exceptions gracefully with user-friendly messages when appropriate

### Android-Specific Guidelines
1. Use Android KTX extensions where available
2. Prefer `ContextCompat` to `Context` for compatibility methods
3. Use view binding instead of `findViewById`
4. Use coroutines instead of callbacks for asynchronous operations
5. Apply appropriate lifecycle awareness to components
6. Handle permissions properly with Activity Result APIs

### Testing Guidelines
1. Unit tests should be isolated and fast
2. Use JUnit 4 for unit tests
3. Use Mockito for mocking dependencies when needed
4. Name tests descriptively: `methodName_condition_result()`
5. Test edge cases and failure scenarios
6. Keep tests focused on a single responsibility

### Comments and Documentation
1. Use KDoc comments for public APIs and complex logic
2. Explain "why" rather than "what" in comments
3. Remove outdated comments when code changes
4. Keep comments concise but informative

### Dependency Injection
1. Follow the principle of dependency inversion
2. Minimize tight coupling between components
3. Prefer constructor injection over field injection

### Resource Management
1. Close resources properly using `use` function for `Closeable` objects
2. Be mindful of memory leaks in Android components
3. Use appropriate lifecycle-aware components

### Best Practices
1. Prefer functional programming constructs (map, filter, etc.) when appropriate
2. Keep functions small and focused
3. Use extension functions for utility methods
4. Avoid global state
5. Write pure functions when possible
6. Use early returns to simplify control flow

## Repository Structure
```
.
├── app/
│   ├── build.gradle.kts        # App module build configuration
│   ├── src/main/
│   │   ├── java/               # Kotlin source files
│   │   ├── res/                # Resources (layouts, drawables, etc.)
│   │   └── AndroidManifest.xml # App manifest
│   └── src/androidTest/        # Instrumentation tests
├── build.gradle.kts            # Root build configuration
└── .github/workflows/          # CI/CD workflows
└── .ai/                        # AI collaboration files
```

## Main Components

### CalendarStatusWidgetProvider.kt
The core widget provider that:
- Extends AppWidgetProvider to handle widget lifecycle events
- Updates widget appearance based on calendar events
- Responds to system events (boot, date/time changes, calendar reminders)
- Contains CalendarTodayReader object that queries all-day calendar events

### MainActivity.kt
The main activity that:
- Handles calendar permission requests
- Provides UI to grant permission and refresh widget manually
- Triggers widget updates after permission changes

### Resources
- `widget_calendar_status.xml` - Simple FrameLayout for widget display
- `activity_main.xml` - Main activity UI with permission controls
- `calendar_status_widget_info.xml` - Widget configuration metadata
- `colors.xml` - Color definitions for different states
- `strings.xml` - Localized strings (Italian)

## Key Logic Flow

1. Widget updates are triggered by:
   - Periodic system updates (handled by onUpdate)
   - System events like boot/date/time changes
   - Manual refresh from main activity

2. Calendar event checking:
   - Queries CalendarContract.Instances for all-day events overlapping today
   - Filters for specific event titles (EVENT_A_TITLE, EVENT_B_TITLE)
   - Event A has priority over Event B

## Configuration

### Customizing Event Titles
Modify the constants in `CalendarStatusWidgetProvider.kt`:
```kotlin
private const val EVENT_A_TITLE = "MAMMA"
private const val EVENT_B_TITLE = "PAPI"
```

### Supported Android Versions
- Minimum SDK: 26 (Android 8.0)
- Target SDK: 35 (Android 15)
- Compile SDK: 35 (Android 15)

## Security Considerations
- The app requires READ_CALENDAR permission to function
- `google-services.json` contains Firebase credentials and should not be committed to public repositories
- In CI/CD, this file is created from GitHub secrets

## Widget Behavior
- Square widget that changes background color based on daily calendar events
- Updates automatically on system events (boot, date/time changes)
- Clicking widget opens the main app activity
- Widget respects system dark/light theme through Material Components theme

## GitHub Actions Build Process
The project includes a GitHub Actions workflow (`.github/workflows/build-apk.yml`) that:
1. Sets up JDK 17 and Android SDK
2. Creates google-services.json from GitHub secret
3. Builds debug APK with `gradle :app:assembleDebug`
4. Optionally publishes to GitHub Releases

## AI Agent Collaboration Framework

This section defines how AI coding agents like Claude Code and OpenCode should collaborate on this project.

### Shared Task Management
- Use `.ai/todo-cc.md` and `.ai/todo-oc.md` for respective task tracking
- Each agent creates and claims tasks before working on them
- Tasks should have clear descriptions, acceptance criteria, and ownership

### Code Review Process
- Both agents should review each other's code changes
- Use pull requests or a similar mechanism for code reviews
- Establish coding standards that both agents follow (as defined above)

### Conflict Resolution Protocol
- When both agents want to modify the same file, use a locking mechanism
- If conflicts arise, prioritize based on task importance and dependencies
- Use git branching strategies to isolate changes

### Progress Synchronization
- Regular status updates through `.ai/status.md` when created
- Milestone-based checkpoints to ensure alignment
- Daily stand-ups (automated) to discuss progress and blockers

### Knowledge Sharing
- Maintain shared knowledge base in `.ai/` directory
- Document decisions and rationales for future reference
- Share insights about the codebase structure and patterns

### Communication Channels
1. **Primary**: Direct file annotations and documentation (`.ai/` directory)
2. **Secondary**: Comments in pull requests/commits
3. **Tertiary**: Status updates in shared markdown files

#### Task Coordination Through File Annotations
Add comments in code files indicating which agent is working on what:
```kotlin
// TODO(opencode): Implement background processing for calendar queries
// TODO(claudecode): Optimize database query filtering
```

#### Status Check-ins
Create status entries with timestamped entries in `.ai/status.log` when needed:
```
[2026-03-21 10:30] opencode: Working on performance optimization in CalendarTodayReader
[2026-03-21 10:45] claudecode: Starting work on intent filter alignment
```

### Role Definition
- **Claude Code**: Focus on code quality, performance optimizations, and architectural improvements
- **OpenCode**: Focus on feature implementation, user experience, and testing coverage
- Both agents cross-train on critical components to ensure redundancy

### Handoff Protocol
When completing a task that another agent might work on next:
- Update documentation with clear handoff notes
- Highlight any partially completed work or caveats
- Communicate through the shared task files

### Quality Assurance Integration
- Each agent reviews the other's work before merging
- Maintain test coverage and code quality standards
- Cross-validate performance improvements and bug fixes
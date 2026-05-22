# Smart Task Manager - Android App

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](./LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9%2B-blue.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-Jetpack%20Compose-green.svg)](https://developer.android.com/jetpack/compose)

Smart Task Manager is an Android productivity app built with **Kotlin** and **Jetpack Compose**. It helps users organize tasks, track deadlines, manage reminders, handle subtasks, and improve productivity through a clean and modern interface.

This application was developed as a **Semester Project** to demonstrate core Android development concepts such as:

- Jetpack Compose UI
- Room Database
- ViewModel & State Management
- WorkManager Background Jobs
- Notifications
- Coroutines
- Navigation Compose

---

## Features

### Core Task Management
✅ Add Tasks  
✅ Edit Tasks  
✅ Delete Tasks  
✅ Mark Tasks as Completed or Pending  
✅ Search Tasks  
✅ Filter Tasks by Status  

### Advanced Features
✅ Dashboard with Total, Completed, Pending, and Overdue Counts  
✅ Due Date & Time Picker  
✅ AM/PM Time Formatting  
✅ Task Priority Levels  
✅ Task Categories  
✅ Recurring Tasks  
✅ Subtasks / Checklist Support  
✅ Overdue Task Highlighting  

### Reminder System
✅ Local Notifications for Tasks Due Within 1 Hour  
✅ WorkManager-based Periodic Reminder Checks  
✅ Notification Channel Support  
✅ Android 13+ Notification Permission Handling  

### Storage
✅ Local Data Persistence using Room Database  
✅ Coroutines for Database Operations  
✅ Safe Fallback Migration for Development  

---

## Tech Stack

| Technology | Usage |
|------------|-------|
| Kotlin | Main Programming Language |
| Jetpack Compose | UI Development |
| Material 3 | Modern UI Components |
| Room Database | Local Storage |
| WorkManager | Background Tasks |
| ViewModel | State Management |
| Coroutines | Async Programming |
| Navigation Compose | Screen Navigation |
| NotificationManager | Task Reminders |

---

## Project Structure

```text
app/src/main/java/com/example/smarttaskmanager/
│
├── MainActivity.kt
│
├── data/
│   ├── Task.kt
│   ├── SubTask.kt
│   ├── TaskDao.kt
│   ├── TaskDatabase.kt
│   └── TaskRepository.kt
│
├── notification/
│   └── NotificationHelper.kt
│
├── ui/
│   └── screens/
│       ├── TaskApp.kt
│       ├── TaskListScreen.kt
│       ├── AddEditTaskScreen.kt
│       └── DashboardScreen.kt
│
├── viewmodel/
│   └── TaskViewModel.kt
│
└── worker/
    ├── ReminderWorker.kt
    └── ReminderScheduler.kt
```

---

## Requirements

Before running the project, make sure you have:

- Android Studio Hedgehog or newer
- Kotlin
- JDK 11+
- Android Emulator or Physical Device
- Minimum SDK 24+

---

## Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/<your-github-username>/Smart-Task-Manager-App-Android-App.git
cd Smart-Task-Manager-App-Android-App
```

### 2. Open the Project

1. Open Android Studio  
2. Click **Open**  
3. Select the project folder  

### 3. Sync Gradle

Wait for Gradle Sync to complete.

If required:

```text
Click "Sync Now"
```

### 4. Run the App

- Connect an Android device **OR**
- Start an Emulator

Then click:

```text
Run ▶ button in Android Studio
```

---

## Build from Terminal

### Build Debug APK

```powershell
.\gradlew assembleDebug
```

### Install APK on Connected Device / Emulator

```powershell
.\gradlew installDebug
```

---

## How the Reminder Feature Works

The reminder system works using **WorkManager** and **Notifications**:

1. Each task stores its due date and time in **milliseconds**
2. `ReminderScheduler` starts a periodic WorkManager task
3. `ReminderWorker` checks pending tasks in the database
4. If a task is due within **1 hour or less**, a notification is triggered
5. The task is marked as **notified** to prevent duplicate alerts

---

## Future Improvements

Planned improvements for future versions:

- Voice Input for Tasks
- Task Export / Import
- Calendar Integration
- Analytics Dashboard & Charts
- Backup & Restore
- Dark / Light Theme Toggle
- Smart Recurring Task Generation

---

## Contributing

Contributions, ideas, and suggestions are welcome.

1. Fork the repository
2. Create a new branch

```bash
git checkout -b feature-name
```

3. Commit your changes

```bash
git commit -m "Added new feature"
```

4. Push changes

```bash
git push origin feature-name
```

5. Open a Pull Request

---

## License

This project is licensed under the **MIT License**.

See the [LICENSE](LICENSE) file for more details.

---

## Author

**Akabir Abbas**  
BSCS Student — Semester Project  
Course: Mobile App Development  

---

## Acknowledgements

Special thanks to:

- Android Developers Documentation  
- Jetpack Compose Documentation  
- Room Persistence Library  
- WorkManager Documentation  
- Material 3 Design System  

---

## Notes

- If you rename the repository, update the clone URL.
- Add screenshots to the `screenshots/` folder and update image paths accordingly.
- Ensure notification permission is enabled on Android 13+ devices.

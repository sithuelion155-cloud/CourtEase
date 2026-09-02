CourtEase

CourtEase is an Android sports-court reservation application designed to make discovering, searching, booking, and managing sports facilities easier through a single mobile platform.

The application allows users to create an account, securely sign in, discover sports courts, search and filter available facilities, view court information, make reservations, manage bookings, receive notifications, and manage their personal profile.

The project was developed as part of the CN6008 Advanced Topics in Computer Science coursework.

Features
Authentication
User registration and sign-in
Email and password authentication
Google authentication
Forgotten-password recovery
Secure sign-out
Court Discovery
Browse available sports courts
View court information
Search for courts
Filter courts by sport
Filter courts by location
View pricing and court details
Booking Management
Select a booking date
Select an available time slot
Confirm a court reservation
Prevent conflicting bookings
View booking history
Cancel confirmed bookings
Notifications
Notification support using Firebase Cloud Messaging
User alerts and application notifications
Dedicated notifications screen
User Profile
Display registered user information
Display name, email, and phone information
Manage the signed-in user session
Secure logout
Navigation and User Experience
Bottom navigation
Separate sections for Home, Courts, Bookings, Notifications, and Profile
Loading, empty, error, and confirmation states
Touch-friendly interface
Responsive Android layouts
Technology Stack
Technology	Purpose
Java	Application development
Android Studio	Android development environment
XML	User interface layouts
Firebase Authentication	User authentication
Firebase Firestore	Application and booking data
Firebase Cloud Messaging	Push notifications
Git	Version control
GitHub	Source code repository
Project Structure
CourtEase/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/sithu/courtease/
│   │       │       ├── activities/
│   │       │       ├── adapters/
│   │       │       ├── fragments/
│   │       │       ├── models/
│   │       │       ├── notifications/
│   │       │       ├── utils/
│   │       │       └── MainActivity.java
│   │       └── res/
│   │           ├── drawable/
│   │           ├── layout/
│   │           ├── mipmap/
│   │           ├── values/
│   │           └── xml/
│   └── build.gradle.kts
│
├── gradle/
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── .gitignore
Application Architecture

CourtEase uses a modular Android application structure.

Activities are used for major application flows such as authentication and booking.
Fragments provide the primary sections of the application.
Adapters connect application data with RecyclerView-based interfaces.
Models represent application entities such as courts and bookings.
Utils contains reusable application functionality.
Notifications contains Firebase Cloud Messaging functionality.
Firebase provides authentication and cloud-based application data services.
Firebase

CourtEase uses Firebase as its backend platform.

Firebase Authentication

Firebase Authentication is used to manage user accounts and authentication sessions.

Cloud Firestore

Cloud Firestore stores application information such as:

User profiles
Court information
Booking records
Notification-related data
Firebase Cloud Messaging

Firebase Cloud Messaging provides push-notification functionality for application alerts and updates.

Note: Firebase project configuration files containing environment-specific configuration are intentionally excluded from the public repository where appropriate.

Requirements

To build and run CourtEase, the development environment should include:

Android Studio
Android SDK
Java development environment
An Android emulator or compatible Android device
A configured Firebase project
Installation
1. Clone the repository
git clone https://github.com/sithuelion155-cloud/CourtEase.git
2. Open the project

Open the cloned project in Android Studio.

3. Configure Firebase

Connect the application to the required Firebase project and provide the appropriate Firebase configuration for local development.

4. Sync Gradle

Allow Android Studio to download the required dependencies and complete Gradle synchronization.

5. Run the application

Run the application on:

An Android emulator, or
A physical Android device with developer options enabled.
Application Flow
Launch Application
       ↓
Authentication
       ↓
Home
       ↓
Browse / Search / Filter Courts
       ↓
View Court Details
       ↓
Select Date and Time
       ↓
Confirm Booking
       ↓
Booking History
       ↓
Notifications / Profile Management
Main Application Screens

The main application areas include:

Login
Registration
Password Recovery
Home
Courts
Search and Filters
Court Details
Booking
Booking History
Notifications
Profile
Security Considerations

CourtEase uses Firebase Authentication to identify users and Firebase security controls to protect application data.

User-specific records are associated with the authenticated Firebase user rather than relying only on information entered into the interface.

Sensitive configuration and unnecessary credentials should not be committed to source control.

Testing

The application was tested during development against key functional areas including:

User registration
User login
Google authentication
Password recovery
Court search
Court filtering
Court selection
Booking creation
Booking history
Booking cancellation
Profile information
Navigation
Notifications
Error handling

Further formal test evidence is documented in the academic coursework report.

Academic Context

This project was developed for:

Module: CN6008 – Advanced Topics in Computer Science
Coursework: Coursework 1 – Part 1
Application: CourtEase
Platform: Android
Language: Java

The coursework requires the application to address authentication, intuitive navigation, responsive design, search and filtering, push notifications, testing, and implementation.

Repository

GitHub repository:

https://github.com/sithuelion155-cloud/CourtEase

Author

Sithu Elion

Bachelor of Science (Hons) Computer Science

Disclaimer

CourtEase is an academic software project developed for educational purposes. Court information, pricing, and other demonstration data used within the application may represent sample application data rather than live commercial booking information.

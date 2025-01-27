# Chatter - A Modern Messaging App

## Overview
**Chatter** is a feature-rich mobile messaging app built using **Kotlin** and **Jetpack Compose** for the frontend, with a **Node.js** backend. The app provides a seamless and secure platform for users to connect, message, and share media with friends. The backend code is hosted in a separate repository: [ChatApp Backend](https://github.com/Sambashivarao-Boyina/ChatApp_Backend.git).

## Features
- **User Authentication:**
  - Email-based login and signup.
  - Google Authentication for quick access.
- **Friend Requests:**
  - Search for users by name.
  - Send, accept, or reject friend requests.
- **Messaging:**
  - Start real-time chat with friends after they accept your request.
  - Send text messages and share images in chats.
- **Notifications:**
  - Get notifications for new messages and friend requests, even when the app is closed.
- **Modern UI/UX:**
  - Built with Jetpack Compose for a smooth and responsive interface.

## Technology Stack
### Frontend
- **Kotlin & Jetpack Compose**: For creating a modern, declarative UI.
- **Dagger Hilt**: For dependency injection and managing app components.
- **Retrofit**: For networking and API communication.
- **Socket.IO**: For real-time messaging functionality.

### Backend
- **Node.js**: Backend server (source code: [ChatApp Backend](https://github.com/Sambashivarao-Boyina/ChatApp_Backend.git)).
- **Socket.IO**: For real-time WebSocket communication.
- **REST APIs**: For user authentication, friend requests, and message handling.

## Screenshots
<p align="center">
  <img src="https://github.com/user-attachments/assets/672a8976-6a19-4a58-bb8a-b18e47dc4b01" alt="Login Screen" width="30%"  style="margin: 20px;">
  <img src="https://github.com/user-attachments/assets/3742250b-7b73-4c6c-a2c4-3ec4608f824d" alt="Chat Screen" width="30%"  style="margin: 20px;">
  <img src="https://github.com/user-attachments/assets/f7022578-9398-420d-8bd9-3833479a3846" alt="Notifications" width="30%"  style="margin: 20px;">
</p>
<p align="center">
  <img src="https://github.com/user-attachments/assets/8d7cd9e4-c3db-4bc1-8e14-bed972ab145e" alt="Friend Requests" width="30%"  style="margin: 20px;">
  <img src="https://github.com/user-attachments/assets/e9b6e819-e02f-4f84-82a6-dbf74be1ee43" alt="Image Sharing" width="30%"  style="margin: 20px;">
  <img src="https://github.com/user-attachments/assets/30c5c705-824f-4fc1-a771-28e9ba0e2084" alt="Image Request" width="30%"  style="margin: 20px;">

</p>

## Installation and Setup
### Prerequisites
1. Android Studio installed on your system.
2. A running instance of the backend server (follow setup instructions in the [ChatApp Backend repository](https://github.com/Sambashivarao-Boyina/ChatApp_Backend.git)).
3. A Firebase project set up for authentication and push notifications.

### Steps to Run the App
1. Clone the repository:
   ```bash
   git clone https://github.com/YourUsername/Chatter.git
   ```

2. Open the project in **Android Studio**.

3. Configure the Firebase:
   - Download the `google-services.json` file from your Firebase project.
   - Place it in the `app/` directory.

4. Update the API URL:
   - Go to the `ApiService` file in the project and set the base URL to match your backend server (e.g., `http://<your-backend-server-url>`).

5. Build and run the app on an emulator or physical device.

## How It Works
### Authentication
- Users can register and log in using their email or Google account.
- Firebase handles authentication and token generation.

### Adding Friends
- Search for other users via the search bar.
- Send friend requests to users, who can accept or reject them.

### Chat Functionality
- Once a friend request is accepted, a chat session is created.
- Real-time messaging is enabled using **Socket.IO**.
- Users can send text and images securely.

### Notifications
- **Push Notifications** are implemented to alert users of new messages or friend requests, even when the app is closed, using Firebase Cloud Messaging (FCM).

## Folder Structure
```
Chatter/
├── app/
│   ├── src/
│   │   ├── main/
│   │       ├── java/
│   │       └── res/
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── google-services.json
└── backend/  (linked backend repository)
```



# Who Doesn't Follow You Back? - Instagram Follower Checker

An Android application that checks who doesn't follow you back on Instagram.
The app loads your Instagram ZIP export and displays a list of users you follow who don't follow you back.

## Features
- Load Instagram ZIP export file
- Automatic analysis of followers and following lists
- Display users who don't follow you back
- Direct link to each user's Instagram profile
- Step-by-step guide on how to download your Instagram data

## Tech Stack
- Kotlin
- Android SDK
- RecyclerView
- Gson
- ZipInputStream for reading ZIP files
- Android Studio

## How to Use
1. Go to Instagram → Settings → Accounts Centre → Your information and permissions → Export your information
2. Create an export, select only Followers and Following, JSON format
3. Download the ZIP file once you receive the email notification
4. Open the app and upload the ZIP file
5. The app automatically displays who doesn't follow you back

## What I Learned
- Working with ZIP files in Android
- Parsing JSON data with Gson
- RecyclerView and Adapter pattern
- Multi-screen navigation with Intents
- Publishing an Android project to GitHub

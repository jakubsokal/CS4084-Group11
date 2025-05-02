# Library Seat Booking App

University of Limerick Library App, this app allows students to book, manage, and cancel their library seat reservations.

## Table of Contents
1. [Installation](#installation)
2. [Getting Started](#getting-started)
3. [Feature Testing Guide](#feature-testing-guide)
   - [User Registration](#user-registration)
   - [User Login](#user-login)
   - [Making a Booking](#making-a-booking)
   - [Managing Bookings](#managing-bookings)
   - [Editing Bookings](#editing-bookings)
   - [Cancelling Bookings](#cancelling-bookings)
   - [Viewing Booking History](#viewing-booking-history)
   - [Notifications and Alerts](#notifications-and-alerts)
4. [Troubleshooting](#troubleshooting)

## Installation

1. Clone the repository to your local machine
2. Open the project in Android Studio
3. Build and run the app on an Android device or emulator

## Getting Started

1. Launch the app on your Android device
2. You'll be presented with the opening screen where you can choose to:
   - Log in
   - Register
3. If you're a new user, proceed to the registration screen
4. If you're an existing user, enter your credentials to log in
5. For quick testing, you can use these test credentials:
   - Email: tester@ul.ie
   - Password: tester123

## Feature Testing Guide

### User Registration
1. From the login screen, tap "Register" button
2. Fill in the registration form with:
   - Your UL email address
   - A secure password
3. Tap "Register" button to create your account
4. If registration is successful you will receive a confirmation message
5. Return to the login screen to test your new credentials

### User Login
1. Enter your registered email address
2. Enter your password
3. Tap "Login" button
4. If login successful you should be redirected to the main screen

### Making a Booking
1. From the main screen, tap "Book a Seat" button
2. Select your preferred:
   - Floor
   - Table
   - Seat
3. Choose your booking date using the date picker
4. Select your start time using the time picker
5. Choose your booking duration (30 minutes to 2 hours)
6. Tap "Confirm Booking" button
7. Verify that:
   - You receive a confirmation message
   - The booking appears in your "Upcoming" bookings
   - You receive a alert notification confirming the booking

### Managing Bookings
1. Navigate to the "Manage" tab
2. Test the filter functionality:
   - "All" - Shows all bookings
   - "Upcoming" - Shows future bookings
   - "History" - Shows past bookings
3. Verify that:
   - Bookings are correctly categorized
   - The list updates when you change filters
   - Past bookings show in history
   - Future bookings show in upcoming

### Editing Bookings
1. In the "Manage" tab, find an upcoming booking
2. Tap the "Edit" button
3. Test editing different aspects:
   - Change the date
   - Change the time
   - Change the duration
   - Change the seat
4. Verify that:
   - You can't edit bookings less than 1 hour before schedule time
   - You can't create overlapping bookings
   - Changes are saved correctly
   - You receive a notification about the edit

### Cancelling Bookings
1. In the "Manage" tab, find a booking
2. Test cancellation for different scenarios:
   - Future bookings
   - Current bookings (in progress)
3. Verify that:
   - You can cancel bookings until they end
   - The cancel button disappears after the booking ends
   - You receive a cancellation notification
   - The booking is removed from upcoming bookings

### Viewing Booking History
1. In the "Manage" tab, select "History" filter
2. Verify that:
   - All past bookings are visible
   - Bookings are sorted by date (most recent first)
   - Booking details are complete and accurate

### Notifications and Alerts
1. Test different notification scenarios:
   - Booking confirmation
   - Booking cancellation
   - Booking updates
2. Verify that:
   - Alerts are accessible within the app
   - Alert types are clearly distinguishable
   - You can mark alerts as read

## Troubleshooting

### Common Issues and Solutions

1. **Login Issues**
   - Ensure you're using the correct email format
   - Try resetting your password if needed

2. **Booking Issues**
   - Verify the seat is available for your selected time
   - Check that you're not trying to book overlapping times
   - Ensure you're not trying to edit a booking less than 1 hour before start

3. **App Crashes**
   - Clear app cache and data
   - Ensure you're using the latest version
   - Check device compatibility

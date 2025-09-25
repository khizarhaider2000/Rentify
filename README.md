# 📱 Rentify – Android Rental Marketplace App

**Rentify** is a mobile Android app that connects people who want to rent out their items (Lessors) with those looking to rent (Renters). It provides a seamless platform for item listings, category management, rental requests, and account control — supporting three roles: **Admin**, **Lessor**, and **Renter**.

> 🚨 **Note:** This public repo is a copy of a group project originally developed in a private repository as part of a university software engineering course. I migrated the code here to showcase the project and make it accessible.

---

## ✨ Features

### 🔒 Admin
- Add, edit, and delete **rental categories** (e.g., Electronics, Vehicles, Tools).
- View all **registered user accounts**.
- Delete or disable lessor/renter accounts.
- Moderate posted items to maintain quality and compliance.

### 📦 Lessor (Item Owner)
- List items for rent, including:
  - Item name, description, rental fee, rental duration
  - Attach a **photo** of the item
  - Select category from admin-defined options
- Edit or delete their existing listings
- Manage availability and rental requests

### 🔍 Renter (Item Seeker)
- Register and log in as a renter
- Search for items by **name**, **category**, or **location**
- Send rental requests for available items
- View item photos and descriptions

---



_Add screenshots here if desired._  
_Example: AddItem screen with photo upload, category dropdown, etc._

---

## 🔧 Tech Stack

- **Language:** Java
- **Platform:** Android Studio Koala (API 33+)
- **Backend:**
  - [Firebase Realtime Database](https://firebase.google.com/docs/database)
  - [Firebase Authentication](https://firebase.google.com/docs/auth)
  - [Firebase Storage](https://firebase.google.com/docs/storage)
- **UI Framework:** Material Design Components
- **Design Docs:** UML Class Diagrams for each deliverable

---

## 📁 Project Structure

- `AddItemActivity.java` – Lessor UI to add/edit items with Firebase Storage image upload
- `LoginActivity.java` – Handles login for Admin, Lessor, and Renter
- `AdminActivity.java` – Admin control panel for managing categories and users
- `RecyclerView` adapters – For listing items, categories, and accounts
- `Item.java`, `Category.java`, `User.java` – Model classes representing domain entities

---

## 🚀 How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/khizarhaider2000/Rentify.git

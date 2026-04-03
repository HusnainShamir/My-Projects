# 📚 Student Book System

## 📌 Introduction
Student Book System is a command-line based communication platform that allows users to send messages and emails, while also maintaining logs for auditing purposes.

> ⚠️ Note: This project focuses on backend logic, file handling, and system design. It is fully functional but uses a CLI interface instead of a graphical UI.

---

## 🎯 Objective
- Enable communication between users  
- Manage private and public messaging  
- Track login activity and user actions  
- Provide audit-level access to system data  

---

## 🕹️ System Mechanics

### 1. Core Functionality
- User login system  
- Private and public messaging  
- Email sending and inbox system  
- Login history tracking  

### 2. Roles System
- **Student**
  - Send/receive messages  
  - Send emails  
  - View inbox  

- **Auditor**
  - View all messages  
  - View all emails  
  - Access login history  
  - Search and analyze data  

---

## 🔁 System Flow
1. User logs in  
2. System validates credentials  
3. Role-based menu appears  
4. User performs actions (message/email/view logs)  
5. System stores data in JSON files  
6. Logout updates login history  

---

## 💾 Data Management
- JSON-based storage system  
- Files used:
  - `users.json`
  - `messages.json`
  - `emails.json`
  - `loginhistory.json`

---

## 🎨 UI/UX Design

### Type
- Command Line Interface (CLI)

### Goals
- Simple navigation  
- Clear data display  
- Functional over visual  

---

## 🔊 Features

- ✔ Login & authentication system  
- ✔ Private & public messaging  
- ✔ Email system  
- ✔ Unread/read message tracking  
- ✔ Login history logging  
- ✔ Auditor control panel  
- ✔ Search by date and user  

---

## 🚧 Future Improvements
- Convert CLI to GUI (Tkinter / Web App)  
- Add password hashing for security  
- Improve data validation  
- Add real-time messaging  
- Database integration (SQL)  

---

## 🛠️ Development Purpose
This project was created to:
- Practice file handling (JSON)  
- Understand system design  
- Learn role-based access control  
- Improve Python logic building  

---

## 📦 How to Run
1. Make sure Python is installed  
2. Ensure `data/` folder contains JSON files  
3. Run:
   ```bash
   python main.py

# MGS - MyGymSpace

## 📖 About the Project
MyGymSpace (MGS) is a Java-based application designed to meet the needs of gym-goers and fitness enthusiasts. It provides a comprehensive booking system that allows athletes to schedule private training sessions with Personal Trainers. The software also includes a booking management system tailored for both Athletes and Personal Trainers to easily manage their schedules.

## ✨ Key Features
* **Session Booking:** Athletes can easily book private training sessions by selecting the type of training, date, time slot, and extra options.
* **Booking Management:** Both Athletes and Personal Trainers can view past and future bookings, with the ability to delete future ones if necessary.
* **Dynamic Customization:** Bookings can be dynamically extended with extra services (e.g., towel rental, sauna access, post-workout shake).

## 🏗 Architecture & Design Patterns
The system is built with maintainability and flexibility in mind, utilizing the following design patterns:
* **Abstract Factory & Singleton:** Used in combination to centrally manage data persistence and isolate storage details from the business logic.
* **Decorator:** Employed to dynamically extend bookings upon creation, seamlessly adding extra costs and modifying descriptions for additional services without creating an exponential number of subclasses.

## 💾 Persistence Modes
The application can run in three different modes by simply changing the configuration before startup:
* **DB Mode:** The primary mode, interacting with a database for permanent and reliable data storage.
* **Demo Mode:** An in-memory execution mode ideal for testing and presentations, requiring zero configuration.
* **Fsys Mode:** A file system storage mode (currently partially implemented for User DAO).

## ⚙️ Requirements
To run the MyGymSpace application, you will need:
* Java Development Kit (JDK) 17 or later.
* Java Runtime Environment (JRE) 17 or later.
* An IDE such as IntelliJ IDEA.

## 👤 Author
**Samuele L'Afflitto** - Software Engineering and WEB Design Course 2025/2026 at University of Rome "Tor Vergata"
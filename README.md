# Developer Habit Tracker

A full-stack web application for tracking developer habits with visual analytics, streak tracking, and heatmap visualization.

## Features

- **User Management**: Create and manage multiple user profiles
- **Habit Tracking**: Define habits with customizable frequencies (Daily, Weekly, Monthly)
- **Streak Calculation**: Automatically calculates current and longest streaks
- **Visual Analytics**:
  - GitHub-style heatmap using Cal-Heatmap
  - Progress charts using Chart.js
  - Completion rate statistics
- **RESTful API**: Full CRUD operations for users and habits
- **Responsive Design**: Mobile-friendly interface with Bootstrap 5

## Technology Stack

### Backend
- **Spring Boot 3.4.3** - Web framework
- **Java 21** - Programming language
- **MyBatis 3.0.3** - SQL mapper framework
- **H2 Database** - In-memory database (development)
- **PostgreSQL** - Production database support
- **Lombok** - Boilerplate code reduction
- **Maven** - Build and dependency management

### Frontend
- **HTML5 / CSS3 / JavaScript** - Core web technologies
- **Bootstrap 5.3.0** - UI framework
- **Cal-Heatmap 4.2.4** - GitHub-style heatmap visualization
- **Chart.js 4.4.0** - Charts and graphs
- **D3.js 7.8.5** - Data visualization (required by Cal-Heatmap)
- **Font Awesome** - Icons

## Prerequisites

Before running this application, ensure you have the following installed:

1. **Java Development Kit (JDK) 17 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Or use OpenJDK: https://adoptium.net/

2. **Apache Maven 3.8+**
   - Download from: https://maven.apache.org/download.cgi
   - Installation guide: https://maven.apache.org/install.html

3. Verify installations:
   ```bash
   java -version
   mvn -version
   ```

## Project Structure

```
Bootify_proj/
├── src/
│   └── main/
│       ├── java/com/habittracker/
│       │   ├── DeveloperHabitTrackerApplication.java  # Main application
│       │   ├── controller/
│       │   │   ├── UserController.java               # User REST API
│       │   │   └── HabitController.java              # Habit REST API
│       │   ├── service/
│       │   │   ├── UserService.java                  # User business logic
│       │   │   └── HabitService.java                 # Habit business logic
│       │   ├── mapper/
│       │   │   ├── UserMapper.java                   # User data access
│       │   │   ├── HabitMapper.java                  # Habit data access
│       │   │   └── HabitEntryMapper.java             # Entry data access
│       │   ├── model/
│       │   │   ├── User.java                         # User entity
│       │   │   ├── Habit.java                        # Habit entity
│       │   │   └── HabitEntry.java                   # Entry entity
│       │   └── dto/
│       │       └── HabitStats.java                   # Statistics DTO
│       └── resources/
│           ├── application.yml                       # Application config
│           ├── schema.sql                            # Database schema
│           └── static/
│               ├── index.html                        # Main UI
│               ├── css/style.css                     # Styles
│               └── js/app.js                         # Frontend logic
└── pom.xml                                           # Maven configuration
```

## Database Schema

The application uses three main tables:

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Habits Table
```sql
CREATE TABLE habits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    color VARCHAR(7) DEFAULT '#4CAF50',
    target_frequency VARCHAR(20) DEFAULT 'DAILY',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Habit Entries Table
```sql
CREATE TABLE habit_entries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    habit_id BIGINT NOT NULL,
    entry_date DATE NOT NULL,
    completed BOOLEAN DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (habit_id, entry_date),
    FOREIGN KEY (habit_id) REFERENCES habits(id)
);
```

## API Endpoints

### User Endpoints
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Habit Endpoints
- `GET /api/habits` - Get all habits
- `GET /api/habits/user/{userId}` - Get habits by user
- `GET /api/habits/{id}` - Get habit by ID
- `POST /api/habits` - Create new habit
- `PUT /api/habits/{id}` - Update habit
- `DELETE /api/habits/{id}` - Delete habit
- `POST /api/habits/{id}/log` - Log habit completion
- `GET /api/habits/{id}/entries?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` - Get entries
- `GET /api/habits/{id}/stats?days=90` - Get habit statistics

## Running the Application

### 1. Clone or Navigate to Project Directory
```bash
cd "c:\Users\Kevin Lai\Downloads\Bootify_proj"
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/developer-habit-tracker-0.0.1-SNAPSHOT.jar
```

### 4. Access the Application
- **Web Interface**: http://localhost:8080
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:habittracker`
  - Username: `sa`
  - Password: (leave blank)

## Sample Data

The application includes sample data for testing:

**Users:**
- John Doe (john.doe@example.com)
- Jane Smith (jane.smith@example.com)

**Habits:**
- Daily Coding Practice
- Code Review
- Read Tech Articles
- Exercise

**Sample entries** are pre-populated for the last 30 days.

## Configuration

### Development Mode (Default)
Uses H2 in-memory database. Data resets on application restart.

### Production Mode (PostgreSQL)
Set environment variables:
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/habittracker
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
```

Or update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/habittracker
    username: your_username
    password: your_password
```

## Features Explanation

### Streak Calculation
- **Current Streak**: Counts consecutive days from today/yesterday. Only active if completed today or yesterday.
- **Longest Streak**: Maximum consecutive days in the habit's entire history.

### Heatmap Visualization
- GitHub-style contribution graph
- Shows last 90 days by default
- Green squares indicate completed days
- Interactive tooltips on hover

### Statistics
- Total completions
- Completion rate (percentage)
- Last completion date
- 30-day progress chart

## Troubleshooting

### Port 8080 Already in Use
Change the port in `application.yml`:
```yaml
server:
  port: 8081
```

### Database Connection Issues
Verify H2 console settings or PostgreSQL connection parameters.

### Frontend Not Loading
Ensure static resources are in `src/main/resources/static/`

### Maven Build Failures
```bash
mvn clean install -U  # Force update dependencies
```

## Development

### Adding New Habits
Use the "Add New Habit" button in the web interface.

### API Testing
Use tools like Postman, curl, or the built-in JavaScript fetch API.

Example curl request:
```bash
curl -X POST http://localhost:8080/api/habits \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "name": "Morning Meditation",
    "description": "10 minutes of mindfulness",
    "color": "#9C27B0",
    "targetFrequency": "DAILY"
  }'
```

## Deployment

### Railway
1. Create a Railway account at https://railway.app
2. Install Railway CLI
3. Deploy:
   ```bash
   railway login
   railway init
   railway up
   ```

### Heroku
1. Create a Heroku account
2. Install Heroku CLI
3. Deploy:
   ```bash
   heroku create your-app-name
   git push heroku main
   ```

### Docker
Create a `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
mvn clean package
docker build -t habit-tracker .
docker run -p 8080:8080 habit-tracker
```

## Future Enhancements

- [ ] User authentication and authorization
- [ ] Email reminders for habit completion
- [ ] Social features (share achievements)
- [ ] Mobile app (React Native / Flutter)
- [ ] Export data to CSV/JSON
- [ ] Habit templates and categories
- [ ] Achievement badges and milestones
- [ ] Dark mode toggle

## License

This project is open source and available under the MIT License.

## Support

For issues or questions, please refer to the PDF specification document: 
`developer_habit_tracker_full_guide.pdf`

## Credits

Built with:
- Spring Boot Framework
- MyBatis ORM
- Cal-Heatmap by wa0x6e
- Chart.js by Chart.js Contributors
- Bootstrap by Twitter

---

**Happy Habit Tracking! 🚀**

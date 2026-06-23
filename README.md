# Java AI Chatbot

A modern AI chatbot application built with Spring Boot 3.x and OpenAI API.

## Features

✨ **AI-Powered Conversations** - Uses OpenAI GPT-3.5/GPT-4 for intelligent responses
💬 **Conversation Management** - Create, retrieve, and delete conversations
📝 **Message History** - Maintains full conversation history with timestamps
🔄 **Context-Aware** - AI understands conversation context for better responses
🚀 **RESTful API** - Easy-to-use REST API endpoints
🐳 **Docker Support** - Ready for containerized deployment
📊 **H2 Database** - In-memory database for development

## Prerequisites

- Java 17+
- Maven 3.9+
- OpenAI API Key ([Get one here](https://platform.openai.com/api-keys))
- Docker & Docker Compose (optional)

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/nibeikaichula/java-ai-chat.git
cd java-ai-chat
```

### 2. Set Up Environment Variables

```bash
cp .env.example .env
# Edit .env and add your OpenAI API Key
echo "OPENAI_API_KEY=sk-..." > .env
```

### 3. Build the Project

```bash
mvn clean package
```

### 4. Run the Application

#### Option A: Direct Java Execution

```bash
export OPENAI_API_KEY="your-openai-api-key"
mvn spring-boot:run
```

#### Option B: Using JAR File

```bash
export OPENAI_API_KEY="your-openai-api-key"
java -jar target/java-ai-chat-1.0.0.jar
```

#### Option C: Using Docker

```bash
docker-compose build
docker-compose up
```

The application will start on `http://localhost:8080`

## API Endpoints

### Chat Endpoints

#### Send Message

```http
POST /api/chat/send
Content-Type: application/json

{
  "content": "Hello, how are you?",
  "conversationId": null
}
```

**Response:**
```json
{
  "id": 1,
  "conversationId": 1,
  "role": "assistant",
  "content": "I'm doing well, thank you for asking...",
  "createdAt": "2024-01-15T10:30:00"
}
```

#### Get Conversation History

```http
GET /api/chat/history/1
```

**Response:**
```json
[
  {
    "id": 1,
    "conversationId": 1,
    "role": "user",
    "content": "Hello, how are you?",
    "createdAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "conversationId": 1,
    "role": "assistant",
    "content": "I'm doing well...",
    "createdAt": "2024-01-15T10:30:05"
  }
]
```

### Conversation Endpoints

#### Get All Conversations

```http
GET /api/conversations
```

#### Get Single Conversation

```http
GET /api/conversations/1
```

#### Delete Conversation

```http
DELETE /api/conversations/1
```

## Project Structure

```
java-ai-chat/
├── src/main/
│   ├── java/com/aichatbot/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/       # REST endpoints
│   │   ├── dto/             # Data transfer objects
│   │   ├── entity/          # JPA entities
│   │   ├── exception/       # Exception handling
│   │   ├── repository/      # Data access layer
│   │   ├── service/         # Business logic
│   │   └── AIChatbotApplication.java
│   └── resources/
│       └── application.yml   # Configuration
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Configuration

### application.yml

```yaml
openai:
  api:
    key: ${OPENAI_API_KEY}      # Your OpenAI API Key
    timeout: 60                  # Request timeout in seconds

spring:
  jpa:
    hibernate:
      ddl-auto: update          # Auto-create database tables
```

## Development

### Running Tests

```bash
mvn test
```

### Database Console (H2)

Access H2 console at: `http://localhost:8080/h2-console`

- **URL:** jdbc:h2:mem:chatdb
- **Username:** sa
- **Password:** (leave blank)

### Logging

Logs are configured in `application.yml`. Change log level:

```yaml
logging:
  level:
    com.aichatbot: DEBUG  # Change to DEBUG for more details
```

## Deployment

### AWS EC2

1. Build Docker image: `docker build -t java-ai-chat .`
2. Push to ECR/Docker Hub
3. Deploy using EC2 or ECS

### Heroku

```bash
heroku login
heroku create java-ai-chat
git push heroku main
```

## Troubleshooting

### "Invalid API Key" Error

- Verify your OpenAI API key is correct
- Check that the key has appropriate permissions
- Ensure the key is set in environment variable

### "Connection Timeout" Error

- Check your internet connection
- Verify OpenAI API is accessible
- Increase timeout in `application.yml`

### Database Issues

- H2 console: Check table creation in logs
- Verify JPA annotations are correct
- Check for SQL errors in logs

## License

MIT License - Feel free to use this project for your own purposes.

## Contributing

Contributions are welcome! Feel free to:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Support

For issues or questions:
- Open a GitHub Issue
- Check the documentation
- Review the API examples

## Future Enhancements

- [ ] Support for GPT-4 models
- [ ] User authentication & authorization
- [ ] PostgreSQL support for production
- [ ] Redis caching for performance
- [ ] WebSocket for real-time messaging
- [ ] Frontend UI (React/Vue)
- [ ] Rate limiting
- [ ] Message streaming

---

**Happy Coding! 🚀**


# TelasTCC


# Ta rodando no backend agora, favor iniciar o spring antes de usar
mudar o application.properties pra funcionar com tua máquina
```Java
spring.application.name=forms
spring.datasource.url=jdbc:postgresql://localhost:5432/forms-test
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
server.port=5000
```

Repositório Temporário do projeto de TCC

## Estrutura do Projeto

```plaintext
TelasTCC/
├── frontend/                  # Angular
├── backend/                   # Node.js API Gateway e Spring Boot MS
└── README.md                  # Documentação
```

### Ta rodando no backend agora, favor iniciar o spring antes de usar

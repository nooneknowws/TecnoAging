
# TelasTCC

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
# TCC TecnoAging

## Estrutura do Projeto

```plaintext
TelasTCC/
├── frontend/                
    └── mobile/                # Kotlyn        
    └── web/                   # Angular
├── backend/                   
     └── _Auth                 # SpringBoot MS de Autenticação
     └── _Forms                # SpringBoot MS dos Formulários
     └── _Gateway              # Node.js API Gateway
     └── _Pacientes            # SpringBoot MS dos Pacientes
     └── _SAGA                 # SpringBoot MS da SAGA Orquestrada
     └── _Tecnicos             # SpringBoot MS dos Tecnicos
     └──  Postman              # Arquivos variados dos TESTES via postman e etc...
└── README.md                  # Documentação
```


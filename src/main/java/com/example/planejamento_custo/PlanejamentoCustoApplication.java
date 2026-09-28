package com.example.planejamento_custo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Properties;

@SpringBootApplication
public class PlanejamentoCustoApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PlanejamentoCustoApplication.class);

        // Força as propriedades do banco de dados diretamente na aplicação
        Properties props = new Properties();

        // Conexão com o Banco de Dados
        props.put("spring.datasource.url", "jdbc:mysql://localhost:3306/custeio_pro?useSSL=false&serverTimezone=UTC");
        props.put("spring.datasource.username", "root");
        props.put("spring.datasource.password", "leo3434");
        props.put("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");

        // Configurações do Hibernate
        props.put("spring.jpa.hibernate.ddl-auto", "update");
        props.put("spring.jpa.show-sql", "true");

        app.setDefaultProperties(props);
        app.run(args);
    }
}
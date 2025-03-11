#  🦷 Odontoprev - API para Gerenciamento de Pacientes e Dentistas 

## 1️⃣ Objetivo da Aplicação
A API Odontoprev foi desenvolvida para otimizar o gerenciamento de pacientes e dentistas, permitindo o cadastro, edição, exclusão e listagem de ambos. O sistema visa fornecer uma solução eficiente e segura para a administração de profissionais e clientes na área odontológica.

---

## 2️⃣ Apresentação da Proposta Tecnológica
🎥 [Assista à apresentação da aplicação](https://youtu.be/Uz4d2euEe4U)

---

## 3️⃣ Equipe
- **Celeste Mayumi Pereira Tanaka (RM552865)** – Responsável pela API em C# e desenvolvimento do modelo preditivo.  
- **Lívia Mariana Lopes (RM552558)** – Responsável pela API em Java e DevOps.  
- **Luana Vieira Santos da Silva (RM552994)** – Responsável pelo desenvolvimento do banco de dados, compliance e quality assurance do projeto.  
- **Todas** – Responsáveis pelo desenvolvimento mobile.  

---

## 4️⃣ Arquitetura da Solução
A aplicação foi desenvolvida utilizando Java Spring MVC, seguindo uma arquitetura em camadas para garantir modularidade e escalabilidade. A estrutura se divide em:
- **Model** – Representação das entidades do sistema.  
- **Repository** – Responsável pela comunicação com o banco de dados.  
- **DTO (Data Transfer Object)** – Abstração para troca de dados.  
- **Service** – Contém a lógica de negócios.  
- **Controller** – Gerencia as requisições HTTP da API.  

---

## 5️⃣ Diagramas da Aplicação
📌 **Diagrama de classes**  
![Diagrama de classe](./images/diagrama-odontoprev-sprint3.png)

📌 **Modelagem do banco de dados**  
![Modelo relacional](./images/RelationalModel.png)

---

## 6️⃣ Instruções para Rodar a Aplicação
### ✅ Pré-requisitos  
- Java 21 instalado.  

### ▶️ Rodando o Projeto
```sh
# Clone o repositório
 git clone https://github.com/LiviaMarianaLopes/odontoprev-spring-mvc.git

# Navegue até o diretório do projeto
 cd odontoprev-spring-mvc

# Rode a aplicação diretamente pela IDE (IntelliJ ou Eclipse)
```

A API estará disponível em `http://localhost:8080`.  

---

## 7️⃣ Funcionalidades da API
📌 **Gerenciamento de Dentistas**  
- Cadastro, edição, consulta e exclusão de dentistas.  

📌 **Cadastro de Pacientes**  
- Cadastro, edição, consulta e exclusão de pacientes.  

---

## 8️⃣ Endpoints da API
### 📍 Página inicial
- `GET /` – Página principal.  

### 📍 Dentistas
- `GET /dentista/lista` – Lista os dentistas cadastrados.  
- `GET /dentista/cadastro` – Página de cadastro de dentista.  
- `POST /dentista/cadastrar` – Cadastra um novo dentista.  
- `GET /dentista/edicao/{id}` – Página de edição de dentista.  
- `POST /dentista/{id}` – Edita um dentista.  
- `GET /dentista/{id}` – Exclui um dentista.  

### 📍 Pacientes
- `GET /paciente/lista` – Lista os pacientes cadastrados.  
- `GET /paciente/cadastro` – Página de cadastro de paciente.  
- `POST /paciente/cadastrar` – Cadastra um novo paciente.  
- `GET /paciente/edicao/{id}` – Página de edição de paciente.  
- `POST /paciente/{id}` – Edita um paciente.  
- `GET /paciente/{id}` – Exclui um paciente.  

---

## 9️⃣ Testes da API
### 📍 Criação de Paciente (POST /paciente/cadastrar)
❌ **Erro**  
![Erro na criação de paciente](images/erro-criacao-paciente.png)  
(Erro devido a dados inválidos).  

✅ **Sucesso**  
![Cadastro de paciente bem-sucedido](images/criacao-paciente.png)  
(Cadastro de paciente realizado com sucesso).  

### 📍 Listagem de Pacientes (GET /paciente/lista)
✅ **Sucesso**  
![Lista de pacientes](images/lista-paciente.png)  

### 📍 Atualização de Paciente (POST /paciente/{id})
✅ **Sucesso**  
![Atualização de paciente](images/atualizacao-paciente.png)  

### 📍 Exclusão de Paciente (GET /paciente/deletar/{id})
✅ **Sucesso**  
![Exclusão de paciente](images/exclusao-paciente.png)  

---

## 🔟 Dificuldades Encontradas e Próximos Passos
### 📌 Dificuldades Encontradas
Durante o desenvolvimento da aplicação, algumas dificuldades foram enfrentadas:

- **Relacionamentos no Banco de Dados**: Foi necessário implementar uma lógica para verificar se um endereço já existia antes de cadastrar um novo paciente. Caso o endereço já estivesse no banco, ele deveria ser reutilizado.
- **Validação de E-mail**: A lógica de validação exigiu verificações tanto no cadastro quanto na edição. No cadastro, era necessário garantir que o e-mail não existisse previamente. Já na edição, o sistema precisava verificar se o e-mail já estava cadastrado e garantir que não fosse alterado para um já existente.
 

### 📌 Próximos Passos
- **Autenticação com Spring Security**, incluindo gestão de perfis de segurança.
- **Implementação de internacionalização**, permitindo suporte a múltiplos idiomas.
- **Configuração de mensageria**, incluindo produtores e consumidores.
- **Monitoramento com Spring Boot Actuator**, para fornecer métricas e insights sobre a API.
- **Integração de Inteligência Artificial com Spring AI**, trazendo recursos avançados para a aplicação.
- **Avaliação da viabilidade da solução**, garantindo que ela atende às necessidades do cliente.

---

## 🚀 Conclusão
Este projeto representa um avanço na digitalização do gerenciamento odontológico. A implementação de uma API robusta e segura é essencial para otimizar os processos e reduzir fraudes. 

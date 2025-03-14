-- Criando a tabela de login
CREATE TABLE OP_LOGIN (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    EMAIL VARCHAR(50) NOT NULL,
    SENHA VARCHAR(100) NOT NULL
);

-- Criando a tabela de estado
CREATE TABLE OP_ESTADO (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    NOME_ESTADO VARCHAR(30),
    SIGLA_ESTADO VARCHAR(5)
);

-- Criando a tabela de cidade
CREATE TABLE OP_CIDADE (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    NOME_CIDADE VARCHAR(30),
    ID_ESTADO INT,
    FOREIGN KEY (ID_ESTADO) REFERENCES OP_ESTADO(ID)
);

-- Criando a tabela de bairro
CREATE TABLE OP_BAIRRO (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    NOME_BAIRRO VARCHAR(30),
    ID_CIDADE INT,
    FOREIGN KEY (ID_CIDADE) REFERENCES OP_CIDADE(ID)
);

-- Criando a tabela de endereço
CREATE TABLE OP_ENDERECO (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    LOGRADOURO VARCHAR(50),
    NUMERO INT,
    CEP VARCHAR(8),
    COMPLEMENTO VARCHAR(100),
    ID_BAIRRO INT,
    FOREIGN KEY (ID_BAIRRO) REFERENCES OP_BAIRRO(ID)
);

-- Criando a tabela de gênero
CREATE TABLE OP_GENERO (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    TITULO VARCHAR(30)
);

-- Criando a tabela de paciente
CREATE TABLE OP_PACIENTE (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    NOME_PACIENTE VARCHAR(100) NOT NULL,
    DATA_DE_NASCIMENTO DATE NOT NULL,
    EMAIL_PACIENTE VARCHAR(50) NOT NULL,
    CPF_PACIENTE VARCHAR(11) NOT NULL,
    TELEFONE_PACIENTE VARCHAR(11) NOT NULL,
    ID_GENERO INT,
    ID_ENDERECO INT,
    ID_LOGIN INT,
    CLIENTE_SUSPEITO CHAR(1) CHECK (CLIENTE_SUSPEITO IN ('S', 'N')),
    FOREIGN KEY (ID_GENERO) REFERENCES OP_GENERO(ID),
    FOREIGN KEY (ID_ENDERECO) REFERENCES OP_ENDERECO(ID),
    FOREIGN KEY (ID_LOGIN) REFERENCES OP_LOGIN(ID)
);

-- Criando a tabela de dentista
CREATE TABLE OP_DENTISTA (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    NOME_DENTISTA VARCHAR(100),
    DATA_DE_NASCIMENTO DATE,
    EMAIL_DENTISTA VARCHAR(50),
    CPF_DENTISTA VARCHAR(11),
    TELEFONE_DENTISTA VARCHAR(11),
    CRO VARCHAR(20),
    ID_GENERO INT,
    ID_ENDERECO INT,
    ID_LOGIN INT,
    DENTISTA_SUSPEITO CHAR(1) CHECK (DENTISTA_SUSPEITO IN ('S', 'N')),
    FOREIGN KEY (ID_GENERO) REFERENCES OP_GENERO(ID),
    FOREIGN KEY (ID_ENDERECO) REFERENCES OP_ENDERECO(ID),
    FOREIGN KEY (ID_LOGIN) REFERENCES OP_LOGIN(ID)
);

-- Criando a tabela de unidade
CREATE TABLE OP_UNIDADE (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    NOME_UNIDADE VARCHAR(255),
    TELEFONE VARCHAR(15),
    ID_ENDERECO INT,
    FOREIGN KEY (ID_ENDERECO) REFERENCES OP_ENDERECO(ID)
);

-- Criando a tabela de consulta
CREATE TABLE OP_CONSULTA (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    DATA_CONSULTA DATETIME,
    ID_PACIENTE INT,
    ID_DENTISTA INT,
    ID_UNIDADE INT,
    FOREIGN KEY (ID_PACIENTE) REFERENCES OP_PACIENTE(ID),
    FOREIGN KEY (ID_DENTISTA) REFERENCES OP_DENTISTA(ID),
    FOREIGN KEY (ID_UNIDADE) REFERENCES OP_UNIDADE(ID)
);


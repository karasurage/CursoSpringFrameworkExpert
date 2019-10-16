# Seleção Database
use brewer;

# Consulta das Tabelas
select * from cerveja;
select * from cidade;
select * from cliente;
select * from estado;
select * from estilo;
select * from grupo;
select * from grupo_permissao;
select * from permissao;
select * from usuario;
select * from usuario_grupo;

# Delete das Tabelas
delete from cerveja where codigo >= 1;
delete from cidade where codigo >= 1;
delete from cliente where codigo >= 1;
delete from estado where codigo >= 1;
delete from estilo where codigo >= 1;
delete from grupo where codigo >= 1;
delete from grupo_permissao where codigo >= 1;
delete from permissao where codigo >= 1;
delete from usuario where codigo >= 1;
delete from usuario_grupo where codigo >= 1;

# V01__criar_tabelas_estilo_e_cerveja.sql
CREATE TABLE estilo (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE cerveja (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    sku VARCHAR(50) NOT NULL,
    nome VARCHAR(80) NOT NULL,
    descricao TEXT NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    teor_alcoolico DECIMAL(10, 2) NOT NULL,
    comissao DECIMAL(10, 2) NOT NULL,
    sabor VARCHAR(50) NOT NULL,
    origem VARCHAR(50) NOT NULL,
    codigo_estilo BIGINT(20) NOT NULL,
    FOREIGN KEY (codigo_estilo) REFERENCES estilo(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO estilo VALUES (0, 'Amber Lager');
INSERT INTO estilo VALUES (0, 'Dark Lager');
INSERT INTO estilo VALUES (0, 'Pale Lager');
INSERT INTO estilo VALUES (0, 'Pilsner');

# V02__criar_coluna_quantidade_estoque_em_cerveja.sql
ALTER TABLE cerveja
  ADD quantidade_estoque INTEGER;
  
# V03__criar_colunas_foto_e_contentType_na_cerveja.sql
ALTER TABLE cerveja
   ADD foto VARCHAR(100),
   ADD content_type VARCHAR(100);
   
# V04__adicionar_cidade_e_estado.sql   
CREATE TABLE estado (
    codigo BIGINT(20) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    sigla VARCHAR(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE cidade (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    codigo_estado BIGINT(20) NOT NULL,
    FOREIGN KEY (codigo_estado) REFERENCES estado(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO estado (codigo, nome, sigla) VALUES (1,'Acre', 'AC');
INSERT INTO estado (codigo, nome, sigla) VALUES (2,'Bahia', 'BA');
INSERT INTO estado (codigo, nome, sigla) VALUES (3,'Goiás', 'GO');
INSERT INTO estado (codigo, nome, sigla) VALUES (4,'Minas Gerais', 'MG');
INSERT INTO estado (codigo, nome, sigla) VALUES (5,'Santa Catarina', 'SC');
INSERT INTO estado (codigo, nome, sigla) VALUES (6,'São Paulo', 'SP');


INSERT INTO cidade (nome, codigo_estado) VALUES ('Rio Branco', 1);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Cruzeiro do Sul', 1);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Salvador', 2);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Porto Seguro', 2);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Santana', 2);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Goiânia', 3);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Itumbiara', 3);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Novo Brasil', 3);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Belo Horizonte', 4);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Uberlândia', 4);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Montes Claros', 4);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Florianópolis', 5);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Criciúma', 5);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Camboriú', 5);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Lages', 5);
INSERT INTO cidade (nome, codigo_estado) VALUES ('São Paulo', 6);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Ribeirão Preto', 6);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Campinas', 6);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Santos', 6);

# V05__adicionar_tabela_cliente.sql
CREATE TABLE cliente (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(80) NOT NULL,
    tipo_pessoa VARCHAR(15) NOT NULL,
    cpf_cnpj VARCHAR(30),
    telefone VARCHAR(20),
    email VARCHAR(50) NOT NULL,
    logradouro VARCHAR(50),
    numero VARCHAR(15),
    complemento VARCHAR(20),
    cep VARCHAR(15),
    codigo_cidade BIGINT(20),
    FOREIGN KEY (codigo_cidade) REFERENCES cidade(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# V06__alterar_cpf_cnpj_para_not_null.sql
ALTER TABLE cliente
	MODIFY cpf_cnpj VARCHAR(30) NOT NULL;

# V07__criar_tabela_usuario_grupo_permissao.sql    
CREATE TABLE usuario (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    senha VARCHAR(120) NOT NULL,
    ativo BOOLEAN DEFAULT true,
    data_nascimento DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE grupo (
    codigo BIGINT(20) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permissao (
    codigo BIGINT(20) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usuario_grupo (
    codigo_usuario BIGINT(20) NOT NULL,
    codigo_grupo BIGINT(20) NOT NULL,
    PRIMARY KEY (codigo_usuario, codigo_grupo),
    FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
    FOREIGN KEY (codigo_grupo) REFERENCES grupo(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE grupo_permissao (
    codigo_grupo BIGINT(20) NOT NULL,
    codigo_permissao BIGINT(20) NOT NULL,
    PRIMARY KEY (codigo_grupo, codigo_permissao),
    FOREIGN KEY (codigo_grupo) REFERENCES grupo(codigo),
    FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# V08__alterando_ativo_do_usuario_para_not_null.sql
ALTER TABLE usuario
	MODIFY ativo BOOLEAN DEFAULT true NOT NULL;

# V09__inserir_grupos.sql
INSERT INTO grupo (codigo, nome) VALUES (1, 'Administrador');
INSERT INTO grupo (codigo, nome) VALUES (2, 'Vendedor');
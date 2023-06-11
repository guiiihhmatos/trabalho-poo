--CREATE TABLE editora (
  --cd_editora INTEGER   NOT NULL ,
  --nm_editora VARCHAR(50)      ,
--PRIMARY KEY(cd_editora));




CREATE TABLE usuario (
  cd_usuario INTEGER   NOT NULL ,
  nm_usuario VARCHAR(100)    ,
  email_usuario VARCHAR(70)    ,
  ra_usuario INTEGER(12)    ,
  senha_usuario VARCHAR(10)    ,
  adm_usuario BOOL  DEFAULT 0    ,
PRIMARY KEY(cd_usuario));




--CREATE TABLE autor (
  --cd_autor INTEGER   NOT NULL ,
  --nm_autor VARCHAR(50)      ,
--PRIMARY KEY(cd_autor));




CREATE TABLE categoria (
  cd_categoria INTEGER   NOT NULL ,
  nm_categoria VARCHAR(50)      ,
PRIMARY KEY(cd_categoria));




CREATE TABLE livro (
  cd_livro INTEGER   NOT NULL ,
  autor_cd_autor INTEGER   NOT NULL ,
  editora_cd_editora INTEGER   NOT NULL ,
  categoria_cd_categoria INTEGER   NOT NULL ,
  nm_livro VARCHAR(100)   NOT NULL ,
  isbn_livro INTEGER(13)    ,
  ano_livro YEAR    ,
  desc_livro VARCHAR(500)    ,
  status_livro BOOL      ,
PRIMARY KEY(cd_livro)      ,
  FOREIGN KEY(categoria_cd_categoria)
    REFERENCES categoria(cd_categoria),
  FOREIGN KEY(editora_cd_editora)
    REFERENCES editora(cd_editora),
  FOREIGN KEY(autor_cd_autor)
    REFERENCES autor(cd_autor));


CREATE INDEX livro_FKIndex1 ON livro (categoria_cd_categoria);
CREATE INDEX livro_FKIndex2 ON livro (editora_cd_editora);
CREATE INDEX livro_FKIndex3 ON livro (autor_cd_autor);


CREATE INDEX IFK_Rel_01 ON livro (categoria_cd_categoria);
CREATE INDEX IFK_Rel_02 ON livro (editora_cd_editora);
CREATE INDEX IFK_Rel_03 ON livro (autor_cd_autor);


CREATE TABLE emprestimo (
  cd_emprestimo INTEGER   NOT NULL ,
  livro_cd_livro INTEGER   NOT NULL ,
  usuario_cd_usuario INTEGER   NOT NULL ,
  dt_emprestimo DATE    ,
  dt_devolucao DATE      ,
  PRIMARY KEY(cd_emprestimo),
  FOREIGN KEY(usuario_cd_usuario)
    REFERENCES usuario(cd_usuario),
  FOREIGN KEY(livro_cd_livro)
    REFERENCES livro(cd_livro)
);

CREATE INDEX emprestimo_FKIndex1 ON emprestimo (usuario_cd_usuario);
CREATE INDEX emprestimo_FKIndex2 ON emprestimo (livro_cd_livro);

CREATE INDEX IFK_Rel_05 ON emprestimo (usuario_cd_usuario);
CREATE INDEX IFK_Rel_06 ON emprestimo (livro_cd_livro);




-- questão 7 Tarefa 7
create table debito
(
	numero_debito integer not null,
	valor_debito double precision not null,
	motivo_debito smallint,
	data_debito date,
	numero_conta integer,
	nome_agencia character varying(50),
	nome_cliente character varying(80),
	primary key (numero_debito),
	foreign key (nome_agencia, numero_conta,  nome_cliente) references conta(nome_agencia, numero_conta,  nome_cliente)
);

